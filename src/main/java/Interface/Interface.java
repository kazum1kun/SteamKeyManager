package Interface;

import Model.Key;
import Utils.Excel.ExcelOp;
import Utils.FileRw.FileParser;
import Utils.FileRw.FileWriter;
import Utils.Net.WebParser;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

/**
 * Main interface class of the Steam Model.Key Manager
 *
 * @author Xuanli Lin
 * @version 0.2.0-alpha
 */
public class Interface extends Application {

    // Core component: a TableView table showing all information
    private static TableView<Key> keyTable = new TableView<>();
    // Core component: a ObservableList containing the actual data
    private static ObservableList<Key> keyList;
    // Filtered keyList based on the keyList
    private static FilteredList<Key> filteredKeyList;
    // The File user chosen
    private static File userFile = null;
    // Current language, default to system locale (EN if unsupported)
    private static Locale currentLocale = L10N.getDefaultLocale();
    // Default resource bundle
//    private static ResourceBundle lang = null;

    // Prompt user to choose a file and attempt to parse it
    private static void prepareKeyList(Stage stage) {
        File selectedFile = FileParser.chooseFile(stage);
        // Clear the content of keyList if a file is already loaded
        if (!keyList.isEmpty()) {
            if (ShowPrompt.confirmLoad(stage)) {
                keyList.clear();
            }
        }

        if (selectedFile != null) {
            userFile = selectedFile;
            switch (FilenameUtils.getExtension(selectedFile.getPath())) {
                case "txt":     // Plain text file
                    keyList = FileParser.parseAndGet(selectedFile);
                    break;
                case "xlsx":     // MS Excel file
                    keyList = ExcelOp.parseExcelAndGet(selectedFile);
                    break;
                case "db":      // Reserved for mySQL db
                    // Do something
                    break;
                default:        // Default to plain text file
                    keyList = FileParser.parseAndGet(selectedFile);
            }

            // Update the title of app when a file is loaded
            stage.setTitle(L10N.get("string_mainUI_appName") + " - " + userFile.getPath());
        } else {
            keyList = FileParser.getEmpty();
        }

        // Load the key list after all
        filteredKeyList = new FilteredList<>(keyList, p -> true);

        // Wrap the FilteredList in a SortedList.
        SortedList<Key> sortedKeyList = new SortedList<>(filteredKeyList);

        // Bind the SortedList comparator to the TableView comparator.
        sortedKeyList.comparatorProperty().bind(keyTable.comparatorProperty());

        // Add sorted (and filtered) data to the table.
        keyTable.setItems(sortedKeyList);
    }

    // Consider it prepareKeyList reversed
    // MODE: 0 = do not check nullity of userFile, effectively Save As...
    //       1 = check for userFile existence, and overwrite uTF without user acknowledge
    static void saveKeyList(Stage stage, int mode) {
        File dest;
        // Ask for save location only when creating a new collection
        if (mode == 1 && userFile == null || mode == 0) {
            dest = FileWriter.chooseFile(stage);
            if (dest != null) {
                userFile = dest;
            }
        } else {
            dest = userFile;
        }

        // Detect the extension of the file and call appropriate file writers
        switch (FilenameUtils.getExtension(userFile.getPath())) {
            case "txt": // Plain text file
                assert dest != null;
                FileWriter.saveToText(dest, keyList);
                break;
            case "xlsx": // MS Excel file
                ExcelOp.saveToExcel(dest, keyList);
                break;
            case "db":   // mySQL database
                // Reserved
                break;
            default:    // Default to text file
                assert dest != null;
                FileWriter.saveToText(dest, keyList);
                break;
        }
    }

    private static void updateLocale(Stage stage, TextField... textFields) {
        // This would set locale for most areas
        L10N.setLocale(currentLocale);

        // Some cannot be set (for now), update them manually
        // APP TITLE
        stage.setTitle(L10N.get("string_mainUI_appName"));

        // TextField prompts
        textFields[0].setPromptText(L10N.get("string_mainUI_game"));
        textFields[1].setPromptText(L10N.get("string_mainUI_key"));
        textFields[2].setPromptText(L10N.get("string_mainUI_notes"));
        textFields[3].setPromptText(L10N.get("string_mainUI_search"));
    }

    @Override
    public void start(Stage primaryStage) {
        // Populate an empty ObservableList on program start
        keyList = FileParser.getEmpty();

        // Some declarations here for you dumb Java
        TextField gameField = new TextField();
        TextField keyField = new TextField();
        TextField notesField = new TextField();

        // Set up a new scene and set properties of the stage
        Scene scene = new Scene(new VBox());
        // change locale of this manually
        primaryStage.setTitle(L10N.get("string_mainUI_appName"));
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);

        // And a label displaying the name of the app
//        Label appNameLabel = new Label(lang.getString("string_mainUI_app_name"));
        Label appNameLabel = L10N.labelForKey("string_mainUI_appName");
        appNameLabel.setFont(new Font("Segoe UI", 20));

        // Make table editable
        keyTable.setEditable(true);

