package datas;

import helper.Utils;
import model.*;
import Users.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

final class Database {
    private final HashMap<String, User> usersData = new HashMap<>();
    private final HashMap<String, Inventory> inventoryData = new HashMap<>();
    private final HashMap<String, Integer> requirementList = new HashMap<>();
    private final HashMap<String, Inventory> manufacturedProductsList = new HashMap<>();
    private final HashMap<String, HashMap<String, Order>> ordersData = new HashMap<>();
    private final HashMap<String, HashMap<String, Integer>> quotationRequests = new HashMap<>();
    private final HashMap<String, Quotation> quotationData = new HashMap<>();
    private final HashMap<String, HashMap<String, Invoice>> invoiceData = new HashMap<>();

    private Database() {
    }

    private static Database database;

    static Database getDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    HashMap<String, Inventory> getInventoryData() {
        return inventoryData;
    }

    HashMap<String, Inventory> getManufacturedProductsList() {
        return manufacturedProductsList;
    }

    HashMap<String, User> getUsersData() {
        return usersData;
    }

    HashMap<String, HashMap<String, Order>> getOrdersData() {
        return ordersData;
    }

    HashMap<String, HashMap<String, Integer>> getQuotationRequests() {
        return quotationRequests;
    }

    HashMap<String, Integer> getRequirementList() {
        return requirementList;
    }

    HashMap<String, Quotation> getQuotationData() {
        return quotationData;
    }

    HashMap<String, HashMap<String, Invoice>> getInvoiceData() {
        return invoiceData;
    }

    void addManger(String id, String role) {
        switch (role) {
            case "inventory":
                usersData.put(id, new InventoryManager(id, role));
                break;
            case "sales":
                usersData.put(id, new SalesHead(id, role));
                break;
            case "manufacturing":
                usersData.put(id, new Manufacturer(id, role));
                break;
            case "customer":
                usersData.put(id, new Customer(id, role));
                break;
        }
    }

    void addToInventory(HashMap<String, Inventory> manufacturedProducts) {
        for (Map.Entry<String, Inventory> map : manufacturedProducts.entrySet()) {
            if (!inventoryData.containsKey(map.getKey())) {
                inventoryData.put(map.getKey(), map.getValue());
            } else {
                inventoryData.get(map.getKey()).addQuantity(map.getValue().getQuantity());
            }
        }
        refreshRequirementList(manufacturedProducts);
        this.manufacturedProductsList.clear();
    }

    private void refreshRequirementList(HashMap<String, Inventory> manufacturedProductsList) {
        manufacturedProductsList.forEach((k, v) -> {
            if (requirementList.containsKey(k)) {
                int newQuantity = requirementList.get(k) - v.getQuantity();
                if (newQuantity <= 0) {
                    requirementList.remove(k);
                } else {
                    requirementList.replace(k, newQuantity);
                }
            }
        });
    }

    void addToRequirementList(HashMap<String, Integer> requirementProposal) {
        for (String productID : requirementProposal.keySet()) {
            if (requirementList.containsKey(productID)) {
                requirementList.put(productID, (requirementList.get(productID) + requirementProposal.get(productID)));
            } else {
                requirementList.put(productID, requirementProposal.get(productID));
            }
        }
    }

    boolean processOrder(String ID, String orderID, Order order) {
        for (IndividualDetails temp : order.getDetails()) {
            if (((inventoryData.get(temp.getProductID()).getQuantity() - temp.getQuantity()) < 0)) {
                return false;
            }
        }
        for (IndividualDetails temp : order.getDetails()) {
            inventoryData.get(temp.getProductID()).subQuantity(temp.getQuantity());
        }
        invoiceData.get(ID).get(orderID).setStatus(true);
        return true;
    }

    void addNewManufacturedProductsToList(String productID, Integer quantity) {
        if (!manufacturedProductsList.containsKey(productID)) {
            manufacturedProductsList.put(productID, new Inventory(inventoryData.get(productID).getProduct(), quantity));
        } else {
            manufacturedProductsList.get(productID).addQuantity(quantity);
        }
    }

    void addNewManufacturedProductsToList(String productID, Inventory products) {
        manufacturedProductsList.put(productID, products);
    }

    void addToQuotationRequest(String customerID, HashMap<String, Integer> quotation) {
        quotationRequests.put(customerID, quotation);
    }

    void addToQuotation(String customerID, Quotation quotation) {
        quotationData.put(customerID, quotation);
    }

    public void confirmOrder(String id) {
        Order order = new Order(id);
        order.setOrderID(Utils.generateID("Order"));
        ArrayList<IndividualDetails> tempList = quotationData.remove(id).getIndividualDetails();
        for (IndividualDetails temp : tempList) {
            if (temp.getStatus().equals(AvailabilityStatus.AVAILABLE)) {
                order.addToOrder(temp);
            }
        }
        if(!ordersData.containsKey(id)) {
            ordersData.put(id, new HashMap<>());
        }
        ordersData.get(id).put(order.getOrderID(), order);
        quotationRequests.remove(id);
    }

    public boolean checkFor(String productNameOrID) {
        ArrayList<HashMap<String, Inventory>> lists = new ArrayList<>();
        lists.add(getInventoryData());
        lists.add(getManufacturedProductsList());
        for (HashMap<String, Inventory> map : lists) {
            for (Inventory inventory : map.values()) {
                if (inventory.getProduct().getProductName().equalsIgnoreCase(productNameOrID)) {
                    return true;
                }
                if (inventory.getProduct().getProductID().equals(productNameOrID)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addToInvoice(String customerID, String orderID, Order order) {
        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        if (!invoiceData.containsKey(customerID)) {
            invoiceData.put(customerID, new HashMap<>());
        }
        invoiceData.get(customerID).put(orderID, invoice);
        ordersData.get(customerID).remove(orderID);
    }
}
