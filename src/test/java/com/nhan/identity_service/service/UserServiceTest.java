package com.nhan.identity_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nhan.identity_service.dto.response.UserResponse;
import com.nhan.identity_service.entity.Users;
import com.nhan.identity_service.exception.AppException;
import com.nhan.identity_service.exception.ErrorCode;
import com.nhan.identity_service.mapper.UserMapper;
import com.nhan.identity_service.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private UserMapper userMapper;

  @InjectMocks private UserService userService;

  private Users mockUser;
  private UserResponse mockUserResponse;

  @BeforeEach
  void setUp() {
    mockUser =
        Users.builder()
            .id(1L)
            .userName("testuser")
            .email("test@example.com")
            .firstName("Test")
            .lastName("User")
            .createdAt(LocalDateTime.now())
            .build();

    mockUserResponse = new UserResponse();
    mockUserResponse.setId(1L);
    mockUserResponse.setUserName("testuser");
    mockUserResponse.setEmail("test@example.com");
    mockUserResponse.setFirstName("Test");
    mockUserResponse.setLastName("User");
  }

  @Test
  void getMyInfo_validUser_success() {
    // Mock SecurityContext
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("testuser");
    SecurityContextHolder.setContext(securityContext);

    when(userRepository.findByuserName("testuser")).thenReturn(Optional.of(mockUser));
    when(userMapper.toUserResponse(mockUser)).thenReturn(mockUserResponse);

    // When
    UserResponse result = userService.getMyInfo();

    // Then
    assertNotNull(result);
    assertEquals("testuser", result.getUserName());
    assertEquals("test@example.com", result.getEmail());

    verify(userRepository, times(1)).findByuserName("testuser");
  }

  @Test
  void getMyInfo_userNotFound_throwsAppException() {
    // Mock SecurityContext
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("unknownuser");
    SecurityContextHolder.setContext(securityContext);

    when(userRepository.findByuserName("unknownuser")).thenReturn(Optional.empty());

    // When & Then
    AppException exception = assertThrows(AppException.class, () -> userService.getMyInfo());
    assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());

    verify(userRepository, times(1)).findByuserName("unknownuser");
  }

  @Test
  void listUser_success() {
    // Given
    when(userRepository.findAll()).thenReturn(List.of(mockUser));
    when(userMapper.toUserResponse(mockUser)).thenReturn(mockUserResponse);

    // When
    List<UserResponse> result = userService.listUser();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("testuser", result.get(0).getUserName());

    verify(userRepository, times(1)).findAll();
  }
}
