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
public class LinhKien extends HangMuc {
    private int soLuongTon;

    public LinhKien() {
        super();
    }

    public LinhKien(String ma, String ten, double donGia, int soLuongTon) {
        super(ma, ten, donGia);
        this.soLuongTon = soLuongTon;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    @Override
    public double tinhThanhTien(int soLuong) {
        return getDonGia() * soLuong;
    }

    @Override
    public void nhapInfo(Scanner sc) {
        super.nhapInfo(sc);
        while (true) {
            try {
                System.out.print("Nhap so luong ton kho: ");
                this.soLuongTon = Integer.parseInt(sc.nextLine());
                if (this.soLuongTon < 0) {
                    System.out.println("So luong ton phai >= 0!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so nguyen hop le!");
            }
        }
    }

    @Override
    public String toString() {
        return "LinhKien [" + super.toString() + ", So luong ton: " + soLuongTon + "]";
    }
}
