package com.example.demo;


import java.util.Date;

public class Employee {
    private Integer employeeId;
    private String name;
    private String position;
    private String department;
    private String contractExpiry;
    private Integer salary;
    private String contactInfo;
    private Date dateOfJoining;
    private String employmentStatus;
    private String gender;

    // Constructor to include all fields
    public Employee(Integer employeeId, String name, String position, String department,
                    Integer salary, String contactInfo, Date dateOfJoining, String contractExpiry,
                    String employmentStatus, String gender) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.department = department;
        this.salary = salary;
        this.contactInfo = contactInfo;
        this.dateOfJoining = dateOfJoining;
        this.contractExpiry = contractExpiry;
        this.employmentStatus = employmentStatus;
        this.gender = gender;
    }
    public Employee(Integer employeeId, String name, String position, String department, String contractExpiry) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.department = department;
        this.contractExpiry = contractExpiry;
    }

    // Getters and setters for all fields
    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContractExpiry() {
        return contractExpiry;
    }

    public void setContractExpiry(String contractExpiry) {
        this.contractExpiry = contractExpiry;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Date getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(Date dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", contractExpiry='" + contractExpiry + '\'' +
                ", salary=" + salary +
                ", contactInfo='" + contactInfo + '\'' +
                ", dateOfJoining=" + dateOfJoining +
                ", employmentStatus='" + employmentStatus + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
