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
                    System.out.println();
                    break main;
            }
        }
    }

    void invoiceProcess() {
        HashMap<String, HashMap<String, Order>> tempList = salesHead.getOrdersDetails();
        if (tempList.size() == 0) {
            System.out.println("\nNO ORDERS");
            return;
        }
        int i = 1;
        for (String ID : tempList.keySet()) {
            System.out.println(i++ + ". " + ID);
        }
        String ID;
        while (true) {
            System.out.print("CUSTOMER ID : ");
            ID = InputVerification.getID();
            if (tempList.containsKey(ID)) {
                break;
            }
            System.out.println("ID NOT FOUND");
        }
        if (tempList.get(ID).isEmpty()) {
            System.out.println("\nNO ORDERS");
            return;
        }
        int ii = 1;
        for (String orderID : tempList.get(ID).keySet()) {
            System.out.println(ii++ + ". " + orderID);
        }
        String orderID;
        while (true) {
            System.out.print("\nORDER ID : ");
            orderID = InputVerification.getID();
            if (tempList.get(ID).containsKey(orderID)) {
                break;
            }
            System.out.println("ID NOT FOUND");
        }
        System.out.println("ORDER DETAILS :");
        for (IndividualDetails temp : tempList.get(ID).get(orderID).getDetails()) {
            System.out.println(temp.getProductID() + " - " + temp.getQuantity());
        }
        salesHead.processInvoice(ID, orderID, tempList.get(ID).get(orderID));
        System.out.println("\nSUCCESSFULLY PROCESSED");
    }

    void quotationProcess() {
        HashMap<String, HashMap<String, Integer>> tempList = salesHead.getQuotationRequests();
        if (tempList.size() == 0) {
            System.out.println("NO REQUESTS");
            return;
        }
        int i = 1;
        for (String custID : tempList.keySet()) {
            System.out.println(i++ + ". " + custID);
        }
        while (true) {
            System.out.print("CUSTOMER ID : ");
            String customerID = InputVerification.getID();
            if (!tempList.containsKey(customerID)) {
                System.out.println("ID NOT FOUND");
                continue;
            }
            System.out.println("\nQUOTATION DETAILS : ");
            for (String productID : tempList.get(customerID).keySet()) {
                System.out.println(productID + " - " + tempList.get(customerID).get(productID));
            }
            System.out.print("DO YOU WANT TO PROCESS THE QUOTATION [y/n] -> ");
            if (InputVerification.yesOrNoCheck()) {
                salesHead.processQuotation(customerID, tempList.get(customerID));
                tempList.remove(customerID);
            }
            break;
        }
        System.out.println("SUCCESSFULLY PROCESSED");
    }
}
