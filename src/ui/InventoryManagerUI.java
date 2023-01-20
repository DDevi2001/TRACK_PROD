package ui;

import datas.controllers.InventoryController;
import helper.InputVerification;
import helper.Utils;
import model.IndividualDetails;
import model.Inventory;
import Users.InventoryManager;
import model.Order;
import model.Quotation;
import ui.functions.InventoryFunctions;

import java.util.HashMap;
import java.util.Map;

public class InventoryManagerUI implements UIManagable{

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
        HashMap<String, Inventory> tempList = returnManufacturedProductsList();
        if (tempList.isEmpty()) {
            System.out.println("No products have manufactured recently");
        } else {
            System.out.println("Below displayed are the products and their quantity to be added in the Inventory");
            for (Map.Entry<String, Inventory> map : tempList.entrySet()) {
                System.out.println(map.getKey() + " : " + map.getValue().getProduct().getProductName() + " -> " + map.getValue().getQuantity());
            }

            System.out.print("Do you want to remove any products [y/n] ");
            String option = InputVerification.yesOrNoCheck();

            if (option.equals("n")) {
                inventoryManager.addProductsToInventory(tempList);
            } else {
                String productID;
                while (true) {
                    System.out.print("Enter the product ID: ");
                    productID = InputVerification.isID();
                    if (tempList.containsKey(productID)) {
                        System.out.print("Do you want to remove all [y/n]");
                        String opt = InputVerification.yesOrNoCheck();
                        if (opt.equals("y")) {
                            tempList.remove(productID);
                        } else {
                            System.out.print("Enter the quantity to be removed: ");
                            while (true) {
                                int quantity = InputVerification.getInt();
                                if ((quantity < tempList.get(productID).getQuantity()) && (quantity > 0)) {
                                    tempList.get(productID).subQuantity(quantity);
                                    break;
                                } else {
                                    System.out.println("Provide valid quantity");
                                }
                            }
                        }
                    } else {
                        System.out.println("No such ID found. Please enter the correct ID");
                    }
                    System.out.println("Do you want to make any other changes [y/n]");
                    String op = InputVerification.yesOrNoCheck();
                    if (op.equals("y")) {
                        continue;
                    }
                    inventoryManager.addProductsToInventory(tempList);
                    System.out.println("Products are successfully added in the Inventory");
                    break;
                }
            }
        }
    }

    void processOrders() {
        HashMap<String, Order> tempList = viewOrders();
        if (tempList.isEmpty()) {
            System.out.println("No orders yet");
        } else {
            System.out.println("Orders to be processed");
            int i = 1;
            for (String ID : tempList.keySet()) {
                System.out.println(i++ + ". " + ID);
            }
            while (true) {
                System.out.println("Enter the order ID to proceed: ");
                String ID = InputVerification.isID();
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
                System.out.println("Do you want to initiate the process" +
                        "\n[y/n]");
                String opt = InputVerification.yesOrNoCheck();
                if (opt.equals("y")) {
                    if (inventoryManager.processOrder(ID, tempList.get(ID))) {
                        tempList.remove(ID);
                    } else {
                        System.out.println("Quantity is insufficient in storage. Wait for the manufacturers to complete the production");
                    }
                    break;
                }
            }
        }
    }
    void proposeRequirement() {
        HashMap<String, Integer> requirementProposal = new HashMap<>();
        HashMap<String, Integer> tempList = viewInventory();
        if (tempList.isEmpty()) {
            System.out.println("No orders yet");
        } else {
            System.out.println("Inventory Details for your reference");
            System.out.println("PRODUCT_ID    QUANTITY_AVAILABLE");
            for (String productID : tempList.keySet()) {
                System.out.println(" " + productID + "          " + tempList.get(productID));
            }
            String check = "1";
            while (check.equals("1")) {
                System.out.println("Enter Product ID: ");
                String productID = InputVerification.isID();
                if (!tempList.containsKey(productID)) {
                    System.out.println("Product ID not found");
                    continue;
                }
                System.out.println("Enter quantity: ");
                int quantity = InputVerification.getInt();
                if (quantity <= 0) {
                    System.out.println("Enter valid option");
                    continue;
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

    HashMap<String, Integer> viewInventory() {
        return inventoryManager.getInventoryDetails();
    }

    HashMap<String, Inventory> returnManufacturedProductsList() {
        return inventoryManager.getManufacturedProductsList();
    }

    HashMap<String, Order> viewOrders() {
        return inventoryManager.getOrdersDetails();
    }

//    public void receiveInventoryController(InventoryController inventoryController) {
//        inventoryManager.setInventoryController(inventoryController);
//    }
}
