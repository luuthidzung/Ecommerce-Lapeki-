package com.ecommerce.service.impl;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.dao.impl.UserDAOImpl;
import com.ecommerce.model.User;
import com.ecommerce.service.UserService;

import java.sql.Date;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDAO userDAO = new UserDAOImpl();

    /**
     * Insert new user
     */
    @Override
    public void insertUser(User user) {
        userDAO.insertUser(user);
    }

    /**
     * Update user information
     */
    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    /**
     * Delete a user
     */
    @Override
    public void deleteUser(int userID) {
        userDAO.deleteUser(userID);
    }

    /**
     * get all user
     */
    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Search user using id
     */
    @Override
    public User getUserById(int userId) {
        return userDAO.getUserByID(userId);
    }

    /**
     * Search user using name
     */
    @Override
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    /**
     * Check exist email in
     */
    @Override
    public boolean checkExistEmail(String email) {
        return userDAO.checkExistEmail(email);
    }

    /**
     * Check exist user's name
     */
    @Override
    public boolean checkExistUsername(String username) {
        return userDAO.checkExistUsername(username);
    }

    /**
     * Login to website
     */
    @Override
    public User login(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * Sign up a new account
     */
    @Override
    public boolean register(String username, String password, String fullname, String mobile, String email,
                            String address, String gender, Date dob) {
        if (userDAO.checkExistEmail(email) || userDAO.checkExistUsername(username) || userDAO.checkExistPhone(mobile)) {
            return false;
        }
        userDAO.insertUser(new User(username, password, fullname, mobile, email, address, gender, dob));
        return true;
    }

    @Override
    public boolean checkExistMobile(String mobile) {
        return userDAO.checkExistPhone(mobile);
    }
}