import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Main interface class of the Steam Key Manager
 *
 * @author Xuanli Lin
 * @version 0.0.2-alpha
 */
public class Interface extends Application {

    // Core component: a TableView table showing all information
    private TableView<Key> keyTable = new TableView<>();
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
        primaryStage.setWidth(515);
        primaryStage.setHeight(515);

        // And a label displaying the name of the app
        Label appNameLabel = new Label("Steam Key Manager");
        appNameLabel.setFont(new Font("Segoe UI", 20));

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
        keyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        // Implement cell editing
        gameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        keyCol.setCellFactory(TextFieldTableCell.forTableColumn());
        notesCol.setCellFactory(TextFieldTableCell.forTableColumn());

        // Add textFields to add a new key. Make them no wider than the table
        TextField gameField = new TextField();
        gameField.setMaxWidth(gameCol.getMaxWidth());
        gameField.setPromptText("Game");

        TextField keyField = new TextField();
        keyField.setMaxWidth(keyCol.getMaxWidth());
        keyField.setPromptText("Key");

        TextField noteField = new TextField();
        noteField.setMaxWidth(notesCol.getPrefWidth());
        noteField.setPromptText("Notes");

        // ...and a button
        Button addButton  = new Button("Add");

        // Set listeners for column cells
        gameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Key,String>>() {
                    @Override
                    // Update the value of the game cell after users finish editing
                    public void handle(TableColumn.CellEditEvent<Key, String> event) {
                        (event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setGame(event.getNewValue());
                    }
                }
        );

        keyCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Key,String>>() {
                    @Override
                    // Update the value of the game cell after users finish editing
                    public void handle(TableColumn.CellEditEvent<Key, String> event) {
                        (event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setKey(event.getNewValue());
                    }
                }
        );

        notesCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Key,String>>() {
                    @Override
                    // Update the value of the game cell after users finish editing
                    public void handle(TableColumn.CellEditEvent<Key, String> event) {
                        (event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setNotes(event.getNewValue());
                    }
                }
        );

        // Set an lambda listener to button action
        addButton.setOnAction(event -> {
            // Create a new Key and add it to keyList
            keyList.add(new Key(
                    gameField.getText(),
                    keyField.getText(),
                    noteField.getText()
            ));
            // Clear input fields
            gameField.clear();
            keyField.clear();
            noteField.clear();
        });

        // Context menu for rows
        keyTable.setRowFactory(new Callback<TableView<Key>, TableRow<Key>>() {
            @Override
            public TableRow<Key> call(TableView<Key> tableView) {
                final TableRow<Key> row = new TableRow<>();
                final ContextMenu cm = new ContextMenu();
                final MenuItem removeRowItem = new MenuItem("Remove");
                // Listener for removing a row
                removeRowItem.setOnAction((ActionEvent event) -> {
                    keyTable.getItems().remove(row.getItem());
                });
                cm.getItems().add(removeRowItem);

                // Set the remove option only show when the row is not empty
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(cm)
                );
                return row;
            }
        });

        // A VBox to house the table
        VBox tableBox = new VBox();
        // A Hbox to hold the add section
        HBox addBox = new HBox();

        // Set properties and add components to VBox
        tableBox.setSpacing(5);
        tableBox.setPadding(new Insets(10, 0, 0, 10));
        tableBox.getChildren().addAll(appNameLabel, keyTable, addBox);

        // Set properties and add components to HBox
        addBox.setSpacing(3);
        addBox.getChildren().addAll(gameField, keyField, noteField, addButton);

        // Add VBox to scene
        ((Group) scene.getRoot()).getChildren().addAll(tableBox);

        // Finalize the settings
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
