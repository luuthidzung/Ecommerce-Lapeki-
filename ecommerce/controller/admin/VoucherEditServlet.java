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
 * @overview VoucherEditServlet is a servlet used to handle the use-case of
 * editing a voucher
 */
@WebServlet("/admin/voucher/edit")
public class VoucherEditServlet extends HttpServlet {
    VoucherService voucherService = new VoucherServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int voucherID = Integer.parseInt(req.getParameter("id"));
        Voucher voucher = voucherService.getVoucherByID(voucherID);
        List<Voucher> vouchers = voucherService.getAllVouchers();
        req.setAttribute("vouchers", vouchers);
        req.setAttribute("voucher", voucher);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/view/admin/voucher-edit.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Voucher voucher = new Voucher();
        try {
            Collection<Part> parts = req.getParts();
            for (Part part : parts) {
                if (part.getName().equals("voucherID")) {
                    voucher.setVoucherID(Integer.parseInt(Arrays.toString(part.getInputStream().readAllBytes())));
                } else if (part.getName().equals("voucherCode")) {
                    voucher.setVoucherCode(Arrays.toString(part.getInputStream().readAllBytes()));
                } else if (part.getName().equals("discountPercent")) {
                    voucher.setDiscountPercentage(Integer.parseInt(Arrays.toString(part.getInputStream().readAllBytes())));
                } else if (part.getName().equals("expireDate")) {
                    voucher.setExpireDate(Date.valueOf(Arrays.toString(part.getInputStream().readAllBytes())));
                }
            }
            voucherService.updateVoucher(voucher);
            resp.sendRedirect("list");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}