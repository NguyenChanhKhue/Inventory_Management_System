package com.Khue.InventoryMgtSystem.dto;

import java.time.LocalDateTime;
import java.util.List;



import com.Khue.InventoryMgtSystem.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

	private Long id;

  private String name;

  private String email;

  @JsonIgnore
  private String password; // khong convert field java -> json


  private String phoneNumber;


  private UserRole role ;

  private List<TransactionDTO> transaction;

  private  LocalDateTime createAt;


}
