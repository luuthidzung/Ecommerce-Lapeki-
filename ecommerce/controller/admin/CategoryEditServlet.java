package com.ecommerce.controller.admin;

import com.ecommerce.model.Category;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.impl.CategoryServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @overview CategoryEditServlet is a servlet used to handle the use case of editing a category
 */
@WebServlet("/admin/category/edit")
public class CategoryEditServlet extends HttpServlet {
    CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int categoryID = Integer.parseInt(req.getParameter("id"));
        Category category = categoryService.getCategoryByID(categoryID);
        List<Category> categories = categoryService.getAllCategories();
        req.setAttribute("categories", categories);
        req.setAttribute("category", category);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/view/admin/category-edit.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category category = new Category();
        try {
            Collection<Part> parts = req.getParts();
            for (Part part : parts) {
                if (part.getName().equals("categoryName")) {
                    System.out.println(category.getCategoryName());
                    category.setCategoryName(Arrays.toString(part.getInputStream().readAllBytes()));
                } else if (part.getName().equals("categoryID")) {
                    String tmp = Arrays.toString(part.getInputStream().readAllBytes());
                    int categoryID = Integer.parseInt(tmp);
                    category.setCategoryID(categoryID);
                    System.out.println(category.getCategoryID());
                }
                categoryService.updateCategory(category);
                resp.sendRedirect(req.getContextPath() + "/admin/category/list");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}