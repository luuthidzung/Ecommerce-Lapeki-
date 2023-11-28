package com.ecommerce.controller.client;

import com.ecommerce.model.CartLine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

@WebServlet("/customer/cart/remove")
public class CartItemRemoveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();
        Object object = httpSession.getAttribute("cart");
        String productID = req.getParameter("productID");
        if (object != null) {
            @SuppressWarnings("unchecked")
            Map<Integer, CartLine> map = (Map<Integer, CartLine>) object;
            map.remove(Integer.parseInt(productID));
            httpSession.setAttribute("cart", map);
        }
        resp.sendRedirect(req.getContextPath() + "/customer/cart");
    }
}