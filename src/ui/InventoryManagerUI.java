package ui;

import helper.InputVerification;
import helper.Utils;
import model.IndividualDetails;
import model.Inventory;
import Users.InventoryManager;
import model.Invoice;
import model.Order;
import ui.functions.InventoryFunctions;

import java.util.HashMap;
import java.util.Map;

public class InventoryManagerUI implements UIManagable {

    InventoryManager inventoryManager;

    public InventoryManagerUI(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public void showFunctions() {
        main:
        while (true) {
            InventoryFunctions[] inventoryFunctions = InventoryFunctions.values();
            Utils.printOptions(inventoryFunctions);
            InventoryFunctions preference = inventoryFunctions[Integer.parseInt(InputVerification.getOption(inventoryFunctions.length)) - 1];

            switch (preference) {
                case ADD_TO_INVENTORY:
                    addToInventory();
                    continue;
                case PROCESS_ORDER:
                    processOrders();
                    continue;
                case PROPOSE_REQUIREMENT:
                    proposeRequirement();
                    continue;
                case EXIT:
                    System.out.println();
                    break main;
            }
        }
    }

    void addToInventory() {
        HashMap<String, Inventory> tempList = inventoryManager.getManufacturedProductsList();
        if (tempList.isEmpty()) {
            System.out.println("\nNO PRODUCTS TO ADD");
            return;
        }
        for (Map.Entry<String, Inventory> map : tempList.entrySet()) {
            System.out.println("\n" + map.getKey() + " : " + map.getValue().getProduct().getProductName() + " -> " + map.getValue().getQuantity());
        }

        System.out.print("\nDO YOU REMOVE ANY PRODUCTS [y/n] -> ");

        if (!InputVerification.yesOrNoCheck()) {
            inventoryManager.addProductsToInventory(tempList);
            System.out.println("\nSUCCESSFULLY ADDED TO THE INVENTORY");
            return;
        }

        String productID;
        while (true) {
            System.out.print("PRODUCT ID : ");
            productID = InputVerification.getID();
            if (!tempList.containsKey(productID)) {
                System.out.println("ID NOT FOUND");
                continue;
            }
            System.out.print("\nDO YOU WANT TO REMOVE ALL [y/n] -> ");
            if (InputVerification.yesOrNoCheck()) {
                tempList.remove(productID);
            } else {
                System.out.print("QUANTITY : ");
                while (true) {
                    int quantity = InputVerification.getInt();
                    if ((quantity < tempList.get(productID).getQuantity()) && (quantity > 0)) {
                        tempList.get(productID).subQuantity(quantity);
                        break;
                    }
                    System.out.println("PROVIDE VALID NUMBER");
                }
            }

            System.out.print("\nDO YOU WANT TO MAKE ANY OTHER CHANGES [y/n] -> ");
            if (InputVerification.yesOrNoCheck()) {
                continue;
            }
            inventoryManager.addProductsToInventory(tempList);
            System.out.println("\nSUCCESSFULLY ADDED TO THE INVENTORY");
            break;
        }
    }

    void processOrders() {
        HashMap<String, HashMap<String, Invoice>> tempList = inventoryManager.getInvoiceDetails();
        if (tempList.isEmpty()) {
            System.out.println("\nZERO ORDERS");
            return;
        }
        int i = 1;
        for (String ID : tempList.keySet()) {
            System.out.println(i++ + ". " + ID);
        }
        String ID;
        while (true) {
            System.out.print("\nCUSTOMER ID : ");
            ID = InputVerification.getID();
            if (tempList.containsKey(ID)) {
                break;
            }
            System.out.println("ID NOT FOUND");
        }
        int ii = 1;
        for (String orderID : tempList.get(ID).keySet()) {
            System.out.println(ii++ + ". " + orderID + (tempList.get(ID).get(orderID).isStatus() ? " -> PROCESSED" : ""));
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
        System.out.println("\nCUSTOMER ID : " + ID +
                "\nORDER ID : " + orderID +
                "\nORDER DETAILS : ");
        if (tempList.get(ID).get(orderID).isStatus()) {
            System.out.println("STATUS : PROCESSED");
            return;
        }
        for (IndividualDetails temp : tempList.get(ID).get(orderID).getOrder().getDetails()) {
            System.out.println(temp.getProductID() + " - " + temp.getQuantity());
        }

        System.out.print("DO YOU WANT TO INITIATE THE PROCESS [y/n] -> ");
        if (InputVerification.yesOrNoCheck()) {
            if (!inventoryManager.processOrder(ID, orderID, tempList.get(ID).get(orderID).getOrder())) {
                System.out.println("QUANTITY IS INSUFFICIENT IN STORAGE. WAIT FOR THE MANUFACTURING DEPARTMENT TO COMPLETE THE PRODUCTION\n");
            }
        }
    }

    void proposeRequirement() {
        HashMap<String, Integer> requirementProposal = new HashMap<>();
        HashMap<String, Integer> tempList = inventoryManager.getInventoryDetails();

        if (tempList.isEmpty()) {
            System.out.println("\nADD PRODUCTS TO START");
            return;
        }
        System.out.println("\nINVENTORY DETAILS FOR YOUR REFERENCE ");
        System.out.println("PRODUCT_ID    QUANTITY_AVAILABLE");
        for (String productID : tempList.keySet()) {
            System.out.println(" " + productID + "             " + tempList.get(productID));
        }
        String check = "1";
        while (check.equals("1")) {
            String productID;
            int quantity;
            while (true) {
                System.out.print("\nPRODUCT ID : ");
                productID = InputVerification.getID();
                if (tempList.containsKey(productID)) {
                    break;
                }
                System.out.println("ID NOT FOUND");
            }
            while (true) {
                System.out.print("QUANTITY : ");
                quantity = InputVerification.getInt();
                if ((quantity < 0) || (quantity > 5000)) {
                    System.out.println("\nPROVIDE OPTIMUM QUANTITY\n");
                    continue;
                }
                break;
            }
            if (!requirementProposal.containsKey(productID)) {
                requirementProposal.put(productID, quantity);
            } else {
                requirementProposal.put(productID, requirementProposal.get(productID) + quantity);
            }
            System.out.println("\nENTER 1 TO CONTINUE OR 2 TO EXIT");
            check = InputVerification.getOption(2);

            if (check.equals("2")) {
                inventoryManager.proposeRequirement(requirementProposal);
                break;
            }
        }
    }
}
