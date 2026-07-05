/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.controller;

import com.mycompany.quanlygara.model.HangMuc;
import com.mycompany.quanlygara.model.LinhKien;
import com.mycompany.quanlygara.model.DichVu;
import com.mycompany.quanlygara.model.RepairOrder;
import com.mycompany.quanlygara.model.RepairOrderDetail;
import com.mycompany.quanlygara.model.Mechanic;
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
public class PhieuSuaChuaDAO implements IRepository<RepairOrder> {

    @Override
    public void themMoi(RepairOrder order) throws Exception {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO repair_orders (license_plate, entry_date, exit_date, mechanic_id, status) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, order.getLicensePlate());
            ps.setTimestamp(2, order.getEntryDate() != null ? new Timestamp(order.getEntryDate().getTime()) : null);
            ps.setTimestamp(3, order.getExitDate() != null ? new Timestamp(order.getExitDate().getTime()) : null);
            ps.setInt(4, order.getMechanicId());
            ps.setString(5, order.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    order.setOrderId(keys.getInt(1));
                }
            }
        }

        // Cap nhat trang thai ky thuat vien thanh "Đang bận"
        KyThuatVienDAO mechanicDAO = new KyThuatVienDAO();
        Mechanic m = mechanicDAO.layTheoId(order.getMechanicId());
        if (m != null) {
            m.setStatus("Đang bận");
            mechanicDAO.capNhat(m);
        }
    }

    @Override
    public void capNhat(RepairOrder order) throws Exception {
        RepairOrder existing = layTheoId(order.getOrderId());
        if (existing == null) {
            throw new Exception("Phieu sua chua can cap nhat khong ton tai!");
        }
        String oldStatus = existing.getStatus();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE repair_orders SET license_plate = ?, entry_date = ?, exit_date = ?, mechanic_id = ?, status = ? WHERE order_id = ?")) {
            ps.setString(1, order.getLicensePlate());
            ps.setTimestamp(2, order.getEntryDate() != null ? new Timestamp(order.getEntryDate().getTime()) : null);
            ps.setTimestamp(3, order.getExitDate() != null ? new Timestamp(order.getExitDate().getTime()) : null);
            ps.setInt(4, order.getMechanicId());
            ps.setString(5, order.getStatus());
            ps.setInt(6, order.getOrderId());
            ps.executeUpdate();
        }

        KyThuatVienDAO mechanicDAO = new KyThuatVienDAO();
        if (existing.getMechanicId() != order.getMechanicId()) {
            Mechanic oldM = mechanicDAO.layTheoId(existing.getMechanicId());
            if (oldM != null) {
                oldM.setStatus("Đang rảnh");
                mechanicDAO.capNhat(oldM);
            }
            Mechanic newM = mechanicDAO.layTheoId(order.getMechanicId());
            if (newM != null && !"COMPLETED".equalsIgnoreCase(order.getStatus())) {
                newM.setStatus("Đang bận");
                mechanicDAO.capNhat(newM);
            }
        }

        if ("COMPLETED".equalsIgnoreCase(order.getStatus()) && !"COMPLETED".equalsIgnoreCase(oldStatus)) {
            Mechanic m = mechanicDAO.layTheoId(order.getMechanicId());
            if (m != null) {
                m.setStatus("Đang rảnh");
                mechanicDAO.capNhat(m);
            }
        }
    }

    @Override
    public void xoa(Object id) throws Exception {
        int targetId = (Integer) id;
        RepairOrder toRemove = layTheoId(targetId);
        if (toRemove == null) {
            throw new Exception("Khong tim thay phieu sua chua co ID = " + targetId + " de xoa!");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Hoan lai linh kien ve kho neu co
            try (PreparedStatement ps = conn.prepareStatement("SELECT ma_hang_muc, so_luong FROM repair_order_details WHERE order_id = ?")) {
                ps.setInt(1, targetId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String ma = rs.getString("ma_hang_muc");
                        int sl = rs.getInt("so_luong");
                        if (ma.toUpperCase().startsWith("LK")) {
                            try (PreparedStatement psUpdate = conn.prepareStatement("UPDATE linh_kien SET so_luong_ton = so_luong_ton + ? WHERE ma = ?")) {
                                psUpdate.setInt(1, sl);
                                psUpdate.setString(2, ma);
                                psUpdate.executeUpdate();
                            }
                        }
                    }
                }
            }
            
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM repair_order_details WHERE order_id = ?")) {
                ps.setInt(1, targetId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM repair_orders WHERE order_id = ?")) {
                ps.setInt(1, targetId);
                ps.executeUpdate();
            }
        }

        // Neu chua hoan thanh, tra ky thuat vien ve trang thai ranh
        if (!"COMPLETED".equalsIgnoreCase(toRemove.getStatus())) {
            KyThuatVienDAO mechanicDAO = new KyThuatVienDAO();
            Mechanic m = mechanicDAO.layTheoId(toRemove.getMechanicId());
            if (m != null) {
                m.setStatus("Đang rảnh");
                mechanicDAO.capNhat(m);
            }
        }
    }

    @Override
    public List<RepairOrder> layTatCa() throws Exception {
        List<RepairOrder> list = new ArrayList<>();
        String sql = "SELECT order_id, license_plate, entry_date, exit_date, mechanic_id, status FROM repair_orders ORDER BY order_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public RepairOrder layTheoId(Object id) throws Exception {
        int targetId = (Integer) id;
        String sql = "SELECT order_id, license_plate, entry_date, exit_date, mechanic_id, status FROM repair_orders WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, targetId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public void addDetail(RepairOrderDetail detail) throws Exception {
        RepairOrder ro = layTheoId(detail.getOrderId());
        if (ro != null && "COMPLETED".equalsIgnoreCase(ro.getStatus())) {
            throw new Exception("Lỗi: Không thể thêm vào phiếu đã hoàn tất (COMPLETED)!");
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            Integer existingQty = null;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT so_luong FROM repair_order_details WHERE order_id = ? AND LOWER(ma_hang_muc) = LOWER(?)")) {
                ps.setInt(1, detail.getOrderId());
                ps.setString(2, detail.getMaHangMuc());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        existingQty = rs.getInt("so_luong");
                    }
                }
            }
            if (existingQty != null) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE repair_order_details SET so_luong = ? WHERE order_id = ? AND LOWER(ma_hang_muc) = LOWER(?)")) {
                    ps.setInt(1, existingQty + detail.getSoLuong());
                    ps.setInt(2, detail.getOrderId());
                    ps.setString(3, detail.getMaHangMuc());
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO repair_order_details (order_id, ma_hang_muc, so_luong) VALUES (?, ?, ?)")) {
                    ps.setInt(1, detail.getOrderId());
                    ps.setString(2, detail.getMaHangMuc());
                    ps.setInt(3, detail.getSoLuong());
                    ps.executeUpdate();
                }
            }
        }
    }

    public void themMoiChiTietVaTruKho(RepairOrderDetail detail) throws Exception {
        RepairOrder ro = layTheoId(detail.getOrderId());
        if (ro != null && "COMPLETED".equalsIgnoreCase(ro.getStatus())) {
            throw new Exception("Lỗi: Không thể thêm linh kiện vào phiếu đã hoàn tất (COMPLETED)!");
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu Transaction
            try {
                if (detail.getMaHangMuc().toUpperCase().startsWith("LK")) {
                    try (PreparedStatement psUpdate = conn.prepareStatement(
                            "UPDATE linh_kien SET so_luong_ton = so_luong_ton - ? WHERE LOWER(ma) = LOWER(?) AND so_luong_ton >= ?")) {
                        psUpdate.setInt(1, detail.getSoLuong());
                        psUpdate.setString(2, detail.getMaHangMuc());
                        psUpdate.setInt(3, detail.getSoLuong());
                        int rows = psUpdate.executeUpdate();
                        if (rows == 0) {
                            throw new Exception("Linh kiện không tồn tại hoặc kho không đủ hàng!");
                        }
                    }
                }
                
                Integer existingQty = null;
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT so_luong FROM repair_order_details WHERE order_id = ? AND LOWER(ma_hang_muc) = LOWER(?)")) {
                    ps.setInt(1, detail.getOrderId());
                    ps.setString(2, detail.getMaHangMuc());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            existingQty = rs.getInt("so_luong");
                        }
                    }
                }
                if (existingQty != null) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE repair_order_details SET so_luong = ? WHERE order_id = ? AND LOWER(ma_hang_muc) = LOWER(?)")) {
                        ps.setInt(1, existingQty + detail.getSoLuong());
                        ps.setInt(2, detail.getOrderId());
                        ps.setString(3, detail.getMaHangMuc());
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO repair_order_details (order_id, ma_hang_muc, so_luong) VALUES (?, ?, ?)")) {
                        ps.setInt(1, detail.getOrderId());
                        ps.setString(2, detail.getMaHangMuc());
                        ps.setInt(3, detail.getSoLuong());
                        ps.executeUpdate();
                    }
                }
                conn.commit(); // Thành công cả 2 mới ghi DB
            } catch (Exception e) {
                conn.rollback(); // Lỗi thì khôi phục lại
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<RepairOrderDetail> getDetailsByOrderId(int orderId) throws Exception {
        List<RepairOrderDetail> result = new ArrayList<>();
        String sql = "SELECT order_id, ma_hang_muc, so_luong FROM repair_order_details WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new RepairOrderDetail(rs.getInt("order_id"), rs.getString("ma_hang_muc"), rs.getInt("so_luong")));
                }
            }
        }
        return result;
    }

    /**
     * Lay toan bo chi tiet phieu sua chua (dung cho bao cao thong ke).
     * Thay the cho JsonStorage.loadList("repair_order_details.json", ...) truoc day.
     */
    public List<RepairOrderDetail> getAllDetails() throws Exception {
        List<RepairOrderDetail> result = new ArrayList<>();
        String sql = "SELECT order_id, ma_hang_muc, so_luong FROM repair_order_details";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new RepairOrderDetail(rs.getInt("order_id"), rs.getString("ma_hang_muc"), rs.getInt("so_luong")));
            }
        }
        return result;
    }

    public List<HangMuc> getDanhSachChiTiet(int orderId) throws Exception {
        List<RepairOrderDetail> details = getDetailsByOrderId(orderId);
        List<HangMuc> danhSachChiTiet = new ArrayList<>();
        LinhKienDAO lkDAO = new LinhKienDAO();
        DichVuDAO dvDAO = new DichVuDAO();
        for (int i = 0; i < details.size(); i++) {
            RepairOrderDetail d = details.get(i);
            LinhKien lk = lkDAO.layTheoId(d.getMaHangMuc());
            if (lk != null) {
                danhSachChiTiet.add(lk);
            } else {
                DichVu dv = dvDAO.layTheoId(d.getMaHangMuc());
                if (dv != null) {
                    danhSachChiTiet.add(dv);
                }
            }
        }
        return danhSachChiTiet;
    }

    private RepairOrder mapRow(ResultSet rs) throws SQLException {
        Timestamp entry = rs.getTimestamp("entry_date");
        Timestamp exit = rs.getTimestamp("exit_date");
        return new RepairOrder(
                rs.getInt("order_id"),
                rs.getString("license_plate"),
                entry != null ? new Date(entry.getTime()) : null,
                exit != null ? new Date(exit.getTime()) : null,
                rs.getInt("mechanic_id"),
                rs.getString("status")
        );
    }
}
