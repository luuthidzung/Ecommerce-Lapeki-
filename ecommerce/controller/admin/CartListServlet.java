package com.ecommerce.controller.admin;

import com.ecommerce.model.Cart;
import com.ecommerce.service.CartService;
import com.ecommerce.service.impl.CartServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/cart/list")
public class CartListServlet extends HttpServlet {
    CartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Cart> cartList = cartService.getAllCarts();
        req.setAttribute("carts", cartList);
        resp.setContentType("text/html");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/view/admin/cart-list.jsp");
        requestDispatcher.forward(req, resp);
    }
}