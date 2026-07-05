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
public abstract class HangMuc {
    private String ma;
    private String ten;
    private double donGia;

    public HangMuc() {
    }

    public HangMuc(String ma, String ten, double donGia) {
        this.ma = ma;
        this.ten = ten;
        this.donGia = donGia;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public void nhapInfo(Scanner sc) {
        System.out.print("Nhap ma hang muc: ");
        this.ma = sc.nextLine().trim();
        while (this.ma.isEmpty()) {
            System.out.print("Ma hang muc khong duoc de trong! Moi nhap lai: ");
            this.ma = sc.nextLine().trim();
        }

        System.out.print("Nhap ten hang muc: ");
        this.ten = sc.nextLine().trim();
        while (this.ten.isEmpty()) {
            System.out.print("Ten hang muc khong duoc de trong! Moi nhap lai: ");
            this.ten = sc.nextLine().trim();
        }
        while (true) {
            try {
                System.out.print("Nhap don gia (VND): ");
                this.donGia = Double.parseDouble(sc.nextLine());
                if (this.donGia < 0) {
                    System.out.println("Don gia phai >= 0!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so thuc hop le!");
            }
        }
    }

    public abstract double tinhThanhTien(int soLuong);

    @Override
    public String toString() {
        return "Ma: " + ma + ", Ten: " + ten + ", Don gia: " + String.format("%,.0f", donGia) + " VND";
    }
}
