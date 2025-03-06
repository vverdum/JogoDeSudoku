package br.com.dio.ui.custom.input;

import br.com.dio.model.Space;
import br.com.dio.service.EventListener;
import br.com.dio.service.EventNum;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import static br.com.dio.service.EventNum.CLEAR_SPACE;

public class NumberText extends JTextField implements EventListener {
    private final Space space;

    public NumberText(Space space) {
        this.space = space;
        var dimension = new Dimension(50,50);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setFont(new Font("Arial", Font.PLAIN,20 ));
        this.setHorizontalAlignment(CENTER);
        this.setDocument(new NumberTexLimit());
        this.setEnabled(!space.isFixed());
        if(space.isFixed()){
            this.setText(space.getActual().toString());
        }
        this.getDocument().addDocumentListener(new DocumentListener() {
            private void changeSpace(){
                if(getText().isEmpty()){
                    space.clearSpace();
                    return;
                }
                space.setActual(Integer.parseInt(getText()));
            }
            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
    @Override
    public void update(EventNum eventType) {
        if(eventType.equals(CLEAR_SPACE) &&(this.isEnabled())){
            this.setText(" ");
        }
    }
}
