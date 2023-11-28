package com.ecommerce.service.impl;

import com.ecommerce.dao.CartDAO;
import com.ecommerce.dao.impl.CartDAOImpl;
import com.ecommerce.model.Cart;
import com.ecommerce.service.CartService;

import java.util.List;

/**
 * @overview
 */
public class CartServiceImpl implements CartService {
    private final CartDAO cartDAO = new CartDAOImpl();

    /**
     * Insert new cart
     */
    @Override
    public int insertCart(Cart cart) {
        return cartDAO.insertCart(cart);
    }

    /**
     * Update cart information
     */
    @Override
    public void updateCartStatus(int cartID, String cartStatus) {
        cartDAO.updateCartStatus(cartID, cartStatus);
    }

    /**
     * get cart using id
     */
    @Override
    public Cart getCartById(int cartId) {
        return cartDAO.getCartById(cartId);
    }

    /**
     * Retrieve all carts
     */
    @Override
    public List<Cart> getAllCarts() {
        return cartDAO.getAllCarts();
    }

    @Override
    public List<Cart> getCartByUserId(int userId) {
        return cartDAO.getCartByUserId(userId);
    }
}