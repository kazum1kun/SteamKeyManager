// Main interface of the Key Manager

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Interface extends Application {

    // Core component: a TableView table showing all information
    private TableView keyTable = new TableView();
    // Core component: a ObservableList containing the actual data
    private ObservableList<Key> keyList = FileParser.get();



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up a new scene and set properties of the stage
        Scene scene = new Scene(new Group());
        primaryStage.setTitle("Steam Key Manager");
        primaryStage.setWidth(500);
        primaryStage.setHeight(400);

        // And a label displaying the name of the app
        Label mainLbl = new Label("Steam Key Manager");
        mainLbl.setFont(new Font("Segoe UI", 20));

        // Make table editable
        keyTable.setEditable(true);

        // Add columns to the table and associate them with ObservableList
        TableColumn gameCol = new TableColumn("Game");
        gameCol.setMinWidth(200);
        gameCol.setCellValueFactory(new PropertyValueFactory<Key, String>("game"));

        TableColumn keyCol = new TableColumn("Key");
        keyCol.setMinWidth(200);
        keyCol.setCellValueFactory(new PropertyValueFactory<Key, String>("key"));

        TableColumn notesCol = new TableColumn("Notes");
        notesCol.setMaxWidth(100);
        notesCol.setCellValueFactory(new PropertyValueFactory<Key, String>("notes"));

        keyTable.setItems(keyList);
        keyTable.getColumns().addAll(gameCol, keyCol, notesCol);

        // A VBox to house the table
        VBox tableBox = new VBox();

        // Set properties and add components to VBox
        tableBox.setSpacing(5);
        tableBox.setPadding(new Insets(10, 0, 0, 10));
        tableBox.getChildren().addAll(mainLbl, keyTable);

        // Add VBox to scene
        ((Group) scene.getRoot()).getChildren().addAll(tableBox);

        // Finalize the settings
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
