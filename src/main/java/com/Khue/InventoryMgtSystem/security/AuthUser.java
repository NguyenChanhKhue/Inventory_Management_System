package com.Khue.InventoryMgtSystem.security;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Khue.InventoryMgtSystem.models.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
// sau khi login , spring se dung class nay de check user , khong dung entity User .
public class AuthUser implements UserDetails{

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(user.getRole().name())); // tra ve list role cua user
  }

  @Override
  public @Nullable String getPassword() {
   return user.getPassword();
  }

  @Override
  public String getUsername() {
   return user.getName();
  }

  
}
