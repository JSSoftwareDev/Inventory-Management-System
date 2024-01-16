package myapp.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class representing the inventory system that manages parts and products.
 * RUNTIME ERROR: Method name should be camelCase, not snake_case.
 * Resolution Method named was made to be camelCase.
 * FUTURE ENHANCEMENT: Consider additional logic for updating parts based on specific requirements.
 * FUTURE ENHANCEMENT: Implement logic to search for products based on the search string.
 */
public class Inventory {

    /**
     * Counter for generating unique part IDs.
     */
    private static int partId = 0;

    /**
     * Counter for generating unique product IDs.
     */
    private static int productId = 0;

    /**
     * List to store all parts in the inventory.
     */
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * List to store all products in the inventory.
     */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Get all parts in the inventory.
     *
     * @return ObservableList of all parts.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Get all products in the inventory.
     *
     * @return ObservableList of all products.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Add a new part to the inventory.
     *
     * @param newPart The part to be added.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Add a new product to the inventory.
     *
     * @param newProduct The product to be added.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Get a new unique ID for a part.
     *
     * @return A new part ID.
     */
    public static int getNewPartId() {
        return ++partId;
    }

    /**
     * Get a new unique ID for a product.
     *
     * @return A new product ID.
     */
    public static int getNewProductId() {
        return ++productId;
    }

    /**
     * Look up a part by its ID.
     *
     * @param partId The ID of the part to look up.
     * @return The found part, or null if not found.
     */
    public static Part lookupPart(int partId) {
        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * Look up parts by their name.
     *
     * @param partName The name of the parts to look up.
     * @return ObservableList of found parts.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().equals(partName)) {
                partsFound.add(part);
            }
        }
        return partsFound;
    }

    /**
     * Look up a product by its ID.
     *
     * @param productId The ID of the product to look up.
     * @return The found product, or null if not found.
     */
    public static Product lookupProduct(int productId) {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Look up products by their name.
     *
     * @param productName The name of the products to look up.
     * @return ObservableList of found products.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().equals(productName)) {
                productsFound.add(product);
            }
        }
        return productsFound;
    }

    /**
     * Update a part at the specified index with a new part.
     *
     * @param index         The index of the part to update.
     * @param selectedPart  The new part to replace the existing one.
     */
    public static void updatePart(int index, Part selectedPart) {
        if (selectedPart != null) {
            allParts.set(index, selectedPart);
        }
    }

    /**
     * Update a product at the specified index with a new product.
     *
     * @param index             The index of the product to update.
     * @param selectedProduct   The new product to replace the existing one.
     */
    public static void updateProduct(int index, Product selectedProduct) {
        if (selectedProduct != null) {
            allProducts.set(index, selectedProduct);
        }
    }

    /**
     * Delete a part from the inventory.
     *
     * @param selectedPart The part to be deleted.
     * @return True if deletion is successful, false otherwise.
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Delete a product from the inventory.
     *
     * @param selectedProduct The product to be deleted.
     * @return True if deletion is successful, false otherwise.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Update a part in the inventory.
     *
     * @param selectedPart The part to be updated.
     * FUTURE ENHANCEMENT: Consider additional logic for updating parts based on specific requirements.
     */
    public static void updatePart(Part selectedPart) {
        // No specific implementation yet for the future enhancement.
        // Consider additional logic for updating parts based on specific requirements.
    }

    /**
     * Search for parts in the inventory based on a search string.
     *
     * @param searchText The search string.
     * @return ObservableList of found parts.
     */
    public static ObservableList<Part> searchParts(String searchText) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchText) ||
                    part.getName().toLowerCase().contains(searchText.toLowerCase())) {
                partsFound.add(part);
            }
        }
        return partsFound;
    }

    /**
     * Get an instance of the inventory.
     *
     * @return An instance of the inventory.
     */
    public static Inventory getInstance() {
        return new Inventory();
    }

    /**
     * Get the part to modify.
     *
     * @return The part to modify.
     * FUTURE ENHANCEMENT: No specific implementation yet for the future enhancement.
     * Consider additional logic for determining the part to modify based on specific requirements.
     */
    public static Part getPartToModify() {
        // Consider additional logic for determining the part to modify based on specific requirements.
        return null;
    }

    public static void updatePart(Part selectedPart, OutSourced modifiedPart) {
    }

    /**
     * Search for products in the inventory based on a search string.
     *
     * @param searchString The search string.
     * @return ObservableList of found products.
     */
    public ObservableList<Product> searchProducts(String searchString) {
        // FUTURE ENHANCEMENT: Implement logic to search for products based on the search string.
        // Consider the requirements for searching products and return the appropriate results.
        return null;
    }
}
