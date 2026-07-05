/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.controller;

import java.util.List;

/**
 *
 * @author ManhQuynh
 */
public interface IRepository<T> {
    void themMoi(T item) throws Exception;
    void capNhat(T item) throws Exception;
    void xoa(Object id) throws Exception;
    List<T> layTatCa() throws Exception;
    T layTheoId(Object id) throws Exception;
}
