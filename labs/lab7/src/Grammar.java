import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private Set<String> nonTerminals = new HashSet<>();
    private Set<String> terminals = new HashSet<>();
    private final HashMap<Set<String>, Set<List<String>>> productionSet = new HashMap<>();
    private String startingSymbol = "";

    public Grammar(String filename) {
        readFromFile(filename);
    }

    private void readFromFile(String filename) {
        List<String> lines;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            lines = br.lines().collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        // non-terminals are on the first line and separated by spaces
        nonTerminals = Arrays.stream(lines.get(0).split(" +")).collect(Collectors.toSet());

        //terminals are on the second line and separated by spaces
        terminals = Arrays.stream(lines.get(1).split(" +")).collect(Collectors.toSet());

        //starting symbol is on the third line
        startingSymbol = lines.get(2).trim();

        //the productionSet is on the remaining lines
        for (int i = 3; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            String[] tokens = line.split("->");
            String[] lhsTokens = tokens[0].split(",");
            String[] rhsTokens = tokens[1].split("\\|");

            Set<String> lhs = new HashSet<>();
            for(String l : lhsTokens)
                lhs.add(l.strip());
            if(!productionSet.containsKey(lhs))
                productionSet.put(lhs,new HashSet<>());

            for(String rhsT : rhsTokens) {
                ArrayList<String> productionElements = new ArrayList<>();
                String[] rhsTokenElement = rhsT.strip().split(" ");
                for(String r : rhsTokenElement)
                    productionElements.add(r.strip());
                productionSet.get(lhs).add(productionElements);
            }
        }
    }

    public String printNonTerminals() {
        StringBuilder sb = new StringBuilder("N = { ");
        for(String n : nonTerminals)
            sb.append(n).append(" ");
        sb.append("}");
        return sb.toString();
    }

    public String printTerminals() {
        StringBuilder sb = new StringBuilder("E = { ");
        for(String e : terminals)
            sb.append(e).append(" ");
        sb.append("}");
        return sb.toString();
    }

    public String printProductions() {
        StringBuilder sb = new StringBuilder("P = { \n");
        productionSet.forEach((lhs, rhs) -> {
            sb.append("\t");
            int count = 0;
            for(String lh : lhs) {
                sb.append(lh);
                count++;
                if(count<lhs.size())
                    sb.append(", ");
            }
            sb.append(" -> ");
            count = 0;
            for(List<String> rh : rhs){
                for(String r : rh) {
                    sb.append(r).append(" ");
                }
                count++;
                if (count < rhs.size())
                    sb.append("| ");

            }
            sb.append("\n");
        });
        sb.append("}");
        return sb.toString();
    }

    public String printProductionsForNonTerminal(String nonTerminal){
        StringBuilder sb = new StringBuilder();

        for(Set<String> lhs : productionSet.keySet()) {
            if(lhs.contains(nonTerminal)) {
                sb.append(nonTerminal).append(" -> ");
                Set<List<String>> rhs = productionSet.get(lhs);
                int count = 0;
                for (List<String> rh : rhs) {
                    for(String r : rh) {
                        sb.append(r).append(" ");
                    }
                    count++;
                    if (count < rhs.size())
                        sb.append("| ");
                }
            }
        }

        return sb.toString();
    }

    public boolean checkIfCFG(){
        var checkStartingSymbol = false;
        for(Set<String> lhs : productionSet.keySet())
            if (lhs.contains(startingSymbol)) {
                checkStartingSymbol = true;
                break;
            }
        if(!checkStartingSymbol)
            return false;

        for(Set<String> lhs : productionSet.keySet()){
            if(lhs.size()>1)
                return false;
            else if(!nonTerminals.contains(lhs.iterator().next()))
                return false;

            Set<List<String>> rhs = productionSet.get(lhs);

            for(List<String> rh : rhs) {
                for (String r : rh) {
                    if(!(nonTerminals.contains(r) || terminals.contains(r) || r.equals("epsilon")))
                        return false;
                }
            }
        }
        return true;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public HashMap<Set<String>, Set<List<String>>> getProductionSet() {
        return productionSet;
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    public Set<List<String>> getProductionForNonterminal(String nonTerminal) {
        for (Set<String> lhs : productionSet.keySet()) {
            if (lhs.contains(nonTerminal)) {
                return productionSet.get(lhs);
            }
        }
        return new HashSet<>();
    }

    public Map<String, List<String>> getProductionsContainingNonTerminal(String nonTerminal) {
        Map<String,List<String>> result = new HashMap<>();
        for (String nTerminal: this.nonTerminals)
            for (List<String> prod: this.getProductionForNonterminal(nTerminal)) {
                if (prod.contains(nonTerminal)) {
                    result.put(nTerminal, prod);
                }
            }
        return result;
    }
}
