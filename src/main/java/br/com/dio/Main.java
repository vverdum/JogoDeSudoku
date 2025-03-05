package br.com.dio;
import br.com.dio.model.Board;
import br.com.dio.model.GameStatusEnum;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.util.stream.Stream;

import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);
    private static Board board;
    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        final var positions = Stream.of(args)
                .collect(toMap(
                        k -> k.split(";")[0],// Define a chave do mapa
                        v -> v.split(";")[1] //Define o valor do mapa
                ));
        // Exibe o mapa resultante
        positions.forEach((key, value) -> {
            System.out.println("Chave: " + key + ", Valor: " + value);
        });

        var option =-1;
        while(true){
            System.out.println("***** Selecione uma das opcoes a seguir *****");
            System.out.println("1 - Iniciar um novo jogo");
            System.out.println("2 - Colocar um novo numero");
            System.out.println("3 - Remover um numero");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar o jogo");
            System.out.println("7 - Finalizar o jogo");
            System.out.println("8 - Sair");
            option = scanner.nextInt();
            switch (option){
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                    default -> System.out.println("Opcao invalida, selecione uma opcao do menu");
            }
        }
    }

    private static void startGame(Map<String, String> positions) {
        if (nonNull(board)){
            System.out.println("O jogo já foi iniciado");
            return;
        }
        List<List<Space>> spaces = new ArrayList<>();
        for(int i = 0; i < BOARD_LIMIT; i++){
            spaces.add((new ArrayList<>()));
            for (int j = 0; j < BOARD_LIMIT; j++){
                var positionConfig = positions.get("%s,%s".formatted(i,j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }
        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar");
    }

    private static void inputNumber() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.println("Informe a coluna em que o numero será inserido");
        var col = runUntilGetValidNumber(0,8);
        System.out.println("Informe a linha em que o numero será inserido");
        var row = runUntilGetValidNumber(0,8);
        System.out.printf("Informe o numero que vai entrar no posição [%s, %s]\n ", col, row);
        var value = runUntilGetValidNumber(1,9);
        if(!board.changeValue(col,row,value)){
            System.out.printf("A posição [%s, %s] tem um valor fixo\n ", col, row);
        }
        board.changeValue(col,row,value);
    }

    private static void removeNumber() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.println("Informe a coluna do numero a ser removido");
        var col = runUntilGetValidNumber(0,8);
        System.out.println("Informe a linha do numero a ser removido");
        var row = runUntilGetValidNumber(0,8);
        if(!board.clearValue(col,row)){
            System.out.printf("A posição [%s, %s] tem um valor fixo\n ", col, row);
        }
    }

    private static void showCurrentGame() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        var args = new Object[BOARD_LIMIT * BOARD_LIMIT];
        var argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++){
            for(var col:board.getSpaces()) {
                args[argPos ++] = ((isNull(col.get(i).getActual())) ? "  " : col.get(i).getActual()+" ");
            }

        }
        System.out.println(" Seu jogo se encontra da seguinte forma:");
        System.out.printf((BOARD_TEMPLATE)+ " %n", args);
    }



    private static void showGameStatus() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.printf(" O jogo se encontra no status %s\n", board.getStatus());
        if(board.hasErrors()){
            System.out.println(" O jogo contém erros");
        }else {
            System.out.println("O jogo não contém erros");
        }
    }

    private static void clearGame() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.println("Tem certeza que deseja limpar o seu jogo e perder todo o seu progresso?");
        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") && ! confirm.equalsIgnoreCase("não")){
            System.out.println("Informe sim ou não");
            confirm = scanner.next();
        }
        if (confirm.equalsIgnoreCase("sim")){
            board.reset();
        }

    }

    private static void finishGame() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        if (board.gameIsFinished()){
            System.out.println("Parabéns, você concluiu o jogo");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("O jogo contém erros");
        } else if (board.getStatus().equals(GameStatusEnum.INCOMPLETE)) {
            System.out.println("O jogo está incompleto");

        }
    }
    private static int runUntilGetValidNumber(final int min, final int max){
        var current = scanner.nextInt();
        while(current < min || current > max){
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current =scanner.nextInt();
        }
        return current;
    }

    
}