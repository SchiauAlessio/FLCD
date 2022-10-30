import exceptions.ScannerException;
import model.MyScanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner programReader = new java.util.Scanner(new File("src\\resources\\p2.alx"));
            StringBuilder program = new StringBuilder();
            while (programReader.hasNextLine()) {
                program.append(programReader.nextLine()).append('\n');
            }
            programReader.close();
            Scanner tokensReader = new java.util.Scanner(new File("src\\resources\\token.in"));
            List<String> tokens = new ArrayList<>();
            while (tokensReader.hasNextLine()) {
                tokens.add(tokensReader.nextLine());
            }
            MyScanner scanner = new MyScanner(program.toString(), tokens);
            try {
                scanner.scan();
            } catch (ScannerException e) {
                System.out.println(e.getMessage());
                scanner.printToSTFile();
                scanner.printToPIFFile();
                return;
            }
            scanner.printToSTFile();
            scanner.printToPIFFile();
            System.out.println("lexically correct");
        } catch (FileNotFoundException e) {
            System.out.println("Source file not found");
        } catch (IOException e) {
            System.out.println("Can't write output");
        }
    }
}