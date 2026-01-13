package com.AI.project;

import javax.swing.*;
import java.awt.*;

public class Edit extends JFrame {

    Edit() {
        setTitle("Ai Resume Analyzer");
        setSize(800, 700);
        setLocationByPlatform(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 2, 250, 666);
        panel.setLayout(new BorderLayout(2, 2));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setBackground(Color.DARK_GRAY);

        add(panel);

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

        JButton editButton = new JButton("Edit");
        editButton.setBounds(645, 50, 80, 20);
        editButton.setBorder(BorderFactory.createEmptyBorder());
        editButton.setContentAreaFilled(false);
        editButton.setForeground(Color.BLUE);
        editButton.setFont(font);

        add(uploadButton);
        add(createButton);
        add(listButton);
        add(viewButton);
        add(editButton);
    }

}
