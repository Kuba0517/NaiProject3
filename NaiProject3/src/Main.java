import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LanguageRecognizer lr = new LanguageRecognizer();
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        while(!userInput.equals("q")) {
            System.out.println("Wprowadź swój tekst do sklasyfikowania: (lub wciśnij q aby wyjść)");
            userInput = scanner.nextLine();
            System.out.println("Klasyfikowany język: " + lr.classifyText(userInput));
        }
    }
}