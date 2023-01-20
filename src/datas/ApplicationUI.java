package datas;

import datas.controllers.AdminController;
import helper.InputVerification;
import helper.Utils;
import Users.Customer;
import Users.InventoryManager;
import Users.Manufacturer;
import Users.SalesHead;
import ui.*;

public class ApplicationUI {
    private AdminController adminController;

    public ApplicationUI() {
        this.adminController = ControllerFactory.getController(AdminController.class, this.getClass());
    }

    static {
        ControllerFactory.getController(AdminController.class, ApplicationUI.class).addUser("INV0001", "inventory");
        ControllerFactory.getController(AdminController.class, ApplicationUI.class).addUser("SAL0001", "sales");
        ControllerFactory.getController(AdminController.class, ApplicationUI.class).addUser("MAN0001", "manufacturing");
        ControllerFactory.getController(AdminController.class, ApplicationUI.class).addUser(Utils.generateID("Customer"), "customer");
    }

    public void signIN() {
        while (true) {
            System.out.println("Enter your ID");
            String id = InputVerification.isID();
            if (!adminController.isUser(id)) {
                System.out.println("No such ID found");
            } else {
                String role = adminController.getUser(id).getRole();
                UIManagable UI;
                switch (role) {
                    case "inventory":
                        UI = new InventoryManagerUI((InventoryManager) adminController.getUser(id));
//                        enterInventoryManagerUI((InventoryManager) adminController.getUser(id));
                        break;
                    case "sales":
                        UI = new SalesHeadUI((SalesHead) adminController.getUser(id));
//                        enterSalesUI((SalesHead) adminController.getUser(id));
                        break;
                    case "manufacturing":
                        UI = new ManufacturerUI((Manufacturer) adminController.getUser(id));
//                        enterManufacturerUI((Manufacturer) adminController.getUser(id));
                        break;
                    case "customer":
                        UI = new CustomerUI((Customer) adminController.getUser(id));
//                        enterCustomerUI((Customer) adminController.getUser(id));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + role);
                }
                UI.showFunctions();
                break;
            }
        }
    }

    public void signUP() {
        System.out.println("Enter your name: ");
        String name = InputVerification.getString();
        String id = Utils.generateID("Customer");
        System.out.println("Your id is "+id+". Please remember it for future use");
        adminController.addUser(id,"customer");
    }

//    public void enterInventoryManagerUI(InventoryManager inventoryManager) {
//        InventoryManagerUI inventoryManagerUI = new InventoryManagerUI(inventoryManager);
//        inventoryManagerUI.receiveInventoryController(new DataManager());
//        inventoryManagerUI.showFunctions();
//    }
//
//    public void enterManufacturerUI(Manufacturer manufacturer) {
//        ManufacturerUI manufacturerUI = new ManufacturerUI(manufacturer);
//        manufacturerUI.receiveManufacturerController(new DataManager());
//        manufacturerUI.showFunctions();
//    }
//
//    public void enterSalesUI(SalesHead salesHead) {
//        SalesHeadUI salesHeadUI = new SalesHeadUI(salesHead);
//        salesHeadUI.receiveSalesController(new DataManager());
//        salesHeadUI.showFunctions();
//    }
//
//    public void enterCustomerUI(Customer customer) {
//        CustomerUI customerUI = new CustomerUI(customer);
//        customerUI.receiveCustomerController(new DataManager());
//        customerUI.showFunctions();
//    }
}