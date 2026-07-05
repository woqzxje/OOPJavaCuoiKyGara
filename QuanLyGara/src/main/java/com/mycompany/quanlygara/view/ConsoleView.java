/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.view;

import com.mycompany.quanlygara.controller.*;
import com.mycompany.quanlygara.model.*;
import java.util.*;

/**
 *
 * @author ManhQuynh
 */
public class ConsoleView {
    private final Scanner sc;
    private final ChuXeDAO ownerController;
    private final XeDAO vehicleController;
    private final KyThuatVienDAO mechanicController;
    private final LinhKienDAO partController;
    private final PhieuSuaChuaDAO repairOrderController;
    private final HoaDonDAO invoiceController;
    private final ReportController reportController;
    private final DichVuDAO dichVuDAO;

    public ConsoleView() {
        this.sc = new Scanner(System.in);
        this.ownerController = new ChuXeDAO();
        this.vehicleController = new XeDAO();
        this.mechanicController = new KyThuatVienDAO();
        this.partController = new LinhKienDAO();
        this.repairOrderController = new PhieuSuaChuaDAO();
        this.invoiceController = new HoaDonDAO();
        this.reportController = new ReportController();
        this.dichVuDAO = new DichVuDAO();
    }

    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        view.start();
    }

    public void start() {
        int choice = -1;
        do {
            showMainMenu();
            try {
                System.out.print("Nhap lua chon cua ban (0-7): ");
                choice = Integer.parseInt(sc.nextLine());
                System.out.println();
                switch (choice) {
                    case 1:
                        menuVehicleAndOwner();
                        break;
                    case 2:
                        menuMechanic();
                        break;
                    case 3:
                        menuPart();
                        break;
                    case 4:
                        menuRepairOrder();
                        break;
                    case 5:
                        menuInvoice();
                        break;
                    case 6:
                        menuReport();
                        break;
                    case 7:
                        menuDichVu();
                        break;
                    case 0:
                        System.out.println("Cam on ban da su dung chuong trinh!");
                        break;
                    default:
                        System.out.println("Lua chon khong hop le! Vui long nhap tu 0 den 7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so nguyen hop le!");
            } catch (Exception e) {
                System.out.println("Da xay ra loi: " + e.getMessage());
            }
            System.out.println();
        } while (choice != 0);
    }

    private void showMainMenu() {
        System.out.println("====================================================");
        System.out.println("        HE THONG QUAN LY GARA SUA CHUA O TO         ");
        System.out.println("====================================================");
        System.out.println("1. Quan ly Xe va Chu xe");
        System.out.println("2. Quan ly Ky thuat vien (Mechanic)");
        System.out.println("3. Quan ly Kho linh kien (LinhKien)");
        System.out.println("4. Quan ly Phieu sua chua (Repair Order)");
        System.out.println("5. Thanh toan & Hoa don (Invoice)");
        System.out.println("6. Bao cao thong ke doanh thu & hieu suat");
        System.out.println("7. Quan ly Danh muc Dich vu (DichVu)");
        System.out.println("0. Thoat chuong trinh");
        System.out.println("====================================================");
    }

    // --- 1. QUAN LY XE VA CHU XE ---
    private void menuVehicleAndOwner() throws Exception {
        int choice = -1;
        do {
            System.out.println("--- MENU QUAN LY XE & CHU XE ---");
            System.out.println("1. Them Chu xe moi (Owner)");
            System.out.println("2. Them Xe moi (Vehicle)");
            System.out.println("3. Xem danh sach Chu xe");
            System.out.println("4. Xem danh sach Xe");
            System.out.println("5. Tim kiem xe theo Bien so");
            System.out.println("6. Tim kiem xe theo Ten chu xe");
            System.out.println("7. Cap nhat thong tin Chu xe");
            System.out.println("8. Xoa Xe");
            System.out.println("0. Quay lai Menu chinh");
            System.out.println("--------------------------------");
            System.out.print("Nhap lua chon (0-8): ");
            try {
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        Owner newOwner = new Owner();
                        newOwner.nhapInfo(sc);
                        try {
                            ownerController.themMoi(newOwner);
                            System.out.println("Them chu xe thanh cong! ID duoc cap: " + newOwner.getId());
                        } catch (DuplicateMaException e) {
                            System.out.println("Loi trung ma: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("Loi: " + e.getMessage());
                        }
                        break;
                    case 2:
                        Vehicle newVehicle = new Vehicle();
                        newVehicle.nhapInfo(sc);
                        Owner owner = ownerController.layTheoId(newVehicle.getOwnerId());
                        if (owner == null) {
                            System.out.println("Loi: Khong tim thay chu xe co ID = " + newVehicle.getOwnerId() + ". Vui long tao chu xe truoc!");
                            break;
                        }
                        Vehicle existing = vehicleController.layTheoId(newVehicle.getLicensePlate());
                        if (existing != null) {
                            System.out.println("Loi: Bien so xe '" + newVehicle.getLicensePlate() + "' da ton tai trong he thong!");
                            break;
                        }
                        vehicleController.themMoi(newVehicle);
                        System.out.println("Them xe thanh cong!");
                        break;
                    case 3:
                        List<Owner> owners = ownerController.layTatCa();
                        System.out.println("Danh sach chu xe:");
                        for (Owner o : owners) {
                            System.out.println(o.toString());
                        }
                        break;
                    case 4:
                        List<Vehicle> vehicles = vehicleController.layTatCa();
                        System.out.println("Danh sach xe trong gara:");
                        for (Vehicle v : vehicles) {
                            System.out.println(v.toString());
                        }
                        break;
                    case 5:
                        System.out.print("Nhap bien so xe can tim: ");
                        String lp = sc.nextLine().trim();
                        List<Vehicle> searchLp = vehicleController.searchByLicensePlate(lp);
                        if (searchLp.isEmpty()) {
                            System.out.println("Khong tim thay xe nao.");
                        } else {
                            for (Vehicle v : searchLp) {
                                System.out.println(v.toString());
                            }
                        }
                        break;
                    case 6:
                        System.out.print("Nhap ten chu xe de tim xe: ");
                        String ownerName = sc.nextLine().trim();
                        List<Vehicle> searchName = vehicleController.searchByOwnerName(ownerName);
                        if (searchName.isEmpty()) {
                            System.out.println("Khong tim thay xe nao.");
                        } else {
                            for (Vehicle v : searchName) {
                                System.out.println(v.toString());
                            }
                        }
                        break;
                    case 7:
                        System.out.print("Nhap ID Chu xe can cap nhat: ");
                        int editOwnerId = Integer.parseInt(sc.nextLine());
                        Owner editOwner = ownerController.layTheoId(editOwnerId);
                        if (editOwner == null) {
                            System.out.println("Khong tim thay chu xe!");
                        } else {
                            System.out.println("Thong tin hien tai: " + editOwner);
                            editOwner.nhapInfo(sc);
                            ownerController.capNhat(editOwner);
                            System.out.println("Cap nhat thong tin chu xe thanh cong!");
                        }
                        break;
                    case 8:
                        System.out.print("Nhap Bien so xe can xoa: ");
                        String deleteLp = sc.nextLine().trim();
                        Vehicle delVeh = vehicleController.layTheoId(deleteLp);
                        if (delVeh == null) {
                            System.out.println("Xe khong ton tai!");
                        } else {
                            vehicleController.xoa(deleteLp);
                            System.out.println("Da xoa xe thanh cong.");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lua chon khong hop le!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so nguyen hop le!");
            }
            System.out.println();
        } while (choice != 0);
    }

    // --- 2. QUAN LY KY THUAT VIEN ---
    private void menuMechanic() throws Exception {
        int choice = -1;
        do {
            System.out.println("--- MENU QUAN LY KY THUAT VIEN ---");
            System.out.println("1. Them Ky thuat vien");
            System.out.println("2. Xem danh sach Ky thuat vien (Sap xep theo ten)");
            System.out.println("3. Xem danh sach Ky thuat vien (Sap xep theo luong tang dan)");
            System.out.println("4. Xem danh sach Ky thuat vien (Sap xep theo luong giam dan)");
            System.out.println("5. Cap nhat thong tin Ky thuat vien");
            System.out.println("6. Xoa Ky thuat vien");
            System.out.println("0. Quay lai Menu chinh");
            System.out.println("---------------------------------");
            System.out.print("Nhap lua chon (0-6): ");
            try {
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        Mechanic newMechanic = new Mechanic();
                        newMechanic.nhapInfo(sc);
                        try {
                            mechanicController.themMoi(newMechanic);
                            System.out.println("Them ky thuat vien thanh cong! ID duoc cap: " + newMechanic.getId());
                        } catch (DuplicateMaException e) {
                            System.out.println("Loi trung ma: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("Loi: " + e.getMessage());
                        }
                        break;
                    case 2:
                        List<Mechanic> mechanicsByName = mechanicController.sortByName();
                        System.out.println("Danh sach ky thuat vien (Xep theo ten):");
                        for (Mechanic m : mechanicsByName) {
                            System.out.println(m.toString());
                        }
                        break;
                    case 3:
                        List<Mechanic> mechanicsBySalaryAsc = mechanicController.sortBySalary(true);
                        System.out.println("Danh sach ky thuat vien (Xep theo luong Tang dan):");
                        for (Mechanic m : mechanicsBySalaryAsc) {
                            System.out.println(m.toString());
                        }
                        break;
                    case 4:
                        List<Mechanic> mechanicsBySalaryDesc = mechanicController.sortBySalary(false);
                        System.out.println("Danh sach ky thuat vien (Xep theo luong Giam dan):");
                        for (Mechanic m : mechanicsBySalaryDesc) {
                            System.out.println(m.toString());
                        }
                        break;
                    case 5:
                        System.out.print("Nhap ID Ky thuat vien can cap nhat: ");
                        int editMechId = Integer.parseInt(sc.nextLine());
                        Mechanic editMech = mechanicController.layTheoId(editMechId);
                        if (editMech == null) {
                            System.out.println("Khong tim thay ky thuat vien!");
                        } else {
                            System.out.println("Thong tin hien tai: " + editMech);
                            editMech.nhapInfo(sc);
                            mechanicController.capNhat(editMech);
                            System.out.println("Cap nhat thong tin ky thuat vien thanh cong!");
                        }
                        break;
                    case 6:
                        System.out.print("Nhap ID Ky thuat vien can xoa: ");
                        int deleteMechId = Integer.parseInt(sc.nextLine());
                        Mechanic delMech = mechanicController.layTheoId(deleteMechId);
                        if (delMech == null) {
                            System.out.println("Ky thuat vien khong ton tai!");
                        } else {
                            mechanicController.xoa(deleteMechId);
                            System.out.println("Da xoa ky thuat vien thanh cong.");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lua chon khong hop le!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so nguyen hop le!");
            }
            System.out.println();
        } while (choice != 0);
    }

    // --- 3. QUAN LY KHO LINH KIEN ---
    private void menuPart() throws Exception {
        int choice = -1;
        do {
            System.out.println("--- MENU QUAN LY KHO LINH KIEN ---");
            System.out.println("1. Them Linh kien moi");
            System.out.println("2. Xem danh sach linh kien (Sap xep theo gia ban tang dan)");
            System.out.println("3. Xem danh sach linh kien (Sap xep theo gia ban giam dan)");
            System.out.println("4. Xem danh sach linh kien (Sap xep theo ton kho tang dan)");
            System.out.println("5. Tim kiem linh kien theo Ten");
            System.out.println("6. Cap nhat Linh kien");
            System.out.println("7. Xoa Linh kien");
            System.out.println("0. Quay lai Menu chinh");
            System.out.println("---------------------------------");
            System.out.print("Nhap lua chon (0-7): ");
            try {
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        LinhKien newPart = new LinhKien();
                        newPart.nhapInfo(sc);
                        partController.themMoi(newPart);
                        System.out.println("Them linh kien vao kho thanh cong! Ma duoc cap: " + newPart.getMa());
                        break;
                    case 2:
                        List<LinhKien> partsPriceAsc = partController.sortByPrice(true);
                        System.out.println("Kho linh kien (Sap xep gia Tang dan):");
                        for (LinhKien p : partsPriceAsc) {
                            System.out.println(p.toString());
                        }
                        break;
                    case 3:
                        List<LinhKien> partsPriceDesc = partController.sortByPrice(false);
                        System.out.println("Kho linh kien (Sap xep gia Giam dan):");
                        for (LinhKien p : partsPriceDesc) {
                            System.out.println(p.toString());
                        }
                        break;
                    case 4:
                        List<LinhKien> partsQtyAsc = partController.sortByQuantity(true);
                        System.out.println("Kho linh kien (Sap xep ton kho Tang dan):");
                        for (LinhKien p : partsQtyAsc) {
                            System.out.println(p.toString());
                        }
                        break;
                    case 5:
                        System.out.print("Nhap ten linh kien can tim kiem: ");
                        String partNameKeyword = sc.nextLine().trim();
                        List<LinhKien> partsByName = partController.searchByName(partNameKeyword);
                        if (partsByName.isEmpty()) {
                            System.out.println("Khong tim thay linh kien nao.");
                        } else {
                            for (LinhKien p : partsByName) {
                                System.out.println(p.toString());
                            }
                        }
                        break;
                    case 6:
                        System.out.print("Nhap Ma Linh kien can cap nhat: ");
                        String editPartId = sc.nextLine().trim();
                        LinhKien editPart = partController.layTheoId(editPartId);
                        if (editPart == null) {
                            System.out.println("Khong tim thay linh kien!");
                        } else {
                            System.out.println("Thong tin hien tai: " + editPart);
                            editPart.nhapInfo(sc);
                            partController.capNhat(editPart);
                            System.out.println("Cap nhat thong tin linh kien thanh cong!");
                        }
                        break;
                    case 7:
                        System.out.print("Nhap Ma Linh kien can xoa: ");
                        String deletePartId = sc.nextLine().trim();
                        LinhKien delPart = partController.layTheoId(deletePartId);
                        if (delPart == null) {
                            System.out.println("Linh kien khong ton tai!");
                        } else {
                            partController.xoa(deletePartId);
                            System.out.println("Da xoa linh kien thanh cong.");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lua chon khong hop le!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so nguyen hop le!");
            }
            System.out.println();
        } while (choice != 0);
    }

    // --- 4. QUAN LY PHIEU SUA CHUA ---
    private void menuRepairOrder() throws Exception {
        int choice = -1;
        do {
            System.out.println("--- MENU PHIEU SUA CHUA ---");
            System.out.println("1. Lap Phieu sua chua moi (Tiep nhan xe)");
            System.out.println("2. Xem danh sach tat ca Phieu sua chua");
            System.out.println("3. Them Linh kien / Dich vu sua chua (Cho xe)");
            System.out.println("4. Xem chi tiet cong viec cua Phieu sua chua");
            System.out.println("5. Cap nhat trang thai Phieu (RECEIVING -> REPAIRING -> COMPLETED)");
            System.out.println("0. Quay lai Menu chinh");
            System.out.println("---------------------------");
            System.out.print("Nhap lua chon (0-5): ");
            try {
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        RepairOrder order = new RepairOrder();
                        order.nhapInfo(sc);
                        Vehicle v = vehicleController.layTheoId(order.getLicensePlate());
                        if (v == null) {
                            System.out.println("Loi: Xe co bien so '" + order.getLicensePlate() + "' chua duoc dang ky tiep nhan! Vui long them xe truoc.");
                            break;
                        }
                        Mechanic m = mechanicController.layTheoId(order.getMechanicId());
                        if (m == null) {
                            System.out.println("Loi: Khong tim thay ky thuat vien co ID = " + order.getMechanicId());
                            break;
                        }
                        repairOrderController.themMoi(order);
                        System.out.println("Lap phieu sua chua thanh cong! ID Phieu: " + order.getOrderId());
                        break;
                    case 2:
                        List<RepairOrder> orders = repairOrderController.layTatCa();
                        System.out.println("Danh sach phieu sua chua:");
                        for (RepairOrder o : orders) {
                            System.out.println(o.toString());
                        }
                        break;
                    case 3:
                        System.out.print("Nhap ID Phieu sua chua can them linh kien/dich vu: ");
                        int orderId = Integer.parseInt(sc.nextLine());
                        RepairOrder oObj = repairOrderController.layTheoId(orderId);
                        if (oObj == null) {
                            System.out.println("Phieu sua chua khong ton tai!");
                            break;
                        }
                        if (oObj.getStatus().equals("COMPLETED")) {
                            System.out.println("Loi: Phieu sua chua nay da hoan thanh va chot don, khong the sua doi!");
                            break;
                        }
                        
                        System.out.println("Chon loai hang muc can them:");
                        System.out.println("1. Linh kien (Phu tung)");
                        System.out.println("2. Dich vu (Cong tho)");
                        System.out.print("Nhap lua chon (1-2): ");
                        int typeChoice = Integer.parseInt(sc.nextLine());
                        
                        RepairOrderDetail detail = new RepairOrderDetail();
                        detail.setOrderId(orderId);
                        
                        if (typeChoice == 1) {
                            System.out.print("Nhap Ma Linh kien: ");
                            String maLK = sc.nextLine().trim();
                            LinhKien lk = partController.layTheoId(maLK);
                            if (lk == null) {
                                System.out.println("Loi: Khong tim thay linh kien voi ma: " + maLK);
                                break;
                            }
                            System.out.print("Nhap so luong su dung: ");
                            int qty = Integer.parseInt(sc.nextLine());
                            if (qty <= 0) {
                                System.out.println("Loi: So luong phai > 0!");
                                break;
                            }
                            detail.setMaHangMuc(maLK);
                            detail.setSoLuong(qty);
                        } else if (typeChoice == 2) {
                            System.out.print("Nhap Ma Dich vu: ");
                            String maDV = sc.nextLine().trim();
                            DichVu dv = dichVuDAO.layTheoId(maDV);
                            if (dv == null) {
                                System.out.println("Loi: Khong tim thay dich vu voi ma: " + maDV);
                                break;
                            }
                            detail.setMaHangMuc(maDV);
                            detail.setSoLuong(1); // quantity is always 1 for service
                        } else {
                            System.out.println("Lua chon loai khong hop le!");
                            break;
                        }
                        
                        // Add detail using transaction method
                        try {
                            repairOrderController.themMoiChiTietVaTruKho(detail);
                            System.out.println("Da them vao phieu sua chua!");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                        
                        // Automatically update status to REPAIRING if it was RECEIVING
                        if (oObj.getStatus().equals("RECEIVING")) {
                            oObj.setStatus("REPAIRING");
                            repairOrderController.capNhat(oObj);
                            System.out.println("Trang thai phieu tu dong chuyen sang: REPAIRING.");
                        }
                        break;
                    case 4:
                        System.out.print("Nhap ID Phieu sua chua can xem chi tiet: ");
                        int showOrderId = Integer.parseInt(sc.nextLine());
                        RepairOrder ro = repairOrderController.layTheoId(showOrderId);
                        if (ro == null) {
                            System.out.println("Phieu sua chua khong ton tai!");
                        } else {
                            System.out.println("=== THONG TIN PHIEU SUA CHUA ===");
                            System.out.println(ro);
                            System.out.println("--- CHI TIET LINH KIEN & CONG VIEC (SU DUNG TINH DA HINH) ---");
                            
                            List<HangMuc> listChiTiet = repairOrderController.getDanhSachChiTiet(showOrderId);
                            List<RepairOrderDetail> rawDetails = repairOrderController.getDetailsByOrderId(showOrderId);
                            
                            if (listChiTiet.isEmpty()) {
                                System.out.println("Chua co linh kien hay cong viec nao duoc ghi nhan.");
                            } else {
                                double totalParts = 0;
                                double totalLabor = 0;
                                
                                for (int i = 0; i < listChiTiet.size(); i++) {
                                    HangMuc hm = listChiTiet.get(i);
                                    int qty = rawDetails.get(i).getSoLuong();
                                    double thanhTien = hm.tinhThanhTien(qty); // polymorphic call
                                    
                                    if (hm instanceof LinhKien) {
                                        System.out.println("- [Linh Kien] " + hm.getTen() + " | Ma: " + hm.getMa() +
                                                           " | So luong: " + qty + " | Don gia: " + String.format("%,.0f", hm.getDonGia()) + 
                                                           " VND | Thanh tien: " + String.format("%,.0f", thanhTien) + " VND");
                                        totalParts += thanhTien;
                                    } else if (hm instanceof DichVu) {
                                        System.out.println("- [Dich Vu] " + hm.getTen() + " | Ma: " + hm.getMa() +
                                                           " | Don gia (Tron goi): " + String.format("%,.0f", hm.getDonGia()) + 
                                                           " VND | Thanh tien: " + String.format("%,.0f", thanhTien) + " VND");
                                        totalLabor += thanhTien;
                                    }
                                }
                                System.out.println("--------------------------------------");
                                System.out.println("Tong tien linh kien: " + String.format("%,.0f", totalParts) + " VND");
                                System.out.println("Tong tien cong: " + String.format("%,.0f", totalLabor) + " VND");
                            }
                        }
                        break;
                    case 5:
                        System.out.print("Nhap ID Phieu sua chua can cap nhat trang thai: ");
                        int editStatusId = Integer.parseInt(sc.nextLine());
                        RepairOrder orderStatus = repairOrderController.layTheoId(editStatusId);
                        if (orderStatus == null) {
                            System.out.println("Phieu sua chua khong ton tai!");
                            break;
                        }
                        System.out.println("Trang thai hien tai: " + orderStatus.getStatus());
                        System.out.print("Nhap trang thai moi (1 - RECEIVING, 2 - REPAIRING, 3 - COMPLETED): ");
                        int stChoice = Integer.parseInt(sc.nextLine());
                        String newStatus = "";
                        if (stChoice == 1) newStatus = "RECEIVING";
                        else if (stChoice == 2) newStatus = "REPAIRING";
                        else if (stChoice == 3) newStatus = "COMPLETED";
                        else {
                            System.out.println("Lua chon khong hop le!");
                            break;
                        }
                        
                        orderStatus.setStatus(newStatus);
                        if (newStatus.equals("COMPLETED")) {
                            orderStatus.setExitDate(new Date());
                        } else {
                            orderStatus.setExitDate(null);
                        }
                        
                        repairOrderController.capNhat(orderStatus);
                        System.out.println("Cap nhat trang thai phieu sua chua thanh cong!");
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lua chon khong hop le!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so nguyen hop le!");
            }
            System.out.println();
        } while (choice != 0);
    }

    // --- 5. THANH TOAN & HOA DON ---
    private void menuInvoice() throws Exception {
        int choice = -1;
        do {
            System.out.println("--- MENU QUAN LY THANH TOAN & HOA DON ---");
            System.out.println("1. Thanh toan va Xuat hoa don cho xe");
            System.out.println("2. Xem danh sach tat ca hoa don da thanh toan");
            System.out.println("0. Quay lai Menu chinh");
            System.out.println("-----------------------------------------");
            System.out.print("Nhap lua chon (0-2): ");
            try {
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        System.out.print("Nhap ID Phieu sua chua can thanh toan: ");
                        int orderId = Integer.parseInt(sc.nextLine());
                        RepairOrder ro = repairOrderController.layTheoId(orderId);
                        if (ro == null) {
                            System.out.println("Phieu sua chua khong ton tai!");
                            break;
                        }
                        
                        if (!ro.getStatus().equals("COMPLETED")) {
                            System.out.println("Loi: Phieu sua chua phai co trang thai 'COMPLETED' (Da hoan thanh sua chua) moi co the thanh toan!");
                            System.out.println("Vui long vao muc 4, cap nhat trang thai phieu sang COMPLETED truoc.");
                            break;
                        }
                        
                        Invoice existingInv = invoiceController.getByOrderId(orderId);
                        if (existingInv != null) {
                            System.out.println("Phieu sua chua nay da duoc thanh toan truoc do!");
                            System.out.println("Chi tiet hoa don: " + existingInv);
                            break;
                        }
                        
                        Invoice generated = invoiceController.generateInvoice(orderId);
                        invoiceController.themMoi(generated);
                        
                        System.out.println("====================================================");
                        System.out.println("          HOA DON THANH TOAN GARA O TO              ");
                        System.out.println("====================================================");
                        System.out.println("Ma hoa don: " + generated.getInvoiceId());
                        System.out.println("Ma phieu sua chua: " + generated.getOrderId());
                        System.out.println("Ngay thanh toan: " + generated.getPaymentDate());
                        System.out.println("Bien so xe: " + ro.getLicensePlate());
                        System.out.println("Tong tien linh kien: " + String.format("%,.0f", generated.getTotalPartCost()) + " VND");
                        System.out.println("Tong tien cong: " + String.format("%,.0f", generated.getTotalLaborCost()) + " VND");
                        System.out.println("Thue VAT (10%): " + String.format("%,.0f", (generated.getTotalPartCost() + generated.getTotalLaborCost()) * 0.10) + " VND");
                        System.out.println("----------------------------------------------------");
                        System.out.println("TONG TIEN THANH TOAN (Gom VAT): " + String.format("%,.0f", generated.getTotalAmount()) + " VND");
                        System.out.println("====================================================");
                        break;
                    case 2:
                        List<Invoice> invoices = invoiceController.layTatCa();
                        System.out.println("Danh sach hoa don da thanh toan:");
                        for (Invoice inv : invoices) {
                            System.out.println(inv.toString());
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lua chon khong hop le!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so nguyen hop le!");
            }
            System.out.println();
        } while (choice != 0);
    }

    // --- 6. BAO CAO THONG KE ---
    private void menuReport() throws Exception {
        int choice = -1;
        do {
            System.out.println("--- MENU BAO CAO THONG KE ---");
            System.out.println("1. Thong ke doanh thu theo khoang thoi gian");
            System.out.println("2. Thong ke Top linh kien ban nhieu nhat");
            System.out.println("3. Thong ke Top xe sua chua nhieu nhat");
            System.out.println("4. Thong ke Top ky thuat vien lam viec nhieu nhat");
            System.out.println("0. Quay lai Menu chinh");
            System.out.println("-----------------------------");
            System.out.print("Nhap lua chon (0-4): ");
            try {
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        System.out.print("Nhap tu ngay (yyyy-MM-dd, hoac Enter de lay het): ");
                        String from = sc.nextLine().trim();
                        System.out.print("Nhap den ngay (yyyy-MM-dd, hoac Enter de lay het): ");
                        String to = sc.nextLine().trim();
                        double revenue = reportController.getRevenue(from, to);
                        System.out.println("----------------------------------------------");
                        System.out.println("TONG DOANH THU THU DUOC: " + String.format("%,.0f", revenue) + " VND");
                        System.out.println("----------------------------------------------");
                        break;
                    case 2:
                        System.out.print("Nhap so luong linh kien can lay (limit): ");
                        int limitParts = Integer.parseInt(sc.nextLine());
                        Map<String, Integer> partsReport = reportController.getMostUsedParts(limitParts);
                        System.out.println("=== TOP LINH KIEN SU DUNG NHIEU NHAT ===");
                        int idx1 = 1;
                        for (Map.Entry<String, Integer> entry : partsReport.entrySet()) {
                            System.out.println(idx1++ + ". " + entry.getKey() + " | So luong: " + entry.getValue());
                        }
                        break;
                    case 3:
                        System.out.print("Nhap so luong xe can lay (limit): ");
                        int limitVehicles = Integer.parseInt(sc.nextLine());
                        Map<String, Integer> vehiclesReport = reportController.getMostRepairedVehicles(limitVehicles);
                        System.out.println("=== TOP XE DUOC SUA CHUA NHIEU NHAT ===");
                        int idx2 = 1;
                        for (Map.Entry<String, Integer> entry : vehiclesReport.entrySet()) {
                            System.out.println(idx2++ + ". " + entry.getKey() + " | So lan sua: " + entry.getValue());
                        }
                        break;
                    case 4:
                        System.out.print("Nhap so luong ky thuat vien can lay (limit): ");
                        int limitMechs = Integer.parseInt(sc.nextLine());
                        Map<String, Integer> mechsReport = reportController.getMostActiveMechanics(limitMechs);
                        System.out.println("=== TOP KY THUAT VIEN DUOC GIAO NHIEU VIEC ===");
                        int idx3 = 1;
                        for (Map.Entry<String, Integer> entry : mechsReport.entrySet()) {
                            System.out.println(idx3++ + ". " + entry.getKey() + " | So phieu dam nhan: " + entry.getValue());
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lua chon khong hop le!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so nguyen hop le!");
            }
            System.out.println();
        } while (choice != 0);
    }

    // --- 7. QUAN LY DANH MUC DICH VU ---
    private void menuDichVu() throws Exception {
        int choice = -1;
        do {
            System.out.println("--- MENU QUAN LY DANH MUC DICH VU ---");
            System.out.println("1. Them Dich vu moi");
            System.out.println("2. Xem danh sach Dich vu");
            System.out.println("3. Tim kiem dich vu theo Ten");
            System.out.println("4. Cap nhat thong tin Dich vu");
            System.out.println("5. Xoa Dich vu");
            System.out.println("0. Quay lai Menu chinh");
            System.out.println("-------------------------------------");
            System.out.print("Nhap lua chon (0-5): ");
            try {
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        DichVu newDV = new DichVu();
                        newDV.nhapInfo(sc);
                        dichVuDAO.themMoi(newDV);
                        System.out.println("Them dich vu moi thanh cong! Ma duoc cap: " + newDV.getMa());
                        break;
                    case 2:
                        List<DichVu> list = dichVuDAO.layTatCa();
                        System.out.println("Danh sach dich vu cua gara:");
                        for (DichVu dv : list) {
                            System.out.println(dv.toString());
                        }
                        break;
                    case 3:
                        System.out.print("Nhap ten dich vu can tim: ");
                        String kw = sc.nextLine().trim();
                        List<DichVu> searchResult = dichVuDAO.searchByName(kw);
                        if (searchResult.isEmpty()) {
                            System.out.println("Khong tim thay dich vu nao.");
                        } else {
                            for (DichVu dv : searchResult) {
                                System.out.println(dv.toString());
                            }
                        }
                        break;
                    case 4:
                        System.out.print("Nhap Ma Dich vu can cap nhat: ");
                        String ma = sc.nextLine().trim();
                        DichVu editDV = dichVuDAO.layTheoId(ma);
                        if (editDV == null) {
                            System.out.println("Khong tim thay dich vu!");
                        } else {
                            System.out.println("Thong tin hien tai: " + editDV);
                            editDV.nhapInfo(sc);
                            dichVuDAO.capNhat(editDV);
                            System.out.println("Cap nhat thong tin dich vu thanh cong!");
                        }
                        break;
                    case 5:
                        System.out.print("Nhap Ma Dich vu can xoa: ");
                        String deleteMa = sc.nextLine().trim();
                        DichVu delDV = dichVuDAO.layTheoId(deleteMa);
                        if (delDV == null) {
                            System.out.println("Dich vu khong ton tai!");
                        } else {
                            dichVuDAO.xoa(deleteMa);
                            System.out.println("Da xoa dich vu thanh cong.");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lua chon khong hop le!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so nguyen hop le!");
            }
            System.out.println();
        } while (choice != 0);
    }
}
