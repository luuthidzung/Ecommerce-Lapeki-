package com.ecommerce.controller.client;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartLine;
import com.ecommerce.model.User;
import com.ecommerce.model.Voucher;
import com.ecommerce.payment.PayPalService;
import com.ecommerce.service.*;
import com.ecommerce.service.impl.*;
import com.ecommerce.tools.MailTools;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

@WebServlet("/customer/checkout")
public class CheckoutServlet extends HttpServlet {
    NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));
    CartService cartService = new CartServiceImpl();
    CartLineService cartLineService = new CartLineServiceImpl();
    VoucherService voucherService = new VoucherServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/view/customer/checkout.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User account = (User) session.getAttribute("account");
        String subTotal = req.getParameter("subtotal");
        String discount = req.getParameter("discount");
        String total = req.getParameter("total");
        String paymentMode = req.getParameter("paymentMode");
        String voucherID = req.getParameter("voucherID");
        Voucher voucher = voucherService.getVoucherByID(Integer.parseInt(voucherID));
        if (paymentMode.equals("PayPal")) { // handle online payment with pay pal
            Object temporaryCart = req.getSession().getAttribute("cart");
            @SuppressWarnings("unchecked")
            Map<Integer, CartLine> map = (Map<Integer, CartLine>) temporaryCart; // keep
            try {
                HttpSession httpSession = req.getSession();
                httpSession.setAttribute("voucher", voucher);
                PayPalService payPalService = new PayPalService();
                String approvalLink = payPalService.authorizePayment(map);
                resp.sendRedirect(approvalLink);
            } catch (PayPalRESTException ex) {
                req.setAttribute("errorMessage", ex.getMessage());
                ex.printStackTrace();
                req.getRequestDispatcher("/view/error-pages/error.jsp").forward(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Cart cart = new Cart();
            cart.setUser(account);
            cart.setOrderDate(new Date(System.currentTimeMillis()));
            cart.setStatus("Pending");
            cart.setPayment(paymentMode);
            cart.setVoucher(voucher);
            cart.setTotal(Long.parseLong(total) + 30000);
            // get inserted cartID
            int cartID = cartService.insertCart(cart);
            cart = cartService.getCartById(cartID);
            Object temporaryCart = session.getAttribute("cart");
            String message = "<p>Your order - <b>#" + cartID + "</b> has been successfully placed. <br>"
                    + " The chosen payment method is <b>" + cart.getPaymentMode() + "</b>. <br>"
                    + "The total charge is " + nf.format(cart.getTotal()) + " VND.<br>"
                    + "Please be noticed! </p> <hr>";
            StringBuilder orderDetail = new StringBuilder("<h3>Order detail - #" + cart.getCartID() + "</h3> <br> "
                    + "<table style=\"width: 100%;\">"
                    + "<thead>"
                    + "<th style=\"width:50%; text-align: left;\">Product name</th>"
                    + "<th style=\"width:20%; text-align: left;\">Price</th>"
                    + "<th style=\"width:10%; text-align: left;\">Quantity</th>"
                    + "<th style=\"width:20%; text-align: left;\">Subtotal</th>"
                    + "</thead>"
                    + "<tbody>");
            if (temporaryCart != null) {
                @SuppressWarnings("unchecked")
                Map<Integer, CartLine> map = (Map<Integer, CartLine>) temporaryCart;
                for (CartLine cartLine : map.values()) {
                    orderDetail.append("<tr>" + "<td>").append(cartLine.getProduct().getProductName()).append("</td>").append("<td>").append(nf.format(cartLine.getUnitPrice())).append("</td>").append("<td> ").append(cartLine.getQuantity()).append("</td>").append("<td>").append(nf.format(cartLine.getQuantity() * cartLine.getUnitPrice())).append("</td>").append("</tr>");
                    cartLine.setCart(cart);
                    cartLineService.insertCartLine(cartLine);
                }
            }
            orderDetail.append("</tbody></table>" + "<hr>" + "<p>Total : ").append(nf.format(Long.parseLong(subTotal))).append(" VND<br>").append("Shipping fee: 30.000VND<br>").append("Discount with voucher: -").append(nf.format(Long.parseLong(discount))).append(" VND<br>").append("Final total: ").append(nf.format(cart.getTotal())).append(" VND</p><hr>").append("<p>Please contact us if any inconveniences come up!<br>").append("Best regards,<br>").append("<b>Lapeki Ecommerce &copy;</b></p>");
            MailTools mailTools = new MailTools();
            try {
                mailTools.sendMail(account.getEmail(), "Lapeki - Order placed successfully", message + orderDetail);
            } catch (Exception e) {
                e.printStackTrace();
            }
            session.removeAttribute("cart");
            req.setAttribute("placed-id", cartID);
            RequestDispatcher rd = req.getRequestDispatcher("/view/customer/checkout-succeed.jsp");
            rd.forward(req, resp);
        }
    }
}