package org.example.oopprojekt2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HelloApplication extends Application {

    private String lastSelectedItem = null;
    private Map<String, List<Toode>> tooted = null;

    private final Button addButton = new Button("Lisa"); // Nupp toodete lisamiseks
    private final Button searchButton = new Button("Otsi"); // Otsingu nupp
    private final Button clearButton = new Button("Eemalda Väljad"); // Kirjete puhastamise nupp
    private final Button tsekkButton = new Button("Prindi Nimekiri"); // Tsekki nupp
    private final TextField searchField = new TextField(); // Otsinguväli

    private final ListView<String> resultsList = new ListView<>();
    private final ListView<String> subResultsList = new ListView<>();

    @Override
    public void start(Stage primaryStage) {

        // Event listener for search field
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String query = searchField.getText().trim();
                resultsList.getItems().add(query);
                searchField.clear();
            }
        });

        // Event listener for adding button
        addButton.setOnAction(event -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                resultsList.getItems().add(query);
                // Puhasta otsinguväli pärast lisamist
                searchField.clear();
            }
        });
        


        // Layout

        Label titleLabel = new Label("Programm odavaimate toodete leidmiseks, tippige tooteid alla!");
        titleLabel.setStyle("-fx-font-size: 20px;");
        titleLabel.setAlignment(Pos.CENTER);

        HBox searchBar = new HBox(5, searchField, addButton, searchButton);
        searchBar.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(titleLabel, 0, 0, 2, 1);
        gridPane.add(searchBar, 0, 1, 2, 1);
       // muudab kuvatut mõistlikult
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(column1, column2);

        gridPane.add(resultsList, 0, 2, 2, 1);
        GridPane.setVgrow(resultsList, Priority.ALWAYS);
        GridPane.setVgrow(subResultsList, Priority.ALWAYS);
        
        // Inside the start method after setting up the layout
        Button infoButton = getInfoButton();

        // Add the infoButton to the top-right corner of the searchBar
        searchBar.getChildren().add(infoButton);
        HBox.setHgrow(infoButton, Priority.NEVER);

        clearButton.setStyle("-fx-background-color: #dd7777;" +
                "-fx-text-fill: #ffffff");
        addButton.setStyle("-fx-background-color: #0096C9;" +
        "-fx-text-fill: #ffffff");
        addButton.setStyle("-fx-background-color: #0096C9;" +
        "-fx-text-fill: #ffffff");
        searchButton.setStyle("-fx-background-color: #0096C9;" +
        "-fx-text-fill: #ffffff");
        tsekkButton.setStyle("-fx-background-color: #0096C9;" +
        "-fx-text-fill: #ffffff");

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
                // Kasutab funktsiooni getProducts et saada hinnad toodetele.
                tooted = x.getProducts();
                resultsList.getItems().clear();
                // Kui ainult yks query siis anna koik tulemused resultList'i.
                if (tooted.size() == 1) {
                    List<Toode> tulemused = tooted.values().iterator().next();
                    resultsList.getItems().addAll(tulemused.stream().map(Toode::valjastaIlusalt).toList());
                    return;
                }
                List<String> tootenimed = new ArrayList<>();
                for (Map.Entry<String, List<Toode>> entry: tooted.entrySet()) {
                    String tootenimi = entry.getKey();
                    tootenimed.add(tootenimi);
                }
                int longestTooteNimi = getWidest(tootenimed);

                for (Map.Entry<String, List<Toode>> entry: tooted.entrySet()) {
                    String tootenimi = entry.getKey();
                    List<Toode> innerList = entry.getValue();
                    // Kuvab toote kõige odavama versiooni
                    String format = "%-" + longestTooteNimi + "s";
                    if (!innerList.isEmpty()) {
                        resultsList.getItems().add(String.format(format, tootenimi) + ": " + innerList.get(0).valjastaIlusalt());
                    } else {
                        resultsList.getItems().add(String.format(format, tootenimi) + ": ei leitud ühtegi toodet.");

                    }
                }

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Event listener for prindi nimekiri button
        tsekkButton.setOnAction(event -> {
            // Get the items from the resultsList
            List<String> items = resultsList.getItems();

            // Define the filename
            String filename = "otsunimekiri.txt";

            // Write the items to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (String item : items) {
                    writer.write(item);
                    writer.newLine();
                }

                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Nimekiri salvestatud");
                infoAlert.setHeaderText("Nimekiri salvestatud");
                infoAlert.setContentText("Nimekiri salvestatud faili: " + filename);
                infoAlert.showAndWait();
            } catch (IOException e) {
                System.err.println("Error occurred while saving results list to file: " + e.getMessage());
            }

        });

        // Event listener for clear button
        clearButton.setOnAction(event -> {
            // Clear the search field
            resultsList.getItems().clear();
            switchButtons(true, searchBar);
            adjustLayout(gridPane, subResultsList, resultsList, false);
        });
        
       // Create a custom cell factory for the ListView
        subResultsList.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(null); // Clear any previous styling
                } else {
                    setText(item);
                    // Highlight top 10 items in light green
                    int index = getIndex();
                    if (index >= 0 && index < 5) {
                        setStyle("-fx-background-color: lightgreen;" +
                                "-fx-text-fill: #000000");
                    } else {
                        setStyle(null); // Clear styling for items beyond top 10
                    }
                }
            }
        });
 



    }

    private static Button getInfoButton() {
        Button infoButton = new Button("?");
        infoButton.setStyle("-fx-background-color: #dd7777;" +
                "-fx-text-fill: #ffffff");

        infoButton.setOnAction(event -> {
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Kuidas kasutada programmi");
            infoAlert.setHeaderText("Kuidas kasutada programmi");
            infoAlert.setContentText("""
                    1. Kirjutage toote nimi otsingusse ja vajutage ENTER või nuppu "Lisa".
                    2. Tooted kuvatakse all nimekirjas. (nt. hapukoor, coca-cola, kodujuust)
                    3. Vajutage "Otsi" ja oodake mõni hetk.
                    4. Kuvatakse toodete nimed, hinnad ja müügikohad.
                    5. Klõpsake tootel, et näha erinevaid hindu ja variante.
                    6. Klõpsake "Eemalda Väljad" toodete eemaldamiseks.
                    7. Klõpsake "Prindi Nimekiri" toodete ja odavaimate hindade salvestamiseks.
                    """);
            infoAlert.showAndWait();
        });
        return infoButton;
    }

    private int getWidest(List<String> tootenimed) {
        int longest = 0;
        for (String toode : tootenimed) {
            int currentLength = toode.length();
            if (currentLength > longest) longest = currentLength;
        }
        return longest;
    }

    // olenevalt showSubResultListist kas kuvab või peidab teised toote variandid.
    private void adjustLayout(GridPane gridPane, ListView<String> subResultsList,
                              ListView<String> resultsList, boolean showSubResultList) {
        if (showSubResultList) {
            gridPane.getChildren().remove(resultsList);
            gridPane.add(resultsList, 0, 2, 1, 1);
            gridPane.add(subResultsList, 1, 2);
            GridPane.setVgrow(subResultsList, Priority.ALWAYS);
        } else {
            gridPane.getChildren().remove(resultsList);
            gridPane.add(resultsList, 0, 2, 2, 1);
            gridPane.getChildren().remove(subResultsList);
        }

    }

    // Meetod millega muuta nupud mis yleval paremal kuvatakse
    private void switchButtons(boolean showSearch, HBox searchBar) {
        if (showSearch) {
            searchBar.getChildren().remove(clearButton);
            searchBar.getChildren().remove(tsekkButton);
            searchBar.getChildren().add(addButton);
            searchBar.getChildren().add(searchButton);
            searchField.setEditable(true);
        } else {
            searchBar.getChildren().add(clearButton);
            searchBar.getChildren().add(tsekkButton);
            searchBar.getChildren().remove(addButton);
            searchBar.getChildren().remove(searchButton);
            searchField.setEditable(false);
        }
    }

    // Näitab pealisti kõrval tooteid, mis on teise hinnaga.
    private void handleSelection(GridPane gridPane) {
        String selectedItem = resultsList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            List<Toode> subresultsTooted = tooted.get(selectedItem.split(": ")[0].trim());
            String[] subresults = subresultsTooted.stream().map(Toode::valjastaIlusalt).toArray(String[]::new);
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
//            // Highlight top 10 items in light green
//            int topCount = Math.min(10, resultsList.getItems().size());
//            for (int i = 0; i < topCount; i++) {
//                resultsList.getItems().get(i).setStyle("-fx-background-color: lightgreen;");
//            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }}