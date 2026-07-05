/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.controller;

import java.util.Map;

/**
 *
 * @author ManhQuynh
 */
public interface IReportService {
    double getRevenue(String fromDate, String toDate) throws Exception;
    Map<String, Integer> getMostUsedParts(int limit) throws Exception;
    Map<String, Integer> getMostRepairedVehicles(int limit) throws Exception;
    Map<String, Integer> getMostActiveMechanics(int limit) throws Exception;
}
