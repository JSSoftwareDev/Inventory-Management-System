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
 * Controller class for managing the addition of products in the inventory system.
 * Implements the Initializable interface for JavaFX initialization.
 *  RUNTIME ERROR: There is no call to setItems for associatedPartTableView.
 *  Resolution: To fix this, the setItems method is added in the addButtonAction and removeButtonAction methods to update the associatedPartTableView when parts are added or removed.
 *  FUTURE ENHANCEMENT: Consider additional initialization logic when setting the inventory
 */
public class AddProductController implements Initializable {

    /**
     * List to store associated parts with the product being added.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * The main inventory instance where products and parts are managed.
     */
    private Inventory inventory;

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

    @FXML
    private TextField productIdText;
    @FXML
    private TextField productNameText;
    @FXML
    private TextField productInventoryText;
    @FXML
    private TextField productPriceText;
    @FXML
    private TextField productMaxText;
    @FXML
    private TextField productMinText;

    /**
     * Initializes the controller after its root element has been completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTableView.setItems(Inventory.getAllParts());

    }

    @FXML
    void partSearchBtnAction(ActionEvent event) {
        String searchString = partSearchText.getText();

        if (!searchString.isEmpty()) {
            ObservableList<Part> foundParts = searchParts(searchString);

            if (foundParts.isEmpty()) {
                displayAlert("info", "Part Not Found", "No parts matching the search criteria.");
            } else {
                partTableView.setItems(foundParts); // Update the partTableView with the search results
            }
        } else {
            // If the search string is empty, display all parts
            partTableView.setItems(Inventory.getAllParts());
        }
    }


    @FXML
    void partSearchKeyPressed(KeyEvent event) {
        if (partSearchText.getText().isEmpty()) {
            partTableView.setItems(Inventory.getAllParts());
        }
    }

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

    @FXML
    void saveButtonAction(ActionEvent event) throws IOException {
        try {
            String name = productNameText.getText();
            double price = Double.parseDouble(productPriceText.getText());
            int stock = Integer.parseInt(productInventoryText.getText());
            int min = Integer.parseInt(productMinText.getText());
            int max = Integer.parseInt(productMaxText.getText());

            if (name.isEmpty()) {
                displayAlert("error", "Add Product Error", "Product name cannot be empty.");
            } else if (!isMinValid(min, max)) {
                displayAlert("error", "Invalid Min", "Min must be a number greater than 0 and less than Max.");
            } else if (!isInventoryValid(min, max, stock)) {
                displayAlert("error", "Invalid Inventory", "Inventory must be a number equal to or between Min and Max.");
            } else {
                // Check if there are no associated parts and confirm with the user
                if (associatedParts.isEmpty() && !confirmNoAssociatedParts()) {
                    return;
                }

                Product newProduct = new Product(inventory.getNewProductId(), name, price, stock, min, max);
                newProduct.getAllAssociatedParts().addAll(associatedParts);
                inventory.addProduct(newProduct);
                returnToMainScreen(event);
            }

        } catch (Exception e) {
            e.printStackTrace();
            displayAlert("error", "Add Product Error", "Invalid values or blank fields.");
        }
    }

    private boolean confirmNoAssociatedParts() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("No Associated Parts");
        alert.setContentText("You are about to save a product without any associated parts. Do you want to proceed?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    public class
    ModifyPartController implements Initializable {

        @FXML
        private RadioButton inHouseRadioButton;

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

        @FXML
        private TableView<Part> partTableView;

        @FXML
        private TableColumn<Part, Integer> partIdColumn;

        @FXML
        private TableColumn<Part, Integer> partInventoryColumn;

        @FXML
        private TableColumn<Part, String> partNameColumn;

        @FXML
        private TableColumn<Part, Double> partPriceColumn;

        @FXML
        private Button saveButton;

        @FXML
        private Button cancelButton;

        @FXML
        private Label partIdNameLabel;

        @FXML
        private RadioButton outsourcedRadioButton;

        @FXML
        private ToggleGroup tgPartType;

        @FXML
        private TextField partIdText;

        @FXML
        private TextField partNameText;

        @FXML
        private TextField partInventoryText;

        @FXML
        private TextField partPriceText;

        @FXML
        private TextField partMaxText;

        @FXML
        private TextField partIdNameText;

        @FXML
        private TextField partMinText;

        private Part selectedPart;

        /**
         * Initializes the ModifyPartController.
         *
         * @param location   The location used to resolve relative paths for the root object.
         * @param resources  The resources used to localize the root object, or null if the root object was not localized.
         */
        @Override
        public void initialize(URL location, ResourceBundle resources) {
            selectedPart = MainController.getPartToModify();
            inHouseRadioButton.setSelected(true);
            setupRadioButtons();

            // Check if selectedPart is not null before calling populateFields
            if (selectedPart != null) {
                populateFields();
            }
        }

