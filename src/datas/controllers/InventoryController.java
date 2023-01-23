package datas.controllers;

import model.Inventory;
import model.Invoice;
import model.Order;

import java.util.HashMap;

public interface InventoryController {
    HashMap<String, Inventory> provideManufacturedProductsList();
    void addStockToInventory(HashMap<String, Inventory> ManufacturedProductsList);
    HashMap<String, Integer> provideInventoryDetails();
    void addToRequirementList(HashMap<String, Integer> requirementProposal);
    boolean processOrder(String ID, String orderID, Order order);

    HashMap<String, HashMap<String, Invoice>> getInvoiceDetails();
}
