package controller;

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

    private ObservableList<Interview> interviews = FXCollections.observableArrayList();

    private VBox addInterviewDlg;

    private VBox addInterviewerDlg;

    private VBox addCandidateDlg;

    private Stage primaryStage;

    private Stage dlgStage;

    private AddInterviewController addInterviewController;

    private AddInterviewerController addInterviewerController;

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

    // инициализируем форму данными
    @FXML
    private void initialize() throws SQLException {
        mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    try {

                        onMouseClicked();
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
    private void onMouseClicked() throws IOException, SQLException {
        //System.out.print("hello!");
        int selectedInterviewId = mainTable.getSelectionModel().getSelectedItem().getIdInterview();
        showEditInterviewDlg(selectedInterviewId);

        String fio = fioFilter.getText();
        String post = postFilter.getText();
        String date = dateFilter.getText();

        interviews.clear();
        interviews.addAll(HelperFactory.getHelper().getInterviewsByCandidateFioAndDateAndPost(fio, post, date));
        mainTable.getItems().removeAll();
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
        mainTable.getItems().removeAll();
        mainTable.setItems(interviews);
    }

    @FXML
    private void onAddInterviewerAction() throws IOException, SQLException {
        showAddInterviewerDlg();
    }

    @FXML
    private void onFilter() throws SQLException {
        String fio = fioFilter.getText();
        String post = postFilter.getText();
        String date = dateFilter.getText();
        interviews.clear();
        interviews.addAll(HelperFactory.getHelper().getInterviewsByCandidateFioAndDateAndPost(fio, post, date));
        mainTable.getItems().removeAll();
        mainTable.setItems(interviews);
    }

    private void showAddInterviewDlg() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getClassLoader().getResource(ConstantManager.FXML_ADD_INTERVIEW_DLG);
        fxmlLoader.setLocation(url);
        VBox node = null;
        node = (VBox) fxmlLoader.load();
        addInterviewController = fxmlLoader.getController();
        addInterviewDlg = node;
        Scene scene = new Scene(addInterviewDlg, 630, 500);
        dlgStage = new Stage();
        dlgStage.setScene(scene);
        dlgStage.setMinHeight(500);
        dlgStage.setMinWidth(630);
        dlgStage.initModality(Modality.APPLICATION_MODAL);
        dlgStage.initOwner(primaryStage);
        dlgStage.setResizable(false);
        dlgStage.setTitle("Добавление собеседования");
        addInterviewController.addInterview();
        addInterviewController.init(dlgStage);
        dlgStage.showAndWait();
    }

    private void showEditInterviewDlg(int selectedInterviewId) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getClassLoader().getResource(ConstantManager.FXML_ADD_INTERVIEW_DLG);
        fxmlLoader.setLocation(url);
        VBox node = null;
        node = (VBox) fxmlLoader.load();
        addInterviewController = fxmlLoader.getController();
        addInterviewDlg = node;
        Scene scene = new Scene(addInterviewDlg, 630, 500);
        dlgStage = new Stage();
        dlgStage.setScene(scene);
        dlgStage.setMinHeight(500);
        dlgStage.setMinWidth(630);
        dlgStage.initModality(Modality.APPLICATION_MODAL);
        dlgStage.initOwner(primaryStage);
        dlgStage.setResizable(false);
        dlgStage.setTitle("Редактирование собеседования");
        addInterviewController.editInterview(selectedInterviewId);
        addInterviewController.init(dlgStage);
        dlgStage.showAndWait();
    }

    private void showAddInterviewerDlg() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getClassLoader().getResource(ConstantManager.FXML_ADD_INTERVIEWER_DLG);
        fxmlLoader.setLocation(url);
        VBox node = null;
        node = (VBox) fxmlLoader.load();
        addInterviewerController = fxmlLoader.getController();
        addInterviewerDlg = node;
        Scene scene = new Scene(addInterviewerDlg, 480, 120);
        dlgStage = new Stage();
        dlgStage.setScene(scene);
        dlgStage.setMinHeight(120);
        dlgStage.setMinWidth(480);
        dlgStage.initModality(Modality.APPLICATION_MODAL);
        dlgStage.initOwner(primaryStage);
        dlgStage.setResizable(false);
        dlgStage.setTitle("Добавление собеседователя");
        addInterviewerController.init(dlgStage);
        dlgStage.showAndWait();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
