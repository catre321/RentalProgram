module com.example.display {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.nguyenhoang.stock to javafx.base;
    opens com.nguyenhoang.client to javafx.base;
    opens com.nguyenhoang.setvalue to javafx.base;

    opens com.example.display to javafx.fxml;
    exports com.example.display;
}