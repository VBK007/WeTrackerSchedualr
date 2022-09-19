package nr.king.wetrack.constant;

public class QueryConstants {
    public static final String selectNumberWithToken ="select USER_ID,NUMBER,ENABLE_NOTIFY,TOKEN,HEADER_TOKEN,NICK_NAME,CREATED_AT,UPDATED_AT,PREVIOUS_TIME from " +
            "UPDATE_NOTIFCATION_NUMBER where ENABLE_NOTIFY=true";


    public static final String SELECT_USER_DETAILS_COUNT ="select count(*) from WE_TRACK_USERS where USER_ID=?";
    public static final String SELECT_NUMBER_DETAILS_COUNT ="select count(*) from NUMBER_FOR_USERS where USER_ID=?";
    public static final String UPDATE_TOKEN_HEADER_IN_NUMBER_MOBILE ="update NUMBER_FOR_USERS set NUMBER=?,TOKEN_HEADER=?," +
            "COUNTRY_CODE=?,UPDATED_AT=current_timestamp,NICK_NAME=? where USER_ID=? and NUMBER=?";

    public  static  final  String  ISNUMBER_HAVING_USER="select USER_ID,NUMBER from NUMBER_FOR_USERS where USER_ID=? and NUMBER=?";


    public static final String UPDATE_DETAILS_IN_NUMBER_MOBILE ="update NUMBER_FOR_USERS set NUMBER=?," +
            "COUNTRY_CODE=?,UPDATED_AT=current_timestamp,NICK_NAME=? where USER_ID=? and NUMBER=?";

    public static final String SELECT_USER_EXPIRY_TIME ="select Expiry_TIME,IS_USER_CREATED_IN_WETRACK_SERVICE,purchase_mode,MAX_NUMBER from WE_TRACK_USERS where USER_ID=?";

    public static final String SELECT_USER_EXPIRY_TIME_WITH_ACCOUNT_DETAILS ="select Expiry_TIME,IS_USER_CREATED_IN_WETRACK_SERVICE,MAX_NUMBER," +
            "purchase_mode from WE_TRACK_USERS where USER_ID=?";

    public static final String GET_ALL_MOBILE_NUMBER ="select USER_ID,NUMBER,TOKEN_HEADER,COUNTRY_CODE,NICK_NAME,PUSH_TOKEN,EXPIRY_TIME from NUMBER_FOR_USERS where USER_ID=?";

    public static final String UPDATE_TOKEN_HEADER = "update WE_TRACK_USERS  set TOKEN_HEADER=? where USER_ID=?";

    public static final String UPDATE_TIMING_USER_DATA = "update WE_TRACK_USERS  set Expiry_TIME=? where USER_ID=?";

    public static final String UPDATE_PURCHASE_USER_TIME_ZONE = "update WE_TRACK_USERS  set purchase_mode=?,MAX_NUMBER=?,IS_PURCHASED=?,Expiry_TIME=?" +
            " where USER_ID=?";


    public static final String UPDATE_PURCHASE_DETAILS = "UPDATE PURCHASED_DETAILS set PURCHASE_MODE=?,PURCHASE_PLATFORM=?,COUNTRY=?,AMOUNT=?,TRANSATION_ID=?,TRANSACTION_REMARK=?," +
            "EXPIRY_DATE=?,UPDATED_AT=current_timestamp where USER_ID=?";

    public static final String INSERT_PURCHASE_DETAILS = "insert into  PURCHASED_DETAILS (USER_ID,PURCHASE_MODE,PURCHASE_PLATFORM,COUNTRY,AMOUNT," +
         "TRANSATION_ID,TRANSACTION_REMARK,EXPIRY_DATE,CREATED_AT,UPDATED_AT) values (?,?,?,?,?,?,?,?,current_timestamp,current_timestamp)";

    public static final String INSERT_PURCHASE_DETAILS_HISTORY = "insert into  PURCHASED_DETAILS_HISTORY (USER_ID,PURCHASE_MODE,PURCHASE_PLATFORM,COUNTRY,AMOUNT," +
         "TRANSATION_ID,TRANSACTION_REMARK,EXPIRY_DATE,CREATED_AT,UPDATED_AT) values (?,?,?,?,?,?,?,?,current_timestamp,current_timestamp)";

    public static  final String GET_UPI_VALUES = "select UPI_ID,PURCHASE_TYPE,PURCHASE_DESCRIBITION,MONEY_IN_INR,MONEY_IN_USD,COLOR_CODE,COLOR_BAR,CREATED_AT,UPDATED_AT from UPI_DETAILS";

    public static  final  String UPDATE_PUSH_NOTIFICATION =
            "update update_notifcation_number set ENABLE_NOTIFY=?, TOKEN =?, HEADER_TOKEN=?, NICK_NAME=? where USER_ID=? and NUMBER=?";

      public static  final String UPDATE_PREVIOUS_TIME = "update update_notifcation_number set PREVIOUS_TIME=? where USER_ID=? and NUMBER=?";

    public static  final  String INSERT_NUMBER_VALUE =
            "insert into update_notifcation_number (USER_ID,NUMBER,ENABLE_NOTIFY,TOKEN,CREATED_AT,UPDATED_AT,HEADER_TOKEN,NICK_NAME,PREVIOUS_TIME) " +
                    "values " +
                    "(?,?,?,?,current_timestamp,current_timestamp,?,?,'')";


    public static final String REMOVE_DEMO_USER_NOTIFICATION
            ="SELECT user_id,package_name,purchase_mode,expiry_time from we_track_users_no_of_login where package_name=? and purchase_mode!='demo'";


    public static final String GET_USER_CONTAIN_NUMBER ="select USER_ID,NUMBER from NUMBER_FOR_USERS where USER_ID=?";
    public static final String UPDATE_NOTIFCATION_DIABLE_NOTFIY ="update update_notifcation_number set ENABLE_NOTIFY=? where USER_ID=? and NUMBER=?";

    public static final String CONSTANT_PACKAGE="com.withcodeplays.familytracker";

}
