package nr.king.wetrack.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import nr.king.wetrack.exceptions.FailedResponseException;
import nr.king.wetrack.http.ApiResponse;
import nr.king.wetrack.http.homeModel.HomeModel;
import nr.king.wetrack.jdbc.JdbcTemplateProvider;
import nr.king.wetrack.utils.CommonUtils;
import nr.king.wetrack.utils.HttpUtils;
import nr.king.wetrack.utils.ResponseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static nr.king.wetrack.constant.LocationTrackingConstants.WETRACK;


@Repository
@EnableAutoConfiguration
public class HomeRepo {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private JdbcTemplateProvider jdbcTemplateProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResponseUtils responseUtils;

    private static final Logger logger = LogManager.getLogger(HomeRepo.class);

    @Autowired
    private HttpUtils httpUtils;


    @Transactional
    public ResponseEntity saveUserDetails(HomeModel homeModel) {
        try {
            int count = doUpdateUser(homeModel);
            if (count == 0) {
                count = createUser(homeModel);
            }
            return responseUtils.constructResponse(200,
                    commonUtils.writeAsString(objectMapper,
                            new ApiResponse(count == 1, "User Created Successfully")));

        } catch (Exception exception) {
            logger.error("Exception in saveuser Details" + exception.getMessage(),
                    exception);
            throw new FailedResponseException(exception.getMessage());
        }
    }

    private int doUpdateUserCreationinWetrack(HomeModel homeModel) {
        return jdbcTemplateProvider.getTemplate()
                .update("update WE_TRACK_USERS set IS_USER_CREATED_IN_WETRACK_SERVICE=?,TOKEN_HEADER=?,Expiry_TIME=? where USER_ID=?",
                        true, homeModel.getId(), LocalDateTime.now().plusHours(3).toString(), homeModel.getId());
    }





    private int createUser(HomeModel homeModel) {
        return jdbcTemplateProvider.getTemplate().update(
                "insert into WE_TRACK_USERS_NO_OF_LOGIN " +
                        "(USER_ID,MOBILE_MODEL,IP_ADDRESS,COUNTRY,ONE_SIGNAL_EXTERNAL_USERID,MOBILE_VERSION," +
                        "CREATED_AT,UPDATED_AT,IS_USER_CREATED_IN_WETRACK_SERVICE,Expiry_TIME,IS_PURCHASED," +
                        "TOKEN_HEADER,IS_NUMBER_ADDER,SCHEMA_NAME,purchase_mode,PACKAGE_NAME," +
                        "MAX_NUMBER)" +
                        "values (?,?,?,?,?,?,current_timestamp,current_timestamp,?,?,?,?,?,?,?,?,?)",
                homeModel.getId(),homeModel.getPhoneModel(),homeModel.getIpAddress(),homeModel.getCountryName(),homeModel.getOneSignalExternalUserId(),
                homeModel.getAppId(),false,LocalDateTime.now().plusHours(3).toString(),false,"",false, WETRACK + homeModel.getId(),"demo",homeModel.getPackageName(),
                1

        );
    }



    private int doandCreateLoginNumberOfTime(HomeModel homeModel) {
        return jdbcTemplateProvider.getTemplate().update("insert into WE_TRACK_USERS_NO_OF_LOGIN " +
                        "(USER_ID,MOBILE_MODEL,IP_ADDRESS,COUNTRY,ONE_SIGNAL_EXTERNAL_USERID,MOBILE_VERSION,Expiry_TIME,IS_PURCHASED," +
                        "CREATED_AT,UPDATED_AT,IS_USER_CREATED_IN_WETRACK_SERVICE,TOKEN_HEADER,IS_NUMBER_ADDER,SCHEMA_NAME,purchase_mode,MAX_NUMBER) " +
                        "values (?,?,?,?,?,?,?,?,current_timestamp,current_timestamp,?,?,?,?,?,?)",
                homeModel.getId(), homeModel.getPhoneModel(), homeModel.getIpAddress(), homeModel.getCountryName(),
                homeModel.getOneSignalExternalUserId(), homeModel.getAppId(), LocalDateTime.now().plusHours(3).toString(), false, false,
                "", false, WETRACK + homeModel.getId(),"demo",1
        );
    }

    private int doUpdateUser(HomeModel homeModel) {
        return jdbcTemplateProvider.getTemplate()
                .update("update WE_TRACK_USERS_NO_OF_LOGIN set USER_ID=?,MOBILE_MODEL=?,IP_ADDRESS=?,COUNTRY=?," +
                                "ONE_SIGNAL_EXTERNAL_USERID=?,MOBILE_VERSION=?,UPDATED_AT = current_timestamp,purchase_mode=?,EXPIRY_TIME=?" +
                                " where USER_ID=?", homeModel.getId(), homeModel.getPhoneModel(), homeModel.getIpAddress(),
                        homeModel.getCountryName(), homeModel.getOneSignalExternalUserId(), homeModel.getAppId(),homeModel.getMobilePhone(),
                        homeModel.getPhoneBrand(),
                        homeModel.getId()
                );
    }


}
