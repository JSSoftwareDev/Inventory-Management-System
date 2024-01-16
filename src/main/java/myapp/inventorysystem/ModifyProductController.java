package myapp.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * RUNTIME ERROR: Handled a generic exception for unexpected errors.
 * Resolution: Used a catch block that prevents the program from crashing and invokes the
 * displayAlert method with a custom message for error handling.
 * FUTURE ENHANCEMENT: Add support for batch updating of associated parts.
 * Controller class for modifying a product.
 */
public class ModifyProductController implements Initializable {

    @FXML
    private Label messageLabel;

    @FXML
    private TextField productIdText;

    @FXML
    private TextField productNameText;

    @FXML
    private TextField productInventoryText;

    @FXML
    private TextField productPriceText;

    @FXML
    private TextField productMinText;

    @FXML
    private TextField productMaxText;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn<Part, Integer> partIdColumn;

    @FXML
    private TableColumn<Part, String> partNameColumn;

    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;

    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    @FXML
    private TextField partSearchText;

    @FXML
    private TableView<Part> associatedPartTableView;

    @FXML
    private TableColumn<Part, Integer> associatedPartIdColumn;

    @FXML
    private TableColumn<Part, String> associatedPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> associatedPartInventoryColumn;

    @FXML
    private TableColumn<Part, Double> associatedPartPriceColumn;

    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private Inventory inventory;
    private Product selectedProduct;
    private Stage stage;

    /**
     * Initializes the controller class.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        associatedParts.clear();
        loadAllParts();
        populateFieldsFromProduct();
    }

    /**
     * Sets up table columns for parts and associated parts.
     */
    private void setupTableColumns() {
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Loads all parts into the table view.
     */
    private void loadAllParts() {
        allParts.setAll(Inventory.getAllParts());
        partTableView.setItems(allParts);
    }

    /**
     * Populates fields with data from the selected product.
     */
    private void populateFieldsFromProduct() {
        if (selectedProduct != null) {
            productIdText.setText(String.valueOf(selectedProduct.getId()));
            productNameText.setText(selectedProduct.getName());
            productInventoryText.setText(String.valueOf(selectedProduct.getStock()));
            productPriceText.setText(String.valueOf(selectedProduct.getPrice()));
            productMaxText.setText(String.valueOf(selectedProduct.getMax()));
            productMinText.setText(String.valueOf(selectedProduct.getMin()));

            associatedParts.clear();
            associatedParts.addAll(selectedProduct.getAllAssociatedParts());
            associatedPartTableView.setItems(associatedParts);
        }
    }

    /**
     * Handles the action when the "Add" button is clicked.
     *
     * @param event The event triggered by the button click.
     */
    @FXML
    void addButtonAction(ActionEvent event) {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            if (!associatedParts.contains(selectedPart)) {
                associatedParts.add(selectedPart);
                associatedPartTableView.setItems(associatedParts);
            } else {
                displayAlert("info", "Part Already Added", "Selected part is already associated with the product.");
            }
        }
    }

