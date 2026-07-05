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
public class DichVu extends HangMuc {

    public DichVu() {
        super();
    }

    public DichVu(String ma, String ten, double donGia) {
        super(ma, ten, donGia);
    }

    @Override
    public double tinhThanhTien(int soLuong) {
        // Dich vu tinh tron goi, so luong luon la 1, thanh tien la don gia
        return getDonGia();
    }

    @Override
    public void nhapInfo(Scanner sc) {
        super.nhapInfo(sc);
    }

    @Override
    public String toString() {
        return "DichVu [" + super.toString() + "]";
    }
}
