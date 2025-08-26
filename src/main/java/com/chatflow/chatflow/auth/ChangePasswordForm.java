package com.chatflow.chatflow.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordForm {
  @NotBlank
  private String currentPassword;

  @NotBlank @Size(min = 8, max = 128)
  private String newPassword;

  @NotBlank
  private String confirmPassword;

  // getters/setters
  public String getCurrentPassword() { return currentPassword; }
  public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
  public String getNewPassword() { return newPassword; }
  public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
  public String getConfirmPassword() { return confirmPassword; }
  public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
