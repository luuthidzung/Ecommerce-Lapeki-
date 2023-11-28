package com.ecommerce.controller.admin;

import com.ecommerce.service.VoucherService;
import com.ecommerce.service.impl.VoucherServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @overview VoucherDeleteServlet is a servlet used to handle the use-case of
 * deleting voucher from database
 */
@WebServlet("/admin/voucher/delete")
public class VoucherDeleteServlet extends HttpServlet {
    VoucherService voucherService = new VoucherServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        voucherService.deleteVoucher(Integer.parseInt(id));
        resp.sendRedirect(req.getContextPath() + "/admin/voucher/list");
    }
}