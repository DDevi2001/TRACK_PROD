package model;

public class Product {
    private String name;
    private String id;
    private float weight;
    private float manufacturingCost;

    public Product(String name, String id, float weight, float manufacturingCost){
        this.name = name;
        this.id = id;
        this.weight = weight;
        this.manufacturingCost = manufacturingCost;
    }

    public String getProductName() {
        return name;
    }
    public String getProductID() {
        return id;
    }

    public float getWeight() {
        return weight;
    }

    public float getManufacturingCost() {
        return manufacturingCost;
    }
}
