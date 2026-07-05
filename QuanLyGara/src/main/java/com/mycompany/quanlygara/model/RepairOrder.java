/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author ManhQuynh
 */
public class RepairOrder {
    private int orderId;
    private String licensePlate;
    private Date entryDate;
    private Date exitDate;
    private int mechanicId;
    private String status; // RECEIVING, REPAIRING, COMPLETED

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public RepairOrder() {
        this.entryDate = new Date();
        this.status = "RECEIVING";
    }

    public RepairOrder(int orderId, String licensePlate, Date entryDate, Date exitDate, int mechanicId, String status) {
        this.orderId = orderId;
        this.licensePlate = licensePlate;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
        this.mechanicId = mechanicId;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getExitDate() {
        return exitDate;
    }

    public void setExitDate(Date exitDate) {
        this.exitDate = exitDate;
    }

    public int getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(int mechanicId) {
        this.mechanicId = mechanicId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void nhapInfo(Scanner sc) {
        System.out.print("Nhap bien so xe (license plate): ");
        this.licensePlate = sc.nextLine().trim();
        while (this.licensePlate.isEmpty()) {
            System.out.print("Bien so xe khong duoc de trong! Moi nhap lai: ");
            this.licensePlate = sc.nextLine().trim();
        }
        while (true) {
            try {
                System.out.print("Nhap ID ky thuat vien dam nhan: ");
                this.mechanicId = Integer.parseInt(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap ID so nguyen hop le!");
            }
        }
        System.out.print("Nhap ngay gio vao xuong (yyyy-MM-dd HH:mm:ss hoặc go Enter de lay hien tai): ");
        String entryDateStr = sc.nextLine().trim();
        if (entryDateStr.isEmpty()) {
            this.entryDate = new Date();
        } else {
            try {
                this.entryDate = sdf.parse(entryDateStr);
            } catch (ParseException e) {
                System.out.println("Sai dinh dang, su dung thoi gian hien tai.");
                this.entryDate = new Date();
            }
        }
        this.status = "RECEIVING";
    }

    @Override
    public String toString() {
        String entryStr = entryDate != null ? sdf.format(entryDate) : "N/A";
        String exitStr = exitDate != null ? sdf.format(exitDate) : "N/A";
        return "RepairOrder [ID: " + orderId + ", License Plate: " + licensePlate + 
               ", Entry: " + entryStr + ", Exit: " + exitStr + 
               ", Mechanic ID: " + mechanicId + ", Status: " + status + "]";
    }
}
