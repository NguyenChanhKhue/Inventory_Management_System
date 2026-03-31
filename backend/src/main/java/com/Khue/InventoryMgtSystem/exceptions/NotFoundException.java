package com.Khue.InventoryMgtSystem.exceptions;


// xử lý các data không được tìm thấy trong DB , mặc dù user đã request đúng.
public class NotFoundException extends RuntimeException{
  public NotFoundException(String message){
    super(message); // hiển thị thông báo
  }
}
