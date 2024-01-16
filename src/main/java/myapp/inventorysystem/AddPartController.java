package myapp.inventorysystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Adding Parts
 * RUNTIME ERROR: Method name should be camelCase, not snake_case.
 * Resolution: Method named was made to be camelCase.
 *  FUTURE ENHANCEMENT: Consider using a more sophisticated logic for generating Machine IDs
 */
public class AddPartController implements Initializable {

    @FXML
    private TextField partIdText;

    @FXML
    private TextField partNameText;

    @FXML
    private TextField partInventoryText;

    @FXML
    private TextField partPriceText;

    @FXML
    private TextField partMinText;

    @FXML
    private TextField partMaxText;

    @FXML
    private TextField partIdNameText;

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private RadioButton outSourcedRadioButton;

    @FXML
    private Label partIdNameLabel;

    /**
     * RUNTIME ERROR: Method name should be camelCase, not snake_case.
     * // FUTURE ENHANCEMENT: I could Validate the generated Machine ID
     */

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

    @FXML
    void inHouseRadioButtonAction(ActionEvent event) {
        partIdNameLabel.setText("Machine ID");
        inHouseRadioButton.setStyle("-fx-background-color: white;");
        outSourcedRadioButton.setStyle("-fx-background-color: transparent;");
    }

    @FXML
    void outsourcedRadioButtonAction(ActionEvent event) {
        partIdNameLabel.setText("Company Name");
        outSourcedRadioButton.setStyle("-fx-background-color: white;");
        inHouseRadioButton.setStyle("-fx-background-color: transparent;");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inHouseRadioButton.setSelected(true);
        setupRadioButtons();
    }

    private void setupRadioButtons() {
        ToggleGroup toggleGroup = new ToggleGroup();
        inHouseRadioButton.setToggleGroup(toggleGroup);
        outSourcedRadioButton.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == inHouseRadioButton) {
                partIdNameLabel.setText("Machine ID");
                inHouseRadioButton.setStyle("-fx-background-color: white;");
                outSourcedRadioButton.setStyle("-fx-background-color: transparent;");
            } else if (newValue == outSourcedRadioButton) {
                partIdNameLabel.setText("Company Name");
                outSourcedRadioButton.setStyle("-fx-background-color: white;");
                inHouseRadioButton.setStyle("-fx-background-color: transparent;");
            }
        });
    }

    // FUTURE ENHANCEMENT: Add validation for the Machine ID generated
    private void validateMachineId(int machineId) {

    }

    @FXML
    void saveButtonAction(ActionEvent event) throws IOException {
        try {
            String name = partNameText.getText();
            double price = Double.parseDouble(partPriceText.getText());
            int stock = Integer.parseInt(partInventoryText.getText());
            int min = Integer.parseInt(partMinText.getText());
            int max = Integer.parseInt(partMaxText.getText());

            System.out.println("Debug: Name=" + name + ", Price=" + price + ", Stock=" + stock + ", Min=" + min + ", Max=" + max);
            System.out.println("Before inventoryValid: Min=" + min + ", Max=" + max + ", Stock=" + stock);

            if (name.isEmpty()) {
                displayAlert(5);
                return;
            }

            if (!minValid(min, max)) {
                return;
            }

            if (!inventoryValid(min, max, stock)) {
                return;
            }

            int id = Inventory.getNewPartId();

            if (inHouseRadioButton.isSelected()) {
                // Logical error: generateMachineId() should be called before creating a new part
                int machineId = generateMachineId();
                validateMachineId(machineId); // FUTURE ENHANCEMENT: I could Validate the generated Machine ID
                System.out.println("Debug: Machine ID=" + machineId);
                InHouse newInHousePart = new InHouse(id, name, price, stock, min, max, machineId);
                configureSavedPart(id, newInHousePart);
            }

            if (outSourcedRadioButton.isSelected()) {
                String companyName = partIdNameText.getText();
                System.out.println("Debug: Company Name=" + companyName);
                OutSourced newOutsourcedPart = new OutSourced(id, name, price, stock, min, max, companyName);
                configureSavedPart(id, newOutsourcedPart);
            }

            returnToMainScreen(event);

        } catch (NumberFormatException e) {
            displayAlert(1);
        }
    }

    private void configureSavedPart(int id, Part part) {
        partIdText.setText(Integer.toString(id));
        partIdText.setDisable(true);
        Inventory.addPart(part);
    }

    private void returnToMainScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Access the controller of the Main form
        MainController mainController = loader.getController();


        mainController.refresh();

        stage.show();
    }

    private boolean minValid(int min, int max) {
        boolean isValid = true;

        if (min < 0 || min > max) {
            isValid = false;
            displayAlert(3);
        }

        return isValid;
    }

    private boolean inventoryValid(int min, int max, int stock) {
        boolean isValid = true;

        System.out.println("Debug: Min=" + min + ", Max=" + max + ", Stock=" + stock);

        if (stock < min || stock > max) {
            isValid = false;
            displayAlert(4);
            System.out.println("Debug: Invalid Inventory - Stock not within Min and Max");
        }

        return isValid;
    }

    private void displayAlert(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Star Wars Part");
                alert.setContentText("Form contains blank fields or invalid values.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Machine ID");
                alert.setContentText("Machine ID may only contain numbers.");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Min");
                alert.setContentText("Min must be a number greater than 0 and less than Max.");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Inventory");
                alert.setContentText("Inventory must be a number equal to or between Min and Max.");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("Name is Empty");
                alert.setContentText("Name cannot be empty.");
                alert.showAndWait();
                break;
        }
    }

    // Implement a method to generate a new machine ID
    // FUTURE ENHANCEMENT: Consider using a more sophisticated logic for generating Machine IDs
    private int generateMachineId() {
        // Logic to generate a new machine ID
        return 123;
    }
}
