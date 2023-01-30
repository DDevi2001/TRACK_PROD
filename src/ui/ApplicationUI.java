package ui;

import datas.ControllerFactory;
import controllers.AdminController;
import helper.InputVerification;
import helper.Utils;
import Users.Customer;
import Users.InventoryManager;
import Users.Manufacturer;
import Users.SalesHead;

public class ApplicationUI {
    private AdminController adminController;

    public ApplicationUI() {
        this.adminController = ControllerFactory.getController(AdminController.class);
    }

    static {
        ControllerFactory.getController(AdminController.class).addUser("INV0001", "inventory");
        ControllerFactory.getController(AdminController.class).addUser("SAL0001", "sales");
        ControllerFactory.getController(AdminController.class).addUser("MAN0001", "manufacturing");
        ControllerFactory.getController(AdminController.class).addUser(Utils.generateID("Customer"), "customer");
    }

    public void signIN() {
        while (true) {
            System.out.print("USER ID : ");
            String id = InputVerification.getID();
            if (!adminController.isUser(id)) {
                System.out.println("No such ID found");
                continue;
            }
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

    public void signUP() {
        System.out.print("NAME : ");
        String name = InputVerification.getString();
        String id = Utils.generateID("Customer");
        System.out.println("YOUR ID : " + id + "\nREMEMBER IT FOR FUTURE USE");
        adminController.addUser(id, "customer");
    }
}