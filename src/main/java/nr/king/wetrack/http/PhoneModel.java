package nr.king.wetrack.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhoneModel {
    private String id;
    private String nickName;
    private  String phoneNumber;
    private String countryCode;
    private String pushToken;
}