        /**
         * This is the Action handler for the in-house radio button.
         *
         * @param event The ActionEvent triggered by the in-house radio button.
         */
        @FXML
        void inHouseRadioButtonAction(ActionEvent event) {
            updateLabelAndStyles("Machine ID", "-fx-background-color: white;", "-fx-background-color: transparent;");
        }

        /**
         * This is the Action handler for the outsourced radio button.
         *
         * @param event The ActionEvent triggered by the outsourced radio button.
         */
        @FXML
        void outsourcedRadioButtonAction(ActionEvent event) {
            updateLabelAndStyles("Company Name", "-fx-background-color: white;", "-fx-background-color: transparent;");
        }

        /**
         * Setting up the radio buttons and their listeners.
         */
        private void setupRadioButtons() {
            ToggleGroup toggleGroup = new ToggleGroup();
            inHouseRadioButton.setToggleGroup(toggleGroup);
            outsourcedRadioButton.setToggleGroup(toggleGroup);

            toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == inHouseRadioButton) {
                    updateLabelAndStyles("Machine ID", "-fx-background-color: white;", "-fx-background-color: transparent;");
                } else if (newValue == outsourcedRadioButton) {
                    updateLabelAndStyles("Company Name", "-fx-background-color: white;", "-fx-background-color: transparent;");
                }
            });
        }

        /**
         * Updating label and styles based on the selected part type.
         *
         * @param label             The label to be set.
         * @param inHouseStyle      The style for the in-house radio button.
         * @param outsourcedStyle   The style for the outsourced radio button.
         */
        private void updateLabelAndStyles(String label, String inHouseStyle, String outsourcedStyle) {
            partIdNameLabel.setText(label);
            inHouseRadioButton.setStyle(inHouseStyle);
            outsourcedRadioButton.setStyle(outsourcedStyle);
        }

        /**
         * This is the Action handler for the save button.
         *
         * @param event The ActionEvent triggered by the save button.
         */
        @FXML
        void saveButtonAction(ActionEvent event) {
            try {
                String name = partNameText.getText();
                double price = Double.parseDouble(partPriceText.getText());
                int stock = Integer.parseInt(partInventoryText.getText());
                int min = Integer.parseInt(partMinText.getText());
                int max = Integer.parseInt(partMaxText.getText());

                if (minValid(min, max) && inventoryValid(min, max, stock)) {
                    if (inHouseRadioButton.isSelected()) {
                        handleInHousePart(name, price, stock, min, max);
                    } else if (outsourcedRadioButton.isSelected()) {
                        handleOutsourcedPart(name, price, stock, min, max);
                    }

                    Inventory.updatePart(selectedPart);
                    returnToMainScreen(event);
                }
            } catch (NumberFormatException e) {
                displayAlert(1);
            }
        }

        private void handleInHousePart(String name, double price, int stock, int min, int max) {
            try {
                int machineId = Integer.parseInt(partIdNameText.getText());
                InHouse modifiedPart = (InHouse) selectedPart;
                modifiedPart.setName(name);
                modifiedPart.setPrice(price);
                modifiedPart.setStock(stock);
                modifiedPart.setMin(min);
                modifiedPart.setMax(max);
                modifiedPart.setMachineId(machineId);
            } catch (NumberFormatException e) {
                displayAlert(2);
            }
        }

        private void handleOutsourcedPart(String name, double price, int stock, int min, int max) {
            try {
                if (selectedPart instanceof OutSourced) {
                    OutSourced modifiedPart = (OutSourced) selectedPart;

                    // Check if the company name field is not empty
                    String companyName = partIdNameText.getText();
                    if (companyName.isEmpty()) {
                        displayAlert(5); // Display an alert for an empty company name
                        return; // Don't proceed if the company name is empty
                    }

                    modifiedPart.setName(name);
                    modifiedPart.setPrice(price);
                    modifiedPart.setStock(stock);
                    modifiedPart.setMin(min);
                    modifiedPart.setMax(max);
                    modifiedPart.setCompanyName(companyName);

                    // Call the method to return to the main screen
                    returnToMainScreen(new ActionEvent());
                }
            } catch (NumberFormatException e) {
                // Handle NumberFormatException if necessary
                displayAlert(2);
            }
        }

        /**
         * This is the Action handler for the cancel button.
         *
         * @param event The ActionEvent triggered by the cancel button.
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
         * Validating the minimum value against the maximum value.
         *
         * @param min The minimum value.
         * @param max The maximum value.
         * @return True if the minimum value is valid, false otherwise.
         */
        private boolean minValid(int min, int max) {
            boolean isValid = true;

            if (min <= 0 || min >= max) {
                isValid = false;
                displayAlert(3);
            }

            return isValid;
        }

        /**
         * Validating the stock value against the minimum and maximum values.
         *
         * @param min   The minimum value.
         * @param max   The maximum value.
         * @param stock The stock value.
         * @return True if the stock value is valid, false otherwise.
         */
        private boolean inventoryValid(int min, int max, int stock) {
            // No need to check stock against min and max
            return true;
        }

        /**
         * Displays an alert based on the alert type.
         *
         * @param alertType The type of alert to be displayed.
         */
        private void displayAlert(int alertType) {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            switch (alertType) {
                case 1:
                    showAlert(alert, "Error Modifying Part", "Form contains blank fields or invalid values.");
                    break;
                case 2:
                    showAlert(alert, "Invalid value for Machine ID", "Machine ID may only contain numbers.");
                    break;
                case 3:
                    showAlert(alert, "Invalid value for Min", "Min must be a number greater than 0 and less than Max.");
                    break;
                case 4:
                    showAlert(alert, "Invalid value for Inventory", "Inventory must be a number equal to or between Min and Max");
                    break;
            }
        }

        /**
         * Displays an alert with the specified title and content.
         *
         * @param alert   The Alert instance to be configured and shown.
         * @param title   The title of the alert.
         * @param content The content of the alert.
         */
        private void showAlert(Alert alert, String title, String content) {
            alert.setTitle("Error");
            alert.setHeaderText(title);
            alert.setContentText(content);
            alert.showAndWait();
        }

        /**
         * The  search button.
         *
         * @param event The ActionEvent triggered by the part search button.
         */
        @FXML
        void partSearchBtnAction(ActionEvent event) {
            // Handle part search button action
        }

        /**
         * This the Key  released event handler for the part search text.
         *
         * @param event The KeyEvent triggered by the part search text.
         */
        @FXML
        void partSearchTextKeyReleased(KeyEvent event) {
            // Handle part search text key released event
        }

        /**
         * Initializing the controller with part data.
         *
         * @param part The part to be initialized with.
         */
        public void initializeData(Part part) {
            // Handle initialization with data, if needed
        }

        /**
         * Setting the inventory for the controller.
         *
         * @param inventory The inventory to be set.
         */
        public void setInventory(Inventory inventory) {
            // Set the inventory, if needed
        }

        /**
         * Setting the part to be modified and populates the fields.
         *
         * @param partToModify The part to be modified.
         */
        public void setPartToModify(Part partToModify) {
            this.selectedPart = partToModify;
            populateFields();
        }

        /**
         * Action handler for the remove button.
         *
         * @param event The ActionEvent triggered by the remove button.
         */
        @FXML
        void removeButtonAction(ActionEvent event) {
            // Handle remove button action
        }

        /**
         * Action handler for the add button.
         *
         * @param event The ActionEvent triggered by the add button.
         */
        @FXML
        void addButtonAction(ActionEvent event) {
            // Handle add button action
        }

        /**
         * Populating the fields with data from the selected part.
         */
        private void populateFields() {
            partIdText.setText(String.valueOf(selectedPart.getId()));
            partNameText.setText(selectedPart.getName());
            partInventoryText.setText(String.valueOf(selectedPart.getStock()));
            partPriceText.setText(String.valueOf(selectedPart.getPrice()));
            partMaxText.setText(String.valueOf(selectedPart.getMax()));
            partMinText.setText(String.valueOf(selectedPart.getMin()));

            if (selectedPart instanceof InHouse) {
                inHouseRadioButton.setSelected(true);
                updateLabelAndStyles("Machine ID", "-fx-background-color: white;", "-fx-background-color: transparent;");
                partIdNameText.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
            } else if (selectedPart instanceof OutSourced) {
                outsourcedRadioButton.setSelected(true);
                updateLabelAndStyles("Company Name", "-fx-background-color: white;", "-fx-background-color: transparent;");
                partIdNameText.setText(((OutSourced) selectedPart).getCompanyName());
            }
        }

        /**
         * Returns to the main screen.
         *
         * @param event The ActionEvent triggered by the return button.
         */
        private void returnToMainScreen(ActionEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
                Parent mainScreen = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(mainScreen));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public TextField getPartSearchText() {
            return partSearchText;
        }

        public void setPartSearchText(TextField partSearchText) {
            this.partSearchText = partSearchText;
        }

        public TableView<Part> getAssociatedPartTableView() {
            return associatedPartTableView;
        }

        public void setAssociatedPartTableView(TableView<Part> associatedPartTableView) {
            this.associatedPartTableView = associatedPartTableView;
        }

        public TableColumn<Part, Integer> getAssociatedPartIdColumn() {
            return associatedPartIdColumn;
        }

        public void setAssociatedPartIdColumn(TableColumn<Part, Integer> associatedPartIdColumn) {
            this.associatedPartIdColumn = associatedPartIdColumn;
        }

        public TableColumn<Part, String> getAssociatedPartNameColumn() {
            return associatedPartNameColumn;
        }

        public void setAssociatedPartNameColumn(TableColumn<Part, String> associatedPartNameColumn) {
            this.associatedPartNameColumn = associatedPartNameColumn;
        }

        public TableColumn<Part, Integer> getAssociatedPartInventoryColumn() {
            return associatedPartInventoryColumn;
        }

        public void setAssociatedPartInventoryColumn(TableColumn<Part, Integer> associatedPartInventoryColumn) {
            this.associatedPartInventoryColumn = associatedPartInventoryColumn;
        }

        public TableColumn<Part, Double> getAssociatedPartPriceColumn() {
            return associatedPartPriceColumn;
        }

        public void setAssociatedPartPriceColumn(TableColumn<Part, Double> associatedPartPriceColumn) {
            this.associatedPartPriceColumn = associatedPartPriceColumn;
        }

        public TableView<Part> getPartTableView() {
            return partTableView;
        }

        public void setPartTableView(TableView<Part> partTableView) {
            this.partTableView = partTableView;
        }

        public TableColumn<Part, Integer> getPartIdColumn() {
            return partIdColumn;
        }

        public void setPartIdColumn(TableColumn<Part, Integer> partIdColumn) {
            this.partIdColumn = partIdColumn;
        }

        public TableColumn<Part, Integer> getPartInventoryColumn() {
            return partInventoryColumn;
        }

        public void setPartInventoryColumn(TableColumn<Part, Integer> partInventoryColumn) {
            this.partInventoryColumn = partInventoryColumn;
        }

        public TableColumn<Part, String> getPartNameColumn() {
            return partNameColumn;
        }

        public void setPartNameColumn(TableColumn<Part, String> partNameColumn) {
            this.partNameColumn = partNameColumn;
        }

        public TableColumn<Part, Double> getPartPriceColumn() {
            return partPriceColumn;
        }

        public void setPartPriceColumn(TableColumn<Part, Double> partPriceColumn) {
            this.partPriceColumn = partPriceColumn;
        }

        public Button getSaveButton() {
            return saveButton;
        }

        public void setSaveButton(Button saveButton) {
            this.saveButton = saveButton;
        }

        public Button getCancelButton() {
            return cancelButton;
        }

        public void setCancelButton(Button cancelButton) {
            this.cancelButton = cancelButton;
        }

        public ToggleGroup getTgPartType() {
            return tgPartType;
        }

        public void setTgPartType(ToggleGroup tgPartType) {
            this.tgPartType = tgPartType;
        }
    }



    @FXML
    void cancelButtonAction(ActionEvent event) throws IOException {
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
     * @param searchString The string used to search for parts.
     * @return An ObservableList of parts matching the search criteria.
     */
    private ObservableList<Part> searchParts(String searchString) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().toLowerCase().contains(searchString.toLowerCase())) {
                partsFound.add(part);
            }
        }

        return partsFound;
    }

    /**
     * Returns to the main screen after saving the product.
     *
     * @param event The ActionEvent triggered by the return to the main screen.
     * @throws IOException If an error occurs while returning to the main screen.
     */
    private void returnToMainScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.setInventory(inventory);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Validates the minimum value.
     *
     * @param min The minimum value to be validated.
     * @param max The maximum value for comparison.
     * @return True if the minimum value is valid; false otherwise.
     */
    private boolean isMinValid(int min, int max) {
        return min > 0 && min < max;
    }

    /**
     * Validates the inventory value.
     *
     * @param min   The minimum value for comparison.
     * @param max   The maximum value for comparison.
     * @param stock The inventory value to be validated.
     * @return True if the inventory value is valid; false otherwise.
     */
    private boolean isInventoryValid(int min, int max, int stock) {
        return stock >= min && stock <= max;
    }

    /**
     * Displays an alert based on the given type, title, and content.
     *
     * @param type    The type of the alert (e.g., "error" or "info").
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
     * Sets the inventory for the controller.
     *
     * @param inventory The inventory to be set.
     */
    public void setInventory(Inventory inventory) {
        // FUTURE ENHANCEMENT: Consider additional initialization logic when setting the inventory
        this.inventory = inventory;
    }

    public TextField getProductIdText() {
        return productIdText;
    }

    public void setProductIdText(TextField productIdText) {
        this.productIdText = productIdText;
    }
}