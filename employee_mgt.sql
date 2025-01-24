-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 24, 2025 at 07:35 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `employee_mgt`
--
CREATE DATABASE IF NOT EXISTS `employee_mgt` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `employee_mgt`;

-- --------------------------------------------------------

--
-- Table structure for table `departments`
--

CREATE TABLE `departments` (
  `department_id` int(11) NOT NULL,
  `department_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `departments`
--

INSERT INTO `departments` (`department_id`, `department_name`) VALUES
(2, 'Finance'),
(1, 'Human Resources'),
(5, 'IT'),
(3, 'Marketing'),
(4, 'Sales');

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees` (
  `employee_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `position` varchar(100) NOT NULL,
  `department_id` int(11) DEFAULT NULL,
  `salary` decimal(10,2) NOT NULL,
  `contact_info` varchar(100) DEFAULT NULL,
  `date_of_joining` date DEFAULT NULL,
  `contract_expiry` date DEFAULT NULL,
  `employment_status` enum('active','on leave','resigned') NOT NULL,
  `gender` enum('Male','Female','Other') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`employee_id`, `name`, `position`, `department_id`, `salary`, `contact_info`, `date_of_joining`, `contract_expiry`, `employment_status`, `gender`) VALUES
(1, 'Alice Johnson', 'HR Manager', 1, 60000.00, 'alice@example.com', '2023-01-10', '2025-01-10', 'active', 'Female'),
(2, 'Bob Smith', 'Finance Analyst', 2, 55000.00, 'bob@example.com', '2022-05-15', '2024-05-15', 'on leave', 'Male'),
(3, 'Charlie Brown', 'Marketing Director', 3, 75000.00, 'charlie@example.com', '2021-08-01', '2026-08-01', 'active', 'Male'),
(4, 'Diana Ross', 'HR Specialist', 1, 45000.00, 'diana@example.com', '2022-06-12', '2024-06-14', 'resigned', 'Female'),
(5, 'Eve White', 'Finance Manager', 2, 80000.00, 'eve@example.com', '2023-02-20', '2025-02-20', 'active', 'Female'),
(6, 'Frank Black ', 'Marketing Coordinator', 3, 50000.00, 'frank@example.com', '2021-11-30', '2023-11-30', 'active', 'Male'),
(7, 'Grace Green', 'Sales Executive', 4, 40000.00, 'grace@example.com', '2020-09-10', '2023-09-10', 'on leave', 'Female'),
(8, 'Hannah Blue', 'Finance Assistant', 2, 40000.00, 'hannah@example.com', '2022-04-04', '2024-04-04', 'active', 'Female'),
(9, 'Isaac Gray', 'Marketing Intern', 3, 25000.00, 'isaac@example.com', '2023-10-01', '2024-10-01', 'active', 'Male'),
(10, 'Jack White', 'Sales Manager', 4, 70000.00, 'jack@example.com', '2021-03-17', '2023-03-17', 'resigned', 'Male'),
(11, 'Katie Black', 'IT Support', 5, 55000.00, 'katie@example.com', '2023-05-01', '2025-05-01', 'active', 'Female');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `departments`
--
ALTER TABLE `departments`
  ADD PRIMARY KEY (`department_id`),
  ADD UNIQUE KEY `department_name` (`department_name`);

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`employee_id`),
  ADD KEY `department_id` (`department_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `departments`
--
ALTER TABLE `departments`
  MODIFY `department_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `employees`
--
ALTER TABLE `employees`
  MODIFY `employee_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `employees`
--
ALTER TABLE `employees`
  ADD CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
