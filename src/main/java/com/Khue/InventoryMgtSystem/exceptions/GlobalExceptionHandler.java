package com.Khue.InventoryMgtSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.Khue.InventoryMgtSystem.dto.Response;

@ControllerAdvice
public class GlobalExceptionHandler { 

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Response> handlerALlException(Exception ex){// Bắt tát cả exception mà chưa handler nào xử lý
    Response response = Response.builder()
              .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
              .message(ex.getMessage())
              .build();
    return new ResponseEntity<>(response , HttpStatus.INTERNAL_SERVER_ERROR);
  }


  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Response> handlerNotFoundException(NotFoundException ex){// Bát lỗi không tim thấy data trong DB
    Response response = Response.builder()
              .status(HttpStatus.NOT_FOUND.value())
              .message(ex.getMessage())
              .build();
    return new ResponseEntity<>(response , HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(NameValueRequiredException.class)
  public ResponseEntity<Response> handlerNameValueRequiredException(NameValueRequiredException ex){// Bát lỗi về field name
    Response response = Response.builder()
              .status(HttpStatus.BAD_REQUEST.value())
              .message(ex.getMessage())
              .build();
    return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<Response> handlerInvalidCredentialsException(InvalidCredentialsException ex){// Bát lỗi về xác thực
    Response response = Response.builder()
              .status(HttpStatus.BAD_REQUEST.value())
              .message(ex.getMessage())
              .build();
    return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
  }

}
