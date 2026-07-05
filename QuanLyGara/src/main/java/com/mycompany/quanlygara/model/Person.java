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
public abstract class Person {
    private int id;
    private String name;
    private String phone;
    private String address;

    public Person() {
    }

    public Person(int id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void nhapInfo(Scanner sc) {
        System.out.print("Nhap ho ten: ");
        this.name = sc.nextLine().trim();
        while (this.name.isEmpty()) {
            System.out.print("Ho ten khong duoc de trong! Moi nhap lai: ");
            this.name = sc.nextLine().trim();
        }

        System.out.print("Nhap so dien thoai: ");
        this.phone = sc.nextLine().trim();
        while (!this.phone.matches("^0\\d{9}$")) {
            System.out.print("So dien thoai khong hop le (Phai gom 10 so, bat dau tu 0). Moi nhap lai: ");
            this.phone = sc.nextLine().trim();
        }

        System.out.print("Nhap dia chi: ");
        this.address = sc.nextLine().trim();
        while (this.address.isEmpty()) {
            System.out.print("Dia chi khong duoc de trong! Moi nhap lai: ");
            this.address = sc.nextLine().trim();
        }
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Phone: " + phone + ", Address: " + address;
    }
}
