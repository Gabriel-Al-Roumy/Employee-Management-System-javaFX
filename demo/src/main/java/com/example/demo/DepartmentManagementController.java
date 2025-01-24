package com.example.demo;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentManagementController {

    @FXML
    private Button viewButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Department> departmentTable;

    @FXML
    private TableColumn<Department, Integer> departmentIdColumn;

    @FXML
    private TableColumn<Department, String> departmentNameColumn;

    @FXML
    private TableColumn<Department, Integer> numEmployeesColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private ObservableList<Department> departmentList = FXCollections.observableArrayList();

    private final Connection connection = DBConnection.getConnection();

    @FXML
    public void initialize() {
        configureTableColumns();
        loadDepartmentData();
        setupRowSelectionListener();
    }

    // ========================= Initialization Methods =========================

    private void configureTableColumns() {
        departmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        departmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        numEmployeesColumn.setCellValueFactory(param -> {
            Department department = param.getValue();
            int numEmployees = getNumEmployeesForDepartment(department);
            return new ReadOnlyObjectWrapper<>(numEmployees);
        });
    }

    private void setupRowSelectionListener() {
        departmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isRowSelected = newSelection != null;
            viewButton.setDisable(!isRowSelected);
            deleteButton.setDisable(!isRowSelected);
        });
    }

    private void loadDepartmentData() {
        departmentList.clear();
        String query = "SELECT department_id, department_name FROM departments";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Department department = new Department(
                        resultSet.getInt("department_id"),
                        resultSet.getString("department_name")
                );
                departmentList.add(department);
            }

            departmentTable.setItems(departmentList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load department data.");
        }
    }

    // ========================= Event Handlers =========================

    @FXML
    private void handleSearch(KeyEvent event) {
        String searchTerm = searchField.getText().toLowerCase();

        ObservableList<Department> filteredList = FXCollections.observableArrayList();
        for (Department department : departmentList) {
            if (department.getDepartmentName().toLowerCase().contains(searchTerm)) {
                filteredList.add(department);
            }
        }

        departmentTable.setItems(filteredList);

        boolean isEmpty = filteredList.isEmpty();
        viewButton.setDisable(isEmpty);
        deleteButton.setDisable(isEmpty);
    }

    @FXML
    private void handleClear(ActionEvent event) {
        searchField.clear();
        departmentTable.setItems(departmentList);

        boolean isEmpty = departmentList.isEmpty();
        viewButton.setDisable(isEmpty);
        deleteButton.setDisable(isEmpty);
    }

    @FXML
    private void handleDelete() {
        Department selected = departmentTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            int numEmployees = getNumEmployeesForDepartment(selected);
            if (numEmployees > 0) {
                showAlert(Alert.AlertType.ERROR, "Cannot Delete Department", "The department cannot be deleted because it still has employees assigned to it.");
                return;
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Are you sure you want to delete the selected department?");
            confirmationAlert.setContentText("This action cannot be undone.");

            if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                String deleteQuery = "DELETE FROM departments WHERE department_id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                    preparedStatement.setInt(1, selected.getDepartmentId());
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "The department has been successfully deleted.");
                        loadDepartmentData();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Unable to delete the selected department.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the department.");
                }
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a department to delete.");
        }
    }

    @FXML
    private void handleView(ActionEvent event) {
        Department selected = departmentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewDepartmentView.fxml"));
                Parent viewView = loader.load();

                ViewDepartmentController controller = loader.getController();
                controller.setDepartmentId(selected.getDepartmentId());

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(viewView));
                stage.setTitle("Employee Management System - Department Management - View Department");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the View Department screen.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a department to view.");
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent dashboard = FXMLLoader.load(getClass().getResource("DashboardView.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboard));
            stage.setTitle("Employee Management System - Dashboard");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load Dashboard view.");
        }
    }

    @FXML
    private void handleAdd(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddDepartment.fxml"));
            Parent addDepartmentView = loader.load();

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(addDepartmentView));
            stage.setTitle("Employee Management System - Department Management - Add Department");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load Add Department view.");
        }
    }

    // ========================= Utility Methods =========================

    private int getNumEmployeesForDepartment(Department department) {
        String query = "SELECT COUNT(*) AS num_employees FROM employees WHERE department_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, department.getDepartmentId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("num_employees");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to retrieve employee count for department.");
        }
        return 0;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
