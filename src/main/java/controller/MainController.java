package controller;

import config.AppConfig;
import config.DatabaseHelper;
import config.HelperFactory;
import entity.Interview;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.ConstantManager;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;

import static util.DBUtil.*;

public class MainController {
    // Связывание данных
    private ObservableList<Interview> interviews = FXCollections.observableArrayList(); // источник данных для интервью
    // Сцены
    private VBox addInterviewDlg;
    private VBox addInterviewerDlg;
    private VBox editCategoryDlg;
    private VBox addCandidateDlg;
    private Stage primaryStage;
    private Stage dlgStage; // ?

    @FXML
    TableView<Interview> mainTable;
    @FXML
    TableColumn<Interview, String> fioColumn;
    @FXML
    TableColumn<Interview, String> postColumn;
    @FXML
    TableColumn<Interview, String> dateColumn;
    @FXML
    TextField fioFilter;
    @FXML
    TextField postFilter;
    @FXML
    TextField dateFilter;


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // инициализируем форму данными
    @FXML
    private void initialize() throws SQLException {
        // реакция на нажатие кнопки DELETE
        mainTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DELETE) {
                    try {
                        onDeleteInterview();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        // реакция на двойной щелчок по таблице
        mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    try {
                        onEditInterview();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // устанавливаем тип и значение которое должно хранится в колонке
        fioColumn.setCellValueFactory(new PropertyValueFactory<Interview, String>("idCandidate"));
        postColumn.setCellValueFactory(new PropertyValueFactory<Interview, String>("post"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Interview, String>("date"));
        // заполняем таблицу данными
        interviews.addAll(HelperFactory.getHelper().getInterview());
        mainTable.setItems(interviews);
    }

    @FXML
    private void onNewInterviewAction() throws IOException, SQLException {

        showAddInterviewDlg();

        String fio = fioFilter.getText();
        String post = postFilter.getText();
        String date = dateFilter.getText();

        interviews.clear();
        interviews.addAll(HelperFactory.getHelper().getInterviewsByCandidateFioAndDateAndPost(fio, post, date));
    }

    @FXML
    private void onAddInterviewerAction() throws IOException, SQLException {
        showEditCategoryDlg();
    }

    @FXML
    private void onFilter() throws SQLException {
        String fio = fioFilter.getText();
        String post = postFilter.getText();
        String date = dateFilter.getText();

        interviews.clear();
        interviews.addAll(HelperFactory.getHelper().getInterviewsByCandidateFioAndDateAndPost(fio, post, date));
    }

    private void onDeleteInterview() throws SQLException {
        Interview selectedInterview = mainTable.getSelectionModel().getSelectedItem();
        if (selectedInterview != null) {
            int selectedInterviewId = selectedInterview.getIdInterview();
            HelperFactory.getHelper().delInterviewById(selectedInterviewId);
            interviews.remove(selectedInterview);
        }
    }

    private void onEditInterview() throws IOException, SQLException {
        // получаем информацию о выделенном собеседовании
        Interview selectedInterview = mainTable.getSelectionModel().getSelectedItem();
        int selectedInterviewId = selectedInterview.getIdInterview();
        int interviewPosition = mainTable.getSelectionModel().getSelectedIndex();
        // вызываем диалог редактирования
        showEditInterviewDlg(selectedInterviewId);
        // получаем из БД обновленное собеседование, удаляем старое из таблицы и вставляем новое
        Interview refreshedInterview = HelperFactory.getHelper().getInterviewById(selectedInterviewId);
        interviews.remove(selectedInterview);
        interviews.add(interviewPosition, refreshedInterview);
    }

    private void showAddInterviewDlg() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getClassLoader().getResource(AppConfig.FXML_ADD_INTERVIEW_DLG_URL);
        fxmlLoader.setLocation(url);
        VBox node = (VBox) fxmlLoader.load();
        AddInterviewController addInterviewController = fxmlLoader.getController();
        addInterviewDlg = node;
        Scene scene = new Scene(addInterviewDlg, 630, 500);
        dlgStage = new Stage();
        dlgStage.setScene(scene);
        dlgStage.setMinHeight(500);
        dlgStage.setMinWidth(630);
        dlgStage.initModality(Modality.APPLICATION_MODAL);
        dlgStage.initOwner(primaryStage);
        dlgStage.setResizable(false);
        dlgStage.setTitle(ConstantManager.ADD_INTERVIEW_TITLE);
        addInterviewController.addInterview();
        addInterviewController.init(dlgStage);
        dlgStage.showAndWait();
    }

    private void showEditInterviewDlg(int selectedInterviewId) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getClassLoader().getResource(AppConfig.FXML_ADD_INTERVIEW_DLG_URL);
        fxmlLoader.setLocation(url);
        VBox node = null;
        node = (VBox) fxmlLoader.load();
        AddInterviewController addInterviewController = fxmlLoader.getController();
        addInterviewDlg = node;
        Scene scene = new Scene(addInterviewDlg, 630, 500);
        dlgStage = new Stage();
        dlgStage.setScene(scene);
        dlgStage.setMinHeight(500);
        dlgStage.setMinWidth(630);
        dlgStage.initModality(Modality.APPLICATION_MODAL);
        dlgStage.initOwner(primaryStage);
        dlgStage.setResizable(false);
        dlgStage.setTitle(ConstantManager.EDIT_INTERVIEW_TITLE);
        addInterviewController.editInterview(selectedInterviewId);
        addInterviewController.init(dlgStage);
        dlgStage.showAndWait();
    }

    private void showEditCategoryDlg() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getClassLoader().getResource(AppConfig.FXML_EDIT_CATEGORY_DLG_URL);
        fxmlLoader.setLocation(url);
        VBox node = null;
        node = (VBox) fxmlLoader.load();
        EditCategoriesController editCategoryController = fxmlLoader.getController();
        editCategoryDlg = node;
        Scene scene = new Scene(editCategoryDlg, 300, 300);
        dlgStage = new Stage();
        dlgStage.setScene(scene);
        dlgStage.setMinHeight(300);
        dlgStage.setMinWidth(300);
        dlgStage.initModality(Modality.APPLICATION_MODAL);
        dlgStage.initOwner(primaryStage);
        dlgStage.setResizable(false);
        dlgStage.setTitle(ConstantManager.EDIT_CATEGORY_TITLE);
        editCategoryController.init(dlgStage);
        dlgStage.showAndWait();
    }

}
