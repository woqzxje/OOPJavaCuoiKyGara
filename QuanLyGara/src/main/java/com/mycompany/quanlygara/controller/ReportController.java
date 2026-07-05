/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.controller;

import com.mycompany.quanlygara.model.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author ManhQuynh
 */
public class ReportController implements IReportService {

    @Override
    public double getRevenue(String fromDate, String toDate) throws Exception {
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        List<Invoice> invoices = hoaDonDAO.layTatCa();

        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;

        if (fromDate != null && !fromDate.trim().isEmpty()) {
            start = sdfInput.parse(fromDate.trim());
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdfInput.parse(toDate.trim()));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            end = cal.getTime();
        }

        double totalRevenue = 0;
        for (int i = 0; i < invoices.size(); i++) {
            Invoice inv = invoices.get(i);
            Date payDate = inv.getPaymentDate();

            boolean matches = true;
            if (start != null && payDate.before(start)) {
                matches = false;
            }
            if (end != null && payDate.after(end)) {
                matches = false;
            }

            if (matches) {
                totalRevenue += inv.getTotalAmount();
            }
        }
        return totalRevenue;
    }

    @Override
    public Map<String, Integer> getMostUsedParts(int limit) throws Exception {
        LinhKienDAO lkDAO = new LinhKienDAO();
        PhieuSuaChuaDAO roDAO = new PhieuSuaChuaDAO();
        List<RepairOrderDetail> allDetails = roDAO.getAllDetails();

        Map<String, Integer> usageMap = new HashMap<>();
        for (int i = 0; i < allDetails.size(); i++) {
            RepairOrderDetail d = allDetails.get(i);
            LinhKien p = lkDAO.layTheoId(d.getMaHangMuc());
            if (p != null) { // Only count parts, not services
                String partName = p.getTen();
                int currentQty = usageMap.containsKey(partName) ? usageMap.get(partName) : 0;
                usageMap.put(partName, currentQty + d.getSoLuong());
            }
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(usageMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });

        Map<String, Integer> result = new LinkedHashMap<>();
        int count = 0;
        for (int i = 0; i < entryList.size() && count < limit; i++) {
            Map.Entry<String, Integer> entry = entryList.get(i);
            result.put(entry.getKey(), entry.getValue());
            count++;
        }
        return result;
    }

    @Override
    public Map<String, Integer> getMostRepairedVehicles(int limit) throws Exception {
        PhieuSuaChuaDAO roDAO = new PhieuSuaChuaDAO();
        XeDAO xeDAO = new XeDAO();

        List<RepairOrder> orders = roDAO.layTatCa();
        Map<String, Integer> repairMap = new HashMap<>();

        for (int i = 0; i < orders.size(); i++) {
            RepairOrder o = orders.get(i);
            Vehicle v = xeDAO.layTheoId(o.getLicensePlate());
            String vehicleInfo = (v != null) ? (v.getLicensePlate() + " - " + v.getBrand() + " " + v.getModel()) 
                                             : o.getLicensePlate();

            int currentCount = repairMap.containsKey(vehicleInfo) ? repairMap.get(vehicleInfo) : 0;
            repairMap.put(vehicleInfo, currentCount + 1);
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(repairMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });

        Map<String, Integer> result = new LinkedHashMap<>();
        int count = 0;
        for (int i = 0; i < entryList.size() && count < limit; i++) {
            Map.Entry<String, Integer> entry = entryList.get(i);
            result.put(entry.getKey(), entry.getValue());
            count++;
        }
        return result;
    }

    @Override
    public Map<String, Integer> getMostActiveMechanics(int limit) throws Exception {
        PhieuSuaChuaDAO roDAO = new PhieuSuaChuaDAO();
        KyThuatVienDAO mechanicDAO = new KyThuatVienDAO();

        List<RepairOrder> orders = roDAO.layTatCa();
        Map<String, Integer> activeMap = new HashMap<>();

        for (int i = 0; i < orders.size(); i++) {
            RepairOrder o = orders.get(i);
            Mechanic m = mechanicDAO.layTheoId(o.getMechanicId());
            String mechanicName = (m != null) ? m.getName() : "Mechanic ID: " + o.getMechanicId();

            int currentCount = activeMap.containsKey(mechanicName) ? activeMap.get(mechanicName) : 0;
            activeMap.put(mechanicName, currentCount + 1);
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(activeMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });

        Map<String, Integer> result = new LinkedHashMap<>();
        int count = 0;
        for (int i = 0; i < entryList.size() && count < limit; i++) {
            Map.Entry<String, Integer> entry = entryList.get(i);
            result.put(entry.getKey(), entry.getValue());
            count++;
        }
        return result;
    }
}
