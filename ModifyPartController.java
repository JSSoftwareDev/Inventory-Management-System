package myapp.inventorysystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *  JavaDoc located in the InventorySystem Folder in a file titled "JavaDoc".
 *  JavaDoc located at C:\Project\JavaDoc.
 * RUNTIME ERROR: Handled a generic exception for unexpected errors.
 * Resolution: Used a catch block that prevents the program from crashing and invokes the displayAlert method with error code 1.
 * FUTURE ENHANCEMENT: Allow users to attach documents or images to a part.
 */

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
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
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
     * @param label           The label to be set.
     * @param inHouseStyle    The style for the in-house radio button.
     * @param outsourcedStyle The style for the outsourced radio button.
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

                // Update the selected part fields
                selectedPart.setName(name);
                selectedPart.setPrice(price);
                selectedPart.setStock(stock);
                selectedPart.setMin(min);
                selectedPart.setMax(max);

                // Update the part in the Inventory
                Inventory.updatePart(selectedPart);

                // Return to the main screen
                returnToMainScreen(event);
            }
        } catch (NumberFormatException e) {
            displayAlert(1);
        }
    }


    private void handleInHousePart(String name, double price, int stock, int min, int max) {
    }

    private void handleOutsourcedPart(String name, double price, int stock, int min, int max) {
        try {
            if (selectedPart instanceof OutSourced) {
                OutSourced modifiedPart = (OutSourced) selectedPart;

                // Check if the company name field is not empty
                String companyName = partIdNameText.getText().trim();
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

                // Assuming the Inventory class has a method updatePart(Part oldPart, Part newPart)
                Inventory.updatePart(selectedPart, modifiedPart);

                // Log statements for debugging
                System.out.println("Selected Part ID: " + selectedPart.getId());
                System.out.println("Modified Part ID: " + modifiedPart.getId());
                System.out.println("Modified Company Name: " + modifiedPart.getCompanyName());

                // Update selectedPart to the modified part
                selectedPart = modifiedPart;

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
}