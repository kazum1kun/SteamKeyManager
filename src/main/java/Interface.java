import Utils.L10N;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.Locale;

/**
 * Main interface class of the Steam Key Manager
 *
 * @author Xuanli Lin
 * @version 0.0.5-1-alpha
 */
public class Interface extends Application {

    // Core component: a TableView table showing all information
    private static TableView<Key> keyTable = new TableView<>();
    // Core component: a ObservableList containing the actual data
    private static ObservableList<Key> keyList;
    // The File user chosen
    private static File userTextFile = null;
    // Current language, default to system locale (EN if unsupported)
    private static Locale currentLocale = L10N.getDefaultLocale();
    // Default resource bundle
//    private static ResourceBundle lang = null;

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

    // Consider it prepareKeyList reversed
    private static void saveKeyList(Stage stage) {
        // Ask for save location only when creating a new collection
        if (userTextFile == null) {
            File dest = FileWriter.chooseFile(stage);
            if (dest != null) {
                userTextFile = dest;
                FileWriter.saveToText(dest, keyList);
            } // Do nothing otherwise
        } else {
            // Write to the opened text file
            FileWriter.saveToText(userTextFile, keyList);
        }
    }

    private static void updateLocale(Stage stage, TextField... textFields) {
        // This would set locale for most areas
//        lang = ResourceBundle.getBundle("language.lang", currentLocale);
        L10N.setLocale(currentLocale);

        // Some cannot be set (for now), update them manually
        // APP TITLE
        stage.setTitle(L10N.get("string_mainUI_appName"));

        // TextField prompts
        textFields[0].setPromptText(L10N.get("string_mainUI_game"));
        textFields[1].setPromptText(L10N.get("string_mainUI_key"));
        textFields[2].setPromptText(L10N.get("string_mainUI_notes"));
    }

    @Override
    public void start(Stage primaryStage) {
        // Multi-Language support
//        lang = ResourceBundle.getBundle("language.lang", currentLocale);

        // Populate an empty ObservableList on program start
        keyList = FileParser.getEmpty();

        // Some declarations here for you dumb Java
        TextField gameField = new TextField();
        TextField keyField = new TextField();
        TextField notesField = new TextField();

        // Set up a new scene and set properties of the stage
        Scene scene = new Scene(new VBox());
        // TODO change locale of this manually
        primaryStage.setTitle(L10N.get("string_mainUI_appName"));
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);

        // And a label displaying the name of the app
//        Label appNameLabel = new Label(lang.getString("string_mainUI_app_name"));
        Label appNameLabel = L10N.labelForKey("string_mainUI_appName");
        appNameLabel.setFont(new Font("Segoe UI", 20));

        // Make table editable
        keyTable.setEditable(true);

        // MenuBar is here!
        MenuBar menuBar = new MenuBar();

        // ---- File
//        Menu fileMenu = new Menu(lang.getString("string_menuBar_file"));
        Menu fileMenu = L10N.menuForKey("string_menuBar_file");
        // -------- Open
//        MenuItem openItem = new MenuItem(lang.getString("string_menuBar_file_open"));
        MenuItem openItem = L10N.menuItemForKey("string_menuBar_file_open");
        openItem.setOnAction((ActionEvent event) -> prepareKeyList(primaryStage));
        // -------- Save
//        MenuItem saveItem = new MenuItem(lang.getString("string_menuBar_file_save"));
        MenuItem saveItem = L10N.menuItemForKey("string_menuBar_file_save");
        saveItem.setOnAction((ActionEvent event) -> saveKeyList(primaryStage));
        // -------- Exit
//        MenuItem closeItem = new MenuItem(lang.getString("string_menuBar_file_exit"));
        MenuItem closeItem = L10N.menuItemForKey("string_menuBar_file_exit");
        // TODO: prompt user to confirm exit
        // Assemble the File Menu
        fileMenu.getItems().addAll(openItem, saveItem, closeItem);

        // ---- Edit
//        Menu editMenu = new Menu(lang.getString("string_menuBar_edit"));
        Menu editMenu = L10N.menuForKey("string_menuBar_edit");

