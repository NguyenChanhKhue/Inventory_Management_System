package com.Khue.InventoryMgtSystem.exceptions;

// Các yêu cầu bắt buộc về Field name
public class NameValueRequiredException extends RuntimeException {
  public NameValueRequiredException(String message){
    super(message);
  }
}
