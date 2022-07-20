package nr.king.wetrack.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.lang.Boolean;
import java.lang.String;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FcmModelData implements Serializable {
  private String to;
  private Notification notification;
  private PushData data;
}
