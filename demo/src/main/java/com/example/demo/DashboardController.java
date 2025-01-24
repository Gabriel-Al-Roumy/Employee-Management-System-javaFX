package com.example.demo;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardController {

    @FXML
    private Label totalEmployeesLabel, totalDepartmentsLabel;

    @FXML
    private Label upcomingEventsLabel;

    private final Connection connection = DBConnection.getConnection();

    // Initialize dashboard data
    @FXML
    public void initialize() {
        if (connection != null) {
            displayTotalEmployees();
            displayUpcomingEvents();
            displayTotalDepartments();
        } else {
            System.out.println("Database connection is not available.");
        }
    }

    private void displayTotalEmployees() {
        String query = "SELECT COUNT(*) AS total FROM employees";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int totalEmployees = rs.getInt("total");
                totalEmployeesLabel.setText(String.valueOf(totalEmployees));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void displayTotalDepartments() {
        String query = "SELECT COUNT(*) AS total FROM departments";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int totalDepartments = rs.getInt("total");
                totalDepartmentsLabel.setText(String.valueOf(totalDepartments));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayUpcomingEvents() {
        String query = "SELECT COUNT(*) AS upcoming FROM employees WHERE contract_expiry BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 1 YEAR)";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int upcomingContracts = rs.getInt("upcoming");
                upcomingEventsLabel.setText(upcomingContracts + " Contract Renewals");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Navigation to Employee Management
    @FXML
    private void goToEmployeeManagement(ActionEvent event) {
        navigateTo(event, "EmployeeManagementView.fxml", "Employee Management System - Employee Management");
    }

    // Navigation to Reports
    @FXML
    private void goToReports(ActionEvent event) {
        navigateTo(event, "ReportsView.fxml", "Employee Management System - Reports");
    }

    // Generalized navigation method
    private void navigateTo(ActionEvent event, String fxmlFile, String Title) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(view));
            stage.setTitle(Title);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleLogout(ActionEvent actionEvent) {
        navigateTo(actionEvent, "LoginView.fxml", "Employee Management System - Login");
    }

    public void goToDepartmentsView(ActionEvent actionEvent) {
        navigateTo(actionEvent, "DepartmentManagementView.fxml", "Employee Management System - Department Management");

    }
}

