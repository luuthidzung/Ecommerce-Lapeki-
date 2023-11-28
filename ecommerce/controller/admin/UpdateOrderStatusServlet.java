package com.ecommerce.controller.admin;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartLine;
import com.ecommerce.service.CartLineService;
import com.ecommerce.service.CartService;
import com.ecommerce.service.impl.CartLineServiceImpl;
import com.ecommerce.service.impl.CartServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/order/status/update")
public class UpdateOrderStatusServlet extends HttpServlet {
    CartService cartService = new CartServiceImpl();
    CartLineService cartLineService = new CartLineServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cartId = Integer.parseInt(req.getParameter("cartID"));
        List<CartLine> cartLines = cartLineService.getCartLineByCartID(cartId);
        Cart cart = cartService.getCartById(cartId);
        req.setAttribute("cart", cart);
        req.setAttribute("cartLines", cartLines);
        req.getRequestDispatcher("/view/admin/cart-status-update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cartID = Integer.parseInt(req.getParameter("cartID"));
        String cartStatus = req.getParameter("cartStatus");
        cartService.updateCartStatus(cartID, cartStatus);
        resp.sendRedirect(req.getContextPath() + "/admin/cart/list");
    }
}