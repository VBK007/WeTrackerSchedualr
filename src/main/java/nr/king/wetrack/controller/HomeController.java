package nr.king.wetrack.controller;


import nr.king.wetrack.http.homeModel.HomeModel;
import nr.king.wetrack.services.HomeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController extends  BaseController{

    @Autowired
    private HomeServices homeServices;

    @PostMapping("/v{version:[1]}/create-deviceUser")
    public ResponseEntity storeUserData(@RequestBody HomeModel homeModel)
    {
        return homeServices.storeUsers(homeModel);
    }


}
