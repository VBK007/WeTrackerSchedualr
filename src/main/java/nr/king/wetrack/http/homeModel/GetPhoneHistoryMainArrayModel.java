package nr.king.wetrack.http.homeModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPhoneHistoryMainArrayModel {
    private ArrayList<GetPhoneNumberHistoryModel> data;
}
