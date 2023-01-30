package Users;

import datas.ControllerFactory;
import controllers.InventoryController;
import model.Inventory;
import model.Invoice;
import model.Order;

import java.util.HashMap;

public class InventoryManager extends User {

    private final InventoryController inventoryController ;
    public InventoryManager(String id, String role) {
        super(id, role);
        this.inventoryController = ControllerFactory.getController(InventoryController.class);
    }

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

    public boolean processOrder(String ID, String orderID, Order order) {
        return inventoryController.processOrder(ID, orderID, order);
    }

    public HashMap<String, HashMap<String, Invoice>> getInvoiceDetails() {
        return inventoryController.getInvoiceDetails();
    }
}
