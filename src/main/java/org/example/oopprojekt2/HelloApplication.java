package org.example.oopprojekt2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
  // UI components
        TextField searchField = new TextField();
        ListView<String> resultsList = new ListView<>();

        // Event listener for search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String query = newValue.trim();
            if (!query.isEmpty()) {
                // Perform search and update results list
                updateSearchResults(query, resultsList);
            } else {
                resultsList.getItems().clear(); // Clear results if search field is empty
            }
        });

        // Layout
        VBox root = new VBox(searchField, resultsList);
        Scene scene = new Scene(root, 400, 300);

        // Stage setup
        primaryStage.setTitle("Search App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to update search results
    private void updateSearchResults(String query, ListView<String> resultsList) {
        // Perform search based on query
        // Dummy implementation - just display the query as a result
        resultsList.getItems().clear();
        resultsList.getItems().add("Result 1 for: " + query);
        resultsList.getItems().add("Result 2 for: " + query);
        // Add more results as needed
    }

    public static void main(String[] args) {
        launch(args);
    }}