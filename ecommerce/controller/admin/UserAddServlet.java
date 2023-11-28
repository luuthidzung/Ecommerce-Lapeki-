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
import jakarta.servlet.http.Part;

import javax.annotation.processing.FilerException;
import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;

/**
 * @overview UserAddServlet is a controller servlet used to handle the use
 * case of Adding a new User to the database
 */
@WebServlet("/admin/user/add")
public class UserAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String eString = req.getParameter("e");
        if (eString != null) {
            if (eString.equals("1")) {
                req.setAttribute("errMsg", "Username existed");
            }
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("/view/admin/user-add.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String gender;
        User user = new User();
        try {
            Collection<Part> parts = req.getParts();
            for (Part part : parts) {
                if (part.getName().equals("email")) {
                    System.out.println(user.getEmail());
                    user.setEmail(Arrays.toString(part.getInputStream().readAllBytes()));
                } else if (part.getName().equals("username")) {
                    System.out.println(user.getUsername());
                    user.setUsername(Arrays.toString(part.getInputStream().readAllBytes()));
                } else if (part.getName().equals("fullname")) {
                    System.out.println(user.getFullname());
                    user.setFullname(Arrays.toString(part.getInputStream().readAllBytes()));
                } else if (part.getName().equals("password")) {
                    System.out.println(user.getPassword());
                    user.setPassword(Arrays.toString(part.getInputStream().readAllBytes()));
                } else if (part.getName().equals("mobile")) {
                    System.out.println(user.getMobile());
                    user.setMobile(Arrays.toString(part.getInputStream().readAllBytes()));
                } else if (part.getName().equals("address")) {
                    System.out.println(user.getAddress());
                    user.setAddress(Arrays.toString(part.getInputStream().readAllBytes()));
                } else if (part.getName().equals("gender")) {
                    String tmp = Arrays.toString(part.getInputStream().readAllBytes());
                    int genderID = Integer.parseInt(tmp);
                    if (genderID == 1) {
                        gender = "Male";
                    } else if (genderID == 2) {
                        gender = "Female";
                    } else {
                        gender = "Other";
                    }
                    System.out.println(gender);
                    user.setGender(gender);
                } else if (part.getName().equals("dob")) {
                    System.out.println(user.getDob());
                    user.setDob(Date.valueOf(Arrays.toString(part.getInputStream().readAllBytes())));
                } else if (part.getName().equals("roleID")) {
                    System.out.println(user.getRoleID());
                    String tmp = Arrays.toString(part.getInputStream().readAllBytes());
                    int roleID = Integer.parseInt(tmp);
                    user.setRoleID(roleID);
                }
            }
            userService.insertUser(user);
            resp.sendRedirect(req.getContextPath() + "/admin/user/list");
        } catch (FilerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/user/add?e=1");
        }
    }
}