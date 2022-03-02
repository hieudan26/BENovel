package com.socialmedia.loginandregistration.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "Thiếu Email")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotEmpty(message = "Thiếu password")
//    @Min(value = 8, message = "Password phải từ 8 kí tự trở lên")
    private String password;

    @NotEmpty(message = "Thiếu tên")
    private String firstName;

    @NotEmpty(message = "Thiếu họ")
    private String lastName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
