package ui;

import helper.InputVerification;
import helper.Utils;
import model.IndividualDetails;
import model.Order;
import Users.SalesHead;
import ui.functions.SalesHeadFunctions;

import java.util.HashMap;

public class SalesHeadUI implements UIManagable {
    SalesHead salesHead;

    public SalesHeadUI(SalesHead salesHead) {
        this.salesHead = salesHead;
    }

    public void showFunctions() {
        main:
        while (true) {
            SalesHeadFunctions[] salesFunctions = SalesHeadFunctions.values();
            Utils.printOptions(salesFunctions);
            SalesHeadFunctions preference = salesFunctions[Integer.parseInt(InputVerification.getOption(salesFunctions.length)) - 1];

            switch (preference) {
                case INVOICE:
                    invoiceProcess();
                    continue;
                case QUOTATIONS:
                    quotationProcess();
                    continue;
                case EXIT:
                    break main;
            }
        }
    }

    void invoiceProcess() {
        HashMap<String, HashMap<String, Order>> tempList = salesHead.getOrdersDetails();
        if (tempList.size() == 0) {
            System.out.println("No orders so far");
            return;
        }
        String check = "1";
        while (check.equals("1")) {
            System.out.println("Choose one to proceed");
            int i = 1;
            for (String ID : tempList.keySet()) {
                System.out.println(i++ + ". " + ID);
            }
            String ID;
            while (true) {
                System.out.print("Enter the ID: ");
                ID = InputVerification.getID();
                if (tempList.containsKey(ID)) {
                    break;
                }
                System.out.println("No such ID found");
            }
            int ii = 1;
            for (String orderID : tempList.get(ID).keySet()) {
                System.out.println(ii++ + ". " + orderID);
            }
            System.out.println("Enter the ID: ");
            String orderID = InputVerification.getID();
            if (!tempList.get(ID).containsKey(orderID)) {
                System.out.println("No such ID found");
                continue;
            }
            System.out.println("Order Details:");
            for (IndividualDetails temp : tempList.get(ID).get(orderID).getDetails()) {
                System.out.println(temp.getProductID() + " - " + temp.getQuantity());
            }
            salesHead.processInvoice(ID, orderID, tempList.get(ID).get(orderID));
            System.out.println("Successfully Processed");

            System.out.println("enter 1 to continue or enter 2 to exit");
            check = InputVerification.getOption(2);
        }
    }

    void quotationProcess() {
        HashMap<String, HashMap<String, Integer>> tempList = salesHead.getQuotationRequests();
        if (tempList.size() == 0) {
            System.out.println("No requests so far");
            return;
        }
        System.out.println("Provided below are the requests from customers");
        int i = 1;
        for (String custID : tempList.keySet()) {
            System.out.println(i++ + ". " + custID);
        }
        while (true) {
            System.out.println("Enter the customer ID: ");
            String customerID = InputVerification.getID();
            if (!tempList.containsKey(customerID)) {
                System.out.println("No such ID found");
                continue;
            }
            System.out.println("Quotation Details: ");
            for (String productID : tempList.get(customerID).keySet()) {
                System.out.println(productID + " - " + tempList.get(customerID).get(productID));
            }
            System.out.print("Do you want to process the quotation [y/n] -> ");
            if (InputVerification.yesOrNoCheck()) {
                salesHead.processQuotation(customerID, tempList.get(customerID));
                break;
            }
        }
        System.out.println("Successfully Processed");
    }
}
