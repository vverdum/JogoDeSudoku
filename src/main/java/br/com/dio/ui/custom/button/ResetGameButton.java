package br.com.dio.ui.custom.button;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ResetGameButton extends JButton {
    public ResetGameButton(final ActionListener actionListener) {
        this.setText("Reiniciar jogo");
        this.addActionListener(actionListener);

    }


}
