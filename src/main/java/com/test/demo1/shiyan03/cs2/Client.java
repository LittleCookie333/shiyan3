package com.test.demo1.shiyan03.cs2;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("------个人通讯录系统------");
            System.out.println("1. 添加新联系人");
            System.out.println("2. 修改联系人信息");
            System.out.println("3. 删除联系人");
            System.out.println("4. 查看联系人信息");
            System.out.println("5. 退出");
            System.out.print("请选择操作: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
            System.out.println("-----------------------");

            switch (choice) {
                case 1:
                    addContact();
                    break;
                case 2:
                    modifyContact();
                    break;
                case 3:
                    deleteContact();
                    break;
                case 4:
                    displayContacts();
                    break;
                case 5:
                    System.out.println("Bye bye~");
                    return;
                default:
                    System.out.println("非法选择!请再试一次~");
            }
        }
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
        System.out.println();
    }

    private void modifyContact() {
        System.out.println("--------修改联系人-------");
        System.out.print("请输入要修改的联系人姓名: ");
        String name = scanner.nextLine();
        System.out.print("请输入新地址: ");
        String address = scanner.nextLine();
        System.out.print("请输入新电话: ");
        String phone = scanner.nextLine();

        Contact contact = new Contact(name, address, phone);
        String response = sendRequest("modify", contact);
        System.out.println(response);
        System.out.println();
    }

    private void deleteContact() {
        System.out.println("--------删除联系人-------");
        System.out.print("请输入要删除的联系人姓名: ");
        String name = scanner.nextLine();
        String response = sendRequest("delete", name);
        System.out.println(response);
        System.out.println();
    }

    private void displayContacts() {
        System.out.println("--------查看联系人-------");
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("display");
            Object response = in.readObject();
            if (response instanceof List) {
                List<Contact> contacts = (List<Contact>) response;
                if (contacts.isEmpty()) {
                    System.out.println("通讯录是空的哦~请添加联系人~");
                } else {
                    for (Contact contact : contacts) {
                        System.out.println(contact);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private String sendRequest(String action, Object data) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(action);
            out.writeObject(data);
            return (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "操作失败!";
        }
    }

    public static void main(String[] args) {
        new Client().start();
    }
}