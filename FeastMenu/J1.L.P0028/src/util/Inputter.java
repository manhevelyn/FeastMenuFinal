/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

// Class nhập dữ liệu

import java.util.Scanner;

public class Inputter {
    // int,String
    // Class
    private Scanner scanner = new Scanner(System.in);
// Non-static
    public String inputString(String msg) {
        System.out.println(msg);
        return scanner.nextLine();
    }
    public int inputInt(String msg) {
        System.out.println(msg);
        return Integer.parseInt(scanner.nextLine());
   
}
}
