package nr.king.wetrack.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class PushData implements Serializable {
    private Boolean content_available;
    private String bodyText;
    private String sound;
    private String organization;
    private String priority;

}
