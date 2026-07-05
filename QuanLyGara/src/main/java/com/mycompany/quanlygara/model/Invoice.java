/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ManhQuynh
 */
public class Invoice {
    private int invoiceId;
    private int orderId;
    private Date paymentDate;
    private double totalPartCost;
    private double totalLaborCost;
    private double vatRate;
    private double totalAmount;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Invoice() {
        this.paymentDate = new Date();
        this.vatRate = 0.10;
    }

    public Invoice(int invoiceId, int orderId, Date paymentDate, double totalPartCost, double totalLaborCost, double vatRate, double totalAmount) {
        this.invoiceId = invoiceId;
        this.orderId = orderId;
        this.paymentDate = paymentDate;
        this.totalPartCost = totalPartCost;
        this.totalLaborCost = totalLaborCost;
        this.vatRate = vatRate;
        this.totalAmount = totalAmount;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getTotalPartCost() {
        return totalPartCost;
    }

    public void setTotalPartCost(double totalPartCost) {
        this.totalPartCost = totalPartCost;
    }

    public double getTotalLaborCost() {
        return totalLaborCost;
    }

    public void setTotalLaborCost(double totalLaborCost) {
        this.totalLaborCost = totalLaborCost;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void calculateTotalAmount() {
        double subTotal = this.totalPartCost + this.totalLaborCost;
        this.totalAmount = subTotal + (subTotal * this.vatRate);
    }

    @Override
    public String toString() {
        String paymentStr = paymentDate != null ? sdf.format(paymentDate) : "N/A";
        return "Invoice [ID: " + invoiceId + ", Order ID: " + orderId + ", Payment Date: " + paymentStr +
               ", Part Cost: " + String.format("%,.0f", totalPartCost) + " VND" +
               ", Labor Cost: " + String.format("%,.0f", totalLaborCost) + " VND" +
               ", VAT Rate: " + (vatRate * 100) + "%" +
               ", Total Amount: " + String.format("%,.0f", totalAmount) + " VND]";
    }
}
