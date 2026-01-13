package com.AI.project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Create extends JFrame {

    JLabel nameLabel, expLabel, skilLabel;
    JTextField nameText, expText, skillText;
    JButton add;

    Create() {
        setTitle("Ai Resume Analyzer");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationByPlatform(false);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 2, 250, 666);
        panel.setLayout(new BorderLayout(1, 1));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setBackground(Color.DARK_GRAY);
        panel.setOpaque(true);
        add(panel);

        nameLabel = new JLabel("Title:");
        nameLabel.setBounds(370, 255, 40, 30);
        add(nameLabel);

        nameText = new JTextField();
        nameText.setBounds(445, 260, 150, 25);
        nameText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(nameText);

        expLabel = new JLabel("Experince:");
        expLabel.setBounds(370, 310, 66, 25);
        add(expLabel);

        expText = new JTextField();
        expText.setBounds(445, 310, 150, 25);
        expText.setBorder(BorderFactory.createLineBorder(Color.black));
        add(expText);

        skilLabel = new JLabel("Skill:");
        skilLabel.setBounds(370, 360, 40, 25);
        add(skilLabel);

        skillText = new JTextField();
        skillText.setBounds(445, 360, 150, 25);
        skillText.setBorder(BorderFactory.createLineBorder(Color.black));
        // skillText.setOpaque(false);
        add(skillText);

        add = new JButton("ADD");
        add.setBounds(460, 405, 65, 25);
        add.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add.setBackground(Color.CYAN);
        add.setForeground(Color.DARK_GRAY);

        add.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "ADD SuccessFully");
            // JOptionPane.showMessageDialog(this, "Invalid credentials", "Error",
            // JOptionPane.ERROR_MESSAGE);
        });
        add(add);

        dashbord();

        setVisible(true);
    }

    void dashbord() {

        Font font = new Font("Arial", Font.BOLD, 14);

        JButton uploadButton = new JButton("Upload");
        uploadButton.setBounds(300, 50, 80, 20);
        uploadButton.setForeground(Color.BLUE);
        uploadButton.setContentAreaFilled(false);
        uploadButton.setFocusPainted(false);
        uploadButton.setBorder(BorderFactory.createEmptyBorder());
        uploadButton.setFont(font);
        uploadButton.addActionListener(e -> {
            dispose();
            new AiResume();

        });

        JButton createButton = new JButton("Create");
        createButton.setBounds(390, 50, 80, 20);
        createButton.setBorder(BorderFactory.createEmptyBorder());
        createButton.setContentAreaFilled(false);
        createButton.setForeground(Color.BLUE);
        createButton.setFont(font);
        createButton.addActionListener(e -> {
            dispose();
            new Create();
        });

        JButton listButton = new JButton("List");
        listButton.setBounds(480, 50, 80, 20);
        listButton.setBorder(BorderFactory.createEmptyBorder());
        listButton.setContentAreaFilled(false);
        listButton.setForeground(Color.BLUE);
        listButton.setFont(font);
        listButton.addActionListener(e -> {
            dispose();
            new List();
        });

        JButton viewButton = new JButton("View");
        viewButton.setBounds(570, 50, 80, 20);
        viewButton.setBorder(BorderFactory.createEmptyBorder());
        viewButton.setContentAreaFilled(false);
        viewButton.setForeground(Color.BLUE);
        viewButton.setFont(font);
        viewButton.addActionListener(e -> {
            dispose();
            new View();
        });

        JButton editButton = new JButton("Edit");
        editButton.setBounds(645, 50, 80, 20);
        editButton.setBorder(BorderFactory.createEmptyBorder());
        editButton.setContentAreaFilled(false);
        editButton.setForeground(Color.BLUE);
        editButton.setFont(font);
        editButton.addActionListener(e -> {
            dispose();
            new Edit();
        });

        add(uploadButton);
        add(createButton);
        add(listButton);
        add(viewButton);
        add(editButton);
    }

}
