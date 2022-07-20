package nr.king.wetrack.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nr.king.wetrack.http.homeModel.HomeModel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPageHistoryNumberModel {
    Integer pageLimit= 1;
    String phoneNumber;
    Integer start= 0;
    private HomeModel homeModel;
}
