/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.model;

import java.util.Scanner;

/**
 *
 * @author ManhQuynh
 */
public class Mechanic extends Person {
    private String specialization;
    private double salary;
    private String status; // "Đang rảnh" / "Đang bận"

    public Mechanic() {
        super();
        this.status = "Đang rảnh";
    }

    public Mechanic(int id, String name, String phone, String address, String specialization, double salary, String status) {
        super(id, name, phone, address);
        this.specialization = specialization;
        this.salary = salary;
        this.status = status;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void nhapInfo(Scanner sc) {
        super.nhapInfo(sc);
        System.out.print("Nhap chuyen mon (specialization): ");
        this.specialization = sc.nextLine().trim();
        while (this.specialization.isEmpty()) {
            System.out.print("Chuyen mon khong duoc de trong! Moi nhap lai: ");
            this.specialization = sc.nextLine().trim();
        }
        while (true) {
            try {
                System.out.print("Nhap luong (salary): ");
                this.salary = Double.parseDouble(sc.nextLine());
                if (this.salary < 0) {
                    System.out.println("Luong phai >= 0!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so thuc hop le!");
            }
        }
        this.status = "Đang rảnh"; // Default when inputting new mechanic
    }

    @Override
    public String toString() {
        return "Mechanic [" + super.toString() + ", Specialization: " + specialization + 
               ", Salary: " + String.format("%,.0f", salary) + " VND, Status: " + status + "]";
    }
}
