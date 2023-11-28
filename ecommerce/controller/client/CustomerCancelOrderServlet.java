package com.ecommerce.controller.client;

import com.ecommerce.service.CartService;
import com.ecommerce.service.impl.CartServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/customer/order/cancel")
public class CustomerCancelOrderServlet extends HttpServlet {
    CartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        cartService.updateCartStatus(id, "Canceled");
        resp.sendRedirect(req.getContextPath() + "/customer/order/detail?id=" + id);
    }
}