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
public  class Notification implements Serializable {
    private Boolean content_available;

    private String subtitle;

    private String Title;

    private String OrganizationId;

    private String body;

    private String priority;

  }
