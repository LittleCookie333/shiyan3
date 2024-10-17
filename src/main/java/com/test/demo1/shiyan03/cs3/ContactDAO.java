package com.test.demo1.shiyan03.cs3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/csdb";
    private static final String USER = "root";
    private static final String PASSWORD = "040503";

    public ContactDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            initDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS contacts " +
                    "(name VARCHAR(100) PRIMARY KEY, " +
                    " address VARCHAR(255), " +
                    " phone VARCHAR(20))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String addContact(Contact contact) {
        String sql = "INSERT INTO contacts (name, address, phone) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getAddress());
            pstmt.setString(3, contact.getPhone());
            pstmt.executeUpdate();
            return "联系人添加成功~";
        } catch (SQLException e) {
            return "添加联系人失败: " + e.getMessage();
        }
    }

    public String modifyContact(Contact contact) {
        String sql = "UPDATE contacts SET address = ?, phone = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.getAddress());
            pstmt.setString(2, contact.getPhone());
            pstmt.setString(3, contact.getName());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0 ? "联系人修改成功~" : "未找到联系人";
        } catch (SQLException e) {
            return "修改联系人失败: " + e.getMessage();
        }
    }

    public String deleteContact(String name) {
        String sql = "DELETE FROM contacts WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0 ? "联系人删除成功~" : "未找到联系人";
        } catch (SQLException e) {
            return "删除联系人失败: " + e.getMessage();
        }
    }

    public String getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            return "获取联系人失败: " + e.getMessage();
        }

        if (contacts.isEmpty()) {
            return "通讯录是空的哦~请添加联系人~";
        } else {
            StringBuilder result = new StringBuilder();
            for (Contact contact : contacts) {
                result.append(contact.toString()).append("\n");
            }
            return result.toString();
        }
    }
}