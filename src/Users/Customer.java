package Users;

import datas.ControllerFactory;
import datas.controllers.CustomerController;
import model.Invoice;
import model.Quotation;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends User{
    public static int count = 0;
    private CustomerController customerController;

    public Customer (String id, String role) {
        super(id, role);
        this.customerController = ControllerFactory.getController(CustomerController.class, this.getClass());
    }

//    public void setCustomerController(CustomerController customerController) {
//        this.customerController = customerController;
//    }
    public HashMap<String, String> getProductDetails() {
        return customerController.provideProductDetails();
    }
    public void requestQuotation(HashMap<String, Integer> quotationRequest) {
        customerController.requestQuotation(quotationRequest, getId());
    }
    public Quotation getQuotation() {
        return customerController.getQuotation(getId());
    }

    public void confirmOrder() {
        customerController.confirmOrder(getId());
    }

    public void cancelQuotation() {
        customerController.cancelQuotation(getId());
    }

    public Invoice getInvoice() {
        return customerController.getInvoice(getId());
    }
}
