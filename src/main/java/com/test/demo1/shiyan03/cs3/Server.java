package com.test.demo1.shiyan03.cs3;

import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 5000;
    private ContactDAO contactDAO;

    public Server() {
        this.contactDAO = new ContactDAO();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("服务器正在监听端口 " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClient(clientSocket);
                } catch (Exception e) {
                    System.err.println("处理客户端请求时发生错误：" + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("服务器启动失败：" + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String action = (String) in.readObject();
            Object data = in.readObject();
            String result = processRequest(action, data);
            out.writeObject(result);
            out.flush();
        }
    }

    private String processRequest(String action, Object data) {
        switch (action) {
            case "display": return contactDAO.getAllContacts();
            case "add": return contactDAO.addContact((Contact) data);
            case "modify": return contactDAO.modifyContact((Contact) data);
            case "delete": return contactDAO.deleteContact((String) data);
            default: return "未知操作";
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}