        // -------- Language
//        Menu langMenu = new Menu(lang.getString("string_menuBar_edit_language"));
        Menu langMenu = L10N.menuForKey("string_menuBar_edit_language");
        final ToggleGroup langGroup = new ToggleGroup();
        // ------------ English
//        RadioMenuItem englishItem = new RadioMenuItem(lang.getString("string_menuBar_edit_language_en"));
        RadioMenuItem englishItem = L10N.radioMenuItemForKey("string_menuBar_edit_language_en");
        englishItem.setUserData(Locale.ENGLISH);
        englishItem.setToggleGroup(langGroup);
        // ------------ Chinese Simplified
//        RadioMenuItem simpChineseItem = new RadioMenuItem(lang.getString("string_menuBar_edit_language_zh_CN"));
        RadioMenuItem simpChineseItem = L10N.radioMenuItemForKey("string_menuBar_edit_language_zh_CN");
        simpChineseItem.setUserData(Locale.SIMPLIFIED_CHINESE);
        simpChineseItem.setToggleGroup(langGroup);
        // ------------ Japanese (WIP)
        RadioMenuItem japaneseItem = L10N.radioMenuItemForKey("string_menuBar_edit_language_ja");
        japaneseItem.setUserData(Locale.JAPANESE);
        japaneseItem.setToggleGroup(langGroup);
        // Default to English
        englishItem.setSelected(true);
        // Assemble the Language menu
        langMenu.getItems().addAll(englishItem, simpChineseItem, japaneseItem);
        // Assemble Edit menu
        editMenu.getItems().addAll(langMenu);

        // Listener for Language menu
        langGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            try {
                currentLocale = (Locale) langGroup.getSelectedToggle().getUserData();
            } catch (NullPointerException ex) {
                System.out.println("Some weird problem happened while switching language. Not a big deal though.");
            }
            updateLocale(primaryStage, gameField, keyField, notesField);
        });


        // Assemble the menu bar
        menuBar.getMenus().addAll(fileMenu, editMenu);


        // Add columns to the table and associate them with ObservableList
//        TableColumn gameCol = new TableColumn(lang.getString("string_mainUI_game"));
        // TODO fix layout
        TableColumn gameCol = L10N.tableColumnForKey("string_mainUI_game");
        gameCol.setMinWidth(200);
        gameCol.setCellValueFactory(new PropertyValueFactory<Key, String>("game"));

//        TableColumn keyCol = new TableColumn(lang.getString("string_mainUI_key"));
        TableColumn keyCol = L10N.tableColumnForKey("string_mainUI_key");
        keyCol.setMinWidth(200);
        keyCol.setCellValueFactory(new PropertyValueFactory<Key, String>("key"));

//        TableColumn notesCol = new TableColumn(lang.getString("string_mainUI_notes"));
        TableColumn notesCol = L10N.tableColumnForKey("string_mainUI_notes");
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
        // TODO update those manually
//        TextField gameField = new TextField();
        gameField.setMaxWidth(gameCol.getMaxWidth());
        gameField.setPromptText(L10N.get("string_mainUI_game"));
//        TextField keyField = new TextField();
        keyField.setMaxWidth(keyCol.getMaxWidth());
        keyField.setPromptText(L10N.get("string_mainUI_key"));

//        TextField notesField = new TextField();
        notesField.setMaxWidth(notesCol.getPrefWidth());
        notesField.setPromptText(L10N.get("string_mainUI_notes"));

        // ...and a button
