import Utils.L10N;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse a txt file and return a corresponding ObservableList
 * Parser engine version 1, supports Key format version 1 and older format
 *
 * @author Xuanli Lin
 * @version 0.0.5-alpha
 */

public final class FileParser {
    private static final String VER1_HEADER = "SKM Data Format 1";
    private static ObservableList<Key> keys = FXCollections.observableArrayList();
    private static int keyAndUrlFound = 0, unrecFound = 0;

    public static File chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(L10N.get("string_fileChooser_open_title"));
        fileChooser.setInitialDirectory(new File(
                System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(L10N.get("string_fileChooser_filter_txt"), "*.txt"),
                new FileChooser.ExtensionFilter(L10N.get("string_fileChooser_filter_allFiles"), "*.*")
        );

        return fileChooser.showOpenDialog(stage);
    }

    // Read in the first line and decide what to do
    public static void parse(File file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "utf-8"))) {
            String firstLine = br.readLine();

            // First case: the text file is of version 1 format
            // Version 1 pattern: %game_name%;%key%;%notes%
            if (firstLine.equals(VER1_HEADER)) {
                parseV1(file);
            }

            // Reserved for upcoming new format

            // Second case: the text file is of older/unrecognized format
            // Prompt user about the incident and attempt to parse it
            else {
                if (ShowPrompt.oldFormat()) {
                    parseOld(file);
                }
            }
        } catch (FileNotFoundException ex) {
            ShowPrompt.fileReadError(file.getPath(), 1);
        } catch (IOException ex) {
            ShowPrompt.fileReadError(file.getPath(), 2);
        }
    }

    // File parser for format version 1
    private static void parseV1(File file) {
        // Create another BufferedReader to start from the beginning
        // NEW! UTF-8 support added
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "utf-8"))) {
            // Omit the first line (which contains meta info)
            br.readLine();

            // Compile strings into corresponding Key objects
            String line;
            while ((line = br.readLine()) != null) {
                List<String> key = Arrays.asList(line.split(";"));
                // If the notes are empty
                if (key.size() < 3) {
                    keys.add(new Key(key.get(0), key.get(1), ""));
                } else {
                    // Or notes are filled
                    keys.add(new Key(key.get(0), key.get(1), key.get(2)));
                }
            }
        } catch (FileNotFoundException ex) {
            ShowPrompt.fileReadError(file.getPath(), 1);
        } catch (IOException ex) {
            ShowPrompt.fileParseError(file.getPath(), 1);
        }
    }

    // File parser for unrecognized format
    private static void parseOld(File file) {
        // Define the pattern for a Steam key and a URL
        // I HATE REGEX!!!!
        Pattern steamKeyPattern = Pattern.compile("[*]*[A-Z0-9]{5}-[A-Z0-9]{5}-[A-Z0-9]{5}");
        Pattern urlPattern = Pattern.compile("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

        // Create another BufferedReader to start from the beginning
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split each line by the regex  delimiter (defined as a combination of punctuation(s) with possible
                // whitespace(s). Hyphen (-) is not a valid delimiter for obvious reason.
                List<String> tokens = Arrays.asList(line.split("; "));

                // Omit the line if insufficient parameter was found
                if (tokens.size() < 2) {
                    unrecFound++;
                    continue;
                }
                String keyToken = null, gameToken = null;
                String firstToken = tokens.get(0).trim();
                String secondToken = tokens.get(1).trim();

                // If the first token matches the pattern of a Steam key or a URL...
                Matcher keyMatcher = steamKeyPattern.matcher(firstToken);
                Matcher urlMatcher = urlPattern.matcher(firstToken);
                if (keyMatcher.find() || urlMatcher.find()) {
                    keyToken = firstToken;
                    gameToken = secondToken;
                    keyAndUrlFound++;
                } else {    // If the second token matches the pattern of a Steam key or a URL...
                    keyMatcher = steamKeyPattern.matcher(secondToken);
                    urlMatcher = urlPattern.matcher(secondToken);
                    if (keyMatcher.find() || urlMatcher.find()) {
                        keyToken = secondToken;
                        gameToken = firstToken;
                        keyAndUrlFound++;
                    } else {
                        unrecFound++;
                    }
                }

                // Add the key to the List
                keys.add(new Key(gameToken, keyToken, ""));
            }
        } catch (FileNotFoundException ex) {
            ShowPrompt.fileReadError(file.getPath(), 1);
        } catch (IOException ex) {
            ShowPrompt.fileParseError(file.getPath(), 1);
        }

        ShowPrompt.analysisReport(keyAndUrlFound, unrecFound);
        // Reset the counter once the process is finished
        keyAndUrlFound = unrecFound = 0;
    }

    public static ObservableList<Key> get() {
        return keys;
    }

    public static ObservableList<Key> parseAndGet(File file) {
        parse(file);
        return keys;
    }

    // Return an empty ObservableList if no file was chosen by user
    public static ObservableList<Key> getEmpty() {
        return keys;
    }
    // Old proof-of-concept pre-populated ObservableList
//    public static ObservableList<Key> get() {
//        ObservableList<Key> val =
//                FXCollections.observableArrayList(
//                        new Key("11111-22222-33333", "Puslum", ""),
//                        new Key("ABCDE-FGHIJ-KLMNO", "Asco", "Null"),
//                        new Key("QWERT-YUIOP-ASDFG", "HFHYFBYF", "FUKCD")
//                );
//        return val;
//    }
}
