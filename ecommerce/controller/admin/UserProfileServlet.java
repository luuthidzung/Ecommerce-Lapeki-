package com.ecommerce.controller.admin;

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

@WebServlet("/admin/profile")
public class UserProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/view/admin/profile.jsp");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("account");
        req.setAttribute("user", user);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserServiceImpl();
        int userID = Integer.parseInt(req.getParameter("userID"));
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fullname = req.getParameter("fullname");
        String mobile = req.getParameter("mobile");
        String address = req.getParameter("address");
        String email = req.getParameter("email");
        System.out.println("Userprofile: " + email);
        String gender;
        String genderCode = req.getParameter("gender");
        int code = Integer.parseInt(genderCode);
        if (code == 1) {
            gender = "Male";
        } else if (code == 2) {
            gender = "Female";
        } else {
            gender = "Other";
        }
        Date dob = Date.valueOf(req.getParameter("dob"));
        int roleID = Integer.parseInt(req.getParameter("roleID"));
        User user = new User(userID, username, password, fullname, mobile, email, address, gender, dob, roleID);
        userService.updateUser(user);
        HttpSession session = req.getSession();
        session.setAttribute("account", user);
        resp.sendRedirect(req.getContextPath() + "/admin/profile");
    }
}