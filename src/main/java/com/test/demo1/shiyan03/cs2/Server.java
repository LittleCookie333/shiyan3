package com.test.demo1.shiyan03.cs2;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 5000;
    private List<Contact> contacts = new ArrayList<>();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("服务器正在监听端口 " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                    String action = (String) in.readObject();
                    switch (action) {
                        case "add":
                            addContact((Contact) in.readObject());
                            out.writeObject("联系人添加成功~");
                            break;
                        case "modify":
                            modifyContact((Contact) in.readObject());
                            out.writeObject("联系人修改成功~");
                            break;
                        case "delete":
                            deleteContact((String) in.readObject());
                            out.writeObject("联系人删除成功~");
                            break;
                        case "display":
                            out.writeObject(contacts);
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addContact(Contact contact) {
        contacts.add(contact);
        System.out.println("添加联系人: " + contact);
    }

    private void modifyContact(Contact newContact) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getName().equals(newContact.getName())) {
                contacts.set(i, newContact);
                System.out.println("修改联系人: " + newContact);
                return;
            }
        }
        System.out.println("未找到联系人: " + newContact.getName());
    }

    private void deleteContact(String name) {
        contacts.removeIf(contact -> contact.getName().equals(name));
        System.out.println("删除联系人: " + name);
    }

    public static void main(String[] args) {
        new Server().start();
    }
}