import java.util.*;

public class Parser {
    private final Grammar grammar;
    private HashMap<String, Set<String>> firstSet;
    private HashMap<String, Set<String>> followSet;
    private HashMap<Pair<String,String>, Pair<String,Integer>> parseTable;
    private List<List<String>> productionsRhs;
    Set<String> temporary = new HashSet<>();

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        this.firstSet = new HashMap<>();
        this.followSet = new HashMap<>();
        this.parseTable = new HashMap<>();

        generateFirst();
        generateFollow();
        generateParseTable();
    }

    public void generateFirst() {
        //initialization
        for (var nonTerminal : grammar.getNonTerminals()) {
            this.firstSet.put(nonTerminal, this.getFirst(nonTerminal));
        }
    }

    public Set<String> getFirst(String nonTerminal) {
        // If x is a terminal, then FIRST(x) = { ‘x’ }
        if (this.grammar.getTerminals().contains(nonTerminal)) {
            return new HashSet<>(Collections.singleton(nonTerminal));
        }
        Set<String> aux = new HashSet<>();
        for (List<String> prod : this.grammar.getProductionForNonterminal(nonTerminal)) {
//            If x-> Є, is a production rule, then add Є to FIRST(x).
            if (this.grammar.getTerminals().contains(prod.get(0)) || prod.get(0).equals("epsilon")) {
                aux.add(prod.get(0));
            } else {
                for (int i = 0; i < prod.size(); i++) {
                    String rule = prod.get(i);
                    if (this.grammar.getNonTerminals().contains(rule)) {
                        boolean check = true;
                        for (int j = 0; j < i; j++) {
                            if (!this.getFirst(prod.get(j)).contains("epsilon")) {
                                check = false;
                                break;
                            }
                        }
                        if (check) {
                            if (!rule.equals("epsilon"))
                                aux.addAll(this.getFirst(rule));
                        }
                    }
                }
            }
        }
        return aux;
    }

    public void generateFollow() {
        //initialization
        for (var nonTerminal : grammar.getNonTerminals()) {
            this.temporary = new HashSet<>();
            this.followSet.put(nonTerminal, this.getFollow(nonTerminal));
        }
    }

    public Set<String> getFollow(String nonTerminal) {
        this.temporary.add(nonTerminal);
        Set<String> res = new HashSet<>();
        if (nonTerminal.equals(this.grammar.getStartingSymbol())) {
            res.add("$");
        }
        Map<String, List<String>> productions = this.grammar.getProductionsContainingNonTerminal(nonTerminal);
        for (String prod : productions.keySet()) {
            String start = prod;
            List<String> rule = productions.get(start);
            for (int i = 0; i < rule.size(); i++) {
                String term = rule.get(i);
                if (term.equals(nonTerminal)) {
                    if (i < rule.size() - 1) {
                        var first_next = this.getFirst(rule.get(i + 1));
                        for (var e : first_next) {
                            if (!e.equals("epsilon")) {
                                res.add(e);
                            }
                        }
                        if (first_next.contains("epsilon")) {
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

    public void generateParseTable() {
        List<String> rows = new ArrayList<>();
        rows.addAll(grammar.getNonTerminals());
        rows.addAll(grammar.getTerminals());
        rows.add("$");

        List<String> columns = new ArrayList<>();
        columns.addAll(grammar.getTerminals());
        columns.add("$");

        for (var row : rows)
            for (var col : columns)
                parseTable.put(new Pair<String, String>(row, col), new Pair<String, Integer>("err",-1));

        for (var col : columns)
            parseTable.put(new Pair<String, String>(col, col), new Pair<String, Integer>("pop",-1));

        parseTable.put(new Pair<String, String>("$", "$"), new Pair<String, Integer>("acc",-1));

        var productions = grammar.getProductionSet();
        this.productionsRhs = new ArrayList<>();
        productions.forEach((k,v) -> {
            var nonterminal = k.iterator().next();
            for(List<String> prod : v)
                    if(!prod.get(0).equals("epsilon"))
                        productionsRhs.add(prod);
                    else {
                        productionsRhs.add(new ArrayList<>(List.of("epsilon", nonterminal)));
                    }
        });
        System.out.println(productionsRhs);

        productions.forEach((k, v) -> {
            var key = k.iterator().next();

            for (var production : v) {
                var firstSymbol = production.get(0);
                if (grammar.getTerminals().contains(firstSymbol))
                    if (parseTable.get(new Pair<>(key, firstSymbol)).getFirst().equals("err"))
                        parseTable.put(new Pair<>(key, firstSymbol), new Pair<>(String.join(" ", production),productionsRhs.indexOf(production)+1));
                    else {
                        try {
                            throw new IllegalAccessException("CONFLICT: Pair "+key+","+firstSymbol);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                else if (grammar.getNonTerminals().contains(firstSymbol)) {
                    if (production.size() == 1)
                        for (var symbol : firstSet.get(firstSymbol))
                            if (parseTable.get(new Pair<>(key, symbol)).getFirst().equals("err"))
                                parseTable.put(new Pair<>(key, symbol), new Pair<>(String.join(" ", production),productionsRhs.indexOf(production)+1));
                            else {
                                try {
                                    throw new IllegalAccessException("CONFLICT: Pair "+key+","+symbol);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                    else {
                        var i = 1;
                        var nextSymbol = production.get(1);
                        var firstSetForProduction = firstSet.get(firstSymbol);

                        while (i < production.size() && grammar.getNonTerminals().contains(nextSymbol)) {
                            var firstForNext = firstSet.get(nextSymbol);
                            if (firstSetForProduction.contains("epsilon")) {
                                firstSetForProduction.remove("epsilon");
                                firstSetForProduction.addAll(firstForNext);
                            }

                            i++;
                            if (i < production.size())
                                nextSymbol = production.get(i);
                        }

                        for (var symbol : firstSetForProduction) {
                            if (symbol.equals("epsilon"))
                                symbol = "$";
                            if (parseTable.get(new Pair<>(key, symbol)).getFirst().equals("err"))
                                parseTable.put(new Pair<>(key, symbol), new Pair<>(String.join(" ", production), productionsRhs.indexOf(production) + 1));
                            else {
                                try {
                                    throw new IllegalAccessException("CONFLICT: Pair " + key + "," + symbol);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    var follow = followSet.get(key);
                    for (var symbol : follow) {
                        if (symbol.equals("epsilon")) {
                            if (parseTable.get(new Pair<>(key, "$")).getFirst().equals("err")) {
                                var prod = new ArrayList<>(List.of("epsilon",key));
                                parseTable.put(new Pair<>(key, "$"), new Pair<>("epsilon", productionsRhs.indexOf(prod) + 1));
                            }
                            else {
                                try {
                                    throw new IllegalAccessException("CONFLICT: Pair "+key+","+symbol);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (parseTable.get(new Pair<>(key, symbol)).getFirst().equals("err")) {
                            var prod = new ArrayList<>(List.of("epsilon",key));
                            parseTable.put(new Pair<>(key, symbol), new Pair<>("epsilon", productionsRhs.indexOf(prod) + 1));
                        }
                        else {
                            try {
                                throw new IllegalAccessException("CONFLICT: Pair "+key+","+symbol);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

    }


    public String printFirst() {
        StringBuilder builder = new StringBuilder();
        firstSet.forEach((k, v) -> {
            builder.append(k).append(": ").append(v).append("\n");
        });
        return builder.toString();
    }

    public String printFollow() {
        StringBuilder builder = new StringBuilder();
        followSet.forEach((k, v) -> {
            builder.append(k).append(": ").append(v).append("\n");
        });
        return builder.toString();
    }

    public String printParseTable() {
        StringBuilder builder = new StringBuilder();
        parseTable.forEach((k, v) -> {
            builder.append(k).append(" -> ").append(v).append("\n");
        });
        return builder.toString();
    }

    public List<Integer> parseSequence(List<String> sequence){
        System.out.println("----------------");
        System.out.println(sequence);
        Stack<String> alpha = new Stack<>();
        Stack<String> beta = new Stack<>();
        List<Integer> result = new ArrayList<>();

        //initialization
        alpha.push("$");
        for(var i=sequence.size()-1;i>=0;--i)
            alpha.push(sequence.get(i));

        beta.push("$");
        beta.push(grammar.getStartingSymbol());

        while(!(alpha.peek().equals("$") && beta.peek().equals("$"))){
            String alphaPeek = alpha.peek();
            String betaPeek = beta.peek();
            Pair<String,String> key = new Pair<>(betaPeek,alphaPeek);
            Pair<String,Integer> value = parseTable.get(key);

            if(!value.getFirst().equals("err")){
                if(value.getFirst().equals("pop")){
                    alpha.pop();
                    beta.pop();
                }
                else {
                    beta.pop();
                    if(!value.getFirst().equals("epsilon")) {
                        String[] val = value.getFirst().split(" ");
                        for (var i = val.length - 1; i >= 0; --i)
                            beta.push(val[i]);
                    }
                    result.add(value.getSecond());
                }
            }
            else {
                System.out.println("Syntax error for key "+key);
                System.out.println("Current alpha and beta for sequence parsing:");
                System.out.println(alpha);
                System.out.println(beta);
                result = new ArrayList<>(List.of(-1));
                return result;
            }
        }

        return result;
    }

    public List<String> getProductionByOrderNumber(int order){
        var production = productionsRhs.get(order-1);
        if(production.contains("epsilon"))
            return List.of("epsilon");
        return production;
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public List<List<String>> getProductionsRhs() {
        return productionsRhs;
    }
}
