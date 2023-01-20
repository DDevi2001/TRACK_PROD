package ui;

import datas.controllers.CustomerController;
import helper.InputVerification;
import helper.Utils;
import model.IndividualDetails;
import model.Invoice;
import Users.Customer;
import model.Quotation;
import ui.functions.CustomerFunctions;

import java.util.*;

public class CustomerUI implements UIManagable{
    Customer customer;

    public CustomerUI(Customer customer) {
        this.customer = customer;
    }

    public void showFunctions() {
        main:
        while (true) {
            CustomerFunctions[] customerFunctions = CustomerFunctions.values();
            Utils.printOptions(customerFunctions);
            CustomerFunctions preference = customerFunctions[Integer.parseInt(InputVerification.getOption(customerFunctions.length)) - 1];

            switch (preference) {
                case REQUEST_QUOTATION:
                    if (Customer.count == 1) {
                        System.out.println("You have already requested for an quotation. Wait until you receive it.");
                        continue;
                    }
                    requestQuotation();
                    continue;
                case VIEW_QUOTATOION:
                    viewQuotation();
                    continue;
                case VIEW_INVOICE:
                    viewInvoice();
                    continue;
                case EXIT:
                    break main;
            }
        }
    }

    void requestQuotation() {
        HashMap<String, Integer> quotationRequest = new HashMap<>();
        HashMap<String, String> productDetails = customer.getProductDetails();
        if (productDetails.isEmpty()) {
            System.out.println("There are no products to purchase. Please visit later");
        } else {
            Customer.count = 1;
            System.out.println("PRODUCTS AVAILABLE");
            for (Map.Entry<String, String> map : productDetails.entrySet()) {
                System.out.println(map.getKey() + " - " + map.getValue());
            }
            System.out.println("Start giving your inputs...");
            String check = "1";
            String productID;
            int quantity;
            while (check.equals("1")) {
                while (true) {
                    System.out.print("Enter the product ID: ");
                    productID = InputVerification.isID();
                    if (productDetails.containsKey(productID)) {
                        break;
                    }
                    System.out.println("ID not found");
                }
                while (true) {
                    System.out.println("Enter the quantity: ");
                    quantity = InputVerification.getInt();
                    if ((quantity < 0) || (quantity > 5000)) {
                        System.out.println("Provide optimum quantity");
                        continue;
                    }
                    break;
                }
                quotationRequest.put(productID, quantity);
                System.out.println("If you still want to add new products enter 1 or enter 2 to exit");
                check = InputVerification.getOption(2);
                if (check.equals("2")) {
                    customer.requestQuotation(quotationRequest);
                    break;
                }
            }
        }
    }

    void viewQuotation() {
        Quotation quotation = customer.getQuotation();
        if (quotation.getIndividualDetails().isEmpty()) {
            System.out.println("Your quotation is yet to be processed");
        } else {
            Formatter fmt = new Formatter();
            System.out.println("CUSTOMER ID  :  " + customer.getId());
            fmt.format("____________________________________________________________________________________________________________________________________________");
            fmt.format("\n%1s %25s %25s %25s %25s\n", "PRODUCT ID", "PRODUCT NAME", "QUANTITY REQUESTED", "AVAILABILITY STATUS", "COST OF THE PRODUCT");
            fmt.format("____________________________________________________________________________________________________________________________________________");
            for (IndividualDetails temp : quotation.getIndividualDetails()) {
                fmt.format("\n%4s %25s %25s %25s %25s\n", temp.getProductID(), temp.getProductName(), temp.getQuantity(), temp.getStatus(),
                        temp.getCost());
            }
            fmt.format("____________________________________________________________________________________________________________________________________________");
            System.out.println(fmt);
            System.out.println("Do you want the order?[y/n]");

            System.out.println("NOTE: ONCE CONFIRMED YOU CANNOT CANCEL THE ORDER AND NON-AVAILABLE PRODUCTS WILL BE AUTOMATICALLY CANCELLED FROM YOUR ORDER");
            String opt = InputVerification.yesOrNoCheck();
            if (opt.equals("y")) {
                customer.confirmOrder();
            } else {
                customer.cancelQuotation();
            }
        }
    }

    void viewInvoice() {
        try {
            Invoice invoice = customer.getInvoice();
            System.out.println("ORDER ID : " + invoice.getOrderID());
            if (invoice.isStatus()) {
                System.out.println("STATUS : Processing started");
            } else {
                System.out.println("STATUS : Yet to process");
            }
            Formatter fmt = new Formatter();
            fmt.format("__________________________________________________________________________________________________________________________________________");
            fmt.format("\n%1s %25s %25s %25s\n", "PRODUCT ID", "PRODUCT NAME", "QUANTITY REQUESTED", "COST OF THE PRODUCT");
            fmt.format("__________________________________________________________________________________________________________________________________________");
            float totalCost = 0;
            for (IndividualDetails temp : invoice.getOrder().getDetails()) {
                fmt.format("\n%4s %25s %25s %25s\n", temp.getProductID(), temp.getProductName(), temp.getQuantity(), temp.getCost());
                totalCost += temp.getCost();
            }
            fmt.format("__________________________________________________________________________________________________________________________________________");
            fmt.format("\n%60s %25s\n", "TOTAL COST", totalCost);
            fmt.format("__________________________________________________________________________________________________________________________________________");
            System.out.println(fmt);
        } catch (Exception e) {
            System.out.println("You haven't placed any orders or your order is yet to be processed");
        }
    }

//    public void receiveCustomerController(CustomerController customerController) {
//        customer.setCustomerController(customerController);
//    }
}
