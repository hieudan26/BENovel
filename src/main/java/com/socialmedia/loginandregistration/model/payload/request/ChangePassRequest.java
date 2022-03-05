package com.socialmedia.loginandregistration.model.payload.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChangePassRequest {
    @NotEmpty
    String password;

    @NotEmpty(message = "Thiếu password")
    @Size(min = 8, message = "Password phải từ 8 kí tự trở lên")
    String newPassword;
}
