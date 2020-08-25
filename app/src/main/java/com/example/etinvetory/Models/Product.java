package com.example.etinvetory.Models;

public class Product {
    private String shelf_Number, category, description, shelf_Letter, units;
    private int quantity, id;


    public Product(int id, String shelf_Number, String category, String description, String shelf_Letter, String units, int quantity) {
        this.id = id;
        this.shelf_Number = shelf_Number;
        this.category = category;
        this.description = description;
        this.shelf_Letter = shelf_Letter;
        this.units = units;
        this.quantity = quantity;
    }

    public Product(String description, int quantity, int id) {
        this.description = description;
        this.quantity = quantity;
        this.id = id;
    }

    public Product() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShelf_Number() {
        return shelf_Number;
    }

    public void setShelf_Number(String shelf_Number) {
        this.shelf_Number = shelf_Number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShelf_Letter() {
        return shelf_Letter;
    }

    public void setShelf_Letter(String shelf_Letter) {
        this.shelf_Letter = shelf_Letter;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", shelf_Number='" + shelf_Number + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", shelf_Letter='" + shelf_Letter + '\'' +
                ", units='" + units + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
