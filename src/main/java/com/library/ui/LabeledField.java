package com.library.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LabeledField extends JPanel {
    JTextField textField;

    public LabeledField (String label){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel(label));
        textField = new JTextField(20);
        add(textField);
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }
}
