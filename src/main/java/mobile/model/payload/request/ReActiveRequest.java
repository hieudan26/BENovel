package mobile.model.payload.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReActiveRequest {
    @NotEmpty
    String email;
}
