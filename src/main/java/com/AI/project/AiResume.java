package com.AI.project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AiResume extends JFrame {

    private JTextField text;

    private JLabel fileLabel;

    public AiResume() {
        setTitle("Ai Resume Analyzer");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationByPlatform(false);

        /*
         * ImageIcon imageIcon=new ImageIcon(
         * "/home/anand/Frame-Work/AI/src/main/java/com/AI/project/images.jpeg");
         * JLabel backGround=new JLabel(imageIcon);
         * add(backGround);
         * panel.add(backGround);
         */

        JPanel panel = new JPanel();
        panel.setBounds(0, 2, 250, 666);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setBackground(Color.DARK_GRAY);
        panel.setOpaque(true);
        add(panel);

        JFileChooser filechoose = new JFileChooser();

        add(filechoose);

        fileLabel = new JLabel("Filename :");
        fileLabel.setBounds(370, 255, 62, 30);
        add(fileLabel);

        text = new JTextField();
        text.setBounds(445, 260, 150, 25);
        // text.setEditable(false);
        add(text);

        dashbord();

        JButton b = new JButton("Upload");
        b.setBounds(450, 300, 100, 30);
        b.setBackground(Color.CYAN);
        b.setForeground(Color.DARK_GRAY);

        add(b);
        b.addActionListener(e -> {
            if (e.getSource() == b) {
                int file = filechoose.showOpenDialog(null);

                text.setText(filechoose.getSelectedFile().getName());
            }
        });

        setVisible(true);
    }

    void dashbord() {

        Font font = new Font("Arial", Font.BOLD, 14);

        JButton uploadButton = new JButton("Upload");
        uploadButton.setBounds(300, 50, 80, 20);
        uploadButton.setForeground(Color.BLUE);
        uploadButton.setContentAreaFilled(false);
        uploadButton.setFocusPainted(true);
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

    // public static void main(String[] args) {

    // new AiResume();

    // }

}