        // Search function implementation with FilteredList
        // Adopted from the tutorial at http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/

        // Enter query here!
        TextField searchField = new TextField();
        searchField.setPromptText(L10N.get("string_mainUI_search"));
        searchField.setPrefWidth(primaryStage.getWidth() / 2);
        // Button to clear search query
        Button clearSeachButton = L10N.buttonForKey("string_mainUI_clear");
        clearSeachButton.setOnAction((ActionEvent event) -> {
            if (!searchField.getText().isEmpty())
                searchField.clear();
        });

        // Bundle searchField and 'clear' button together
        HBox searchBox = new HBox(searchField, clearSeachButton);
        searchBox.setSpacing(5);

        searchField.textProperty().addListener(((observable, oldValue, newValue) ->
                filteredKeyList.setPredicate(key -> {
                    // Display all keys if search field is empty
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare search query with game names and notes
                    String lowerCaseQuery = newValue.toLowerCase();
                    // Query matches a game
                    // Query matches a notes
                    return key.getGame().toLowerCase().contains(lowerCaseQuery) ||
                            key.getNotes().toLowerCase().contains(lowerCaseQuery);
                // Query matches nothing
                })));

        // MenuBar is here!
        MenuBar menuBar = new MenuBar();

        // ---- File
        Menu fileMenu = L10N.menuForKey("string_menuBar_file");
        // -------- Open
        MenuItem openItem = L10N.menuItemForKey("string_menuBar_file_open");
        openItem.setOnAction((ActionEvent event) -> prepareKeyList(primaryStage));
        // -------- Save
        MenuItem saveItem = L10N.menuItemForKey("string_menuBar_file_save");
        saveItem.setOnAction((ActionEvent event) -> saveKeyList(primaryStage, 1));
        // -------- Save as
        MenuItem saveAsItem = L10N.menuItemForKey("string_menuBar_file_saveAs");
        saveAsItem.setOnAction((ActionEvent event) -> saveKeyList(primaryStage, 0));
        // -------- Exit
        MenuItem closeItem = L10N.menuItemForKey("string_menuBar_file_exit");
        closeItem.setOnAction((ActionEvent event) -> {
            if (ShowPrompt.confirmQuit())
                primaryStage.close();
        });
        // Assemble the File Menu
        fileMenu.getItems().addAll(openItem, saveItem, saveAsItem, closeItem);

        // ---- Edit
        Menu editMenu = L10N.menuForKey("string_menuBar_edit");

        // -------- Language
        Menu langMenu = L10N.menuForKey("string_menuBar_edit_language");
        final ToggleGroup langGroup = new ToggleGroup();
        // ------------ English
        RadioMenuItem englishItem = L10N.radioMenuItemForKey("string_menuBar_edit_language_en");
        englishItem.setUserData(Locale.ENGLISH);
        englishItem.setToggleGroup(langGroup);
        // ------------ Chinese Simplified
        RadioMenuItem simpChineseItem = L10N.radioMenuItemForKey("string_menuBar_edit_language_zh_CN");
        simpChineseItem.setUserData(Locale.SIMPLIFIED_CHINESE);
        simpChineseItem.setToggleGroup(langGroup);
        // ------------ Japanese (WIP)
        RadioMenuItem japaneseItem = L10N.radioMenuItemForKey("string_menuBar_edit_language_ja");
        japaneseItem.setUserData(Locale.JAPANESE);
        japaneseItem.setToggleGroup(langGroup);
        // Default to system locale (English if not supported)
        switch (currentLocale.toString()) {
            case "en_US":
                englishItem.setSelected(true);
                break;
            case "zh_CN":
                simpChineseItem.setSelected(true);
                break;
            case "ja_JP":
                japaneseItem.setSelected(true);
                break;
        }
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
            updateLocale(primaryStage, gameField, keyField, notesField, searchField);
        });

        // Assemble the menu bar
        menuBar.getMenus().addAll(fileMenu, editMenu);

        // Confirm quit when user clicks the close button on top right
        primaryStage.setOnCloseRequest(event -> {
            if (ShowPrompt.confirmQuit())
                primaryStage.close();
            else
                event.consume();
        });


        // Add columns to the table and associate them with ObservableList
        // COLUMN WIDTHS
        TableColumn gameCol = L10N.tableColumnForKey("string_mainUI_game");
        gameCol.setMinWidth(150);
        gameCol.setPrefWidth(180);
        gameCol.setCellValueFactory(new PropertyValueFactory<Key, String>("game"));

        TableColumn keyCol = L10N.tableColumnForKey("string_mainUI_key");
        keyCol.setMinWidth(135);
        keyCol.setMaxWidth(135);
        keyCol.setCellValueFactory(new PropertyValueFactory<Key, String>("key"));

        TableColumn notesCol = L10N.tableColumnForKey("string_mainUI_notes");
        notesCol.setMinWidth(100);
        notesCol.setCellValueFactory(new PropertyValueFactory<Key, String>("notes"));

        keyTable.getColumns().addAll(gameCol, keyCol, notesCol);
        keyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Make the table matches the height of the tableBox
        keyTable.setPrefHeight(1000);

        // Implement cell editing
        gameCol.setCellFactory(column -> EditCell.createStringEditCell());
        keyCol.setCellFactory(column -> EditCell.createStringEditCell());
        notesCol.setCellFactory(column -> EditCell.createStringEditCell());

        // Add textFields to add a new key. Make them no wider than the table
        gameField.setMaxWidth(gameCol.getMaxWidth());
        gameField.setPromptText(L10N.get("string_mainUI_game"));

        keyField.setMaxWidth(keyCol.getMaxWidth());
        keyField.setPromptText(L10N.get("string_mainUI_key"));

        notesField.setMaxWidth(notesCol.getPrefWidth());
        notesField.setPromptText(L10N.get("string_mainUI_notes"));

        // Set the widths of these TextFields
        gameField.setPrefColumnCount(20);
        keyField.setPrefColumnCount(17);
        notesField.setPrefColumnCount(20);

        // ...and a button
