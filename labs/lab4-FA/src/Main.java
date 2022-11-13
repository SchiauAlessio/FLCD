import exceptions.ScannerException;
import model.FiniteAutomaton;
import model.MyScanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        try {
//            Scanner programReader = new java.util.Scanner(new File("src\\resources\\p2.alx"));
//            StringBuilder program = new StringBuilder();
//            while (programReader.hasNextLine()) {
//                program.append(programReader.nextLine()).append('\n');
//            }
//            programReader.close();
//            Scanner tokensReader = new java.util.Scanner(new File("src\\resources\\token.in"));
//            List<String> tokens = new ArrayList<>();
//            while (tokensReader.hasNextLine()) {
//                tokens.add(tokensReader.nextLine());
//            }
//            MyScanner scanner = new MyScanner(program.toString(), tokens);
//            try {
//                scanner.scan();
//            } catch (ScannerException e) {
//                System.out.println(e.getMessage());
//                scanner.printToSTFile();
//                scanner.printToPIFFile();
//                return;
//            }
//            scanner.printToSTFile();
//            scanner.printToPIFFile();
//            System.out.println("lexically correct");
//        } catch (FileNotFoundException e) {
//            System.out.println("Source file not found");
//        } catch (IOException e) {
//            System.out.println("Can't write output");
//        }

        FiniteAutomaton fa = new FiniteAutomaton("src\\resources\\fa.in");
        System.out.println("1. Print states");
        System.out.println("2. Print alphabet");
        System.out.println("3. Print output states");
        System.out.println("4. Print in state");
        System.out.println("5. Print transitions");
        System.out.println("6. Check word");
        System.out.println("0. Exit");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(">>");
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> fa.printStates();
                case "2" -> fa.printAlphabet();
                case "3" -> fa.printOutputStates();
                case "4" -> fa.printInitialState();
                case "5" -> fa.printTransitions();
                case "6" -> {
                    System.out.print("word: ");
                    String word = scanner.nextLine();
                    System.out.println(fa.checkAccepted(word));
                }
                case "0" -> System.exit(0);
            }
        }
    }
}