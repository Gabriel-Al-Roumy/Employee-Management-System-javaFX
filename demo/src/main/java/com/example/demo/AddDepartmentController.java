package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddDepartmentController {

    private final Connection connection = DBConnection.getConnection(); // Connection defined at the top

    @FXML
    private TextField departmentNameField;

    @FXML
    private Button backButton;

    @FXML
    private Button saveButton;

    // Save department to the database
    @FXML
    private void handleSave(ActionEvent event) {
        String departmentName = departmentNameField.getText().trim();

        if (departmentName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Department name cannot be empty.");
            return;
        }

        String checkQuery = "SELECT COUNT(*) FROM departments WHERE department_name = ?";
        String insertQuery = "INSERT INTO departments (department_name) VALUES (?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            // Check if the department name already exists
            checkStmt.setString(1, departmentName);
            try (ResultSet resultSet = checkStmt.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    showAlert(Alert.AlertType.ERROR, "Duplicate Department", "A department with this name already exists.");
                    return;
                }
            }

            // Insert the new department
            insertStmt.setString(1, departmentName);
            int rowsInserted = insertStmt.executeUpdate();

            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Department added successfully.");
                goBack(event); // Navigate back to DepartmentManagementView
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Unable to add the department. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the department.");
        }
    }

    // Navigate back to DepartmentManagementView
    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("DepartmentManagementView.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Department Management");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load Department Management view.");
        }
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
