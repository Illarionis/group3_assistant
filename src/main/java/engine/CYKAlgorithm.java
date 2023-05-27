package engine;

import java.util.*;

public final class CYKAlgorithm {
    public static boolean solve(String s, ContextFreeGrammar g) {
        int n = s.length();
        if (n == 0) return false;

        List<String> nonTerminals = g.getNonTerminals();
//        List<String> terminals = g.getTerminals();
        Map<String, List<String[]>> rules = g.getRules();
        String startSymbol = g.getStartSymbol();

        boolean[][][] table = new boolean[n][n][nonTerminals.size()];

        // Fill the table
        for (int i = 0; i < n; i++) {
            char symbol = s.charAt(i);
            for (int j = 0; j < nonTerminals.size(); j++) {
                String nt = nonTerminals.get(j);
                List<String[]> productions = rules.get(nt);
                if (productions != null && productions.contains(Collections.singletonList(String.valueOf(symbol)))) {
                    table[i][i][j] = true;
                }
            }
        }

        for (int l = 2; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                int j = i + l - 1;
                for (int k = i; k <= j - 1; k++) {
                    for (int a = 0; a < nonTerminals.size(); a++) {
                        for (int b = 0; b < nonTerminals.size(); b++) {
                            for (int c = 0; c < nonTerminals.size(); c++) {
                                if (table[i][k][a] && table[k + 1][j][b]) {
                                    List<String> sequence = Arrays.asList(nonTerminals.get(a), nonTerminals.get(b));
                                    List<String[]> productions = rules.get(nonTerminals.get(c));
                                    if (productions != null && productions.contains(sequence)) {
                                        table[i][j][c] = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Check if the input belongs to the grammar
        int startSymbolIndex = nonTerminals.indexOf(startSymbol);
        return startSymbolIndex >= 0 && table[0][n - 1][startSymbolIndex];
    }
}