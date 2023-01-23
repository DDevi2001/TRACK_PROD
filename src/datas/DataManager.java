package datas;

import datas.controllers.*;
import model.*;
import Users.User;

import java.util.HashMap;
import java.util.Map;

class DataManager implements AdminController, InventoryController, CustomerController, SalesController, ManufacturerController {
    private final Database db = Database.getDatabase();

    @Override
    public void addUser(String id, String role) {
        db.addManger(id, role);
    }

    @Override
    public boolean isUser(String id) {
        return db.getUsersData().containsKey(id);
    }

    @Override
    public User getUser(String id) {
        return db.getUsersData().get(id);
    }

    @Override
    public HashMap<String, Inventory> provideManufacturedProductsList() {
        return db.getManufacturedProductsList();
    }

    @Override
    public void addStockToInventory(HashMap<String, Inventory> manufacturedProductsList) {
        db.addToInventory(manufacturedProductsList);
    }

    @Override
    public HashMap<String, Integer> provideInventoryDetails() {
        HashMap<String, Integer> tempList = new HashMap<>();
        HashMap<String, Inventory> tempInvList = db.getInventoryData();
        for (String productID : tempInvList.keySet()) {
            tempList.put(productID, tempInvList.get(productID).getQuantity());
        }
        return tempList;
    }

    @Override
    public void addToRequirementList(HashMap<String, Integer> requirementProposal) {
        db.addToRequirementList(requirementProposal);
    }

    @Override
    public HashMap<String, Order> getOrdersDetails() {
        return db.getOrdersData();
    }

    @Override
    public boolean processOrder(String ID, Order order) {
        return db.processOrder(ID, order);
    }

    @Override
    public void processQuotation(String customerID, HashMap<String, Integer> customerRequest) {
        Quotation quotation = new Quotation();
        customerRequest.forEach((k, v) -> {
            AvailabilityStatus status = availabilityCheck(k, v);
            quotation.setIndividualDetails(k, db.getInventoryData().get(k).getProduct().getProductName(), v,
                    status, ((status.equals(AvailabilityStatus.AVAILABLE)) ? (v * db.getInventoryData().get(k).getProduct().getManufacturingCost()) : 0.0F));
        });
        db.addToQuotation(customerID, quotation);
    }

    @Override
    public void processInvoice(String customerID, Order order) {
        db.addToInvoice(customerID, order);
    }

    AvailabilityStatus availabilityCheck(String productID, int quantity) {
        if ((db.getInventoryData().get(productID).getQuantity() - quantity) < 0) {
            if (db.getRequirementList().containsKey(productID)) {
                db.getRequirementList().replace(productID, (db.getRequirementList().get(productID) + quantity));
            } else {
                db.getRequirementList().put(productID, quantity);
            }
            if ((db.getInventoryData().get(productID).getQuantity() - quantity) < -1000) {
                return AvailabilityStatus.NOT_AVAILABLE;
            } else {
                return AvailabilityStatus.AVAILABLE;
            }
        }
        return AvailabilityStatus.AVAILABLE;
    }

    @Override
    public HashMap<String, HashMap<String, Integer>> getQuotationRequests() {
        return db.getQuotationRequests();
    }

    @Override
    public boolean checkExistenceOf(String productNameOrID) {
        return db.checkFor(productNameOrID);
    }

    @Override
    public HashMap<String, String> getProductDetails() {
        HashMap<String, String> productDetails = new HashMap<>();
        HashMap<String, Inventory> tempInvData = db.getInventoryData();
        for (Map.Entry<String, Inventory> inventoryEntry : tempInvData.entrySet()) {
            productDetails.put(inventoryEntry.getKey(), inventoryEntry.getValue().getProduct().getProductName());
        }
        return productDetails;
    }

    @Override
    public void requestQuotation(HashMap<String, Integer> quotationRequest, String customerID) {
        db.addToQuotationRequest(customerID, quotationRequest);
    }

    @Override
    public Quotation getQuotation(String customerID) {
        return db.getQuotationData().get(customerID);
    }

    @Override
    public void confirmOrder(String id) {
        db.confirmOrder(id);
    }

    @Override
    public void cancelQuotation(String id) {
        db.getQuotationData().remove(id);
    }

    @Override
    public Invoice getInvoice(String id) {
        return db.getInvoiceData().get(id);
    }

    @Override
    public boolean isRequestPresent(String id) {
        return (db.getQuotationRequests().containsKey(id) || db.getOrdersData().containsKey(id));
    }

    @Override
    public HashMap<String, Integer> getRequirementList() {
        return db.getRequirementList();
    }

    @Override
    public void sendNewManufacturedProductsToInventory(String productID, Integer quantity) {
        db.addNewManufacturedProductsToList(productID, quantity);
    }

    @Override
    public void sendNewManufacturedProductsToInventory(String productID, Inventory products) {
        db.addNewManufacturedProductsToList(productID, products);
    }

    @Override
    public boolean isEmpty() {
        return db.getInventoryData().isEmpty();
    }

}
