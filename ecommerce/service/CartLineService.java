package com.ecommerce.service;

import com.ecommerce.model.CartLine;

import java.util.List;

public interface CartLineService {
    /**
     * Insert new cartline
     */
    void insertCartLine(CartLine cartLine);

    /**
     * Retrieve all cartLine belong to the desired cart
     *
     * @param cartID the id of cartID
     */
    List<CartLine> getCartLineByCartID(int cartID);
}