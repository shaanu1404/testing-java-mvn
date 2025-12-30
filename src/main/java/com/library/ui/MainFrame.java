package com.library.ui;

import com.library.model.Student;
import com.library.util.DBConnection;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    JPanel mainPanel;
    JPanel formPanel;
    DefaultTableModel model;
    JTable table;

    public MainFrame() {
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        addFormPanel();

        String[] columns = {"Id", "Name", "Email", "Age"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        mainPanel.add(new JScrollPane(table));

        List<Object[]> rows = fetchData();
        if (rows != null) {
            setDataToTable(rows);
        }

        add(mainPanel);
    }

    private List<Object[]> fetchData() {
        try{
            Connection connection = DBConnection.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM students");

            List<Object[]> rows = new ArrayList<>();

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String email = result.getString("email");
                int age = result.getInt("age");

                Student student = new Student(id, name, email, age);
                Object[] row = {
                        student.getId(),
                        student.getName(),
                        student.getEmail(),
                        student.getAge(),
                };
                rows.add(row);
            }

            connection.close();

            return rows;
        } catch (Exception e) {
            String message = "Error: " + e.getMessage();
            System.out.println(message);
            JOptionPane.showMessageDialog(this.getContentPane(), message);
            return null;
        }
    }

    private void addFormPanel() {
        formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        LabeledField nameField = new LabeledField("Name");
        formPanel.add(nameField);
        LabeledField emailField = new LabeledField("Email");
        formPanel.add(emailField);
        LabeledField ageField = new LabeledField("Age");
        formPanel.add(ageField);

        JButton submitButton = new JButton("Submit");
        formPanel.add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    String email = emailField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    addNewRow(name, email, age);

                    nameField.setText("");
                    emailField.setText("");
                    ageField.setText("");
                } catch ( NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(mainPanel, "Error: " + nfe.getMessage() + "\nPlease try again");
                }
            }
        });

        mainPanel.add(formPanel);
    }

    private void setDataToTable(List<Object[]> rows) {
        model.setRowCount(0);
        for (Object[] row: rows) {
            model.addRow(row);
        }
    }

    private void addNewRow(String name, String email, int age) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO students (name, email, age) values (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setInt(3, age);
            int newRows = preparedStatement.executeUpdate();
            if (newRows <= 0) throw new Exception("Failed to insert row to database");
            conn.close();

            JOptionPane.showMessageDialog(mainPanel, "Row inserted successfully");

            List<Object[]> rows = fetchData();
            if (rows != null) {
                setDataToTable(rows);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Error: " + e.getMessage() + "\nPlease try again");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
