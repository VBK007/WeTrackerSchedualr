package nr.king.wetrack.http.homeModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPhoneNumberHistoryModel {
    public String status;
    public String phoneNumber;
    public String timeStamp;
    public String nickName;
}
