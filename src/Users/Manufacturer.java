package Users;

import datas.ControllerFactory;
import controllers.ManufacturerController;
import model.Inventory;

import java.util.HashMap;

public class Manufacturer extends User {

    private final ManufacturerController manufacturerController;
    public Manufacturer(String id, String role) {
        super(id, role);
        this.manufacturerController = ControllerFactory.getController(ManufacturerController.class);
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