//        Button addButton = new Button(lang.getString("string_mainUI_add"));
        Button addButton = L10N.buttonForKey("string_mainUI_add");

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
                    notesField.getText()
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
            notesField.clear();
        });

        // Context menu for rows
        keyTable.setRowFactory(tableView -> {
            final TableRow<Key> row = new TableRow<>();
            final ContextMenu cm = new ContextMenu();
//            final MenuItem removeRow = new MenuItem(lang.getString("string_contextMenu_remove"));
//            final MenuItem copyKey = new MenuItem(lang.getString("string_contextMenu_copy"));
//            final MenuItem copyKeyAndRemove = new MenuItem(lang.getString("string_contextMenu_copyAndRemove"));
            final MenuItem removeRow = L10N.menuItemForKey("string_contextMenu_remove");
            final MenuItem copyKey = L10N.menuItemForKey("string_contextMenu_copy");
            final MenuItem copyKeyAndRemove = L10N.menuItemForKey("string_contextMenu_copyAndRemove");

            // Listener for removing a row
            removeRow.setOnAction((ActionEvent event) -> keyTable.getItems().remove(row.getItem()));

            // Listener for copying a key
            copyKey.setOnAction((ActionEvent event) -> {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                int selectedRow = keyTable.getSelectionModel().getSelectedIndex();

                String key = keyTable.getItems().get(selectedRow).getKey();
                content.putString(key);
                clipboard.setContent(content);
            });

            // Listener for copying a key then delete the row
            copyKeyAndRemove.setOnAction((ActionEvent event) -> {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                int selectedRow = keyTable.getSelectionModel().getSelectedIndex();

                String key = keyTable.getItems().get(selectedRow).getKey();
                content.putString(key);
                clipboard.setContent(content);
                keyTable.getItems().remove(row.getItem());
            });

            // Assemble the menu
            cm.getItems().addAll(removeRow, copyKey, copyKeyAndRemove);

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
        // A Hbox to hold the add section and the menu bar
        HBox addBox = new HBox();


        // Set properties and add components to VBox
        tableBox.setSpacing(5);
        tableBox.setPadding(new Insets(10, 0, 0, 10));
        tableBox.getChildren().addAll(appNameLabel, keyTable, addBox);

        // Set properties and add components to HBox
        addBox.setSpacing(3);
        addBox.getChildren().addAll(gameField, keyField, notesField, addButton);

        // Add VBox to scene
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, tableBox);

        // Finalize the settings
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


final class ShowPrompt {

    // Prompt for to file I/O errors
    static void fileReadError(String pathToFile, int context) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(L10N.get("string_alert_fileReadError_title"));
        alert.setHeaderText(L10N.get("string_alert_fileReadError_header"));
        if (context == 1) {
            alert.setContentText(L10N.get("string_alert_fileReadError_content_1",
                    L10N.get("string_mainUI_appName"), L10N.get("string_mainUI_appNameShort"), pathToFile));
        }
        if (context == 2) {
            alert.setContentText(L10N.get("string_alert_fileReadError_content_2",
                    L10N.get("string_mainUI_appName"), L10N.get("string_mainUI_appNameShort"), pathToFile));
        }
        alert.showAndWait();
    }

    // Prompt for parsing errors
    static void fileParseError(String pathToFile, int context) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(L10N.get("string_alert_fileParseError_title"));
        alert.setHeaderText(L10N.get("string_alert_fileParseError_header"));
        if (context == 1) {
            alert.setContentText(L10N.get("string_alert_fileParseError_content_1",
                    L10N.get("string_mainUI_appName")));
        }

        alert.showAndWait();
    }

    // Prompt to ask whether user want to analyze the file
    static boolean oldFormat() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(L10N.get("string_alert_oldFormat_title"));
        alert.setHeaderText(L10N.get("string_alert_oldFormat_header"));
        alert.setContentText(L10N.get("string_alert_oldFormat_content",
                L10N.get("string_mainUI_appName"), L10N.get("string_mainUI_appNameShort")));
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        return (alert.showAndWait().get() == ButtonType.YES);
    }

    // Let user know the parse results
    static void analysisReport(int ok, int failed) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(L10N.get("string_alert_analysisReport_title"));
        alert.setHeaderText(L10N.get("string_alert_analysisReport_header",
                L10N.get("string_mainUI_appNameShort")));
        alert.setContentText(L10N.get("string_alert_analysisReport_content", ok, failed));
        alert.showAndWait();
    }
}
