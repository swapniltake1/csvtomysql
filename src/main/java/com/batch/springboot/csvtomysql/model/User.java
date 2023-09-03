package com.batch.springboot.csvtomysql.model;


public class User {
    private long userId;
    private String Address;
    private String name;
    private String salary;
    private String company;
    private String department;



    
    public User() {

    }

    public User(long userId, String address, String name, String salary, String company, String department) {
        this.userId = userId;
        Address = address;
        this.name = name;
        this.salary = salary;
        this.company = company;
        this.department = department;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "user [userId=" + userId + ", Address=" + Address + ", name=" + name + ", salary=" + salary
                + ", company=" + company + ", department=" + department + "]";
    }

    
}
