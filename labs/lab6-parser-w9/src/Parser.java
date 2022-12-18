import java.util.*;

public class Parser {

    private final Grammar grammar;
    HashMap<String, Set<String>> first = new HashMap<>();
    HashMap<String, Set<String>> follow = new HashMap<>();

    Set<String> temporary = new HashSet<>();

    public Parser(String filename) {
        this.grammar = new Grammar(filename);
        this.getAllFirst();
        this.getAllFollow();
    }

    public void getAllFirst(){
        for (var nonTerminal : grammar.nonTerminals) {
            this.first.put(nonTerminal, this.getFirst(nonTerminal));
        }
    }

    public void getAllFollow(){
        for (var nonTerminal : grammar.nonTerminals) {
            this.temporary = new HashSet<>();
            this.follow.put(nonTerminal, this.getFollow(nonTerminal));
        }
    }

    public Set<String> getFirst(String nonTerminal){
        if (this.grammar.terminals.contains(nonTerminal)){
            return new HashSet<>(Collections.singleton(nonTerminal));
        }
        Set<String> aux = new HashSet<>();
        for(List<String> prod: this.grammar.getProductionsForNonTerminal(nonTerminal)){
            if (this.grammar.terminals.contains(prod.get(0)) || prod.get(0).equals("epsilon")){
                aux.add(prod.get(0));
            }
            else {
                for (int i = 0; i < prod.size(); i++) {
                    String rule = prod.get(i);
                    if (this.grammar.nonTerminals.contains(rule)){
                        boolean check = true;
                        for (int j = 0; j<i; j++){
                            if (!this.getFirst(prod.get(j)).contains("epsilon")){
                                check = false;
                                break;
                            }
                        }
                        if (check){
                            if (!rule.equals("epsilon"))
                                aux.addAll(this.getFirst(rule));
                        }
                    }
                }
            }
        }
        return aux;
    }

    public Set<String> getFollow(String nonTerminal){
        this.temporary.add(nonTerminal);
        Set<String> res = new HashSet<>();
        if (nonTerminal.equals(this.grammar.startingSymbol)){
            res.add("$");
        }
        Map<String,List<String>> productions = this.grammar.getProductionsContainingNonTerminal(nonTerminal);
        for (String prod: productions.keySet()){
            String start = prod;
            List<String> rule = productions.get(start);
            for(int i =0; i<rule.size(); i++){
                String term = rule.get(i);
                if (term.equals(nonTerminal)){
                    if(i<rule.size()-1){
                        var first_next = this.getFirst(rule.get(i+1));
                        for (var e: first_next){
                            if (!e.equals("epsilon")){
                                res.add(e);
                            }
                        }
                        if (first_next.contains("epsilon")){
                            res.addAll(this.getFollow(start));
                        }
                    } else {
                        if (!this.temporary.contains(start)) {
                            this.temporary.add(start);
                            res.addAll(this.getFollow(start));
                        }
                    }
                }
            }
        }
        return res;
    }

    public String printFirst(){
        StringBuilder sb = new StringBuilder();
        sb.append("First:\n");
        for (var key: this.first.keySet()){
            sb.append(key).append(" : ").append(this.first.get(key)).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String printFollow(){
        StringBuilder sb = new StringBuilder();
        sb.append("Follow:\n");
        for (var key: this.follow.keySet()){
            sb.append(key).append(" : ").append(this.follow.get(key)).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
