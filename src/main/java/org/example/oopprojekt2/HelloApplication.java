package org.example.oopprojekt2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
  // UI components
        TextField searchField = new TextField();
        Button searchButton = new Button("Search"); // Button for searching
        ListView<String> resultsList = new ListView<>();

        // Event listener for search field
        searchField.setOnKeyPressed(event -> {
            String query = searchField.getText().trim();
            if (event.getCode() == KeyCode.ENTER) {
                updateSearchResults(query, resultsList);
                searchField.clear();
            }
        });
        
        // Event listener for search button
        searchButton.setOnAction(event -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                // Perform search and update results list
                updateSearchResults(query, resultsList);

                // Clear the text field after search
                searchField.clear();
            } else {
                resultsList.getItems().clear(); // Clear results if search field is empty
            }
        });

        // Layout
        HBox searchBar = new HBox(5, searchField, searchButton);
        searchBar.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        VBox root = new VBox(10, searchBar, resultsList);
        root.setPadding(new Insets(80));
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
        resultsList.getItems().add(query);
        // Add more results as needed
    }

    public static void main(String[] args) {
        launch(args);
    }}