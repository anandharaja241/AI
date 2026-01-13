package com.AI.project;

import javax.swing.*;
import java.awt.*;
public class DashBord extends JFrame{
        
    void dashbord1() {

        Font font =new Font("Arial",Font.BOLD,14);

        JButton main = new JButton("Main");
        main.setBounds(300, 50, 80, 20);
        main.setForeground(Color.BLUE);
        main.setContentAreaFilled(false);
        main.setBorder(BorderFactory.createEmptyBorder());
        main.setFont(font);
        main.addActionListener(e ->{
            dispose();
            new AiResume();

        });

        JButton listButton = new JButton("List");
        listButton.setBounds(365, 50, 80, 20);
        listButton.setBorder(BorderFactory.createEmptyBorder());
        listButton.setContentAreaFilled(false);
        listButton.setForeground(Color.BLUE);
        listButton.setFont(font);

        JButton viewButton = new JButton("Page");
        viewButton.setBounds(430, 50, 80, 20);
        viewButton.setBorder(BorderFactory.createEmptyBorder());
        viewButton.setContentAreaFilled(false);
        viewButton.setForeground(Color.BLUE);
        viewButton.setFont(font);

        JButton editButton = new JButton("List");
        editButton.setBounds(480, 50, 80, 20);
        editButton.setBorder(BorderFactory.createEmptyBorder());
        editButton.setContentAreaFilled(false);
        editButton.setForeground(Color.BLUE);
        editButton.setFont(font);

        add(main);
        add(listButton);
        add(viewButton);
        add(editButton);
    }
}
