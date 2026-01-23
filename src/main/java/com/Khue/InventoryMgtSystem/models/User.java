package com.Khue.InventoryMgtSystem.models;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Id;

import com.Khue.InventoryMgtSystem.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@AllArgsConstructor

@Table(name="users")
@Data
@Builder
public class User {
  @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

  @NotBlank(message = "name is required")
  private String name;

  @NotBlank(message = "email is required")
  @Column(unique = true)
  private String email;

  @NotBlank(message = "password is required")
  private String password;

  @NotBlank(message = "phoneNumber is required")
  @Column(name = "phone_number")
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  private UserRole role ;

  @OneToMany(mappedBy = "user")
  private List<Transaction> transaction;

  @Column(name = "create_at")
  private final LocalDateTime createAt =  LocalDateTime.now();

  @Override
  public String toString() {
    return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password
        + ", phoneNumber=" + phoneNumber + ", role=" + role + ", createAt=" + createAt + "]";
  }

}
