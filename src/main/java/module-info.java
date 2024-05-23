module org.example.oopprojekt2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires org.seleniumhq.selenium.edge_driver;
    requires java.desktop;


    opens org.example.oopprojekt2 to javafx.fxml;
    exports org.example.oopprojekt2;
}