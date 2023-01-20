package Users;

import datas.ControllerFactory;
import datas.controllers.InventoryController;
import model.Inventory;
import model.Order;

import java.util.HashMap;

public class InventoryManager extends User {

    InventoryController inventoryController ;
    public InventoryManager(String id, String role) {
        super(id, role);
        this.inventoryController = ControllerFactory.getController(InventoryController.class, this.getClass());
    }

//    public void setInventoryController(){
//        this.inventoryController = inventoryController;
//    }

    public HashMap<String, Inventory> getManufacturedProductsList() {
        return inventoryController.provideManufacturedProductsList();
    }

    public void addProductsToInventory(HashMap<String, Inventory> manufacturedProductsList) {
        inventoryController.addStockToInventory(manufacturedProductsList);
    }

    public HashMap<String , Integer> getInventoryDetails() {
        return inventoryController.provideInventoryDetails();
    }

    public void proposeRequirement(HashMap<String , Integer> requirementProposal) {
        inventoryController.addToRequirementList(requirementProposal);
    }

    public HashMap<String, Order> getOrdersDetails() {
        return inventoryController.getOrdersDetails();
    }

    public boolean processOrder(String ID, Order order) {
        return inventoryController.processOrder(ID, order);
    }
}
