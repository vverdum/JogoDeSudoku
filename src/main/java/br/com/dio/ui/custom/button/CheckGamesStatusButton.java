package br.com.dio.ui.custom.button;

import javax.swing.*;
import java.awt.event.ActionListener;

public class CheckGamesStatusButton extends JButton {
    public CheckGamesStatusButton(final ActionListener actionListener) {
        this.setText("Verificar jogo ");
        this.addActionListener(actionListener);
    }
}
