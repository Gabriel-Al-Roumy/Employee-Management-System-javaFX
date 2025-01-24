# Employee-Management-System-javaFX
A JavaFX-based Employee Management System with MySQL integration, supporting department and employee management, built for efficient and user-friendly operations"
# Employee Management System

## Overview
The **Employee Management System** is a desktop application built with **JavaFX** and **MySQL**, designed to simplify employee and department management. It provides a user-friendly interface and supports essential CRUD operations (Create, Read, Update, Delete) for employees and departments.

## Features
- **Employee Management**:
  - Add, edit, delete, and view employee details.
  - Search and filter employees by attributes like name, position, department, and contract expiry.
- **Department Management**:
  - Add, edit, and delete department names with validation to prevent duplicates.
  - Search for departments by name for easy navigation.
- **Dashboard**:
  - Displays dynamic values like total employees and upcoming contract renewals.
- **Reports**:
  - Generate detailed reports, including:
    - **Salary Report**: Summarizes salary data, including total payroll, average salary, and salary distribution by department or position.
    - **Contract Expiry Report**: Lists employees with expiring contracts.
    - **Employment Status Report**: Summarizes the number of active, on-leave, and resigned employees.
    - **Gender Diversity Report**: Tracks gender distribution across departments or the company.

## Technologies Used
- **JavaFX**: For building the user interface.
- **MySQL**: For the database (hosted locally via XAMPP).
- **JDBC**: To connect Java with MySQL.
- **SceneBuilder**: For designing FXML views.

## Database Setup
1. Install MySQL and create a database called `employee_mgt`.
2. Import the provided `employee_mgt.sql` file (located in the `db/` folder).
3. Use the following default connection settings:
   - **Host**: `localhost`
   - **Port**: `3306`
   - **Database**: `employee_management`
   - **User**: `root`
   - **Password**: `''` (blank by default)

## Getting Started
### Prerequisites
- **Java JDK 17+**
- **IntelliJ IDEA** (or any Java IDE)
- **XAMPP** (for MySQL)

### Steps to Run
1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/EmployeeManagementSystem.git



