package nr.king.wetrack.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import nr.king.wetrack.exceptions.FailedResponseException;
import nr.king.wetrack.http.*;
import nr.king.wetrack.http.homeModel.GetPhoneHistoryMainArrayModel;
import nr.king.wetrack.http.homeModel.HomeModel;
import nr.king.wetrack.jdbc.JdbcTemplateProvider;
import nr.king.wetrack.services.SchedularServices;
import nr.king.wetrack.utils.CommonUtils;
import nr.king.wetrack.utils.HttpUtils;
import nr.king.wetrack.utils.ResponseUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import static nr.king.wetrack.constant.LocationTrackingConstants.FCM_PUSH;
import static nr.king.wetrack.constant.LocationTrackingConstants.GET_LAST_HISTORY;
import static nr.king.wetrack.constant.QueryConstants.*;

@Component
public class UpdateNotificationRepo {


    @Autowired
    private ResponseUtils responseUtils;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private JdbcTemplateProvider jdbcTemplate;
    private static final Logger logger = LogManager.getLogger(UpdateNotificationRepo.class);


    public ResponseEntity updateNotfication(NotificationModel notificationModel) {
        try {
            int count = updateNoty(notificationModel);
            if (count == 0) {
                count = insertNotify(notificationModel);
            }

            return responseUtils.constructResponse(200,
                    commonUtils.writeAsString(objectMapper,
                            new ApiResponse(
                                    count == 1,
                                    (count == 1) ? "Notification Updated" : "Notifcation Not Updated"
                            )
                    ));

        } catch (Exception exception) {

            throw new FailedResponseException(exception.getMessage());
        }
    }

    private int insertNotify(NotificationModel notificationModel) {
        return jdbcTemplate.getTemplate().update(
                INSERT_NUMBER_VALUE,
                notificationModel.getUserId(),
                notificationModel.getNumberId().toString(),
                notificationModel.isEnable(),
                notificationModel.getPushToken(),
                notificationModel.getHeaderToken(),
                notificationModel.getNickName()
        );
    }

    private int updateNoty(NotificationModel notificationModel) {
        return jdbcTemplate.getTemplate().update(
                UPDATE_PUSH_NOTIFICATION,
                notificationModel.isEnable(),
                notificationModel.getPushToken(),
                notificationModel.getHeaderToken(),
                notificationModel.getNickName(),
                notificationModel.getUserId(),
                notificationModel.getNumberId().toString()
        );

    }


    public void doPushNotifcation() {
        try {

            SqlRowSet sqlRowSet = jdbcTemplate.getTemplate().queryForRowSet(selectNumberWithToken);
            while (sqlRowSet.next()) {
                HttpResponse httpResponse = httpUtils.doPostRequest(0,
                        GET_LAST_HISTORY,
                        commonUtils.getHeadersMaps(sqlRowSet.getString("USER_ID")),
                        "Checking the Do Push Notification",
                        commonUtils.writeAsString(objectMapper,
                                new GetPageHistoryNumberModel(
                                        500,
                                        sqlRowSet.getString("number"),
                                        0,
                                        new HomeModel()
                                ))
                );
                GetPhoneHistoryMainArrayModel getPageHistoryNumberModel = commonUtils.safeParseJSON(objectMapper, httpResponse.getResponse(), GetPhoneHistoryMainArrayModel.class);
                if (getPageHistoryNumberModel != null && getPageHistoryNumberModel.getData() != null && "available".equalsIgnoreCase(getPageHistoryNumberModel.getData().get(0).status)) {

                    if (sqlRowSet.getString("PREVIOUS_TIME").isEmpty() ||
                            !getPageHistoryNumberModel.getData().get(0).getTimeStamp().equals(sqlRowSet.getString("PREVIOUS_TIME"))) {
                        logger.info("Response data  zero postion" + commonUtils.writeAsString(objectMapper, getPageHistoryNumberModel.getData().get(0)));
                        logger.info("Response data  first postion" + commonUtils.writeAsString(objectMapper, getPageHistoryNumberModel.getData().get(1)));
                        int updateLastUpdateTime = doUpdatePreviousTime(
                                getPageHistoryNumberModel.getData().get(0).phoneNumber,
                                getPageHistoryNumberModel.getData().get(0).timeStamp,
                                sqlRowSet.getString("USER_ID")
                        );
                        logger.info("Updated the Prebvious time stamp for Number " +
                                getPageHistoryNumberModel.getData().get(0).phoneNumber +
                                "\n"
                                + updateLastUpdateTime);

                        HttpResponse fcmReuest = httpUtils.doPostRequest(0,
                                FCM_PUSH,
                                commonUtils.getHeadersMap(),
                                "Doing Push Notication",
                                commonUtils.writeAsString(objectMapper, new FcmModelData(
                                        sqlRowSet.getString("TOKEN"),
                                        new Notification(
                                                true,
                                                "",
                                                "WeTracker Status",
                                                "2",
                                                sqlRowSet.getString("NICK_NAME") + " is Online",
                                                "high"
                                        ),
                                        new PushData(
                                                true,
                                                sqlRowSet.getString("NICK_NAME") + " is Online",
                                                "app_sound.wav",
                                                "FamilyTracker",
                                                "high"

                                        )
                                ))
                        );
                        logger.info("Http Resposne for fcmRequest" + commonUtils.writeAsString(objectMapper, fcmReuest.getResponse()));

                    }

                }

                Thread.sleep(500,500);

            }

        } catch (Exception exception) {
            logger.error("Exception in schedualr " + exception.getMessage(), exception);
        }
    }

    private int doUpdatePreviousTime(String phoneNumber, String timeStamp, String userId) {
        return jdbcTemplate.getTemplate().update(UPDATE_PREVIOUS_TIME,
                timeStamp,
                userId,
                phoneNumber
        );
    }
}
