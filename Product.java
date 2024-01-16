package myapp.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents a product in the inventory system.
 */
public class Product {

    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Constructor to create a new product.
     *
     * @param id    The ID of the product.
     * @param name  The name of the product.
     * @param price The price of the product.
     * @param stock The stock quantity of the product.
     * @param min   The minimum stock level for the product.
     * @param max   The maximum stock level for the product.
     *              FUTURE ENHANCEMENT: Consider additional logic for setting all associated parts based on specific requirements.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Get the ID of the product.
     *
     * @return The product ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the ID of the product.
     *
     * @param id The product ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the name of the product.
     *
     * @return The product name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the product.
     *
     * @param name The product name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the price of the product.
     *
     * @return The product price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set the price of the product.
     *
     * @param price The product price.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Get the stock quantity of the product.
     *
     * @return The stock quantity.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Set the stock quantity of the product.
     *
     * @param stock The stock quantity.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Get the minimum stock level for the product.
     *
     * @return The minimum stock level.
     */
    public int getMin() {
        return min;
    }

    /**
     * Set the minimum stock level for the product.
     *
     * @param min The minimum stock level.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Get the maximum stock level for the product.
     *
     * @return The maximum stock level.
     */
    public int getMax() {
        return max;
    }

    /**
     * Set the maximum stock level for the product.
     *
     * @param max The maximum stock level.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Add an associated part to the product.
     *
     * @param part The part to be added.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Delete an associated part from the product.
     *
     * @param selectedAssociatedPart The part to be deleted.
     * @return True if deletion is successful, false otherwise.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Get all associated parts of the product.
     *
     * @return ObservableList of associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * Get names of all associated parts.
     *
     * @return ObservableList of associated part names.
     */
    public ObservableList<String> getAssociatedPartsNames() {
        ObservableList<String> names = FXCollections.observableArrayList();
        for (Part part : associatedParts) {
            names.add(part.getName());
        }
        return names;
    }

    /**
     * Override the toString method to provide a string representation of the product.
     *
     * @return String representation of the product.
     */
    @Override
    public String toString() {
        return "Product ID: " + getId() + "\nProduct Name: " + getName() + "\nInventory Level: " + getStock() +
                "\nPrice: " + getPrice() + "\nAssociated Parts: " + getAssociatedPartsNames().toString();
    }

}