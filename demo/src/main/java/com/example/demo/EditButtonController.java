package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditButtonController {

    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField positionField;
    @FXML
    private ComboBox<String> departmentComboBox;
    @FXML
    private TextField salaryField;
    @FXML
    private TextField contactInfoField;
    @FXML
    private TextField contractExpiryField;
    @FXML
    private ComboBox<String> statusComboBox;

    private final Connection connection = DBConnection.getConnection();
    private int employeeId;

    @FXML
    private void initialize() {
        populateDepartmentComboBox(); // Populate the department combo box on view load
    }

    // Setter to receive employee_id
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
        loadEmployeeDetails();
    }

    // Load employee details from the database
    private void loadEmployeeDetails() {
        String query = "SELECT name, gender, position, department_id, salary, contact_info, contract_expiry, employment_status " +
                "FROM employees WHERE employee_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nameField.setText(resultSet.getString("name"));
                genderComboBox.setValue(resultSet.getString("gender"));
                positionField.setText(resultSet.getString("position"));
                departmentComboBox.setValue(getDepartmentName(resultSet.getInt("department_id")));
                salaryField.setText(String.valueOf(resultSet.getDouble("salary")));
                contactInfoField.setText(resultSet.getString("contact_info"));
                contractExpiryField.setText(resultSet.getString("contract_expiry"));
                statusComboBox.setValue(resultSet.getString("employment_status"));
            } else {
                showAlert("Error", "Employee not found in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to load employee details.");
        }
    }

    // Populate the department ComboBox with department names
    public void populateDepartmentComboBox() {
        String query = "SELECT department_name FROM departments";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                departmentComboBox.getItems().add(resultSet.getString("department_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to load department names.");
        }
    }


    // Handles saving the updated employee details
    @FXML
    private void handleSave() {
        String updateQuery = "UPDATE employees SET name = ?, gender = ?, position = ?, department_id = ?, salary = ?, " +
                "contact_info = ?, contract_expiry = ?, employment_status = ? WHERE employee_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setString(2, genderComboBox.getValue());
            preparedStatement.setString(3, positionField.getText());
            preparedStatement.setInt(4, getDepartmentId(departmentComboBox.getValue()));
            preparedStatement.setDouble(5, Double.parseDouble(salaryField.getText()));
            preparedStatement.setString(6, contactInfoField.getText());
            preparedStatement.setString(7, contractExpiryField.getText());
            preparedStatement.setString(8, statusComboBox.getValue());
            preparedStatement.setInt(9, employeeId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Employee updated successfully.");
            } else {
                showAlert("Error", "Failed to update employee details.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to save employee details.");
        }
    }

    // Utility to get department_id from department_name
    private int getDepartmentId(String departmentName) {
        String query = "SELECT department_id FROM departments WHERE department_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, departmentName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("department_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Get department name from department_id (optional utility function)
    private String getDepartmentName(int departmentId) {
        String query = "SELECT department_name FROM departments WHERE department_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, departmentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("department_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Show alert for messages
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goBack() {
        try {
            // Load the EmployeeManagementView
            Parent employeeManagementView = FXMLLoader.load(getClass().getResource("EmployeeManagementView.fxml"));

            // Get the current stage from any UI element in the current view
            Stage stage = (Stage) nameField.getScene().getWindow();

            // Set the new scene and update the title
            stage.setScene(new Scene(employeeManagementView));
            stage.setTitle("Employee Management System - Employee Management");
            stage.centerOnScreen(); // Ensure the stage is centered
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Unable to go back to Employee Management View.");
        }
    }

}