/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.controller;

import com.mycompany.quanlygara.model.Invoice;
import com.mycompany.quanlygara.model.LinhKien;
import com.mycompany.quanlygara.model.DichVu;
import com.mycompany.quanlygara.model.RepairOrderDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ManhQuynh
 */
public class HoaDonDAO implements IRepository<Invoice> {

    @Override
    public void themMoi(Invoice invoice) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT invoice_id FROM invoices WHERE order_id = ?")) {
                ps.setInt(1, invoice.getOrderId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new Exception("Hoa don cho phieu sua chua ID = " + invoice.getOrderId() + " da ton tai!");
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO invoices (order_id, payment_date, total_part_cost, total_labor_cost, vat_rate, total_amount) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, invoice.getOrderId());
                ps.setTimestamp(2, invoice.getPaymentDate() != null ? new Timestamp(invoice.getPaymentDate().getTime()) : null);
                ps.setDouble(3, invoice.getTotalPartCost());
                ps.setDouble(4, invoice.getTotalLaborCost());
                ps.setDouble(5, invoice.getVatRate());
                ps.setDouble(6, invoice.getTotalAmount());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        invoice.setInvoiceId(keys.getInt(1));
                    }
                }
            }
        }
    }

    @Override
    public void capNhat(Invoice invoice) throws Exception {
        String sql = "UPDATE invoices SET order_id = ?, payment_date = ?, total_part_cost = ?, total_labor_cost = ?, vat_rate = ?, total_amount = ? WHERE invoice_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoice.getOrderId());
            ps.setTimestamp(2, invoice.getPaymentDate() != null ? new Timestamp(invoice.getPaymentDate().getTime()) : null);
            ps.setDouble(3, invoice.getTotalPartCost());
            ps.setDouble(4, invoice.getTotalLaborCost());
            ps.setDouble(5, invoice.getVatRate());
            ps.setDouble(6, invoice.getTotalAmount());
            ps.setInt(7, invoice.getInvoiceId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new Exception("Hoa don can cap nhat khong ton tai!");
            }
        }
    }

    @Override
    public void xoa(Object id) throws Exception {
        int targetId = (Integer) id;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM invoices WHERE invoice_id = ?")) {
            ps.setInt(1, targetId);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new Exception("Khong tim thay hoa don co ID = " + targetId + " de xoa!");
            }
        }
    }

    @Override
    public List<Invoice> layTatCa() throws Exception {
        return querySql(
                "SELECT invoice_id, order_id, payment_date, total_part_cost, total_labor_cost, vat_rate, total_amount FROM invoices ORDER BY invoice_id",
                null);
    }

    @Override
    public Invoice layTheoId(Object id) throws Exception {
        int targetId = (Integer) id;
        List<Invoice> list = querySql(
                "SELECT invoice_id, order_id, payment_date, total_part_cost, total_labor_cost, vat_rate, total_amount FROM invoices WHERE invoice_id = ?",
                targetId);
        return list.isEmpty() ? null : list.get(0);
    }

    public Invoice getByOrderId(int orderId) throws Exception {
        List<Invoice> list = querySql(
                "SELECT invoice_id, order_id, payment_date, total_part_cost, total_labor_cost, vat_rate, total_amount FROM invoices WHERE order_id = ?",
                orderId);
        return list.isEmpty() ? null : list.get(0);
    }

    public Invoice generateInvoice(int orderId) throws Exception {
        PhieuSuaChuaDAO roDAO = new PhieuSuaChuaDAO();
        LinhKienDAO lkDAO = new LinhKienDAO();
        DichVuDAO dvDAO = new DichVuDAO();

        List<RepairOrderDetail> details = roDAO.getDetailsByOrderId(orderId);
        double totalPartCost = 0;
        double totalLaborCost = 0;

        for (int i = 0; i < details.size(); i++) {
            RepairOrderDetail d = details.get(i);
            LinhKien lk = lkDAO.layTheoId(d.getMaHangMuc());
            if (lk != null) {
                totalPartCost += lk.tinhThanhTien(d.getSoLuong());
            } else {
                DichVu dv = dvDAO.layTheoId(d.getMaHangMuc());
                if (dv != null) {
                    totalLaborCost += dv.tinhThanhTien(d.getSoLuong());
                } else {
                    throw new Exception("Loi: Khong tim thay hang muc co ma = " + d.getMaHangMuc());
                }
            }
        }

        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setTotalPartCost(totalPartCost);
        invoice.setTotalLaborCost(totalLaborCost);
        invoice.setVatRate(0.10); // 10% VAT
        invoice.calculateTotalAmount();

        return invoice;
    }

    private List<Invoice> querySql(String sql, Integer idParam) throws Exception {
        List<Invoice> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (idParam != null) {
                ps.setInt(1, idParam);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    private Invoice mapRow(ResultSet rs) throws SQLException {
        Timestamp pay = rs.getTimestamp("payment_date");
        return new Invoice(
                rs.getInt("invoice_id"),
                rs.getInt("order_id"),
                pay != null ? new Date(pay.getTime()) : null,
                rs.getDouble("total_part_cost"),
                rs.getDouble("total_labor_cost"),
                rs.getDouble("vat_rate"),
                rs.getDouble("total_amount")
        );
    }
}
