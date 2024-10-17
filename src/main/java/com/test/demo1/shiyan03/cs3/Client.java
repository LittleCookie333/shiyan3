package com.test.demo1.shiyan03.cs3;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("------个人通讯录系统------");
            System.out.println("1. 查看联系人信息");
            System.out.println("2. 添加新联系人");
            System.out.println("3. 修改联系人信息");
            System.out.println("4. 删除联系人");
            System.out.println("5. 退出");
            System.out.print("请选择操作: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
            System.out.println("-----------------------");

            switch (choice) {
                case 1: displayContacts(); break;
                case 2: addContact(); break;
                case 3: modifyContact(); break;
                case 4: deleteContact(); break;
                case 5: System.out.println("Bye bye~"); return;
                default: System.out.println("非法选择!请再试一次~");
            }
        }
    }

    private void displayContacts() {
        System.out.println("--------查看联系人-------");
        String response = sendRequest("display", null);
        System.out.println(response);
    }

    private void addContact() {
        System.out.println("--------添加联系人-------");
        System.out.print("请输入联系人姓名: ");
        String name = scanner.nextLine();
        System.out.print("请输入联系人地址: ");
        String address = scanner.nextLine();
        System.out.print("请输入联系人电话: ");
        String phone = scanner.nextLine();

        Contact contact = new Contact(name, address, phone);
        String response = sendRequest("add", contact);
        System.out.println(response);
    }

    private void modifyContact() {
        System.out.println("--------修改联系人-------");
        System.out.print("请输入要修改的联系人姓名: ");
        String name = scanner.nextLine();
        System.out.print("请输入新住址: ");
        String address = scanner.nextLine();
        System.out.print("请输入新电话: ");
        String phone = scanner.nextLine();

        Contact contact = new Contact(name, address, phone);
        String response = sendRequest("modify", contact);
        System.out.println(response);
    }

    private void deleteContact() {
        System.out.println("--------删除联系人-------");
        System.out.print("请输入要删除的联系人姓名: ");
        String name = scanner.nextLine();
        String response = sendRequest("delete", name);
        System.out.println(response);
    }

    private String sendRequest(String action, Object data) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(action);
            out.writeObject(data);
            out.flush();

            return (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "操作失败: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        new Client().start();
    }
}