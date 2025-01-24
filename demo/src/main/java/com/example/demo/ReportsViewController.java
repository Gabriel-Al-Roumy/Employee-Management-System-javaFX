package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportsViewController {

    @FXML
    private TextArea reportDisplayArea;

    private final Connection connection = DBConnection.getConnection();

    // Generate Salary Report
    @FXML
    private void generateSalaryReport() {
        String query = "SELECT d.department_name, AVG(e.salary) AS average_salary, SUM(e.salary) AS total_salary " +
                "FROM employees e " +
                "JOIN departments d ON e.department_id = d.department_id " +
                "GROUP BY d.department_name";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder report = new StringBuilder();
            report.append("Salary Report\n\n");
            while (rs.next()) {
                String departmentName = rs.getString("department_name");
                double averageSalary = rs.getDouble("average_salary");
                double totalSalary = rs.getDouble("total_salary");
                report.append(String.format("Department: %s\n", departmentName))
                        .append(String.format("  - Average Salary: %.2f\n", averageSalary))
                        .append(String.format("  - Total Salary: %.2f\n", totalSalary))
                        .append("\n");
            }
            reportDisplayArea.setText(report.toString());

        } catch (Exception e) {
            showErrorDialog("Error", "Error generating salary report: " + e.getMessage());
        }
    }

    // Generate Contract Expiry Report
    @FXML
    private void generateContractExpiryReport() {
        String query = "SELECT name, contract_expiry FROM employees WHERE contract_expiry BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 1 YEAR)";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder report = new StringBuilder();
            report.append("Contract Expiry Report\n\n");
            while (rs.next()) {
                String name = rs.getString("name");
                String contractExpiry = rs.getString("contract_expiry");
                report.append(String.format("Employee: %s\n", name))
                        .append(String.format("  - Contract Expiry: %s\n", contractExpiry))
                        .append("\n");
            }
            reportDisplayArea.setText(report.toString());

        } catch (Exception e) {
            showErrorDialog("Error", "Error generating contract expiry report: " + e.getMessage());
        }
    }

    // Generate Employment Status Report
    @FXML
    private void generateEmploymentStatusReport() {
        String query = "SELECT employment_status, COUNT(*) AS count FROM employees GROUP BY employment_status";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder report = new StringBuilder();
            report.append("Employment Status Report\n\n");
            while (rs.next()) {
                String status = rs.getString("employment_status");
                int count = rs.getInt("count");
                report.append(String.format("Status: %s\n", status))
                        .append(String.format("  - Count: %d\n", count))
                        .append("\n");
            }
            reportDisplayArea.setText(report.toString());

        } catch (Exception e) {
            showErrorDialog("Error", "Error generating employment status report: " + e.getMessage());
        }
    }

    // Generate Gender Diversity Report (per department)
    @FXML
    private void generateGenderDiversityReport() {
        String query = "SELECT d.department_name, e.gender, COUNT(*) AS count " +
                "FROM employees e " +
                "JOIN departments d ON e.department_id = d.department_id " +
                "GROUP BY d.department_name, e.gender";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder report = new StringBuilder();
            report.append("Gender Diversity Report\n\n");
            while (rs.next()) {
                String departmentName = rs.getString("department_name");
                String gender = rs.getString("gender");
                int count = rs.getInt("count");
                report.append(String.format("Department: %s\n", departmentName))
                        .append(String.format("  - Gender: %s\n", gender))
                        .append(String.format("    Count: %d\n", count))
                        .append("\n");
            }
            reportDisplayArea.setText(report.toString());

        } catch (Exception e) {
            showErrorDialog("Error", "Error generating gender diversity report: " + e.getMessage());
        }
    }

    // Back Button Logic
    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent dashboard = FXMLLoader.load(getClass().getResource("DashboardView.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboard));
            stage.setTitle("Employee Management System - Dashboard");
        } catch (Exception e) {
            showErrorDialog("View Load Error", "Error loading the Dashboard view.");
            e.printStackTrace();
        }
    }

    // Error dialog method
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
