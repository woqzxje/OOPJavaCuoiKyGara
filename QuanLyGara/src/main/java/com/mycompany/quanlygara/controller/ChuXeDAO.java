/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.controller;

import com.mycompany.quanlygara.model.Owner;
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
public class ChuXeDAO implements IRepository<Owner> {

    @Override
    public void themMoi(Owner owner) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            if (owner.getId() > 0) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM owners WHERE id = ?")) {
                    ps.setInt(1, owner.getId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new DuplicateMaException("Ma chu xe '" + owner.getId() + "' da ton tai!");
                        }
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM owners WHERE phone = ?")) {
                ps.setString(1, owner.getPhone());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new Exception("So dien thoai '" + owner.getPhone() + "' da ton tai!");
                    }
                }
            }

            if (owner.getId() > 0) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO owners (id, name, phone, address) VALUES (?, ?, ?, ?)")) {
                    ps.setInt(1, owner.getId());
                    ps.setString(2, owner.getName());
                    ps.setString(3, owner.getPhone());
                    ps.setString(4, owner.getAddress());
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO owners (name, phone, address) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, owner.getName());
                    ps.setString(2, owner.getPhone());
                    ps.setString(3, owner.getAddress());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            owner.setId(keys.getInt(1));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void capNhat(Owner owner) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM owners WHERE phone = ? AND id <> ?")) {
                ps.setString(1, owner.getPhone());
                ps.setInt(2, owner.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new Exception("So dien thoai '" + owner.getPhone() + "' da bi trung voi chu xe khac!");
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE owners SET name = ?, phone = ?, address = ? WHERE id = ?")) {
                ps.setString(1, owner.getName());
                ps.setString(2, owner.getPhone());
                ps.setString(3, owner.getAddress());
                ps.setInt(4, owner.getId());
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    throw new Exception("Chu xe can cap nhat khong ton tai!");
                }
            }
        }
    }

    @Override
    public void xoa(Object id) throws Exception {
        int targetId = (Integer) id;
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM vehicles WHERE owner_id = ?")) {
                ps.setInt(1, targetId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new Exception("Không thể xóa chủ xe có ID = " + targetId + " vì người này đang có xe trong hệ thống!");
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM owners WHERE id = ?")) {
                ps.setInt(1, targetId);
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    throw new Exception("Khong tim thay chu xe co ID = " + targetId + " de xoa!");
                }
            }
        }
    }

    @Override
    public List<Owner> layTatCa() throws Exception {
        List<Owner> list = new ArrayList<>();
        String sql = "SELECT id, name, phone, address FROM owners ORDER BY id";
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
    public Owner layTheoId(Object id) throws Exception {
        int targetId = (Integer) id;
        String sql = "SELECT id, name, phone, address FROM owners WHERE id = ?";
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

    public List<Owner> searchByName(String keyword) throws Exception {
        List<Owner> result = new ArrayList<>();
        String sql = "SELECT id, name, phone, address FROM owners WHERE LOWER(name) LIKE ? ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }
        return result;
    }

    private Owner mapRow(ResultSet rs) throws SQLException {
        return new Owner(rs.getInt("id"), rs.getString("name"), rs.getString("phone"), rs.getString("address"));
    }
}
