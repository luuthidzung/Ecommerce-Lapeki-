package com.ecommerce.controller.client;

import com.ecommerce.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Authorization role of account
 */
@WebServlet("/waiting")
public class WaitingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            req.setAttribute("username", user.getUsername());
            if (user.getRoleID() == 1) { //If user is administrator
                resp.sendRedirect(req.getContextPath() + "/admin"); //redirect to admin page
            } else {
                resp.sendRedirect(req.getContextPath() + "/"); //redirect to Home page
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}