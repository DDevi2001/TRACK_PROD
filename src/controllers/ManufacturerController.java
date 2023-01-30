package controllers;

import model.Inventory;

import java.util.HashMap;

public interface ManufacturerController {

    boolean checkExistenceOf(String productName);
    HashMap<String, String> getProductDetails();
    HashMap<String, Integer> getRequirementList();
    void sendNewManufacturedProductsToInventory(String productID, Integer quantity);
    void sendNewManufacturedProductsToInventory(String productID, Inventory products);

    boolean isEmpty();
}
