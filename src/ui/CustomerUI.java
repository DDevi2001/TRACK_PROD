package ui;

import helper.InputVerification;
import helper.Utils;
import model.IndividualDetails;
import model.Invoice;
import Users.Customer;
import model.Quotation;
import ui.functions.CustomerFunctions;

import java.util.*;

public class CustomerUI implements UIManagable {
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
            System.out.println("\nThere are no products to purchase. Please visit later\n");
            return;
        }

        if ((customer.getQuotation() != null) && (customer.isRequestPresent())) {
            System.out.println("You have already posted an request. Kindly wait until the entire process gets complete.");
            return;
        }

        System.out.println("PRODUCTS AVAILABLE");
        for (Map.Entry<String, String> map : productDetails.entrySet()) {
            System.out.println(map.getKey() + " - " + map.getValue());
        }

        System.out.println("\nStart giving your inputs...");
        String check = "1";
        String productID;
        int quantity;
        while (check.equals("1")) {
            while (true) {
                System.out.print("\nPRODUCT ID : ");
                productID = InputVerification.getID();
                if (productDetails.containsKey(productID)) {
                    break;
                }
                System.out.println("ID not found");
            }
            while (true) {
                System.out.print("QUANTITY : ");
                quantity = InputVerification.getInt();
                if ((quantity < 0) || (quantity > 5000)) {
                    System.out.println("Provide optimum quantity");
                    continue;
                }
                break;
            }
            quotationRequest.put(productID, quantity);

            System.out.println("\nIf you still want to add new products enter 1 or enter 2 to exit");
            check = InputVerification.getOption(2);
            if (check.equals("2")) {
                customer.requestQuotation(quotationRequest);
                break;
            }
        }
    }

    void viewQuotation() {
        Quotation quotation = customer.getQuotation();

        if (quotation == null) {
            System.out.println("Yet to start your journey...");
            return;
        }

        if (quotation.getIndividualDetails().isEmpty()) {
            System.out.println("Your quotation is yet to be processed");
            return;
        }

        Formatter fmt = new Formatter();
        System.out.println("CUSTOMER ID  :  " + customer.getId());
        fmt.format("____________________________________________________________________________________________________________________________________________");
        fmt.format("\n%1s %25s %25s %25s %25s\n", "PRODUCT ID", "PRODUCT NAME", "QUANTITY REQUESTED", "AVAILABILITY STATUS", "COST OF THE PRODUCT");
        fmt.format("____________________________________________________________________________________________________________________________________________");
        for (IndividualDetails individualDetails : quotation.getIndividualDetails()) {
            fmt.format("\n%4s %25s %25s %25s %25s\n", individualDetails.getProductID(), individualDetails.getProductName(),
                    individualDetails.getQuantity(), individualDetails.getStatus(), individualDetails.getCost());
        }
        fmt.format("____________________________________________________________________________________________________________________________________________");
        System.out.println(fmt);

        System.out.print("DO YOU WANT TO CONFIRM THE ORDER? [y/n] -> ");
        System.out.println("NOTE: ONCE CONFIRMED YOU CANNOT CANCEL THE ORDER AND NON-AVAILABLE PRODUCTS WILL BE AUTOMATICALLY CANCELLED FROM YOUR ORDER");
        if (InputVerification.yesOrNoCheck()) {
            customer.confirmOrder();
            return;
        }
        customer.cancelQuotation();
    }

    void viewInvoice() {
        if (!customer.isRequestPresent()) {
            System.out.println("You haven't placed any orders");
            return;
        }
        Invoice invoice = customer.getInvoice();
        if (invoice == null) {
            System.out.println("Your order is yet to be confirmed from our side");
            return;
        }

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
    }
}
