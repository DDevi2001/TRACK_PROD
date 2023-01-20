package model;

import java.util.ArrayList;

public class Order {
    private String customerID;
    private final ArrayList<IndividualDetails> details = new ArrayList<>();

    public Order (String customerID) {
        this.customerID = customerID;
    }

    public void addToOrder(IndividualDetails request) {
        details.add(request);
    }

    public ArrayList<IndividualDetails> getDetails() {
        return details;
    }
    public String getCustomerID() {
        return customerID;
    }
}
