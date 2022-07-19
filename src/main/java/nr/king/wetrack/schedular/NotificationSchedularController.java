package nr.king.wetrack.schedular;

import com.fasterxml.jackson.databind.ObjectMapper;
import nr.king.wetrack.jdbc.JdbcTemplateProvider;
import nr.king.wetrack.utils.CommonUtils;
import nr.king.wetrack.utils.HttpUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationSchedularController {

    @Autowired
    private JdbcTemplateProvider jdbcTemplateProvider;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LogManager.getLogger(NotificationSchedularController.class);






}
