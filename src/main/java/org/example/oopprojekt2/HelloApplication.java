package org.example.oopprojekt2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HelloApplication extends Application {

    private boolean isSubResultsListAdded = true;
    private String lastSelectedItem = null;
    private Map<String, List<Toode>> tooted = null;

    private Button addButton = new Button("Add"); // Button for adding items
    private Button searchButton = new Button("Search"); // Button for searching for prices
    private Button clearButton = new Button("Clear"); // Button for clearing everything
    private TextField searchField = new TextField();

    private ListView<String> resultsList = new ListView<>();
    private ListView<String> subResultsList = new ListView<>();

    @Override
    public void start(Stage primaryStage) {

        // Event listener for search field
        searchField.setOnKeyPressed(event -> {
            String query = searchField.getText().trim();
            if (event.getCode() == KeyCode.ENTER) {
                updateSearchResults(query, resultsList);
                searchField.clear();
            }
        });

        // Event listener for adding button
        addButton.setOnAction(event -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                // add and update results list
                updateSearchResults(query, resultsList);

                // Clear the text field after adding
                searchField.clear();
            }
        });
        


        // Layout
        HBox searchBar = new HBox(5, searchField, addButton, searchButton);
        searchBar.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(searchBar, 0, 0, 2, 1);
       // Adjust column constraints for the search bar width
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(column1, column2);

        gridPane.add(resultsList, 0, 1, 2, 1);
        GridPane.setVgrow(resultsList, Priority.ALWAYS);
        GridPane.setVgrow(subResultsList, Priority.ALWAYS);

        isSubResultsListAdded = false;

        Scene scene = new Scene(gridPane, 600, 400);

        // Stage setup
        primaryStage.setTitle("Search App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Event listener for item click in the resultsList
        resultsList.setOnMouseClicked(event -> {
            handleSelection(gridPane);
        });

        // Event listener for space button click in the resultsList
        resultsList.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                handleSelection(gridPane);
            }
        });

        // Event listener for Search button
        searchButton.setOnAction(event -> {
            ArrayList<String> toodeteList = new ArrayList<>(resultsList.getItems());
            Kasutaja x = new Kasutaja(toodeteList);
            switchButtons(false, searchBar);
            try {
                tooted = x.getProducts();
                resultsList.getItems().clear();
                // Kui ainult yks query siis anna koik tulemused resultList'i.
                if (tooted.size() == 1) {
                    List<Toode> tulemused = tooted.values().iterator().next();
                    resultsList.getItems().addAll(tulemused.stream().map(Toode::toString).toList());
                    return;
                }
                for (Map.Entry<String, List<Toode>> entry: tooted.entrySet()) {
                    String tootenimi = entry.getKey();
                    List<Toode> innerList = entry.getValue();
                    if (!innerList.isEmpty()) {
                        resultsList.getItems().add(tootenimi + ": " + innerList.get(0).toString());
                    }
                }

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Event listener for clear button
        clearButton.setOnAction(event -> {
            // Clear the search field
            resultsList.getItems().clear();
            switchButtons(true, searchBar);
            adjustLayout(gridPane, subResultsList, resultsList, false);
        });

    }
    private void adjustLayout(GridPane gridPane, ListView<String> subResultsList,
                              ListView<String> resultsList, boolean showSubResultList) {
        if (showSubResultList) {
            gridPane.getChildren().remove(resultsList);
            gridPane.add(resultsList, 0, 1, 1, 1);
            gridPane.add(subResultsList, 1, 1);
            GridPane.setVgrow(subResultsList, Priority.ALWAYS);
            isSubResultsListAdded = true;
        } else {
            gridPane.getChildren().remove(resultsList);
            gridPane.add(resultsList, 0, 1, 2, 1);
            gridPane.getChildren().remove(subResultsList);
            isSubResultsListAdded = false;
        }

    }

    // Method to update items in main list
    private void updateSearchResults(String query, ListView<String> resultsList) {
        resultsList.getItems().add(query);
    }

    // Method to update sub results list (different cheese sorts)
    private void updateSubResultsList(String[] selectedItems, ListView<String> subResultsList) {
        subResultsList.getItems().addAll(selectedItems);
    }
    // Method to switch between search and add buttons
    private void switchButtons(boolean showSearch, HBox searchBar) {
        if (showSearch) {
            searchBar.getChildren().remove(clearButton);
            searchBar.getChildren().add(addButton);
            searchBar.getChildren().add(searchButton);
            searchField.setEditable(true);
        } else {
            searchBar.getChildren().add(clearButton);
            searchBar.getChildren().remove(addButton);
            searchBar.getChildren().remove(searchButton);
            searchField.setEditable(false);
        }
    }
    private void handleSelection(GridPane gridPane) {
        String selectedItem = resultsList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            List<Toode> subresultsTooted = tooted.get(selectedItem.split(": ")[0]);
            String[] subresults = subresultsTooted.stream().map(Toode::toString).toArray(String[]::new);
            subResultsList.getItems().clear();
            subResultsList.getItems().addAll(subresults);
            if (selectedItem.equals(lastSelectedItem)) {
                adjustLayout(gridPane, subResultsList, resultsList, false);
                lastSelectedItem = null;
            } else {
                lastSelectedItem = selectedItem;
                if (!gridPane.getChildren().contains(subResultsList)) {
                    adjustLayout(gridPane, subResultsList, resultsList, true);
                }
            }
        }
    }



    public static void main(String[] args) {
        launch(args);
    }}