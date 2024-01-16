package myapp.inventorysystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;

import static myapp.inventorysystem.Inventory.*;

/**
 * Main class for the Inventory Management System application.
 *    /**
 *      * The Javadoc for this class is located in the "Inventory System" file within the "JavaDoc" folder.
 *      * Please refer to that folder for detailed documentation.
 *      * RUNTIME ERROR: Ensuring that the part and product IDs are unique when adding the sample data.
 *      * Resolution: Corrected this by using Inventory.getNewPartId() and Inventory.getNewProductId() for unique IDs.
 *      * FUTURE ENHANCEMENT: We can consider implementing a confirmation dialog before deleting a part.
 *      * Initializing sample data for the Inventory Management System.
 *      */
public class Main extends Application {
    private MainController mainController; // Reference to the controller

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent root = loader.load();

        // Store a reference to the controller
        mainController = loader.getController();

        primaryStage.setTitle("Galactic Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // Add sample data here
        initializeSampleData();
    }
    /**
     * The main entry point for the Galactic Inventory Management System application.
     *
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializing sample data for the Inventory Management System.
     * <p>
     * RUNTIME ERROR: Ensuring that the part and product IDs are unique when adding the sample data.
     * Corrected this by using Inventory.getNewPartId() and Inventory.getNewProductId() for unique IDs.
     */
    private static void initializeSampleData() {
        // Sample Parts
        InHouse part1 = new InHouse(getNewPartId(), "Shield", 10.0, 5, 1, 20, 123);
        OutSourced part2 = new OutSourced(getNewPartId(), "Shield Plasma", 15.0, 8, 5, 50, "ABC Ltd.");
        InHouse part3 = new InHouse(getNewPartId(), "Ray Gun", 8.0, 12, 5, 30, 456);

        // Adding sample Parts to Inventory
        addPart(part1);
        addPart(part2);
        addPart(part3);

        // Sample Products
        Product product1 = new Product(getNewProductId(), "DeathStar", 25.0, 15, 5, 50);
        Product product2 = new Product(getNewProductId(), "RazorCrest", 30.0, 20, 10, 100);

        // Associating Parts with Products
        product1.addAssociatedPart(part1);
        product1.addAssociatedPart(part2);
        product2.addAssociatedPart(part2);
        product2.addAssociatedPart(part3);

        // Adding sample Products to Inventory
        addProduct(product1);
        addProduct(product2);
    }

    /**
     * Handling the key press event for the part search text field.
     *
     * @param keyEvent The KeyEvent representing the key press event.
     *                 FUTURE ENHANCEMENT: Here we can consider implementing advanced search functionality.
     */
    public void partSearchTextKeyPressed(KeyEvent keyEvent) {
        // Call a method in the controller to handle part searching
        mainController.handlePartSearch();
    }

    /**
     * Handling the action event for part deletion.
     *
     * @param actionEvent The ActionEvent representing the user's action triggering part deletion.
     *                    FUTURE ENHANCEMENT: We can consider implementing a confirmation dialog before deleting a part.
     */
    public void partDeleteAction(ActionEvent actionEvent) {
        // Call a method in the controller to handle part deletion
        mainController.handlePartDeletion();
    }
}
