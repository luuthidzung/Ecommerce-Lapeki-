package com.ecommerce.controller.client;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.impl.ProductServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/product/search")
public class ProductSearchByName extends HttpServlet {
    ProductService productService = new ProductServiceImpl();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("pname");
        System.out.println(name);
        // a list of all search result
        List<Product> results = productService.searchProductByName(name);
        // a list of products in ascending order of price
        List<Product> lowPrice = new ArrayList<>(results);
        lowPrice.sort((o1, o2) -> -compareProductPrice(o1, o2));
        List<Product> highPrice = new ArrayList<>(results);
        highPrice.sort(this::compareProductPrice);
        List<Product> az = new ArrayList<>(results);
        az.sort(this::compareProductName);
        List<Product> za = new ArrayList<>(results);
        za.sort((o1, o2) -> -compareProductName(o1, o2));
        List<Category> categories = getCategories(results);
        System.out.println("No. categories: " + categories.size());
        req.setAttribute("results", results);
        req.setAttribute("lowPrice", lowPrice);
        req.setAttribute("highPrice", highPrice);
        req.setAttribute("az", az);
        req.setAttribute("za", za);
        req.setAttribute("categories", categories);
        req.setAttribute("pname", name);
        req.getRequestDispatcher("/view/customer/product-search-by-name.jsp").forward(req, resp);
    }

    /**
     * Comapre products by their prices
     */
    public int compareProductPrice(Product o1, Product o2) {
        return Long.compare(o2.getProductPrice(), o1.getProductPrice());
    }

    /**
     * Comapre products by their name
     */
    public int compareProductName(Product o1, Product o2) {
        return o1.getProductName().compareTo(o2.getProductName());
    }

    public List<Category> getCategories(List<Product> list) {
        List<Category> cat = new ArrayList<>();
        for (Product p : list) {
            if (!cat.contains(p.getCategory())) {
                cat.add(p.getCategory());
            }
        }
        return cat;
    }
}