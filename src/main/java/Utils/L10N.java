package Utils;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * L10N utility class..
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 *         ALL CREDIT GOES TO THE ORIGINAL AUTHOR P.J. Meisch.
 *         This file was modified by me (Xuanli Lin) to suit the need for SKM.
 * @version 0.0.6-alpha
 */
public final class L10N {

    /**
     * the current selected Locale.
     */
    private static final ObjectProperty<Locale> locale;

    static {
        locale = new SimpleObjectProperty<>(getDefaultLocale());
        locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
    }

    /**
     * get the supported Locales.
     *
     * @return List of Locale objects.
     */
    public static List<Locale> getSupportedLocales() {
        return new ArrayList<>(Arrays.asList(Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE, Locale.JAPANESE));
    }

    /**
     * get the default locale. This is the systems default if contained in the supported locales, english otherwise.
     *
     * @return System default locale
     */
    public static Locale getDefaultLocale() {
        Locale sysDefault = Locale.getDefault();
        return getSupportedLocales().contains(sysDefault) ? sysDefault : Locale.ENGLISH;
    }

    public static Locale getLocale() {
        return locale.get();
    }

    public static void setLocale(Locale locale) {
        localeProperty().set(locale);
        Locale.setDefault(locale);
    }

    public static ObjectProperty<Locale> localeProperty() {
        return locale;
    }

    /**
     * gets the string with the given key from the resource bundle for the current locale and uses it as first argument
     * to MessageFormat.format, passing in the optional args and returning the result.
     *
     * @param key  message key
     * @param args optional arguments for the message
     * @return localized formatted string
     */
    public static String get(final String key, final Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle("language.lang", getLocale());
        return MessageFormat.format(bundle.getString(key), args);
    }

    /**
     * creates a String binding to a localized String for the given message bundle key
     *
     * @param key key
     * @return String binding
     */
    public static StringBinding createStringBinding(final String key, Object... args) {
        return Bindings.createStringBinding(() -> get(key, args), locale);
    }

    /**
     * creates a String Binding to a localized String that is computed by calling the given func
     *
     * @param func function called on every change
     * @return StringBinding
     */
    public static StringBinding createStringBinding(Callable<String> func) {
        return Bindings.createStringBinding(func, locale);
    }

    /**
     * creates a bound Label whose value is computed on language change.
     *
     * @param func the function to compute the value
     * @return Label
     */
    public static Label labelForValue(Callable<String> func) {
        Label label = new Label();
        label.textProperty().bind(createStringBinding(func));
        return label;
    }

    /**
     * creates a bound Button for the given resourcebundle key
     *
     * @param key  ResourceBundle key
     * @param args optional arguments for the message
     * @return Button
     */
    public static Button buttonForKey(final String key, final Object... args) {
        Button button = new Button();
        button.textProperty().bind(createStringBinding(key, args));
        return button;
    }

    /**
     * creates a bound Label for the given resourcebundle key
     *
     * @param key  ResourceBundle key
     * @param args optional arguments for the message
     * @return Label
     */
    public static Label labelForKey(final String key, final Object... args) {
        Label label = new Label();
        label.textProperty().bind(createStringBinding(key, args));
        return label;
    }

    /**
     * creates a bound Menu for the given resourcebundle key
     *
     * @param key  ResourceBundle key
     * @param args optional arguments for the message
     * @return Menu
     */
    public static Menu menuForKey(final String key, final Object... args) {
        Menu menu = new Menu();
        menu.textProperty().bind(createStringBinding(key, args));
        return menu;
    }

    /**
     * creates a bound MenuItem for the given resourcebundle key
     *
     * @param key  ResourceBundle key
     * @param args optional arguments for the message
     * @return MenuItem
     */
    public static MenuItem menuItemForKey(final String key, final Object... args) {
        MenuItem menuItem = new MenuItem();
        menuItem.textProperty().bind(createStringBinding(key, args));
        return menuItem;
    }

    /**
     * creates a bound RadioMenuItem for the given resourcebundle key
     *
     * @param key  ResourceBundle key
     * @param args optional arguments for the message
     * @return RadioMenuItem
     */
    public static RadioMenuItem radioMenuItemForKey(final String key, final Object... args) {
        RadioMenuItem radioMenuItem = new RadioMenuItem();
        radioMenuItem.textProperty().bind(createStringBinding(key, args));
        return radioMenuItem;
    }

    /**
     * creates a bound TableColumn for the given resourcebundle key
     *
     * @param key  ResourceBundle key
     * @param args optional arguments for the message
     * @return TableColumn
     */
    public static TableColumn tableColumnForKey(final String key, final Object... args) {
        TableColumn tableColumn = new TableColumn();
        tableColumn.textProperty().bind(createStringBinding(key, args));
        return tableColumn;
    }

    /**
     * creates a bound TextField for the given resourcebundle key
     *
     * @param key  ResourceBundle key
     * @param args optional arguments for the message
     * @return TextField
     */
    public static TextField textFieldForKey(final String key, final Object... args) {
        TextField textField = new TextField();
        textField.textProperty().bind(createStringBinding(key, args));
        return textField;
    }

    // Get current Resource Bundle
    public static ResourceBundle getCurrentResBundle() {
        return ResourceBundle.getBundle("language.lang", getLocale());
    }
}