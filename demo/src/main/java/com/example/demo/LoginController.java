package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.sql.Connection;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("admin")) {
            // Navigate to the next scene (e.g., Dashboard)
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                try {
                    Parent dashboard = FXMLLoader.load(getClass().getResource("DashboardView.fxml"));
                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(dashboard));
                    stage.setTitle("Employee Management System - Dashboard");
                    stage.centerOnScreen();
                } catch (Exception e) {
                    e.printStackTrace();
              }
            } else {
                errorLabel.setText("Database connection failed. Please try again.");
            }
        }
                else {
            errorLabel.setText("Invalid username or password!");

        }

    }

    public void handleExit(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close(); // Close the application
    }

}
