package datas.controllers;

import model.Inventory;
import model.Order;

import java.util.HashMap;

public interface InventoryController {
    HashMap<String, Inventory> provideManufacturedProductsList();
    void addStockToInventory(HashMap<String, Inventory> ManufacturedProductsList);
    HashMap<String, Integer> provideInventoryDetails();
    void addToRequirementList(HashMap<String, Integer> requirementProposal);
    HashMap<String, Order> getOrdersDetails();
    boolean processOrder(String ID, Order order);
}
