import Utils.L10N;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * This class contains nothing but bunch of prompts
 * Separated from main Interface class since ver 0.2.0
 *
 * @author Xuanli Lin
 * @version 0.2.0-alpha
 * @since 0.2.0-alpha
 */
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

    // Prompt for file creation errors
    static void fileCreateError(String pathToFile) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(L10N.get("string_alert_fileCreateError_title"));
        alert.setHeaderText(L10N.get("string_alert_fileCreateError_header"));
        alert.setContentText(L10N.get("string_alert_fileCreateError_content",
                L10N.get("string_mainUI_appName"), L10N.get("string_mainUI_appNameShort"), pathToFile));

        alert.showAndWait();
    }

    // Prompt for a successful save
    static void fileSaved(String pathToFile) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(L10N.get("string_alert_fileSaved_title"));
        alert.setHeaderText(null);
        alert.setContentText(L10N.get("string_alert_fileSaved_content",
                L10N.get("string_mainUI_appName"), pathToFile));

        alert.showAndWait();
    }

    // Prompt for file save errors
    static void fileSaveError(String pathToFile) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(L10N.get("string_alert_fileSaveError_title"));
        alert.setHeaderText(L10N.get("string_alert_fileSaveError_header",
                L10N.get("string_mainUI_appNameShort")));
        alert.setContentText(L10N.get("string_alert_fileSaveError_content",
                L10N.get("string_mainUI_appName"), L10N.get("string_mainUI_appNameShort"), pathToFile));

        alert.showAndWait();
    }

    // Ask user whether they want to purge the list and load a new file
    static boolean confirmLoad(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(L10N.get("string_alert_confirmLoad_title"));
        alert.setHeaderText(L10N.get("string_alert_confirmLoad_header"));
        alert.setContentText(L10N.get("string_alert_confirmLoad_content"));
        ButtonType saveThenLoad = new ButtonType(L10N.get("string_alert_confirmLoad_button"), ButtonBar.ButtonData.YES);

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, saveThenLoad);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == saveThenLoad) {
            Interface.saveKeyList(stage, 1);
        }

        return (result.get() == ButtonType.YES);
    }

    static boolean confirmQuit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(L10N.get("string_alert_confirmQuit_title"));
        alert.setHeaderText(L10N.get("string_alert_confirmQuit_header"));
        alert.setContentText(L10N.get("string_alert_confirmQuit_content"));

        return (alert.showAndWait().get() == ButtonType.OK);
    }
}
