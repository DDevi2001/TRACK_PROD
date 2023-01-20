package datas.controllers;

import model.Invoice;
import model.Quotation;

import java.util.HashMap;

public interface CustomerController {
    HashMap<String, String> provideProductDetails();
    void requestQuotation(HashMap<String, Integer> quotationRequest, String customerID);
    Quotation getQuotation(String customerID);

    void confirmOrder(String id);

    void cancelQuotation(String id);

    Invoice getInvoice(String id);
}
