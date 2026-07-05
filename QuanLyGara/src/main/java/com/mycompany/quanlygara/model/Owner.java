/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlygara.model;

import java.util.Scanner;

/**
 *
 * @author ManhQuynh
 */
public class Owner extends Person {

    public Owner() {
        super();
    }

    public Owner(int id, String name, String phone, String address) {
        super(id, name, phone, address);
    }

    @Override
    public void nhapInfo(Scanner sc) {
        super.nhapInfo(sc);
    }

    @Override
    public String toString() {
        return "Owner [" + super.toString() + "]";
    }
}
