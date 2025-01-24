package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewDepartmentController {

    @FXML
    private Label departmentNameLabel;
    @FXML
    private Label employeeCountLabel;
    @FXML
    private TableView<SimplifiedEmployee> employeeTable;
    @FXML
    private TableColumn<SimplifiedEmployee, String> employeeNameColumn;
    @FXML
    private TableColumn<SimplifiedEmployee, Integer> employeeSalaryColumn;

    private final Connection connection = DBConnection.getConnection();
    private int departmentId;

    private final ObservableList<SimplifiedEmployee> employeeList = FXCollections.observableArrayList();

    // Method to set the department ID passed from the previous screen
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
        loadDepartmentDetails();
        loadEmployeeData();
    }

    // Method to load department details (name, employee count)
    private void loadDepartmentDetails() {
        String departmentQuery = "SELECT department_name FROM departments WHERE department_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(departmentQuery)) {
            statement.setInt(1, departmentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                departmentNameLabel.setText(resultSet.getString("department_name"));
                loadEmployeeCount();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Unable to load department details.");
        }
    }

    // Method to load the number of employees in the department
    private void loadEmployeeCount() {
        String countQuery = "SELECT COUNT(*) AS employee_count FROM employees WHERE department_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(countQuery)) {
            statement.setInt(1, departmentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int employeeCount = resultSet.getInt("employee_count");
                employeeCountLabel.setText(employeeCount + " employees");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Unable to load employee count.");
        }
    }

    // Method to load employee data into the TableView
    private void loadEmployeeData() {
        employeeList.clear();
        String employeeQuery = "SELECT employee_id, name, salary FROM employees WHERE department_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(employeeQuery)) {
            statement.setInt(1, departmentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SimplifiedEmployee employee = new SimplifiedEmployee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("salary")
                );
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Unable to load employee data.");
        }

        // Set up the TableView columns
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeeSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));

        // Populate the TableView with the data
        employeeTable.setItems(employeeList);
    }

    // Placeholder method for showing error alerts
    private void showErrorAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to handle the back button click
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent departmentManagementView = FXMLLoader.load(getClass().getResource("DepartmentManagementView.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(departmentManagementView));
            stage.setTitle("Employee Management System - Department Management");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
