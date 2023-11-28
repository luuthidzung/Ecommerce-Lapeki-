package com.ecommerce.dao;

import com.ecommerce.model.Cart;

import java.util.List;

/**
 * @overview CartDAO is an interface which facilitates communication to database
 */
public interface CartDAO {
    /**
     * Insert a new cart record into Cart table
     */
    int insertCart(Cart cart);

    /**
     * Update cart in database
     */
    boolean updateCart(Cart cart);

    /**
     * Update cart status in database
     */
    boolean updateCartStatus(int cartID, String cartStatus);

    /**
     * Delete a cart in database by using it's id
     */
    boolean deleteCart(int id);

    /**
     * Get Cart from database by using it's id
     */
    Cart getCartById(int cartId);

    /**
     * Get Cart from database by using it's user id
     */
    List<Cart> getCartByUserId(int userId);

    /**
     * Get all carts in database
     */
    List<Cart> getAllCarts();

    /**
     * Search all carts by using email
     */
    List<Cart> searchCartByEmail(String email);

    /**
     * Search all carts by using phone
     */
    List<Cart> searchCartByPhone(String phone);
}