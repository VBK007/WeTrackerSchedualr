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
        return jdbcTemplate.getTemplate().update(INSERT_NUMBER_VALUE,
                notificationModel.getUserId(),
                notificationModel.getNumberId().toString(),
                notificationModel.isEnable(),
                notificationModel.getPushToken()
        );
    }

    private int updateNoty(NotificationModel notificationModel) {
        return jdbcTemplate.getTemplate().update(UPDATE_PUSH_NOTIFICATION,
                notificationModel.isEnable(),
                notificationModel.getPushToken(),
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
               logger.info("Response data "+getPageHistoryNumberModel.getData().get(0));
                if (getPageHistoryNumberModel != null && getPageHistoryNumberModel.getData()!=null && "available".equalsIgnoreCase(getPageHistoryNumberModel.getData().get(0).status)) {
                    HttpResponse fcmReuest = httpUtils.doPostRequest(0,
                            FCM_PUSH,
                            commonUtils.getHeadersMap(),
                            "Doing Push Notication",
                            commonUtils.writeAsString(objectMapper, new FcmModelData(
                                    sqlRowSet.getString("token"),
                                    new Notification(
                                            true,
                                            "",
                                            "WeTracker Status",
                                            "2",
                                            "Bharath is Online",
                                            "high"
                                            ),
                                    new PushData(
                                            true,
                                            "Bharath is Online",
                                            "app_sound.wav",
                                            "FamilyTracker",
                                            "high"

                                    )
                            ))
                    );

                    logger.info("HttpReposne for fcmRequest"+commonUtils.writeAsString(objectMapper,fcmReuest.getResponse()));

                }
            }

        } catch (Exception exception) {
            logger.error("Exception in schedualr " + exception.getMessage(), exception);
        }
    }
}
