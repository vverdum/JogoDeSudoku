package br.com.dio;
import br.com.dio.ui.custom.screen.MainScreen;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toMap;

public class UIMain {
    public static void main(String[] args) {
        final var gameConfig = Stream.of(args)
                .collect(toMap(
                        k -> k.split(";")[0],// Define a chave do mapa
                        v -> v.split(";")[1] //Define o valor do mapa
                ));

        var mainScreen = new MainScreen(gameConfig);
        MainScreen.buildMainScreen();


    }
}
