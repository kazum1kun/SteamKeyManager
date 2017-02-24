// Parse a txt file and return a corresponding ObservableList

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class FileParser {
    public static void parse()
    {

    }

    public static ObservableList<Key> get()
    {
        ObservableList<Key> val =
                FXCollections.observableArrayList(
                        new Key("11111-22222-33333", "Puslum", ""),
                        new Key("ABCDE-FGHIJ-KLMNO", "Asco", "Null"),
                        new Key("QWERT-YUIOP-ASDFG", "HFHYFBYF", "FUKCD")
                );
        return val;
    }
}
