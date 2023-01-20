package ui;

import datas.controllers.ManufacturerController;
import helper.InputVerification;
import helper.Utils;
import model.Inventory;
import Users.Manufacturer;
import model.Product;
import ui.functions.ManufacturerFunctions;

import java.util.HashMap;
import java.util.Map;

public class ManufacturerUI implements UIManagable{
    Manufacturer manufacturer;

    public ManufacturerUI(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void showFunctions() {
        main:
        while (true) {
            ManufacturerFunctions[] manufacturerFunctions = ManufacturerFunctions.values();
            Utils.printOptions(manufacturerFunctions);
            ManufacturerFunctions preference = manufacturerFunctions[Integer.parseInt(InputVerification.getOption(manufacturerFunctions.length)) - 1];
            switch (preference) {
                case VIEW_REQUIREMENTS:
                    printInventoryRequirements();
                    continue;
                case ADD_MANUFACTURED_PRODUCTS:
                    main1:
                    while (true) {
                        System.out.println("1. New product" +
                                "\n2. Existing Product" +
                                "\n3. Go back");
                        String option = InputVerification.getOption(3);
                        switch (option) {
                            case "1":
                                addNewProduct();
                                continue;
                            case "2":
                                manufactureProduct();
                                continue;
                            default:
                                break main1;
                        }
                    }
                    continue;
                case EXIT:
                    break main;
            }
        }
    }

    void printInventoryRequirements() {
        HashMap<String, Integer> tempList = manufacturer.returnRequirementList();
        if (tempList.isEmpty()) {
            System.out.println("There is no requirement so far");
        } else {
            for (Map.Entry<String, Integer> requirements : tempList.entrySet()) {
                System.out.println(requirements.getKey() + " : " + requirements.getValue());
            }
        }
    }

    public void manufactureProduct() {
        if (isEmpty()) {
            System.out.println("No products in inventory. Create a new one to start your journey.");
        } else {
            HashMap<String, Integer> tempReqList = manufacturer.returnRequirementList();

            String check = "1";
            while (check.equals("1")) {
                String productID;
                while (true) {
                    System.out.print("Enter the product ID: ");
                    productID = InputVerification.isID();
                    if (containsProduct(productID)) {
                        break;
                    }
                    System.out.println("ID not found");
                }

                int quantity;
                while (true) {
                    System.out.println("Enter the quantity: ");
                    quantity = InputVerification.getInt();
                    if (tempReqList.containsKey(productID)) {
                        if ((quantity < tempReqList.get(productID)) || (quantity > (tempReqList.get(productID) + 100))) {
                            System.out.println("Provide optimum quantity");
                            continue;
                        }
                    } else {
                        if ((quantity < 10) || (quantity > 10000)) {
                            System.out.println("Provide optimum quantity");
                            continue;
                        }
                    }

                    break;
                }

                sendNewManufacturedProducts(productID, quantity);

                System.out.println("If you still want to add new products enter 1 or enter 2 to exit");
                check = InputVerification.getOption(2);
            }
        }
    }

    void addNewProduct() {
        String check = "1";
        while (check.equals("1")) {
            //Get new product name
            System.out.print("Enter the name of new product to be added: ");
            String newProduct = InputVerification.getString();
            if (containsProduct(newProduct)) {
                System.out.println("The product is already in manufacturing. " +
                        "Please try to add a new product");
                continue;
            }

            //Generate ID for the new product
            String newProductID = Utils.generateID("Product");
            System.out.println("Product ID: " + newProductID);

            //Get manufacturing cost for the product
            float manufacturingCost;
            while (true) {
                System.out.print("Enter the manufacturing cost of one " + newProduct + ": ");
                manufacturingCost = InputVerification.getFloat();
                if ((manufacturingCost >= 10) && (manufacturingCost <= 1000000)) {
                    break;
                }
                System.out.println("Please enter valid price");
            }

            //Get weight of the product
            float productWeight;
            while (true) {
                System.out.print("Enter the weight of one " + newProduct + "(in kg): ");
                productWeight = InputVerification.getFloat();
                if (productWeight > 0.01 && productWeight < 1000) {
                    break;
                }
                System.out.println("Please enter a valid weight(in kg)");
            }

            //Get quantity to be added to the inventory
            int quantity;
            while (true) {
                System.out.print("Enter the quantity: ");
                quantity = InputVerification.getInt();
                if ((quantity >= 1000) && (quantity <= 10000)) {
                    break;
                }
                System.out.println("Please enter a valid number");
            }

            Product product = new Product(newProduct, newProductID, productWeight, manufacturingCost);
            Inventory inventory = new Inventory(product, quantity);
            sendNewManufacturedProducts(newProductID, inventory);

            System.out.println("If you still want to add new products enter 1 or enter 2 to exit");
            check = InputVerification.getOption(2);
        }
    }

    void sendNewManufacturedProducts(String productID, Inventory products) {
        manufacturer.sendNewManufacturedProducts(productID, products);
    }

    void sendNewManufacturedProducts(String productID, Integer quantity) {
        manufacturer.sendNewManufacturedProducts(productID, quantity);
    }

    boolean containsProduct(String productNameOrID) {
        return manufacturer.containsProduct(productNameOrID);
    }

//    public void receiveManufacturerController(ManufacturerController manufacturerController) {
//        manufacturer.setManufacturerController(manufacturerController);
//    }

    boolean isEmpty() {
        return manufacturer.isEmpty();
    }
}
