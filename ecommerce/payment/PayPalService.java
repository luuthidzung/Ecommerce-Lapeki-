package com.ecommerce.payment;

import com.ecommerce.model.CartLine;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PayPalService {
    private static final String CLIENT_ID = "AZfHEUX0D1o8-NceGpw26YiJfs_RQMQ-SQkwr8397LNVmhLya7FevYkpjBiersfw9aRcc1gcgqSH9-1H";
    private static final String CLIENT_SECRET = "EClN3vm6PLvL0a2VeS4q35anDXSzX4ocZyvkhW3vEOwjSw6XK29tLlhYY5R1m4RhmDHGixAHrd_EFx79";
    private static final String MODE = "sandbox";

    public PayPalService() {
    }

    public String authorizePayment(Map<Integer, CartLine> map) throws PayPalRESTException {
        Payer payer = getPayerInformation();
        RedirectUrls redirectUrls = getRedirectUrls();
        List<Transaction> transactions = getTransactionInformation(map);
        Payment requestPayment = new Payment();
        requestPayment.setTransactions(transactions);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payer);
        requestPayment.setIntent("authorize");
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        Payment approvedPayment = requestPayment.create(apiContext);
        return getApprovalLink(approvedPayment);
    }

    /**
     * Get payer information, which is later sent to PayPal for verification
     */
    private Payer getPayerInformation() {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName("Nguyen").setLastName("Dat").setEmail("thedat0309@gmail.com");
        payer.setPayerInfo(payerInfo);
        return payer;
    }

    public Payment getPaymentDetail(String paymentID) throws PayPalRESTException {
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return Payment.get(apiContext, paymentID);
    }

    /**
     * Specify the urls to which PayPal will redirect during the checkout process
     */
    private RedirectUrls getRedirectUrls() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/SE2-Ecommerce_WebProject/");
        redirectUrls.setReturnUrl("http://localhost:8080/SE2-Ecommerce_WebProject/customer/review_payment");
        return redirectUrls;
    }

    /**
     * Get all order details to send to PayPal
     */
    private List<Transaction> getTransactionInformation(Map<Integer, CartLine> map) {
        long subtotal = 0;
        for (CartLine line : map.values()) {
            subtotal += line.getUnitPrice() * line.getQuantity();
        }
        Details details = new Details();
        details.setShipping("30000");
        details.setSubtotal("" + subtotal);
        details.setTax("0");
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal("" + (subtotal + 30000));
        amount.setDetails(details);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("A");
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();
        for (CartLine line : map.values()) {
            Item item = new Item();
            item.setCurrency("USD");
            item.setName(line.getProduct().getProductName());
            item.setPrice("" + (line.getQuantity() * line.getUnitPrice()));
            item.setQuantity("" + line.getQuantity());
            items.add(item);
        }
        itemList.setItems(items);
        transaction.setItemList(itemList);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        return transactions;
    }

    /**
     * Parse the approved Payment object returned from PayPal. Find approval URL in
     * JSON response.
     */
    private String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;
        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }
        return approvalLink;
    }
}