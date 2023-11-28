package com.ecommerce.controller.admin;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.impl.CategoryServiceImpl;
import com.ecommerce.service.impl.ProductServiceImpl;
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
 * @overview ProductAddServlet is a controller servlet used to handle the use
 * case of Adding a new Product to the database
 */
@WebServlet(urlPatterns = "/admin/product/add")
public class ProductAddServlet extends HttpServlet {
    ProductService productService = new ProductServiceImpl();
    CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = categoryService.getAllCategories();
        System.out.println(categories.size());
        req.setAttribute("categories", categories);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/view/admin/product-add.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = new Product();
        try {
            Collection<Part> parts = req.getParts();
            for (Part part : parts) {
                if (part.getName().equals("productName")) {
                    product.setProductName(Arrays.toString(part.getInputStream().readAllBytes()));
                    System.out.println(part.getName() + " - " + product.getProductName());
                } else if (part.getName().equals("productPrice")) {
                    product.setProductPrice(Long.parseLong(Arrays.toString(part.getInputStream().readAllBytes())));
                    System.out.println(part.getName() + " - " + product.getProductPrice());
                } else if (part.getName().equals("instock")) {
                    product.setInstock(Integer.parseInt(Arrays.toString(part.getInputStream().readAllBytes())));
                    System.out.println(part.getName() + " - " + product.getInstock());
                } else if (part.getName().equals("productDesc")) {
                    product.setProductDesc(Arrays.toString(part.getInputStream().readAllBytes()));
                    System.out.println(part.getName() + " - " + product.getProductDesc());
                } else if (part.getName().equals("category")) {
                    product.setCategory(categoryService.getCategoryByID(Integer.parseInt(Arrays.toString(part.getInputStream().readAllBytes()))));
                    System.out.println(part.getName() + " - " + product.getCategory());
                } else if (part.getName().equals("productImg")) {
                    product.setProductImg(Arrays.toString(part.getInputStream().readAllBytes()));
                    String[] imgs = Arrays.toString(part.getInputStream().readAllBytes()).split(",");
                    for (String img : imgs) {
                        System.out.println(img);
                    }
                    System.out.println(part.getName() + " - " + product.getProductImg());
                }
            }
            productService.insertProduct(product);
            resp.sendRedirect("list");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}