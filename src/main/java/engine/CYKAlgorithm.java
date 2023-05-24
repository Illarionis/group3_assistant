package engine;

import java.util.List;

public final class CYKAlgorithm {
    public static boolean solve(String s, ContextFreeGrammar g) {
        int n = s.length();
        if (n == 0) return false;

        var nonTerminals = g.getNonTerminals();
        var terminals = g.getTerminals();
        var rules = g.getRules();
        var startSymbol = g.getStartSymbol();

        boolean[][][] table = new boolean[n][n][nonTerminals.size()];

        // Fill the first row of the table
        for (int i = 0; i < n; i++) {
            for (String t : terminals) {
                if (s.substring(i, i + 1).equals(t)) {
                    for (int j = 0; j < nonTerminals.size(); j++) {
                        String nt = nonTerminals.get(j);
                        if (rules.containsKey(nt) && rules.get(nt).contains(new String[]{t})) {
                            table[0][i][j] = true;
                        }
                    }
                }
            }
        }

        // Fill the rest of the table
        for (int l = 2; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                for (int j = 1; j <= l - 1; j++) {
                    for (int k = 0; k < nonTerminals.size(); k++) {
                        String nt = nonTerminals.get(k);
                        if (rules.containsKey(nt)) {
                            List<String[]> alternatives = rules.get(nt);
                            for (String[] a : alternatives) {
                                if (a.length == 2) { // Modification: Check if the alternative has 2 symbols
                                    String b = a[0];
                                    String c = a[1];
                                    int bIndex = nonTerminals.indexOf(b);
                                    int cIndex = nonTerminals.indexOf(c);
                                    if (bIndex >= 0 && cIndex >= 0 && table[j - 1][i][bIndex] && table[l - j - 1][i + j][cIndex]) {
                                        table[l - 1][i][k] = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Check if the input belongs to the grammar
        for (int i = 0; i < nonTerminals.size(); i++) {
            if (rules.containsKey(startSymbol) && rules.get(startSymbol).contains(new String[]{nonTerminals.get(i)})
                    && table[n - 1][0][i]) {
                return true;
            }
        }
        return false;
    }
}
