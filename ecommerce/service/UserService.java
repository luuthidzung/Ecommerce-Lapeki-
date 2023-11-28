package com.ecommerce.service;

import com.ecommerce.model.User;

import java.sql.Date;
import java.util.List;

/**
 * @overview
 */
public interface UserService {
    /**
     * Insert new user
     */
    void insertUser(User user);

    /**
     * Update user information
     */
    void updateUser(User user);

    /**
     * Delete a user
     */
    void deleteUser(int userId);

    /**
     * Search user using id
     */
    User getUserById(int userId);

    /**
     * Search user using name
     */
    User getUserByUsername(String username);

    /**
     * Check exist email in
     */
    boolean checkExistEmail(String email);

    /**
     * Check exist username
     */
    boolean checkExistUsername(String username);

    /**
     * Check exist phone
     */
    boolean checkExistMobile(String mobile);

    /**
     * Login to website
     */
    User login(String username, String password);

    /**
     * Sign up a new account
     */
    boolean register(String username, String password, String fullname, String mobile, String email,
                     String address, String gender, Date dob);

    /**
     * get all user
     */
    List<User> getAllUsers();
}