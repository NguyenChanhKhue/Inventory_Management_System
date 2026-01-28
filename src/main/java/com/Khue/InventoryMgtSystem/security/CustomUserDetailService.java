package com.Khue.InventoryMgtSystem.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Khue.InventoryMgtSystem.exceptions.NotFoundException;
import com.Khue.InventoryMgtSystem.models.User;
import com.Khue.InventoryMgtSystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

// class này để spring lấy thông tin user khi authenticate
public class CustomUserDetailService implements UserDetailsService{
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
   User user = userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("User email not found"));
  
   return AuthUser.builder()
                  .user(user)
                  .build();
  }
}
