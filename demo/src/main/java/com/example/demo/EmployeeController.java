package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.Node;

public class EmployeeController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, String> idColumn;

    @FXML
    private TableColumn<Employee, String> nameColumn;

    @FXML
    private TableColumn<Employee, String> positionColumn;

    @FXML
    private TableColumn<Employee, String> departmentColumn;

    @FXML
    private TableColumn<Employee, String> contractExpiryColumn;

    @FXML
    private Button addButton, editButton, deleteButton, viewProfileButton;

    @FXML
    private ComboBox<String> searchComboBox;

    private final Connection connection = DBConnection.getConnection();
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set default value for ComboBox
        searchComboBox.setValue("Name");  // Default search by Name

        // Initialize columns
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeId().toString()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        positionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosition()));
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        contractExpiryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContractExpiry()));

        loadEmployeeData();

        // Listeners for the reactive search
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                handleSearch();  // Trigger search when text changes
            } else {
                loadEmployeeData();  // Reset to all data if the search field is empty
            }
        });

        // Handle ComboBox value changes
        searchComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (searchField.getText() != null && !searchField.getText().isEmpty()) {
                handleSearch();  // Trigger search when ComboBox value changes
            }
        });

        // Disable buttons if no row is selected
        employeeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean isRowSelected = newValue != null;
            editButton.setDisable(!isRowSelected);
            deleteButton.setDisable(!isRowSelected);
            viewProfileButton.setDisable(!isRowSelected);
        });
    }

    private void loadEmployeeData() {
        String query = "SELECT e.employee_id, e.name, e.position, d.department_name, e.contract_expiry " +
                "FROM employees e JOIN departments d ON e.department_id = d.department_id ORDER BY e.employee_id";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            employeeList.clear();
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getString("department_name"),
                        rs.getString("contract_expiry")
                );
                employeeList.add(employee);
            }
            employeeTable.setItems(employeeList);
        } catch (SQLException e) {
            showErrorDialog("Database Error", "Error loading employee data from the database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText();
        String searchBy = searchComboBox.getValue();

        if (searchBy == null || searchBy.isEmpty()) {
            // Set default search to "Name" if ComboBox is null
            searchBy = "Name";
        }

        // If search field is empty, reload all employee data
        if (searchTerm.isEmpty()) {
            loadEmployeeData();
            return;  // No need to search if field is empty
        }

        String query = "SELECT e.employee_id, e.name, e.position, d.department_name, e.contract_expiry " +
                "FROM employees e JOIN departments d ON e.department_id = d.department_id WHERE ";

        switch (searchBy) {
            case "Name":
                query += "LOWER(e.name) LIKE LOWER(?)";
                break;
            case "Position":
                query += "LOWER(e.position) LIKE LOWER(?)";
                break;
            case "Department":
                query += "LOWER(d.department_name) LIKE LOWER(?)";
                break;
            case "ID":
                query += "e.employee_id = ?";
                break;
            default:
                return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            if (searchBy.equals("ID")) {
                try {
                    // Parse the search term as an integer when searching by ID
                    stmt.setInt(1, Integer.parseInt(searchTerm));
                } catch (NumberFormatException e) {
                    // If the search term is not a valid integer, display nothing
                    employeeList.clear();
                    employeeTable.setItems(employeeList);
                    return;
                }
            } else {
                // For textual search fields
                stmt.setString(1, "%" + searchTerm + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                employeeList.clear();
                while (rs.next()) {
                    Employee employee = new Employee(
                            rs.getInt("employee_id"),
                            rs.getString("name"),
                            rs.getString("position"),
                            rs.getString("department_name"),
                            rs.getString("contract_expiry")
                    );
                    employeeList.add(employee);
                }
                employeeTable.setItems(employeeList);
            }
        } catch (SQLException e) {
            showErrorDialog("Database Error", "Error executing search query.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        searchField.clear();
        searchComboBox.setValue("Name");  // Reset ComboBox to default value
        loadEmployeeData();
        // Re-disable buttons when selection is cleared
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        viewProfileButton.setDisable(true);
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            Parent addView = FXMLLoader.load(getClass().getResource("AddEmployeeView.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(addView));
            stage.setTitle("Employee Management System - Employee Management - Add Employee");
        } catch (Exception e) {
            showErrorDialog("View Load Error", "Error loading the Add Employee view.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        // Get the selected employee from the TableView
        try {
            // Load the EditButtonView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditButtonView.fxml"));
            Parent root = loader.load();

            // Get the controller for EditButtonView
            EditButtonController controller = loader.getController();

            // Pass the employee_id of the selected employee
            Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
            controller.setEmployeeId(selectedEmployee.getEmployeeId());

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Update the scene with the new view
            stage.setScene(new Scene(root));
            stage.setTitle("Employee Management System - Employee Management - Edit Employee");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Optionally clear the form or selection in the current view
        handleClear(null);
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            String query = "DELETE FROM employees WHERE employee_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, selectedEmployee.getEmployeeId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    employeeList.remove(selectedEmployee);
                    employeeTable.refresh();
                    // Re-disable buttons after deletion
                    editButton.setDisable(true);
                    deleteButton.setDisable(true);
                    viewProfileButton.setDisable(true);
                }
            } catch (SQLException e) {
                showErrorDialog("Database Error", "Error deleting the employee record.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleViewProfile(ActionEvent event) {
        // Get the selected employee from the TableView
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            try {
                // Load the ViewProfileView FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewProfileView.fxml"));
                Parent root = loader.load();

                // Get the controller for ViewProfileView
                ViewProfileController controller = loader.getController();

                // Pass the employee_id of the selected employee to the controller
                controller.setEmployeeId(selectedEmployee.getEmployeeId());

                // Get the current stage
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Update the scene with the new view
                stage.setScene(new Scene(root));
                stage.setTitle("Employee Management System - Employee Management - View Employee Profile");

            } catch (IOException e) {
                showErrorDialog("View Load Error", "Error loading the View Profile view.");
                e.printStackTrace();
            }
        }
    }


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

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
