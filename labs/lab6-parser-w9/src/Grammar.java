import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    List<String> nonTerminals;
    List<String> terminals;
    String startingSymbol;
    ProductionSet productionSet;


    Grammar(String filename) {
        List<String> lines;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            lines = br.lines().collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        // non-terminals are on the first line and separated by spaces
        nonTerminals = Arrays.stream(lines.get(0).split(" +")).collect(Collectors.toList());

        //terminals are on the second line and separated by spaces
        terminals = Arrays.stream(lines.get(1).split(" +")).collect(Collectors.toList());

        //starting symbol is on the third line
        startingSymbol = lines.get(2).trim();

        //the productionSet is on the remaining lines
        productionSet = new ProductionSet();
        for (int i = 3; i < lines.size(); i++) {
            String[] production = lines.get(i).trim().split("->");
            List<String> lhs = Arrays.stream(production[0].trim().split(" ")).collect(Collectors.toList());
//            List<String> rhs = Arrays.stream(production[1].trim().split(" ")).filter(it -> !Objects.equals(it, "epsilon")).collect(Collectors.toList());
//            List<String> rhs = Arrays.stream(production[1].trim().split(" ")).collect(Collectors.toList());
            List<String> rhs = Arrays.stream(production[1].trim().split("\\|")).toList();
            for (String s : rhs) {
                List<String> rhs2 = Arrays.stream(s.trim().split(" ")).collect(Collectors.toList());
                productionSet.addProduction(lhs, rhs2);
            }
//            productionSet.addProduction(lhs, rhs);
        }
    }


    public boolean checkCFG() {
        return productionSet.getProductions().entrySet().stream().allMatch(it -> it.getKey().size() == 1
        && nonTerminals.contains(it.getKey().get(0)));
    }

    public Map<String,List<String>> getProductionsContainingNonTerminal(String nonTerminal) {
        Map<String,List<String>> result = new HashMap<>();
        for (String nTerminal: this.nonTerminals)
            for (List<String> prod: this.productionSet.getProductionsOf(nTerminal)) {
                if (prod.contains(nonTerminal)) {
                    result.put(nTerminal, prod);
                }
            }
        return result;
    }

    public List<List<String>> getProductionsForNonTerminal(String nonTerminal) {
        return new ArrayList<>(this.productionSet.getProductionsOf(nonTerminal));
    }
}
