package nr.king.wetrack.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import nr.king.wetrack.exceptions.FailedResponseException;
import nr.king.wetrack.http.ApiResponse;
import nr.king.wetrack.http.NotificationModel;
import nr.king.wetrack.jdbc.JdbcTemplateProvider;
import nr.king.wetrack.utils.CommonUtils;
import nr.king.wetrack.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import static nr.king.wetrack.constant.QueryConstants.INSERT_NUMBER_VALUE;
import static nr.king.wetrack.constant.QueryConstants.UPDATE_PUSH_NOTIFICATION;

@Component
public class UpdateNotificationRepo {


    @Autowired
    private ResponseUtils responseUtils;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private JdbcTemplateProvider jdbcTemplate;


    public ResponseEntity updateNotfication(NotificationModel notificationModel)
    {
        try {
            int count = updateNoty(notificationModel);
            if (count==0)
            {
                count  = insertNotify(notificationModel);
            }

            return responseUtils.constructResponse(200,
                    commonUtils.writeAsString(objectMapper,
                            new ApiResponse(
                                    count==1,
                                    (count==1)?"Notification Updated":"Notifcation Not Updated"
                            )
                            ));

        }
        catch (Exception exception)
        {

            throw new FailedResponseException(exception.getMessage());
        }
    }

    private int insertNotify(NotificationModel notificationModel) {
        return jdbcTemplate.getTemplate().update(INSERT_NUMBER_VALUE,
                notificationModel.getUserId(),
                notificationModel.getNumberId(),
                notificationModel.isEnable()
                );
    }

    private int updateNoty(NotificationModel notificationModel) {
        return jdbcTemplate.getTemplate().update(UPDATE_PUSH_NOTIFICATION,
                notificationModel.isEnable(),
                notificationModel.getUserId(),
                notificationModel.getNumberId()
                );

    }


}
