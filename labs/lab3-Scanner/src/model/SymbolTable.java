package model;

public class SymbolTable {
    private final HashTable<String> identifierTable;
    private final HashTable<String> intConstantsTable;
    private final HashTable<String> stringConstantsTable;

    public SymbolTable() {
        identifierTable = new HashTable<>(107);
        intConstantsTable = new HashTable<>(107);
        stringConstantsTable = new HashTable<>(107);
    }

    public boolean addToIdentifierTable(String token){
        return identifierTable.add(token);
    }

    public boolean addToIntConstantsTable(String token){
        return intConstantsTable.add(token);
    }

    public boolean addToStringConstantsTable(String token){
        return stringConstantsTable.add(token);
    }

    public HashTable<String> getIdentifierTable() {
        return identifierTable;
    }

    public HashTable<String> getIntConstantsTable() {
        return intConstantsTable;
    }

    public HashTable<String> getStringConstantsTable() {
        return stringConstantsTable;
    }

    @Override
    public String toString() {
        return "SymbolTable{" +"\n"+
                "identifierTable=" + identifierTable +"\n"+
                "intConstantsTable=" + intConstantsTable +"\n"+
                "stringConstantsTable=" + stringConstantsTable +"\n"+
                '}';
    }
}
