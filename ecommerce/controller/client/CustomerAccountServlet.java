package com.ecommerce.controller.client;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import com.ecommerce.service.impl.UserServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/customer/profile")
public class CustomerAccountServlet extends HttpServlet {
    UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/view/customer/account-profile.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User account = (User) session.getAttribute("account");
        // Unchangeable customer details
        int userID = account.getUserID();
        String username = account.getUsername();
        String email = account.getEmail();
        int roleID = account.getRoleID();
        // Changeable
        String mobile = req.getParameter("mobile");
        String fullname = req.getParameter("fullname");
        String gender = req.getParameter("gender");
        Date dob = Date.valueOf(req.getParameter("dob"));
        String address = req.getParameter("address");
        String changePassword = req.getParameter("change-password");
        String password = account.getPassword();
        if (changePassword != null) {//			String oldPassword = req.getParameter("old-password");
            String newPassword = req.getParameter("new-password");
            System.out.println(newPassword);
            password = newPassword;
        }
        account = new User(userID, username, password, fullname, mobile, email, address, gender, dob, roleID);
        userService.updateUser(account);
        session.setAttribute("account", account);
        resp.sendRedirect(req.getContextPath() + "/customer/profile");
    }
}