package com.chatflow.chatflow.auth;

/* jakarta is used for input validation */
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupForm {
  @NotBlank @Size(min = 3, max = 50)
  private String username;

  @NotBlank @Size(min = 6, max = 100)
  private String password;

  @NotBlank @Size(min = 6, max = 100)
  private String confirmPassword;
}
