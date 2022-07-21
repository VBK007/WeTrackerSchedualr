package nr.king.wetrack.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationModel {
    boolean enable =false;
    Long numberId;
    String userId;
    String pushToken="";
    String headerToken="";
    String nickName="";
}
