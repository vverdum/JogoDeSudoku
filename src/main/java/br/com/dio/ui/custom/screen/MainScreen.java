package br.com.dio.ui.custom.screen;
import br.com.dio.model.Space;
import br.com.dio.service.BoardService;
import br.com.dio.service.NotifierService;
import br.com.dio.ui.custom.button.CheckGamesStatusButton;
import br.com.dio.ui.custom.button.FinishGameButton;
import br.com.dio.ui.custom.button.ResetGameButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.input.NumberText;
import br.com.dio.ui.custom.panel.MainPanel;
import br.com.dio.ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static br.com.dio.service.EventNum.CLEAR_SPACE;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class MainScreen {
    private final static Dimension dimension = new Dimension(600,600);
    private static BoardService boardService;
    public static  NotifierService notifierService;
    private static JButton finishGameButton;
    private static JButton checkGameStatusButton;
    private static JButton resetButton;

    public MainScreen(final Map<String,String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }


    public static void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        for(int r = 0; r < 9; r+=3) {
            var endRow = r + 2;
            for (int c = 0; c < 9; c += 3) {
                var endCol = c + 2;
                var spaces = getSpacesFromSector(boardService.getSpaces(),c,endCol,r,endRow);
                JPanel sector = generateSection(spaces);
                mainPanel.add(sector);
            }
        }
        addResetButton(mainPanel);
        addShowGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private static void addFinishGameButton(final JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e ->{
            if(boardService.gameIsFinished()){
                showMessageDialog(null,"Parabéns, você concluiu o jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                var message = "Seu jogo contém alguma inconsistencia,\n ajuste e tente novamente.";
                showMessageDialog(null, message);
            }
        });
        mainPanel.add(finishGameButton);
    }
    private static void addShowGameStatusButton(final JPanel mainPanel) {
        checkGameStatusButton = new CheckGamesStatusButton(e->{
        var hasErrors = boardService.hasErrors();
        var gameStatus = boardService.getStatus();
        var message = switch (gameStatus){
            case NON_STARTED -> " O jogo não foi iniciado";
            case INCOMPLETE -> " O jogo está incompleto";
            case COMPLETE -> " O jogo está completo";
        };
        message += hasErrors ? " e contém erros": " e não contém erros";
        showMessageDialog(null, message);
    });
        mainPanel.add(checkGameStatusButton);
    }

    private static void addResetButton(final JPanel mainPanel) {
        resetButton = new ResetGameButton(e->{
            var dialogResult = showConfirmDialog(
                    null,
                    "Quer mesmo reiniciar o jogo?",
                    "Limpar o jogo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (dialogResult == 0){
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }
        });
        mainPanel.add(resetButton);
    }
    private static List<Space> getSpacesFromSector(final List<List<Space>> spaces,
                                                   final int initCol, final int endCol,
                                                   final int initRow, final int endRow){
        List<Space> spaceSector = new ArrayList<>();
        for(int r = initRow; r<= endRow; r++) {
            for (int c = initCol; c <= endCol; c++) {
                spaceSector.add(spaces.get(c).get(r));
            }
        }
        return spaceSector;
    }
    private static JPanel generateSection(final List<Space> spaces){
        List<NumberText>  fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscriber(CLEAR_SPACE,t));
        return new SudokuSector(fields);

    }
}
