import Utils.L10N;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;

/**
 * Write the Table to a file
 * Supports txt, xlsx (upcoming), mysql (upcoming), maybe more
 * <p>
 * Writer engine version 1, supports key format 1
 *
 * @author Xuanli Lin
 * @version 0.1.0-alpha
 */

public class FileWriter {
    private static final String VER1_HEADER = "SKM Data Format 1";

    public static File chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(L10N.get("string_fileChooser_save_title"));
        fileChooser.setInitialDirectory(new File(
                System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(L10N.get("string_fileChooser_filter_txt"), "*.txt"),
                new FileChooser.ExtensionFilter(L10N.get("string_fileChooser_filter_allFiles"), "*.*")
        );

        return fileChooser.showSaveDialog(stage);
    }

    // Save the collection to a text file
    public static void saveToText(File destination, ObservableList<Key> keys) {
        // EMPTY the content of the file, if it exists
        // Ik it's stupid, but by far the most reliable one
        if (Files.exists(destination.toPath())) {
            try {
                // PrintWriter truncates everything in the file upon opening
                PrintWriter pw = new PrintWriter(destination);
                pw.close();
                // That's it!! No need to write empty string to the file.
            } catch (FileNotFoundException ex) {
                // Since we made sure the file already exists, it's impossible for this to happen.
                // NOP
            }
        } else {
            // Create the file if it does not exist
            try {
                Files.createFile(destination.toPath());
            } catch (IOException ex) {
                ShowPrompt.fileCreateError(destination.getPath());
            }
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(destination), "utf-8"))) {
            // Metainfo
            writer.write(VER1_HEADER);
            writer.write(System.getProperty("line.separator"));
            // Keys
            for (Key key : keys) {
                writer.write(key.getGame() + ";" + key.getKey() + ";" + key.getNotes() + System.getProperty("line.separator"));
            }
            ShowPrompt.fileSaved(destination.getPath());
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            // Impossible. Do nothing here
            // NOP
        } catch (IOException ex) {
            ShowPrompt.fileSaveError(destination.getPath());
        }
    }
}
