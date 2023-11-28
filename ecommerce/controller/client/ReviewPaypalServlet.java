package com.ecommerce.controller.client;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartLine;
import com.ecommerce.model.User;
import com.ecommerce.model.Voucher;
import com.ecommerce.payment.PayPalService;
import com.ecommerce.service.CartLineService;
import com.ecommerce.service.CartService;
import com.ecommerce.service.impl.CartLineServiceImpl;
import com.ecommerce.service.impl.CartServiceImpl;
import com.ecommerce.tools.MailTools;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
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

/**
 * Servlet implementation class ReviewPaypalServlet
 */
@WebServlet("/customer/review_payment")
public class ReviewPaypalServlet extends HttpServlet {
    NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));
    CartLineService cartLineService = new CartLineServiceImpl();
    private static final long serialVersionUID = 1L;
    CartService cartService = new CartServiceImpl();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String paymentId = req.getParameter("paymentId");
        req.getParameter("PayerID");
        try {
            PayPalService paymentService = new PayPalService();
            Payment payment = paymentService.getPaymentDetail(paymentId);
            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0); //Fix it multiple transaction
            ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();
            double tempTotal = Double.parseDouble(transaction.getAmount().getTotal());
            long total = (long) tempTotal;
            req.setAttribute("payer", payerInfo);
            req.setAttribute("transaction", transaction);
            req.setAttribute("shippingAddress", shippingAddress);
            //Insert cart to database
            HttpSession session = req.getSession();
            User account = (User) session.getAttribute("account");
            Voucher voucher = (Voucher) session.getAttribute("voucher");
            Cart cart = new Cart();
            cart.setUser(account);
            cart.setOrderDate(new Date(System.currentTimeMillis()));
            cart.setStatus("Checked out");
            cart.setPayment("PayPal");
            cart.setVoucher(voucher);
            cart.setTotal(total);
            int cartID = cartService.insertCart(cart);
            cart.setCartID(cartID);
            Object temporaryCart = session.getAttribute("cart");
            String message = "<p>Your order - <b>#" + cartID + "</b> has been successfully placed. <br>"
                    + " The chosen payment method is <b> PayPal</b>. <br>"
                    + "The total charge is " + total + " VND.<br>"
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
                orderDetail.append("</tbody></table>" + "<hr>" + "<p>Total : ").append(transaction.getAmount().getDetails().getSubtotal()).append(" VND<br>").append("Shipping fee: 30.000VND<br>").append("Discount with voucher: -").append(nf.format(voucher.getDiscountPercentage() * total / 100)).append(" VND<br>").append("Final total: ").append(nf.format(total)).append(" VND</p><hr>").append("<p>Please contact us if any inconveniences come up!<br>").append("Best regards,<br>").append("<b>Lapeki Ecommerce &copy;</b></p>");
                MailTools mailTools = new MailTools();
                try {
                    mailTools.sendMail(account.getEmail(), "Lapeki - Order placed successfully", message + orderDetail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            session.removeAttribute("voucher");
            session.removeAttribute("cart");
            req.setAttribute("placed-id", cartID);
            RequestDispatcher rd = req.getRequestDispatcher("/view/customer/checkout-succeed.jsp");
            rd.forward(req, resp);
        } catch (PayPalRESTException ex) {
            req.setAttribute("errorMessage", ex.getMessage());
            ex.printStackTrace();
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}