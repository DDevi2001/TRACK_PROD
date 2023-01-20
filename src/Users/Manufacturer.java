package Users;

import datas.ControllerFactory;
import datas.controllers.ManufacturerController;
import model.Inventory;

import java.util.HashMap;

public class Manufacturer extends User {

    ManufacturerController manufacturerController;
    public Manufacturer(String id, String role) {
        super(id, role);
        this.manufacturerController = ControllerFactory.getController(ManufacturerController.class, this.getClass());
    }

//    public void setManufacturerController(ManufacturerController manufacturerController) {
//        this.manufacturerController = manufacturerController;
//    }
    public HashMap<String, String> getProductsDetails() {
        return manufacturerController.provideProductDetails();
    }
    public boolean containsProduct(String productNameOrID) {
        return manufacturerController.checkExistenceOf(productNameOrID);
    }
    public HashMap<String, Integer> returnRequirementList() {
        return manufacturerController.getRequirementList();
    }

    public void sendNewManufacturedProducts(String productID, Integer quantity) {
        manufacturerController.sendNewManufacturedProductsToInventory(productID, quantity);
    }

    public void sendNewManufacturedProducts(String productID, Inventory products) {
        manufacturerController.sendNewManufacturedProductsToInventory(productID, products);
    }

    public boolean isEmpty() {
        return manufacturerController.isEmpty();
    }
}