//        Button addButton = new Button(lang.getString("string_mainUI_add"));
        Button addButton = L10N.buttonForKey("string_mainUI_add");

        // Change content of table on commit
        // Update the value of the game cell after users finish editing
        gameCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Key, String>>) event -> (event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setGame(event.getNewValue())
        );

        // Update the value of the game cell after users finish editing
        keyCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Key, String>>) event -> (event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setKey(event.getNewValue())
        );

        // Update the value of the game cell after users finish editing
        notesCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Key, String>>) event -> (event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setNotes(event.getNewValue())
        );

        // Add the input to the list
        addButton.setOnAction(event -> {
            // Add the key only if at least game and key is present
            if (!gameField.getText().isEmpty() && !keyField.getText().isEmpty()) {
                // Create a new Model.Key and add it to keyList
                Key key = new Key(
                        gameField.getText(),
                        keyField.getText(),
                        notesField.getText()
                );
                keyList.add(key);

                // Clear input fields
                gameField.clear();
                keyField.clear();
                notesField.clear();
            }
            // Database Test
//            try {
//                Utils.Db.KeyDao mysql = new Utils.Db.KeyDao(Utils.Db.DBTool.MYSQL);
//                Utils.Db.KeyDao sqlite = new Utils.Db.KeyDao(Utils.Db.DBTool.SQLITE);
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
        });

        // Context menu for rows
        keyTable.setRowFactory(tableView -> {
            final TableRow<Key> row = new TableRow<>();
            final ContextMenu cm = new ContextMenu();
            final MenuItem removeRow = L10N.menuItemForKey("string_contextMenu_remove");
            final MenuItem copyKey = L10N.menuItemForKey("string_contextMenu_copy");
            final MenuItem copyKeyAndRemove = L10N.menuItemForKey("string_contextMenu_copyAndRemove");
            final MenuItem viewInSteam = L10N.menuItemForKey("string_contextMenu_viewInSteam");


            // Listener for removing a row
            // Method updated to reflect changes on the VC model
            removeRow.setOnAction((ActionEvent event) -> {
                Key selectedKey = keyTable.getSelectionModel().getSelectedItem();
                if (selectedKey != null) {
                    keyList.remove(selectedKey);
                }
            });

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

                // Updated
                Key selectedKey = keyTable.getSelectionModel().getSelectedItem();
                if (selectedKey != null) {
                    keyList.remove(selectedKey);
                }
            });

            // Listener for viewing a game in Steam
            viewInSteam.setOnAction((ActionEvent event) -> {
                int selectedRow = keyTable.getSelectionModel().getSelectedIndex();
                String gameName = keyTable.getItems().get(selectedRow).getGame();
                String steamURL = WebParser.findURLOfGame(gameName, L10N.getLocale().getLanguage());
                // Get system desktop
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URL(steamURL).toURI());
                } catch (URISyntaxException ex) {
                    System.out.println("Bad URL!!");
                } catch (IOException ex) {
                    System.out.println("IO Exception");
                }
            });

            // Commit change on focus lost
            keyTable.setOnKeyPressed(event -> {
                TablePosition pos = keyTable.getFocusModel().getFocusedCell();
                if (pos != null && event.getCode().isLetterKey()) {
                    keyTable.edit(pos.getRow(), pos.getTableColumn());
                }
            });


            // Assemble the menu
            cm.getItems().addAll(removeRow, copyKey, copyKeyAndRemove, viewInSteam);

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

        // Set properties and add components to HBox
        addBox.setSpacing(5);
        addBox.getChildren().addAll(gameField, keyField, notesField, addButton);

        // Set properties and add components to VBox
        // TABLE INSECTS WITH THE BOUNDARY
        tableBox.setSpacing(5);
        tableBox.setPadding(new Insets(10, 10, 10, 10));
        tableBox.getChildren().addAll(appNameLabel, searchBox, keyTable, addBox);

        // Add VBox to scene
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, tableBox);

        // Make sure key table is focused on app start
        keyTable.requestFocus();

        // Finalize the settings
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
