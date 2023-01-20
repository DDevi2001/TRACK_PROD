package model;

public class Invoice {
    private String orderID;
    private Order order;
    private boolean status;

    public Order getOrder() {
        return order;
    }

    public String getOrderID() {
        return orderID;
    }

    public boolean isStatus() {
        return status;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
