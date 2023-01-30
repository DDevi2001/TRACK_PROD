package ui;

import helper.InputVerification;
import helper.Utils;
import model.Inventory;
import Users.Manufacturer;
import model.Product;
import ui.functions.ManufacturerFunctions;

import java.util.HashMap;
import java.util.Map;

public class ManufacturerUI implements UIManagable {
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
                        System.out.println("\n1. NEW PRODUCT" +
                                "\n2. EXISTING PRODUCT" +
                                "\n3. GO BACK");
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
            System.out.println("\nNO REQUIREMENTS");
            return;
        }
        for (Map.Entry<String, Integer> requirements : tempList.entrySet()) {
            System.out.println(requirements.getKey() + " : " + requirements.getValue());
        }
    }

    public void manufactureProduct() {
        if (manufacturer.isEmpty()) {
            System.out.println("\nCREATE A NEW PRODUCT TO START");
            return;
        }
        HashMap<String, Integer> tempReqList = manufacturer.returnRequirementList();
        String check = "1";
        while (check.equals("1")) {
            String productID;
            while (true) {
                System.out.print("PRODUCT ID : ");
                productID = InputVerification.getID();
                if (manufacturer.containsProduct(productID)) {
                    break;
                }
                System.out.println("ID NOT FOUND");
            }

            int quantity;
            while (true) {
                System.out.print("QUANTITY : ");
                quantity = InputVerification.getInt();
                if (tempReqList.containsKey(productID)) {
                    if ((quantity < tempReqList.get(productID)) || (quantity > (tempReqList.get(productID) + 100))) {
                        System.out.println("PROVIDE OPTIMUM QUANTITY");
                        continue;
                    }
                } else {
                    if ((quantity < 10) || (quantity > 10000)) {
                        System.out.println("PROVIDE OPTIMUM QUANTITY");
                        continue;
                    }
                }
                break;
            }

            manufacturer.sendNewManufacturedProducts(productID, quantity);

            System.out.println("ENTER 1 TO CONTINUE OR 2 TO EXIT");
            check = InputVerification.getOption(2);
        }
    }

    void addNewProduct() {
        String check = "1";
        while (check.equals("1")) {
            //Get new product name
            System.out.print("\nPRODUCT NAME : ");
            String newProduct = InputVerification.getString();
            if (manufacturer.containsProduct(newProduct)) {
                System.out.println("ALREADY IN MANUFACTURING");
                continue;
            }

            //Generate ID for the new product
            String newProductID = Utils.generateID("Product");
            System.out.println("PRODUCT ID: " + newProductID);

            //Get manufacturing cost for the product
            float manufacturingCost;
            while (true) {
                System.out.print("MANUFACTURING COST(of one) : ");
                manufacturingCost = InputVerification.getFloat();
                if ((manufacturingCost >= 10) && (manufacturingCost <= 1000000)) {
                    break;
                }
                System.out.println("ENTER VALID PRICE");
            }

            //Get weight of the product
            float productWeight;
            while (true) {
                System.out.print("WEIGHT(of one)(in kg) : ");
                productWeight = InputVerification.getFloat();
                if (productWeight > 0.01 && productWeight < 1000) {
                    break;
                }
                System.out.println("ENTER VALID WEIGHT");
            }

            //Get quantity to be added to the inventory
            int quantity;
            while (true) {
                System.out.print("QUANTITY : ");
                quantity = InputVerification.getInt();
                if ((quantity >= 1000) && (quantity <= 10000)) {
                    break;
                }
                System.out.println("ENTER VALID NUMBER");
            }

            Product product = new Product(newProduct, newProductID, productWeight, manufacturingCost);
            Inventory inventory = new Inventory(product, quantity);
            manufacturer.sendNewManufacturedProducts(newProductID, inventory);

            System.out.println("\nENTER 1 TO CONTINUE OR 2 TO EXIT");
            check = InputVerification.getOption(2);
        }
    }
}
