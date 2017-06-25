import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse a txt file and return a corresponding ObservableList
 * Parser engine version 1
 *
 * @author Xuanli Lin
 * @version 0.0.5-alpha
 */

public final class FileParser {
    private static final String VER1_HEADER = "SKM Data Format 1";
    private static ObservableList<Key> keys;

    public static File getFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select a text file...");
        fileChooser.setInitialDirectory(new File(
                System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text File", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        return fileChooser.showOpenDialog(stage);
    }

    // Read in the first line and decide what to do
    public static void parse(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
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
            ShowPrompt.fileReadError(file.getAbsolutePath(), 1);
        } catch (IOException ex) {
            ShowPrompt.fileReadError(file.getAbsolutePath(), 2);
        }
    }

    // File parser for format version 1
    private static void parseV1(File file) {
        // Create another BufferedReader to start from the beginning
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
            // Omit the first line (which contains meta info)
            br.readLine();

            // Compile strings into corresponding Key objects
            String line;
            while ((line = br.readLine()) != null) {
                List<String> key = Arrays.asList(line.split(";"));
                keys.add(new Key(key.get(0), key.get(1), key.get(2)));
            }
        } catch (FileNotFoundException ex) {
            ShowPrompt.fileReadError(file.getAbsolutePath(), 1);
        } catch (IOException ex) {
            ShowPrompt.fileParseError(file.getAbsolutePath(), 1);
        }
    }

    // File parser for unrecognized format
    private static void parseOld(File file) {
        // Define the pattern for a Steam key and a URL
        Pattern steamKeyPattern = Pattern.compile("[A-Z0-9]{5}-[A-Z0-9]{5}-[A-Z0-9]{5}");
        Pattern urlPattern = Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");

        // Create another BufferedReader to start from the beginning
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split each line by the regex  delimiter (defined as a combination of punctuation(s) with possible
                // whitespace(s). Hyphen (-) is not a valid delimiter for obvious reason.
                List<String> tokens = Arrays.asList(line.split("[\\p{Punct}\\s]&&[^-]+"));
                String keyToken, gameToken;
                String firstToken = tokens.get(0);
                String secondToken = tokens.get(1);

                // If the first token matches the pattern of a Steam key...
                Matcher keyMatcher = steamKeyPattern.matcher(firstToken);
                Matcher urlMatcher = urlPattern.matcher(firstToken);
                if (keyMatcher.matches() || urlMatcher.matches()) {
                    keyToken = firstToken;
                    gameToken = secondToken;
                } else {
                    keyMatcher = steamKeyPattern.matcher(secondToken);
                    urlMatcher = urlPattern.matcher(secondToken);
                    if (keyMatcher.matches() || urlMatcher.matches()) {
                        keyToken = secondToken;
                        gameToken = firstToken;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            ShowPrompt.fileReadError(file.getAbsolutePath(), 1);
        } catch (IOException ex) {
            ShowPrompt.fileParseError(file.getAbsolutePath(), 1);
        } catch (IllegalFormatException ex) {
            ShowPrompt.cantDetect();
        }
    }

    // TODO: rewrite get method here to reflect new change
    // TODO: TEST!!!

    // Old proof-of-concept pre-populated ObservableList
    public static ObservableList<Key> get() {
        ObservableList<Key> val =
                FXCollections.observableArrayList(
                        new Key("11111-22222-33333", "Puslum", ""),
                        new Key("ABCDE-FGHIJ-KLMNO", "Asco", "Null"),
                        new Key("QWERT-YUIOP-ASDFG", "HFHYFBYF", "FUKCD")
                );
        return val;
    }
}
