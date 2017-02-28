// Class for individual key data

import javafx.beans.property.SimpleStringProperty;

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

    public String getGame() {
        return game.get();
    }

    public String getNotes() {
        return notes.get();
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public void setGame(String game) {
        this.game.set(game);
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }
}
