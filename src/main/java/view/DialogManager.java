package view;

import javafx.scene.control.Alert;

public class DialogManager {
    /**
     * Создает и выводит диалоговое окно с информационным сообщением
     * @param title заголовок окна
     * @param text текст сообщения
     */
    public static void showInfoDialog(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.setHeaderText("");
        alert.showAndWait();
    }

    /**
     * Создает и выводит диалоговое окно с сообщением об ошибке
     * @param title заголовок окна
     * @param text текст ошибки
     */
    public static void showErrorDialog(String title, String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.setHeaderText("");
        alert.showAndWait();
    }

    /**
     * Создает и выводит диалоговое окно с запросом подтверждения действия
     * @param title заголовок окна
     * @param text текст сообщения
     */
    public static boolean showConfirmDialog(String title, String text){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.setHeaderText("");
        alert.showAndWait();
        return !alert.getResult().getButtonData().isCancelButton();
    }
}
