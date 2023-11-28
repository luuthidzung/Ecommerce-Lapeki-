package com.ecommerce.controller.client;

import com.ecommerce.model.Cart;
import com.ecommerce.model.User;
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
import java.util.Objects;

@WebServlet("/customer/order")
public class CustomerOrdersServlet extends HttpServlet {
    CartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User account = (User) req.getSession().getAttribute("account");
        List<Cart> orders = null;
        try {
            orders = cartService.getCartByUserId(account.getUserID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("no orders: " + Objects.requireNonNull(orders).size());
        System.out.println("user id - " + account.getUserID());
        req.setAttribute("orders", orders);
        RequestDispatcher rd = req.getRequestDispatcher("/view/customer/account-orders.jsp");
        rd.forward(req, resp);
    }
}