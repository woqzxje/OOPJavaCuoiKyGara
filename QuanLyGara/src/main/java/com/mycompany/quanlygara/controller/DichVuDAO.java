/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.controller;

import com.mycompany.quanlygara.model.DichVu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ManhQuynh
 */
public class DichVuDAO implements IRepository<DichVu> {

    @Override
    public void themMoi(DichVu dv) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            if (dv.getMa() == null || dv.getMa().trim().isEmpty()) {
                int maxId = 0;
                try (PreparedStatement ps = conn.prepareStatement("SELECT ma FROM dich_vu");
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        try {
                            String cleanMa = rs.getString("ma").replace("DV", "");
                            int idNum = Integer.parseInt(cleanMa);
                            if (idNum > maxId) {
                                maxId = idNum;
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
                dv.setMa("DV" + String.format("%03d", maxId + 1));
            } else {
                try (PreparedStatement ps = conn.prepareStatement("SELECT ma FROM dich_vu WHERE LOWER(ma) = LOWER(?)")) {
                    ps.setString(1, dv.getMa());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new Exception("Ma dich vu '" + dv.getMa() + "' da ton tai!");
                        }
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO dich_vu (ma, ten, don_gia) VALUES (?, ?, ?)")) {
                ps.setString(1, dv.getMa());
                ps.setString(2, dv.getTen());
                ps.setDouble(3, dv.getDonGia());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void capNhat(DichVu dv) throws Exception {
        String sql = "UPDATE dich_vu SET ten = ?, don_gia = ? WHERE LOWER(ma) = LOWER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dv.getTen());
            ps.setDouble(2, dv.getDonGia());
            ps.setString(3, dv.getMa());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new Exception("Dich vu can cap nhat khong ton tai!");
            }
        }
    }

    @Override
    public void xoa(Object id) throws Exception {
        String ma = (String) id;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM dich_vu WHERE LOWER(ma) = LOWER(?)")) {
            ps.setString(1, ma);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new Exception("Khong tim thay dich vu co ma '" + ma + "' de xoa!");
            }
        }
    }

    @Override
    public List<DichVu> layTatCa() throws Exception {
        return querySql("SELECT ma, ten, don_gia FROM dich_vu ORDER BY ma", null);
    }

    @Override
    public DichVu layTheoId(Object id) throws Exception {
        String ma = (String) id;
        List<DichVu> list = querySql("SELECT ma, ten, don_gia FROM dich_vu WHERE LOWER(ma) = LOWER(?)", ma);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<DichVu> searchByName(String keyword) throws Exception {
        return querySql("SELECT ma, ten, don_gia FROM dich_vu WHERE LOWER(ten) LIKE ? ORDER BY ma",
                "%" + keyword.toLowerCase() + "%");
    }

    private List<DichVu> querySql(String sql, String param) throws Exception {
        List<DichVu> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (param != null) {
                ps.setString(1, param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    private DichVu mapRow(ResultSet rs) throws SQLException {
        return new DichVu(rs.getString("ma"), rs.getString("ten"), rs.getDouble("don_gia"));
    }
}
