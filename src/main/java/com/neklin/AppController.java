package com.neklin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Timestamp;

public class AppController {
    private final DatabaseController dbController = new DatabaseController();
    private final ObservableList<TaskGetter> taskList = FXCollections.observableArrayList();

    @FXML
    private Label tip;

    @FXML
    private TextField taskField;

    @FXML
    private TableView<TaskGetter> taskTable;

    @FXML
    private TableColumn<TaskGetter, Integer> idColumn;

    @FXML
    private TableColumn<TaskGetter, String> nameColumn;

    @FXML
    private TableColumn<TaskGetter, Timestamp> dateColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        refreshTable();

        tip.setText("?");
        tip.setOnMouseEntered(event -> {
            tip.setText("\uD83D\uDD0Dleft-click on a task to delete");
            tip.setStyle("-fx-font-size: 12px");
        });

        tip.setOnMouseExited(event -> {
            tip.setText("\uD83D\uDD0E?");
            tip.setStyle("-fx-font-size: 14px");
        });

        taskField.setOnAction(event -> {
            String userInput = taskField.getText();
            dbController.addProduct(userInput);
            refreshTable();
            taskField.clear();
        });

        taskTable.setOnMouseClicked(event -> {
            TaskGetter selected = taskTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                deleteTask(selected);
            }
        });
    }

    private void refreshTable() {
        taskList.clear();
        taskList.addAll(dbController.getAllTasks());
        taskTable.setItems(taskList);
    }

    private void deleteTask(TaskGetter task) {
        if (dbController.deleteTask(task.getId())) {
        refreshTable();
        System.out.println("✅ Задача удалена!");
        } else {
            System.out.println("❌ Ошибка при удалении!");
        }
    }
}
