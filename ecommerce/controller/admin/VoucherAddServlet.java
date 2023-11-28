package com.ecommerce.controller.admin;

import com.ecommerce.model.Voucher;
import com.ecommerce.service.VoucherService;
import com.ecommerce.service.impl.VoucherServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @overview VoucherAddServlet is a controller servlet used to handle the use
 * case of Adding a new Voucher to the database
 */
@WebServlet(urlPatterns = "/admin/voucher/add")
public class VoucherAddServlet extends HttpServlet {
    VoucherService voucherService = new VoucherServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Voucher> vouchers = voucherService.getAllVouchers();
        req.setAttribute("vouchers", vouchers);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/view/admin/voucher-add.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Voucher voucher = new Voucher();
        try {
            Collection<Part> parts = req.getParts();
            for (Part part : parts) {
                System.out.println(part.getName());
                if (part.getName().equals("voucherCode")) {
                    voucher.setVoucherCode(Arrays.toString(part.getInputStream().readAllBytes()));
                    System.out.println(part.getName() + " - " + voucher.getVoucherCode());
                } else if (part.getName().equals("discountPercent")) {
                    voucher.setDiscountPercentage(Integer.parseInt(Arrays.toString(part.getInputStream().readAllBytes())));
                    System.out.println(part.getName() + " - " + voucher.getDiscountPercentage());
                } else if (part.getName().equals("expireDate")) {
                    voucher.setExpireDate(Date.valueOf(Arrays.toString(part.getInputStream().readAllBytes())));
                    System.out.println(part.getName() + " - " + voucher.getExpireDate());
                }
            }
            voucherService.insertVoucher(voucher);
            resp.sendRedirect("list");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}