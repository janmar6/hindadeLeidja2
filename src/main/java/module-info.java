module org.example.oopprojekt2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;


    opens org.example.oopprojekt2 to javafx.fxml;
    exports org.example.oopprojekt2;
}