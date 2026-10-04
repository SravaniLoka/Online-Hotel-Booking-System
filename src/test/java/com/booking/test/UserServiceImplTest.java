package com.booking.test;

import com.booking.dao.UserDAO;
import com.booking.model.User;
import com.booking.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDAO);
    }

    @Test
    void createUser_success() {

        User user = new User();

        user.setFullName("John Doe");
        user.setEmail("john@gmail.com");
        user.setPasswordHash("password123");
        user.setPhone("9876543210");
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        when(userDAO.create(user)).thenReturn(true);

        boolean result = userService.createUser(user);

        assertTrue(result);

        verify(userDAO, times(1)).create(user);
    }

    @Test
    void createUser_failure() {

        User user = new User();

        user.setFullName("John Doe");
        user.setEmail("john@gmail.com");
        user.setPasswordHash("password123");
        user.setPhone("9876543210");
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        when(userDAO.create(user)).thenReturn(false);

        boolean result = userService.createUser(user);

        assertFalse(result);

        verify(userDAO, times(1)).create(user);
    }

    @Test
    void getUserById_userFound() {

        Long userId = 1L;
        User user = new User();

        when(userDAO.findById(userId)).thenReturn(user);

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(user, result);

        verify(userDAO, times(1)).findById(userId);
    }

    @Test
    void getUserById_userNotFound() {

        Long userId = 999L;

        when(userDAO.findById(userId)).thenReturn(null);

        User result = userService.getUserById(userId);

        assertNull(result);

        verify(userDAO, times(1)).findById(userId);
    }

    @Test
    void getAllUsers_success() {

        User user1 = new User();
        User user2 = new User();

        List<User> users = Arrays.asList(user1, user2);

        when(userDAO.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(users, result);

        verify(userDAO, times(1)).findAll();
    }

    @Test
    void getAllUsers_emptyList() {

        when(userDAO.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userDAO, times(1)).findAll();
    }

    @Test
    void updateUser_success() {

        User user = new User();

        user.setUserId(1L);
        user.setFullName("John Doe");
        user.setEmail("john@gmail.com");
        user.setPasswordHash("password123");
        user.setPhone("9876543210");
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        when(userDAO.update(user)).thenReturn(true);

        boolean result = userService.updateUser(user);

        assertTrue(result);

        verify(userDAO, times(1)).update(user);
    }

    @Test
    void updateUser_failure() {

        User user = new User();

        user.setUserId(1L);
        user.setFullName("John Doe");
        user.setEmail("john@gmail.com");
        user.setPasswordHash("password123");
        user.setPhone("9876543210");
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        when(userDAO.update(user)).thenReturn(false);

        boolean result = userService.updateUser(user);

        assertFalse(result);

        verify(userDAO, times(1)).update(user);
    }

    @Test
    void deleteUser_success() {

        Long userId = 1L;

        when(userDAO.delete(userId)).thenReturn(true);

        boolean result = userService.deleteUser(userId);

        assertTrue(result);

        verify(userDAO, times(1)).delete(userId);
    }

    @Test
    void deleteUser_failure() {

        Long userId = 999L;

        when(userDAO.delete(userId)).thenReturn(false);

        boolean result = userService.deleteUser(userId);

        assertFalse(result);

        verify(userDAO, times(1)).delete(userId);
    }
}