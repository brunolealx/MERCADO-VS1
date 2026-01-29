package com.creativex.ui.ajuda;

import javax.swing.*;
import java.awt.*;

public class AjudaForm extends JPanel {
    public AjudaForm() {
        setLayout(new BorderLayout());
        add(new JLabel("❓ Ajuda — Documentação e suporte", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
