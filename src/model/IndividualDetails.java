package model;

import datas.ControllerFactory;
import datas.controllers.InventoryController;

public class IndividualDetails {
    String productID;
    String productName;
    int quantity;
    AvailabilityStatus status;
    float cost;

    public IndividualDetails(String productID, String productName, int quantity, AvailabilityStatus status, float cost) {
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.status = status;
        this.cost = cost;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public AvailabilityStatus getStatus() {
        return status;
    }

    public float getCost() {
        return cost;
    }
}
