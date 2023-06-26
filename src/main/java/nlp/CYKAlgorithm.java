package nlp;

import java.util.List;
import java.util.Map;

public final class CYKAlgorithm {

    public String[][] buildTable(List<String> terminals, ContextFreeGrammar g) {
        // Getting grammar data
        final List<String> nonTerminals = g.getNonTerminals();
        final Map<String, List<List<String>>> rules = g.getRules();

        // Building the table
        final int n = terminals.size();
        final String[][] table = new String[n][n];

        /* *
         * Read the code as follows:
         *  For every terminal from left to right forming the sentence
         *      For every non-terminal in the grammar
         *          If there exists a rule such that R[non-terminal] -> terminal
         *          Then, the (i,i)-th entry of the table is equal to the non-terminal
         * */
        for (int i = 0; i < n; i++) {
            for (String nt : nonTerminals) {
                final List<List<String>> options = rules.get(nt);
                for (List<String> substitution : options) {
                    if (substitution.size() != 1) continue;
                    final String t = terminals.get(i);
                    if (substitution.get(0).equalsIgnoreCase(t)) {
                        if (table[i][i] != null) table[i][i] = table[i][i] + "," + nt;
                        else table[i][i] = nt;
                        break;
                    }
                }
            }
        }

        for (int l = 1; l <= n; l++) {
            for (int i = 0; i < n-l+1; i++) {
                final int j = i+l-1;
                for (int k = i; k < j; k++) {
                    if (table[i][k] == null || table[k+1][j] == null) continue;
                    final List<String> l1 = List.of(table[i][k].split(","));
                    final List<String> l2 = List.of(table[k+1][j].split(","));
                    for (String nt : nonTerminals) {
                        final List<List<String>> options = rules.get(nt);
                        for (List<String> substitution : options) {
                            if (substitution.size() != 2) continue;
                            final String B = substitution.get(0);
                            final String C = substitution.get(1);
                            if (l1.contains(B) && l2.contains(C)) {
                                if (table[i][j] == null) table[i][j] = nt;
                                else if (!table[i][j].contains(nt)) table[i][j] = table[i][j] + "," + nt;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return table;
    }

    public boolean solve(List<String> terminals, ContextFreeGrammar g) {
        final String[][] parseTable = buildTable(terminals, g);
        final String entry = parseTable[0][terminals.size()-1];
        return entry != null && entry.contains(g.getStartSymbol());
    }
}