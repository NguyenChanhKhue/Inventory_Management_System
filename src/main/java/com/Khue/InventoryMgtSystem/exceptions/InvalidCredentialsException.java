package com.Khue.InventoryMgtSystem.exceptions;

// Các ngoại lệ xác thực 
public class InvalidCredentialsException extends RuntimeException{
  public InvalidCredentialsException(String message){
    super(message);
  }
}