    /**
     * Handles the action when the "Remove" button is clicked.
     *
     * @param event The event triggered by the button click.
     */
    @FXML
    void removeButtonAction(ActionEvent event) {
        Part selectedPart = associatedPartTableView.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Removal");
            confirmationAlert.setContentText("Are you sure you want to remove this associated part?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(selectedPart);
                associatedPartTableView.setItems(associatedParts);
            }
        }
    }


    /**
     * Handles the action when the "Save" button is clicked.
     *
     * @param event The event triggered by the button click.
     */
    @FXML
    void saveButtonAction(ActionEvent event) {
        try {
            validateAndSaveProduct(event);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            displayAlert("error", "Modify Product Error", "Invalid values or blank fields.");
        }
    }

    private void validateAndSaveProduct(ActionEvent event) {
        String newName = productNameText.getText();
        int newStock = Integer.parseInt(productInventoryText.getText());
        double newPrice = Double.parseDouble(productPriceText.getText());
        int newMin = Integer.parseInt(productMinText.getText());
        int newMax = Integer.parseInt(productMaxText.getText());

        if (newMin >= newMax || newStock < newMin || newStock > newMax) {
            displayAlert("error", "Validation Error", "Min should be less than Max, and Inventory should be between Min and Max.");
            return;
        }

        selectedProduct.setName(newName);
        selectedProduct.setStock(newStock);
        selectedProduct.setPrice(newPrice);
        selectedProduct.setMin(newMin);
        selectedProduct.setMax(newMax);

        // Update associated parts of the selected product
        selectedProduct.getAllAssociatedParts().setAll(associatedParts);

        displayAlert("info", "Product Modified", "Product has been successfully modified.");

        // Return to the main screen
        returnToMainScreen(event);
    }


    /**
     * Handles the action when the "Delete" button is clicked.
     *
     * @param event The event triggered by the button click.
     */
    @FXML
    void deleteButtonAction(ActionEvent event) {
        if (selectedProduct != null) {
            if (!associatedParts.isEmpty()) {
                // Display a warning about associated parts
                displayAlert("error", "Deletion Error", "Cannot delete a product with associated parts.");
                return;
            }

            // No associated parts, proceed with deletion
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deleteAlert.setTitle("Confirm Deletion");
            deleteAlert.setContentText("Are you sure you want to delete this product?");
            Optional<ButtonType> result = deleteAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedProduct);
                returnToMainScreen(event);
            }
        }
    }

    /**
     * Handles the action when the "Cancel" button is clicked.
     *
     * @param event The event triggered by the button click.
     */
    @FXML
    void cancelButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Do you want to cancel changes and return to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            returnToMainScreen(event);
        }
    }

    /**
     * Searches for parts based on the given search string.
     *
     * @param searchString The string to search for in parts.
     * @return The list of found parts.
     */
    private ObservableList<Part> searchParts(String searchString) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();

        for (Part part : allParts) {
            String partId = String.valueOf(part.getId());
            String partName = part.getName().toLowerCase();

            if (partId.contains(searchString) || partName.contains(searchString.toLowerCase())) {
                partsFound.add(part);
            }
        }

        return partsFound;
    }

    /**
     * Validates input and saves the modified product.
     */

    /**
     * Returns to the main screen.
     *
     * @param event The event triggered by the button click.
     */
    private void returnToMainScreen(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog with the given type, title, and content.
     *
     * @param type    The type of the alert (error or info).
     * @param title   The title of the alert.
     * @param content The content of the alert.
     */
    private void displayAlert(String type, String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);

        switch (type) {
            case "error":
                alert.setTitle(title);
                alert.setHeaderText(title);
                alert.setContentText(content);
                alert.showAndWait();
                break;
            case "info":
                alertInfo.setTitle(title);
                alertInfo.setHeaderText(title);
                alertInfo.setContentText(content);
                alertInfo.showAndWait();
                break;
        }
    }

    /**
     * Sets the product to be modified.
     *
     * @param productToModify The product to be modified.
     */
    public void setProductToModify(Product productToModify) {
        this.selectedProduct = productToModify;
        populateFieldsFromProduct();
    }

    /**
     * Sets the inventory for the controller.
     *
     * @param inventory The inventory to set.
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Handles the key released event in the part search text field.
     *
     * @param event The key event.
     */
    @FXML
    void partSearchTextKeyReleased(KeyEvent event) {
        String searchString = partSearchText.getText().toLowerCase();
        ObservableList<Part> filteredParts = searchParts(searchString);
        partTableView.setItems(filteredParts);
    }

    @FXML
    void partSearchBtnAction(ActionEvent actionEvent) {
        String searchString = partSearchText.getText().toLowerCase();
        ObservableList<Part> filteredParts = searchParts(searchString);
        partTableView.setItems(filteredParts);

        if (filteredParts.isEmpty()) {
            displayAlert("info", "No Parts Found", "No parts found matching the search criteria.");
        }
    }

}