package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewProfileController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label positionLabel;
    @FXML
    private Label departmentLabel;
    @FXML
    private Label salaryLabel;
    @FXML
    private Label contactInfoLabel;
    @FXML
    private Label dojLabel;
    @FXML
    private Label contractExpiryLabel;
    @FXML
    private Label statusLabel;

    private final Connection connection = DBConnection.getConnection();
    private int employeeId;

    // Method to set employee ID and fetch details from the database
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
        loadEmployeeDetails();
    }

    // Method to load employee details from the database using employeeId
    private void loadEmployeeDetails() {
        String query = "SELECT e.name, e.gender, e.position, d.department_name, e.salary, e.contact_info, e.date_of_joining, e.contract_expiry, e.employment_status " +
                "FROM employees e JOIN departments d ON e.department_id = d.department_id WHERE e.employee_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nameLabel.setText(rs.getString("name"));
                    genderLabel.setText(rs.getString("gender"));
                    positionLabel.setText(rs.getString("position"));
                    departmentLabel.setText(rs.getString("department_name"));
                    salaryLabel.setText(String.format("$%,.2f", rs.getDouble("salary")));
                    contactInfoLabel.setText(rs.getString("contact_info"));
                    dojLabel.setText(rs.getDate("date_of_joining").toString());
                    contractExpiryLabel.setText(rs.getString("contract_expiry"));
                    statusLabel.setText(rs.getString("employment_status"));
                }
            }
        } catch (SQLException e) {
            showErrorDialog("Database Error", "Error loading employee details.");
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent dashboard = FXMLLoader.load(getClass().getResource("EmployeeManagementView.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboard));
            stage.setTitle("Employee Management System - Employee Management");
        } catch (IOException e) {
            showErrorDialog("View Load Error", "Error loading the Dashboard view.");
            e.printStackTrace();
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
