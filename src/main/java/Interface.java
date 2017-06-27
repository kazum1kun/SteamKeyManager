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

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main interface class of the Steam Key Manager
 *
 * @author Xuanli Lin
 * @version 0.0.5-alpha
 */
public class Interface extends Application {

    // Core component: a TableView table showing all information
    private static TableView<Key> keyTable = new TableView<>();
    // Core component: a ObservableList containing the actual data
    private static ObservableList<Key> keyList;
    // The File user chosen
    private static File userTextFile;

    public static void main(String[] args) {
        launch(args);
    }

    // Prompt user to pick a file before SKM starts. Parse the file if possible. If no file is chosen, start SKM with
    // blank key list
    private static void prepareKeyList(Stage stage) {
        File temp = FileParser.chooseFile(stage);
        if (temp != null) {
            userTextFile = temp;
            keyList = FileParser.parseAndGet(temp);
        } else {
            keyList = FileParser.getEmpty();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Multi-Language support
        ResourceBundle lang = ResourceBundle.getBundle("language.lang", Locale.ENGLISH);

        // Prepare the key list before program starts
        prepareKeyList(primaryStage);

        // Set up a new scene and set properties of the stage
        Scene scene = new Scene(new Group());
        primaryStage.setTitle(lang.getString("APP_NAME"));
        primaryStage.setWidth(515);
        primaryStage.setHeight(515);

        // And a label displaying the name of the app
        Label appNameLabel = new Label(lang.getString("APP_NAME"));
        appNameLabel.setFont(new Font("Segoe UI", 20));

        // Make table editable
        keyTable.setEditable(true);

        // Add columns to the table and associate them with ObservableList
        TableColumn gameCol = new TableColumn(lang.getString("TXT_GAME"));
        gameCol.setMinWidth(200);
        gameCol.setCellValueFactory(new PropertyValueFactory<Key, String>("game"));

        TableColumn keyCol = new TableColumn(lang.getString("TXT_KEY"));
        keyCol.setMinWidth(200);
        keyCol.setCellValueFactory(new PropertyValueFactory<Key, String>("key"));

        TableColumn notesCol = new TableColumn(lang.getString("TXT_NOTES"));
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
        gameField.setPromptText(lang.getString("TXT_GAME"));

        TextField keyField = new TextField();
        keyField.setMaxWidth(keyCol.getMaxWidth());
        keyField.setPromptText(lang.getString("TXT_KEY"));

        TextField noteField = new TextField();
        noteField.setMaxWidth(notesCol.getPrefWidth());
        noteField.setPromptText(lang.getString("TXT_NOTES"));

        // ...and a button
        Button addButton  = new Button(lang.getString("TXT_ADD"));

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
            Key key = new Key(
                    gameField.getText(),
                    keyField.getText(),
                    noteField.getText()
            );
            keyList.add(key);

            // Database Test
//            try {
//                KeyDao mysql = new KeyDao(Utils.DBTool.MYSQL);
//                KeyDao sqlite = new KeyDao(Utils.DBTool.SQLITE);
//
////                mysql.insertKey(key);
////                sqlite.insertKey(key);
//
////                String oldkey = "AAAAA-BBBBB-CCCCC";
////                String newkey = "11111-22222-33333";
////                mysql.updateKey(oldkey, newkey);
////                sqlite.updateKey(oldkey, newkey);
////                mysql.updateData(key);
////                sqlite.updateData(key);
////                mysql.delKey(key);
////                sqlite.delKey(key);
////                mysql.getAllKeys();
////                sqlite.getAllKeys();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }

            // Clear input fields
            gameField.clear();
            keyField.clear();
            noteField.clear();
        });

        // Context menu for rows
        keyTable.setRowFactory(tableView -> {
            final TableRow<Key> row = new TableRow<>();
            final ContextMenu cm = new ContextMenu();
            final MenuItem removeRowItem = new MenuItem(lang.getString("TXT_REMOVE"));
            // Listener for removing a row
            removeRowItem.setOnAction((ActionEvent event) -> keyTable.getItems().remove(row.getItem()));
            cm.getItems().add(removeRowItem);

            // Set the remove option only show when the row is not empty
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(cm)
            );
            return row;
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

final class ShowPrompt {
    // Prompt for to file I/O errors
    static void fileReadError(String pathToFile, int context) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fatal Error");
        alert.setHeaderText("An fatal error has occurred");
        if (context == 1) {
            alert.setContentText("Steam Key Manager was unable to access file \"" + pathToFile + "\". " +
                    "Please make sure the file exists and SKM has permissions to access it.");
        }
        if (context == 2) {
            alert.setContentText("Steam Key Manager was unable to access file \"" + pathToFile + "\". " +
                    "Please make sure SKM has permissions to access it, and it is not in use by another program.");
        }
        alert.showAndWait();
    }

    // Prompt for parsing errors
    static void fileParseError(String pathToFile, int context) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("File Parse Error");
        alert.setHeaderText("An parsing error has occurred");
        if (context == 1) {
            alert.setContentText("Steam Key Manager has recognized the format of the file, " +
                    "but unable to parse the content. Make sure the file is not in use by another program.");
        }

        alert.showAndWait();
    }

    // Prompt to ask whether user want to analyze the file
    static boolean oldFormat() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Unrecognized Format");
        alert.setHeaderText("Old or unrecognized format");
        alert.setContentText("Steam Key Manager was unable to recognize the format of the file. " +
                "Do you want SKM to attempt to analyze the file?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        return (alert.showAndWait().get() == ButtonType.YES);
    }

    // Alert user if SKM cannot detect a pattern
    static void analysisReport(int ok, int failed) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Analyze results");
        alert.setHeaderText("SKM has finished analyzing the file");
        alert.setContentText(ok + " valid Steam keys / redemption url was found.\n" +
                +failed + " lines were failed to parse.");
        alert.showAndWait();
    }
}
