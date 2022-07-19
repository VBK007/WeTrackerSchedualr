package nr.king.wetrack.http.purchaseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpiTransactionValue {
    private String apiId;
    private ArrayList<PremiumModels> valueList;
 }
