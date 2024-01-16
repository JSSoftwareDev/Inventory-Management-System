package myapp.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *  RUNTIME ERROR: error in deletion process.
 *  Resolution: I Corrected an error in the deletion process to handle success and failure cases.
 *  FUTURE ENHANCEMENT: We can consider implementing validation checks for modifying a product to ensure data integrity.
 *
 */

// Controller class for the main view of the Inventory Management System.
public class MainController implements Initializable {

    // Inventory instance
    private Inventory inventory;

    // Buttons for adding and modifying products
    @FXML
    private Button addProductButton;

    @FXML
    private Button modifyProductButton;

    // TextFields for searching parts and products
    @FXML
    private TextField partSearchText;

    @FXML
    private TextField productSearchText;

    // TableViews for displaying parts and products
    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableView<Product> productTableView;

    // TableColumns for parts
    @FXML
    private TableColumn<Part, Integer> partIdColumn;

    @FXML
    private TableColumn<Part, String> partNameColumn;

    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;

    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    // TableColumns for products
    @FXML
    private TableColumn<Product, Integer> productIdColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Integer> productInventoryColumn;

    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    /**
     * Retrieves all parts from the inventory.
     *
     * @return ObservableList of all parts.
     */
    public static ObservableList<Part> getAllParts() {
        return Inventory.getAllParts();
    }

    /**
     * Searching for parts based on the given search text.
     *
     * @param searchText The text to search for parts.
     * @return ObservableList of parts matching the search criteria.
     */
    public static ObservableList<Part> searchParts(String searchText) {
        return Inventory.searchParts(searchText);
    }

    /**
     * Retrieving the part to be modified.
     *
     * @return The part to be modified.
     */
    public static Part getPartToModify() {
        return Inventory.getPartToModify();
    }

    /**
     * Setting the inventory for the controller.
     *
     * @param inventory The inventory to set.
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Handling the action event for adding a product.
     *
     * @throws IOException if an error occurs during FXMLLoader loading.
     */
    @FXML
    void addProductAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProductView.fxml"));
        Parent root = loader.load();

        AddProductController addProductController = loader.getController();
        addProductController.setInventory(inventory);

