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
public class RepairOrderDetail {
    private int orderId;
    private String maHangMuc;
    private int soLuong;

    public RepairOrderDetail() {
    }

    public RepairOrderDetail(int orderId, String maHangMuc, int soLuong) {
        this.orderId = orderId;
        this.maHangMuc = maHangMuc;
        this.soLuong = soLuong;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getMaHangMuc() {
        return maHangMuc;
    }

    public void setMaHangMuc(String maHangMuc) {
        this.maHangMuc = maHangMuc;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void nhapInfo(Scanner sc) {
        System.out.print("Nhap ma hang muc (Linh kien / Dich vu): ");
        this.maHangMuc = sc.nextLine().trim();
        while (true) {
            try {
                System.out.print("Nhap so luong/lan su dung: ");
                this.soLuong = Integer.parseInt(sc.nextLine());
                if (this.soLuong <= 0) {
                    System.out.println("So luong phai > 0!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("So luong phai la so nguyen!");
            }
        }
    }

    @Override
    public String toString() {
        return "RepairOrderDetail [Order ID: " + orderId + ", Ma Hang Muc: " + maHangMuc + 
               ", So Luong: " + soLuong + "]";
    }
}
