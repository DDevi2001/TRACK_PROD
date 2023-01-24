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
                case VIEW_QUOTATION:
                    viewQuotation();
                    continue;
                case VIEW_INVOICE:
                    viewInvoice();
                    continue;
                case EXIT:
                    System.out.println();
                    break main;
            }
        }
    }

    void requestQuotation() {
        HashMap<String, Integer> quotationRequest = new HashMap<>();
        HashMap<String, String> productDetails = customer.getProductDetails();

        if (productDetails.isEmpty()) {
            System.out.println("\nNO PRODUCTS TO PURCHASE. PLEASE VISIT US LATER\n");
            return;
        }

        if (customer.isRequestPresent()) {
            System.out.println("\nALREADY POSTED AN REQUEST. WAIT FOR THE PROCESS TO COMPLETE");
            return;
        }

        System.out.println("\nPRODUCTS AVAILABLE");
        for (Map.Entry<String, String> map : productDetails.entrySet()) {
            System.out.println(map.getKey() + " - " + map.getValue());
        }
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
                System.out.println("ID NOT FOUND");
            }
            while (true) {
                System.out.print("QUANTITY : ");
                quantity = InputVerification.getInt();
                if ((quantity < 0) || (quantity > 5000)) {
                    System.out.println("PROVIDE OPTIMUM QUANTITY");
                    continue;
                }
                break;
            }
            quotationRequest.put(productID, quantity);

            System.out.println("\nENTER 1 TO CONTINUE OR 2 TO EXIT");
            check = InputVerification.getOption(2);
            if (check.equals("2")) {
                customer.requestQuotation(quotationRequest);
                break;
            }
        }
    }

    void viewQuotation() {
        Quotation quotation = customer.getQuotation();

        if (!customer.isRequestPresent()) {
            System.out.println("\nYET TO START YOUR JOURNEY");
            return;
        }
        if (quotation == null) {
            System.out.println("\nYET TO PROCESS YOUR REQUEST");
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
        System.out.println("NOTE: ONCE CONFIRMED YOU CANNOT CANCEL THE ORDER AND NON-AVAILABLE PRODUCTS WILL BE AUTOMATICALLY CANCELLED FROM YOUR ORDER");
        System.out.print("DO YOU WANT TO CONFIRM THE ORDER? [y/n] -> ");

        if (InputVerification.yesOrNoCheck()) {
            customer.confirmOrder();
            return;
        }
        customer.cancelQuotation();
    }

    void viewInvoice() {
        if (!customer.isOrderPresent()) {
            System.out.println("\nNO ORDERS");
            return;
        }
        HashMap<String, Invoice> invoice = customer.getInvoice();
        if (invoice == null) {
            System.out.println("YET TO PROCESS THE ORDER");
            return;
        }
        String check = "1";
        while (check.equals("1")) {
            int i = 1;
            for (Invoice invoiceList : invoice.values()) {
                System.out.println(i++ + ". " + invoiceList.getOrder().getOrderID());
            }
            System.out.println("ORDER ID : ");
            String ID = InputVerification.getID();
            if (!invoice.containsKey(ID)) {
                System.out.println("ID NOT FOUND");
                continue;
            }
            System.out.println("ORDER ID : " + invoice.get(ID));
            if (invoice.get(ID).isStatus()) {
                System.out.println("STATUS : Processing started");
            } else {
                System.out.println("STATUS : Yet to process");
            }
            Formatter fmt = new Formatter();
            fmt.format("__________________________________________________________________________________________________________________________________________");
            fmt.format("\n%1s %25s %25s %25s\n", "PRODUCT ID", "PRODUCT NAME", "QUANTITY REQUESTED", "COST OF THE PRODUCT");
            fmt.format("__________________________________________________________________________________________________________________________________________");
            float totalCost = 0;
            for (IndividualDetails temp : invoice.get(ID).getOrder().getDetails()) {
                fmt.format("\n%4s %25s %25s %25s\n", temp.getProductID(), temp.getProductName(), temp.getQuantity(), temp.getCost());
                totalCost += temp.getCost();
            }
            fmt.format("__________________________________________________________________________________________________________________________________________");
            fmt.format("\n%60s %25s\n", "TOTAL COST", totalCost);
            fmt.format("__________________________________________________________________________________________________________________________________________");
            System.out.println(fmt);

            System.out.println("ENTER 1 TO CONTINUE OR 2 TO EXIT");
            check = InputVerification.getOption(2);
        }
    }
}
