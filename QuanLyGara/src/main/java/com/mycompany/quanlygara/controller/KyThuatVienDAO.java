/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.controller;

import com.mycompany.quanlygara.model.Mechanic;
import com.mycompany.quanlygara.model.DuplicateMaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ManhQuynh
 */
public class KyThuatVienDAO implements IRepository<Mechanic> {

    @Override
    public void themMoi(Mechanic mechanic) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            if (mechanic.getId() > 0) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM mechanics WHERE id = ?")) {
                    ps.setInt(1, mechanic.getId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new DuplicateMaException("Ma ky thuat vien '" + mechanic.getId() + "' da ton tai!");
                        }
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM mechanics WHERE phone = ?")) {
                ps.setString(1, mechanic.getPhone());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new Exception("So dien thoai '" + mechanic.getPhone() + "' da ton tai cho mot ky thuat vien khac!");
                    }
                }
            }
            if (mechanic.getStatus() == null) {
                mechanic.setStatus("Đang rảnh");
            }

            if (mechanic.getId() > 0) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO mechanics (id, name, phone, address, specialization, salary, status) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                    ps.setInt(1, mechanic.getId());
                    ps.setString(2, mechanic.getName());
                    ps.setString(3, mechanic.getPhone());
                    ps.setString(4, mechanic.getAddress());
                    ps.setString(5, mechanic.getSpecialization());
                    ps.setDouble(6, mechanic.getSalary());
                    ps.setString(7, mechanic.getStatus());
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO mechanics (name, phone, address, specialization, salary, status) VALUES (?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, mechanic.getName());
                    ps.setString(2, mechanic.getPhone());
                    ps.setString(3, mechanic.getAddress());
                    ps.setString(4, mechanic.getSpecialization());
                    ps.setDouble(5, mechanic.getSalary());
                    ps.setString(6, mechanic.getStatus());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            mechanic.setId(keys.getInt(1));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void capNhat(Mechanic mechanic) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM mechanics WHERE phone = ? AND id <> ?")) {
                ps.setString(1, mechanic.getPhone());
                ps.setInt(2, mechanic.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new Exception("So dien thoai '" + mechanic.getPhone() + "' da trung voi mot ky thuat vien khac!");
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE mechanics SET name = ?, phone = ?, address = ?, specialization = ?, salary = ?, status = ? WHERE id = ?")) {
                ps.setString(1, mechanic.getName());
                ps.setString(2, mechanic.getPhone());
                ps.setString(3, mechanic.getAddress());
                ps.setString(4, mechanic.getSpecialization());
                ps.setDouble(5, mechanic.getSalary());
                ps.setString(6, mechanic.getStatus());
                ps.setInt(7, mechanic.getId());
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    throw new Exception("Ky thuat vien can cap nhat khong ton tai!");
                }
            }
        }
    }

    @Override
    public void xoa(Object id) throws Exception {
        int targetId = (Integer) id;
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM repair_orders WHERE mechanic_id = ? AND status != 'COMPLETED'")) {
                ps.setInt(1, targetId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new Exception("Không thể xóa thợ có ID = " + targetId + " vì người này đang đảm nhận phiếu sửa chữa chưa hoàn thành!");
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM mechanics WHERE id = ?")) {
                ps.setInt(1, targetId);
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    throw new Exception("Khong tim thay ky thuat vien co ID = " + targetId + " de xoa!");
                }
            }
        }
    }

    @Override
    public List<Mechanic> layTatCa() throws Exception {
        return querySql("SELECT id, name, phone, address, specialization, salary, status FROM mechanics ORDER BY id", null);
    }

    @Override
    public Mechanic layTheoId(Object id) throws Exception {
        int targetId = (Integer) id;
        List<Mechanic> list = querySql(
                "SELECT id, name, phone, address, specialization, salary, status FROM mechanics WHERE id = ?", targetId);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Mechanic> sortBySalary(boolean ascending) throws Exception {
        String order = ascending ? "ASC" : "DESC";
        return querySql("SELECT id, name, phone, address, specialization, salary, status FROM mechanics ORDER BY salary " + order, null);
    }

    public List<Mechanic> sortByName() throws Exception {
        return querySql("SELECT id, name, phone, address, specialization, salary, status FROM mechanics ORDER BY name ASC", null);
    }

    private List<Mechanic> querySql(String sql, Integer idParam) throws Exception {
        List<Mechanic> list = new ArrayList<>();
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

    private Mechanic mapRow(ResultSet rs) throws SQLException {
        return new Mechanic(rs.getInt("id"), rs.getString("name"), rs.getString("phone"), rs.getString("address"),
                rs.getString("specialization"), rs.getDouble("salary"), rs.getString("status"));
    }
}
