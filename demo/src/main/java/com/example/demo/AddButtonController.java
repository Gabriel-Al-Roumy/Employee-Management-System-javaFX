package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddButtonController {

    @FXML private TextField nameField;
    @FXML private TextField positionField;
    @FXML private ComboBox<String> departmentComboBox;
    @FXML private TextField salaryField;
    @FXML private TextField contactInfoField;
    @FXML private TextField dojField;
    @FXML private TextField contractExpiryField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<String> genderComboBox;

    private final Connection connection = DBConnection.getConnection(); // Assuming connection is already established

    // Load departments from the database and populate the ComboBox
    public void initialize() {
        ObservableList<String> departments = FXCollections.observableArrayList();
        String query = "SELECT department_name FROM departments";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                departments.add(rs.getString("department_name"));
            }
            departmentComboBox.setItems(departments);

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to load departments: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        // Get the data from the form
        String name = nameField.getText();
        String position = positionField.getText();
        String department = departmentComboBox.getValue();
        String salaryText = salaryField.getText();
        String contactInfo = contactInfoField.getText();
        String doj = dojField.getText();  // Keep as text field
        String contractExpiry = contractExpiryField.getText();  // Keep as text field
        String employmentStatus = statusComboBox.getValue();
        String gender = genderComboBox.getValue();

        // Validate that all required fields are filled
        if (name.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "Please provide a name.");
            return;
        }
        if (position.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "Please provide a position.");
            return;
        }
        if (department == null || department.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "Please select a department.");
            return;
        }
        if (salaryText.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "Please provide a salary.");
            return;
        }
        if (contactInfo.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "Please provide contact information.");
            return;
        }
        if (doj.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "Please provide a date of joining.");
            return;
        }
        if (contractExpiry.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "Please provide a contract expiry date.");
            return;
        }
        if (employmentStatus == null || employmentStatus.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "Please select an employment status.");
            return;
        }
        if (gender == null || gender.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "Please select a gender.");
            return;
        }

        // Try to parse the salary and handle invalid number input
        double salary;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Input Error", "Please provide a valid salary (numeric value).");
            return;
        }

        // Insert the data into the database
        String query = "INSERT INTO employees (name, position, department_id, salary, contact_info, date_of_joining, contract_expiry, employment_status, gender) "
                + "VALUES (?, ?, (SELECT department_id FROM departments WHERE department_name = ?), ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, position);
            stmt.setString(3, department);
            stmt.setDouble(4, salary);
            stmt.setString(5, contactInfo);
            stmt.setString(6, doj);
            stmt.setString(7, contractExpiry);
            stmt.setString(8, employmentStatus);
            stmt.setString(9, gender);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(AlertType.INFORMATION, "Employee Added", "Employee added successfully!");
                handlePostSaveAction(event);  // Ask if they want to add another or return
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to add employee: " + e.getMessage());
        }
    }


    @FXML
    private void handleClear(ActionEvent event) {
        // Clear all fields
        nameField.clear();
        positionField.clear();
        departmentComboBox.getSelectionModel().clearSelection();
        salaryField.clear();
        contactInfoField.clear();
        dojField.clear();
        contractExpiryField.clear();
        statusComboBox.getSelectionModel().clearSelection();
        genderComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            // Load the EmployeeManagementView
            Parent employeeManagementView = FXMLLoader.load(getClass().getResource("EmployeeManagementView.fxml"));

            // Get the current stage from any UI element in the current view
            Stage stage = (Stage) nameField.getScene().getWindow();

            // Set the new scene and update the title
            stage.setScene(new Scene(employeeManagementView));
            stage.setTitle("Employee Management System - Employees");
            stage.centerOnScreen(); // Ensure the stage is centered
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Unable to go back to Employee Management View.");
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Post save action method
    private void handlePostSaveAction(ActionEvent event) {
        boolean addAnother = confirmAction("Employee Added", "Do you want to add another employee?");
        if (addAnother) {
            handleClear(null);  // Clear fields to add a new employee
        } else {
            goBack(event);  // Go back to Employee Management view
        }
    }

    // Confirm action for add another employee or return
    private boolean confirmAction(String title, String message) {
        // Create a confirmation alert with custom buttons
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Replace default buttons with 'Yes' and 'No'
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the alert and return true if the 'Yes' button is clicked, false if 'No' is clicked
        return alert.showAndWait().filter(response -> response == yesButton).isPresent();
    }

}
