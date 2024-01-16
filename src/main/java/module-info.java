
module myapp.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;

    opens myapp.inventorysystem to javafx.fxml;
    exports myapp.inventorysystem;
}
