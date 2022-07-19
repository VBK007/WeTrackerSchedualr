package nr.king.wetrack.controller;

import nr.king.wetrack.http.NotificationModel;
import nr.king.wetrack.services.UpdateNotificationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateNotificationController {

    @Autowired
    private UpdateNotificationServices updateNotificationServices;

    @PostMapping("/v{version:[1]}/update-status")
    public ResponseEntity updateStatus (@RequestBody NotificationModel notificationModel)
    {
        return updateNotificationServices.updateNotifcationService(notificationModel);
    }
}
