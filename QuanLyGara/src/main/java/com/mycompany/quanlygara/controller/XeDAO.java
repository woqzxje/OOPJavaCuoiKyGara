/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.controller;

import com.mycompany.quanlygara.model.Vehicle;
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


public class XeDAO implements IRepository<Vehicle> {

    @Override
    public void themMoi(Vehicle vehicle) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT license_plate FROM vehicles WHERE LOWER(license_plate) = LOWER(?)")) {
                ps.setString(1, vehicle.getLicensePlate());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new Exception("Bien so xe '" + vehicle.getLicensePlate() + "' da ton tai!");
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO vehicles (license_plate, brand, model, production_year, owner_id) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, vehicle.getLicensePlate());
                ps.setString(2, vehicle.getBrand());
                ps.setString(3, vehicle.getModel());
                ps.setInt(4, vehicle.getProductionYear());
                ps.setInt(5, vehicle.getOwnerId());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void capNhat(Vehicle vehicle) throws Exception {
        String sql = "UPDATE vehicles SET brand = ?, model = ?, production_year = ?, owner_id = ? WHERE LOWER(license_plate) = LOWER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicle.getBrand());
            ps.setString(2, vehicle.getModel());
            ps.setInt(3, vehicle.getProductionYear());
            ps.setInt(4, vehicle.getOwnerId());
            ps.setString(5, vehicle.getLicensePlate());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new Exception("Xe can cap nhat khong ton tai!");
            }
        }
    }

    @Override
    public void xoa(Object id) throws Exception {
        String lp = (String) id;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM vehicles WHERE LOWER(license_plate) = LOWER(?)")) {
            ps.setString(1, lp);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new Exception("Khong tim thay xe co bien so '" + lp + "' de xoa!");
            }
        }
    }

    @Override
    public List<Vehicle> layTatCa() throws Exception {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT license_plate, brand, model, production_year, owner_id FROM vehicles ORDER BY license_plate";
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
    public Vehicle layTheoId(Object id) throws Exception {
        String lp = (String) id;
        String sql = "SELECT license_plate, brand, model, production_year, owner_id FROM vehicles WHERE LOWER(license_plate) = LOWER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<Vehicle> searchByLicensePlate(String lp) throws Exception {
        List<Vehicle> result = new ArrayList<>();
        String sql = "SELECT license_plate, brand, model, production_year, owner_id FROM vehicles WHERE LOWER(license_plate) LIKE ? ORDER BY license_plate";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + lp.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }
        return result;
    }

    public List<Vehicle> searchByOwnerName(String ownerName) throws Exception {
        List<Vehicle> result = new ArrayList<>();
        String sql = "SELECT v.license_plate, v.brand, v.model, v.production_year, v.owner_id "
                + "FROM vehicles v JOIN owners o ON v.owner_id = o.id "
                + "WHERE LOWER(o.name) LIKE ? ORDER BY v.license_plate";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + ownerName.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }
        return result;
    }

    private Vehicle mapRow(ResultSet rs) throws SQLException {
        return new Vehicle(rs.getString("license_plate"), rs.getString("brand"), rs.getString("model"),
                rs.getInt("production_year"), rs.getInt("owner_id"));
    }
}
