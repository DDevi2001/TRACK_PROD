package model;

public class Invoice {
    private Order order;
    private boolean status;

    public Order getOrder() {
        return order;
    }

    public boolean isStatus() {
        return status;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
