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
 * @overview ProductEditServlet is a servlet used to handle the use case of
 * editing a product.
 */
@WebServlet("/admin/product/edit")
public class ProductEditServlet extends HttpServlet {
    ProductService productService = new ProductServiceImpl();
    CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int productID = Integer.parseInt(req.getParameter("id"));
        Product product = productService.getProductByID(productID);
        List<Category> categories = categoryService.getAllCategories();
        req.setAttribute("product", product);
        req.setAttribute("categories", categories);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/view/admin/product-edit.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = new Product();
        try {
            Collection<Part> parts = req.getParts();
            for (Part part : parts) {
                if (part.getName().equals("productID")) {
                    product.setProductID(Integer.parseInt(Arrays.toString(part.getInputStream().readAllBytes()).substring(11)));
                    System.out.println(product.getProductID());
                } else if (part.getName().equals("productName")) {
                    product.setProductName(Arrays.toString(part.getInputStream().readAllBytes()));
                    System.out.println(product.getProductName());
                } else if (part.getName().equals("productPrice")) {
                    product.setProductPrice(Long.parseLong(Arrays.toString(part.getInputStream().readAllBytes())));
                    System.out.println(product.getProductPrice());
                } else if (part.getName().equals("instock")) {
                    product.setInstock(Integer.parseInt(Arrays.toString(part.getInputStream().readAllBytes())));
                    System.out.println(product.getInstock());
                } else if (part.getName().equals("productDesc")) {
                    product.setProductDesc(Arrays.toString(part.getInputStream().readAllBytes()));
                    System.out.println(product.getProductDesc());
                } else if (part.getName().equals("category")) {
                    product.setCategory(categoryService.getCategoryByID(Integer.parseInt(Arrays.toString(part.getInputStream().readAllBytes()))));
                    System.out.println(product.getCategory());
                } else if (part.getName().equals("productImg")) {
                    System.out.println(product.getProductImg());
                    product.setProductImg(Arrays.toString(part.getInputStream().readAllBytes()));
                }
//				System.out.println(product.toString());
            }
            productService.updateProduct(product);
            resp.sendRedirect(req.getContextPath() + "/admin/product/list");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}