package pt.kkosmico.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent implements Serializable {
    private Long id;
    private String name;
    private String email;
}
