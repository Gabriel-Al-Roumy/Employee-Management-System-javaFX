package com.example.demo;

public class SimplifiedEmployee {
    private Integer employeeId;
    private String name;
    private Integer salary;

    // Constructor
    public SimplifiedEmployee(Integer employeeId, String name, Integer salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.salary = salary;
    }

    // Getters and setters
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

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}

