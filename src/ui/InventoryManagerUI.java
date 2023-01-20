package ui;

import helper.InputVerification;
import helper.Utils;
import model.IndividualDetails;
import model.Inventory;
import Users.InventoryManager;
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
                    break main;
            }
        }
    }

    void addToInventory() {
        HashMap<String, Inventory> tempList = inventoryManager.getManufacturedProductsList();
        if (tempList.isEmpty()) {
            System.out.println("No products have manufactured recently");
            return;
        }

        System.out.println("Below displayed are the products and their quantity to be added in the Inventory");
        for (Map.Entry<String, Inventory> map : tempList.entrySet()) {
            System.out.println(map.getKey() + " : " + map.getValue().getProduct().getProductName() + " -> " + map.getValue().getQuantity());
        }

        System.out.print("DO YOU REMOVE ANY PRODUCTS [y/n] -> ");

        if (!InputVerification.yesOrNoCheck()) {
            inventoryManager.addProductsToInventory(tempList);
            return;
        }

        String productID;
        while (true) {
            System.out.print("PRODUCT ID : ");
            productID = InputVerification.getID();
            if (!tempList.containsKey(productID)) {
                System.out.println("No such ID found. Please enter the correct ID");
                continue;
            }

            System.out.print("DO YOU WANT TO REMOVE ALL [y/n] -> ");
            if (InputVerification.yesOrNoCheck()) {
                tempList.remove(productID);
            } else {
                System.out.print("Enter the quantity to be removed: ");
                while (true) {
                    int quantity = InputVerification.getInt();
                    if ((quantity < tempList.get(productID).getQuantity()) && (quantity > 0)) {
                        tempList.get(productID).subQuantity(quantity);
                        break;
                    }
                    System.out.println("Provide valid quantity");
                }
            }

            System.out.print("DO YOU WANT TO MAKE ANY OTHER CHANGES [y/n] -> ");
            if (InputVerification.yesOrNoCheck()) {
                continue;
            }
            inventoryManager.addProductsToInventory(tempList);
            System.out.println("Successfully added to the Inventory");
            break;
        }
    }

    void processOrders() {
        HashMap<String, Order> tempList = inventoryManager.getOrdersDetails();
        if (tempList.isEmpty()) {
            System.out.println("No orders yet");
            return;
        }
        System.out.println("Orders to be processed");
        int i = 1;
        for (String ID : tempList.keySet()) {
            System.out.println(i++ + ". " + ID);
        }
        while (true) {
            System.out.println("ORDER ID : ");
            String ID = InputVerification.getID();
            if (tempList.containsKey(ID)) {
                System.out.println("ORDER DETAILS:" +
                        "\nCUSTOMER ID : " + tempList.get(ID).getCustomerID() +
                        "\nORDER       : ");
                for (IndividualDetails temp : tempList.get(ID).getDetails()) {
                    System.out.println(temp.getProductID() + " - " + temp.getQuantity());
                }
            } else {
                System.out.println("No such ID exist");
                continue;
            }
            System.out.println("DO YOU WANT TO INITIATE THE PROCESS [y/n] -> ");
            if (InputVerification.yesOrNoCheck()) {
                if (inventoryManager.processOrder(ID, tempList.get(ID))) {
                    tempList.remove(ID);
                    break;
                }
                System.out.println("Quantity is insufficient in storage. Wait for the manufacturers to complete the production");
            }
        }
    }

    void proposeRequirement() {
        HashMap<String, Integer> requirementProposal = new HashMap<>();
        HashMap<String, Integer> tempList = inventoryManager.getInventoryDetails();

        if (tempList.isEmpty()) {
            System.out.println("No products in inventory to propose requirement");
            return;
        }
        System.out.println("Inventory Details for your reference");
        System.out.println("PRODUCT_ID    QUANTITY_AVAILABLE");
        for (String productID : tempList.keySet()) {
            System.out.println(" " + productID + "          " + tempList.get(productID));
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
            if (!requirementProposal.containsKey(productID)) {
                requirementProposal.put(productID, quantity);
            } else {
                requirementProposal.put(productID, requirementProposal.get(productID) + quantity);
            }
            System.out.println("If you still want to add new products enter 1 or enter 2 to exit");
            check = InputVerification.getOption(2);

            if (check.equals("2")) {
                inventoryManager.proposeRequirement(requirementProposal);
                break;
            }
        }
    }
}
