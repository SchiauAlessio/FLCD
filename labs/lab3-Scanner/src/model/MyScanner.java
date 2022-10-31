package model;

import exceptions.ScannerException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyScanner {
    private final String program;
    private final List<String> tokens;
    private final HashTable<String> symbolTable;
    private final HashTable<String> constantsTable;
    private final List<Pair<String,Integer>> pif;
    private int index;
    private int currentLine;

    private String faultToken;

    public MyScanner(String program, List<String> tokens) {
        this.program = program;
        this.tokens = tokens;
        this.symbolTable = new HashTable<>(107);
        this.constantsTable = new HashTable<>(107);
        this.pif = new ArrayList<>();
        this.index = 0;
        this.currentLine = 1;
    }

    public String getFaultToken() {
        return faultToken;
    }

    /**
     * Skips characters that are considered blank/whitespace.
     * @return true if any skips happened, false otherwise.
     */
    private boolean skipWhiteSpace() {
        boolean changed = false;
        while (index < program.length() && Character.isWhitespace(program.charAt(index))) {
            if (program.charAt(index) == '\n') {
                currentLine++;
                changed = true;
            }
            index++;
        }
        return changed;
    }

    /**
     * Checks whether at the current index the program starts with "//" and skips until the next line if it does<br>
     * If it is the last line we reach the end of the program
     * @return true if any comments were skipped, false otherwise.
     */
    private boolean skipComment() {
        boolean changed = false;
        if (program.startsWith("//", index)) {
            changed = true;
            while (index < program.length() && program.charAt(index) != '\n') {
                index++;
            }
        }
        return changed;
    }

    /**
     * Uses regex to match string constants.<br>
     * Since we only allow letters, digits, _ and space we first check whether the pattern matches
     * and if it does it is added to the constants table and the pif and we continue from the end of the constant<br>
     * If the pattern does not match we check whether it has valid quotation marks and if it does, we throw an exception
     * marking that it contains invalid characters<br>
     * If it does not have valid quotation marks we throw an exception marking that it does not have valid quotation marks
     * @return true if a match was found, false otherwise.
     * @throws ScannerException if an invalid string constant was found.
     */
    private boolean stringConstant() throws ScannerException {
        //must begin with " and end with " and must contain only letters, digits, _ and space
        Pattern strRegex = Pattern.compile("^\"([a-zA-z0-9_ ]*)\"");
        Matcher matcher = strRegex.matcher(program.substring(index));
        if (matcher.find()) {
            String token = matcher.group(1);
            pif.add(new Pair<>("strConst", -2));
            constantsTable.add(token);
            index += matcher.end();
            System.out.println("Found string constant: " + token);
            return true;
        }

        //starts with "  contains at least one character not " and ends with "
        strRegex = Pattern.compile("^\"[^\"]+\"");
        matcher = strRegex.matcher(program.substring(index));
        if (matcher.find()) {
            throw new ScannerException("Lexical error: Invalid characters inside string on line " + currentLine);
        }

        //start with "
        strRegex = Pattern.compile("^\"");
        matcher = strRegex.matcher(program.substring(index));
        if (matcher.find())
            throw new ScannerException("Lexical error: String not closed on line " + currentLine);
        return false;
    }

    /**
     * Uses regex to match int constants.<br>
     * An int constant is either zero or
     * @return true if a match was found, false otherwise.
     */
    private boolean intConstant() {
        Pattern intRegex = Pattern.compile("^([+-]?[1-9]\\d*|0)");
        Matcher matcher = intRegex.matcher(program.substring(index));
        if (matcher.find()) {
            String token = matcher.group(1);
            if (pif.size() > 0) {
                Integer pif_last = pif.get(pif.size() - 1).getValue();
                //special case. If the last element in the pif was not in token.in (token list) then we do not consider
                //it an intConstant (it would have been if pif_last was 0)
                if ((token.charAt(0) == '+' || token.charAt(0) == '-') && (pif_last == -1 || pif_last == -2)) {
                    return false;
                }
                //contains requires a String, so we concatenate the character to an empty string
                if (!tokens.contains(""+program.charAt(index+ matcher.end())) && !Character.isDigit(program.charAt(index+ matcher.end())))
                    return false;
                System.out.println("Found int constant: " + token);
            }
            pif.add(new Pair<>("intConst", -1));
            constantsTable.add(token);
            index += matcher.end();
            return true;
        }
        return false;
    }

    /**
     * Checks if the current token is part of the tokens list.<br>
     * @return true if a match was found, false otherwise.
     */
    private boolean tokenFromList() {
        for (String token : tokens) {
            if (program.startsWith(token, index)) {
                pif.add(new Pair<>(token, 0));
                index += token.length();
                System.out.println("Found token from list: " + token);
                return true;
            }
        }
        return false;
    }

    /**
     * Uses regex to match identifiers.<br>
     * Identifiers must begin with a letter and can contain letters, digits and _ <br>
     * In the pif they are added as identifiers and their position in the symbol table is added as the second element
     * @return true if a match was found, false otherwise.
     */
    private boolean identifier() {
        Pattern idRegex = Pattern.compile("^([a-zA-Z_][a-zA-Z0-9_]*)");
        Matcher matcher = idRegex.matcher(program.substring(index));
        if (matcher.find()) {
            String token = matcher.group(1);
            System.out.println("Found identifier: " + token);
            if(symbolTable.add(token))
                pif.add(new Pair<>("id", symbolTable.getPosition(token).getKey()));
            index += matcher.end();
            return true;
        }
        return false;
    }

    /**
     * Processes a new token.<br>
     * All whitespaces and comments are skipped.<br>
     * If the token is a string constant, int constant, token from the list or an identifier, it is added to the pif in
     * the corresponding method.<br>
     * If a token is not found, an exception is thrown having as message the fault token and the line it was found on.
     * @throws ScannerException When a lexical error occurs.
     */
    private void nextToken() throws ScannerException {
        while (true) {
            if (!skipWhiteSpace() && !skipComment())
                break;
        }
        if (index == program.length())
            return;
        if (stringConstant() || intConstant() || tokenFromList() || identifier()) {
            return;
        }
        StringBuilder faultTokenSb = new StringBuilder();
        while (index < program.length() && (!Character.isWhitespace(program.charAt(index)) || program.charAt(index) == '\n') && !tokens.contains(program.charAt(index) + "")) {
            faultTokenSb.append(program.charAt(index));
            index++;
        }
        this.faultToken = faultTokenSb.toString();
        throw new ScannerException("Lexical error: Cannot classify token " + faultToken + " on line " + currentLine);
    }

    /**
     * Performs the scanning process of the program.<br>
     * @throws ScannerException When a lexical error occurs.
     */
    public void scan() throws ScannerException {
        while (index < program.length()) {
            nextToken();
        }
    }

    /**
     * Prints the symbol table to a file. (in our case ST.out)
     * @throws IOException When the file writer fails.
     */
    public void printToSTFile() throws IOException {
        FileWriter writer = new FileWriter("src\\resources\\ST.out");
        writer.write(symbolTable.toString());
        writer.close();
    }

    /**
     * Prints the pif to a file. (in our case PIF.out)
     * @throws IOException When the file writer fails.
     */
    public void printToPIFFile() throws IOException {
        FileWriter writer = new FileWriter("src\\resources\\PIF.out");
        StringBuilder str = new StringBuilder();
        pif.forEach(e -> str.append(e.getKey()).append(" -> ").append(e.getValue()).append('\n'));
        writer.write(str.toString());
        writer.close();
    }
}