        Stage stage = new Stage();
        stage.setTitle("Add Product");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Handling the action event for modifying a product.
     * <p>
     * FUTURE ENHANCEMENT: We can consider implementing validation checks for modifying a product to ensure data integrity.
     *
     * @param event The action event.
     * @throws IOException if an error occurs during FXMLLoader loading.
     */
    @FXML
    void modifyProductAction(ActionEvent event) throws IOException {
        Product productToModify = productTableView.getSelectionModel().getSelectedItem();

        if (productToModify == null) {
            displayAlert(4);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProductView.fxml"));
            Parent root = loader.load();

            ModifyProductController modifyProductController = loader.getController();
            modifyProductController.setInventory(inventory);
            modifyProductController.setProductToModify(productToModify);

            Stage stage = new Stage();
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    /**
     * Handling the action event for exiting the application.
     *
     * @param event The action event.
     */
    @FXML
    void exitButtonAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Handling the action event for adding a part.
     *
     * @param event The action event.
     * @throws IOException if an error occurs during FXMLLoader loading.
     */
    @FXML
    void partAddAction(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddPartView.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the action event for deleting a part.
     * <p>
     * FUTURE ENHANCEMENT: We can implement a confirmation dialog before deleting a part.
     *
     * @param event The action event.
     */
    @FXML
    void partDeleteAction(ActionEvent event) {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            displayAlert(3);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to delete the selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    Inventory.deletePart(selectedPart);
                    partTableView.setItems(Inventory.getAllParts());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Handling the action event for modifying a part.
     *
     * @param event The action event.
     * @throws IOException if an error occurs during FXMLLoader loading.
     */
    @FXML
    void partModifyAction(ActionEvent event) throws IOException {
        Part partToModify = partTableView.getSelectionModel().getSelectedItem();

        if (partToModify == null) {
            displayAlert(3);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPartView.fxml"));
            Parent root = loader.load();

            ModifyPartController modifyPartController = loader.getController();
            modifyPartController.setPartToModify(partToModify);
            modifyPartController.setInventory(inventory);

            Stage stage = new Stage();
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    /**
     * Handling the action event for searching parts.
     *
     * @param event The action event.
     */
    @FXML
    void partSearchBtnAction(ActionEvent event) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = partSearchText.getText().toLowerCase();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().toLowerCase().contains(searchString)) {
                partsFound.add(part);
            }
        }

        partTableView.setItems(partsFound);

        if (partsFound.isEmpty()) {
            displayAlert(1);
        }
    }

    /**
     * Handling the key pressed event for searching parts.
     *
     * @param event The key event.
     */
    @FXML
    void partSearchTextKeyPressed(KeyEvent event) {
        if (partSearchText.getText().isEmpty()) {
            partTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * Handling the action event for adding a product.
     *
     * @param event The action event.
     * @throws IOException if an error occurs during FXMLLoader loading.
     */
    @FXML
    void productAddAction(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddProductView.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the action event for deleting a product.
     * <p>
     * FUTURE ENHANCEMENT: We can implement a confirmation dialog before deleting a part.
     * <p>
     * RUNTIME ERROR: I Corrected an error in the deletion process to handle success and failure cases.
     *
     * @param event The action event.
     */
    @FXML
    void productDeleteAction(ActionEvent event) {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            displayAlert(4); // No product selected error
            return;
        }

        if (productHasAssociatedParts(selectedProduct)) {
            // Display error message for associated parts
            displayAlert(5);
        } else {
            // Confirm deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to delete the selected product?");
            Optional<ButtonType> result = alert.showAndWait();

            // Corrected runtime error handling the deletion process
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Refresh productTableView to reflect changes
                productTableView.setItems(Inventory.getAllProducts());

                if (Inventory.deleteProduct(selectedProduct)) {
                    // Deletion successful
                    productTableView.setItems(Inventory.getAllProducts());
                } else {
                    // Display error message for product not deleted
                    displayAlert(6);
                }
            }
        }
    }

    /**
     * Checks to see if a product has associated parts.
     *
     * @param product The product to check.
     * @return True if the product has associated parts, false otherwise.
     */
    private boolean productHasAssociatedParts(Product product) {
        return !product.getAllAssociatedParts().isEmpty();
    }


    /**
     * Handling the action event for modifying a product.
     *
     * @param event The action event.
     * @throws IOException if an error occurs during FXMLLoader loading.
     */
    @FXML
    void productModifyAction(ActionEvent event) throws IOException {
        Product productToModify = productTableView.getSelectionModel().getSelectedItem();

        if (productToModify == null) {
            displayAlert(4);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProductView.fxml"));
            Parent root = loader.load();

            ModifyProductController modifyProductController = loader.getController();
            modifyProductController.setInventory(inventory);
            modifyProductController.setProductToModify(productToModify);

            Stage stage = new Stage();
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    /**
     * Handling the action event for searching products.
     *
     * @param event The action event.
     */
    @FXML
    void productSearchBtnAction(ActionEvent event) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        String searchString = productSearchText.getText().toLowerCase();

        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(searchString) ||
                    product.getName().toLowerCase().contains(searchString)) {
                productsFound.add(product);
            }
        }

        productTableView.setItems(productsFound);

        if (productsFound.isEmpty()) {
            displayAlert(2);
        }
    }

    /**
     * Handling the key pressed event for searching products.
     *
     * @param event The key event.
     */
    @FXML
    void productSearchTextKeyPressed(KeyEvent event) {
        if (productSearchText.getText().isEmpty()) {
            productTableView.setItems(Inventory.getAllProducts());
        }
    }

    /**
     * This Initializes the controller, setting up the part and product TableViews and columns.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partTableView.setItems(Inventory.getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(Inventory.getAllProducts());
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Displays an alert based on the given alert type.
     *
     * @param alertType The type of alert to display.
     */
    private void displayAlert(int alertType) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Alert alertError = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1:
                alert.setTitle("Information");
                alert.setHeaderText("Part not found");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Information");
                alert.setHeaderText("Product not found");
                alert.showAndWait();
                break;
            case 3:
                alertError.setTitle("Error");
                alertError.setHeaderText("Part not selected");
                alertError.showAndWait();
                break;
            case 4:
                alertError.setTitle("Error");
                alertError.setHeaderText("Product not selected");
                alertError.showAndWait();
                break;
            case 5:
                alertError.setTitle("Error");
                alertError.setHeaderText("Parts Associated");
                alertError.setContentText("All parts must be removed from the product before deletion.");
                alertError.showAndWait();
                break;
            case 6:
                alertError.setTitle("Error");
                alertError.setHeaderText("Product not deleted");
                alertError.setContentText("An error occurred while deleting the product.");
                alertError.showAndWait();
                break;
        }
    }

    /**
     * Refreshes the user interface, updating displayed data and views.
     * Invoked to reflect any changes in the inventory system.
     */
    public void refresh() {
        // Implementation goes here
    }

    /**
     * Handles the search functionality for parts in the inventory.
     * Invoked when the user initiates a search for a specific part.
     */
    public void handlePartSearch() {
        // Implementation goes here
    }

    /**
     * Handles the deletion of a part from the inventory.
     * Invoked when the user triggers the part deletion action.
     * Displays a confirmation dialog before removing the selected part.
     */
    public void handlePartDeletion() {
        // Implementation goes here
    }
}
