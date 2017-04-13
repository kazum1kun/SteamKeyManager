import javafx.beans.property.SimpleStringProperty;

/**
 * Class representing individual entries
 *
 * @author Xuanli Lin
 * @version 0.0.2-alpha
 */
public class Key {
    // Fields to hold properties of the key
    private SimpleStringProperty game;
    private SimpleStringProperty key;
    private SimpleStringProperty notes;

    // Default constructor
    Key(String game, String key, String notes){
        this.game = new SimpleStringProperty(game);
        this.key = new SimpleStringProperty(key);
        this.notes = new SimpleStringProperty(notes);
    }

    // Accessors and mutators

    public String getKey() {
        return key.get();
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getGame() {
        return game.get();
    }

    public void setGame(String game) {
        this.game.set(game);
    }

    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }
}
