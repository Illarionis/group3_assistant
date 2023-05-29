package engine;

import org.junit.jupiter.api.Test;

import java.util.Objects;

final class AssistantTest {
    private void testAssociate(Assistant a, String in, String out, String expected) {
        assert Objects.equals(a.associate(in, out), expected);
    }


    private void testGet(Assistant a, String in, String expected) {
        assert Objects.equals(a.getAssociation(in), expected);
    }

    private void testRemove(Assistant a, String in, String expected) {
        assert Objects.equals(a.removeAssociation(in), expected);
    }

    private void testValidate(Assistant a, String in) {
        assert a.validate(in);
    }

    @Test
    public void testAssociate() {
        final Assistant a = new Assistant();
        testAssociate(a, "x", "y", null);
        testAssociate(a, "y", "z", null);
        testAssociate(a, "x", "z", "y");
    }

    @Test
    public void testGet() {
        final Assistant a = new Assistant();
        a.associate("x", "y");
        a.associate("y", "z");
        testGet(a, "x", "y");
        testGet(a, "y", "z");
    }

    @Test
    public void testRemove() {
        final Assistant a = new Assistant();
        a.associate("x", "y");
        a.associate("y", "z");
        testRemove(a, "x", "y");
        testRemove(a, "y", "z");
    }

    @Test
    public void testValidate() {
        final Assistant a= new Assistant();

        // Defining grammar
        ContextFreeGrammar g = new ContextFreeGrammar();
        g.registerNonTerminals("S", "A", "B", "C");
        g.registerTerminals("a", "b");
        g.setStartSymbol("S");

        // Registering the rules
        g.createRule("S", "AB", "BC");
        g.createRule("A", "BA", "a");
        g.createRule("B", "CC", "b");
        g.createRule("C", "AB", "a");

        // Sorting the non-terminals and terminals
        g.sortNonTerminals();
        g.sortTerminals();

        // Registering the grammar
        a.registerGrammar(g);

        // Testing valid
        testValidate(a, "baaba");

        // Defining a new grammar
        g = new ContextFreeGrammar();
        g.registerNonTerminals("S", "N", "P", "V", "PP", "Det", "NP", "VP");
        g.registerTerminals("she", "eats", "fish", "with", "a", "fork");
        g.setStartSymbol("S");

        // Registering the rules
        g.createRule("S","NP VP");
        g.createRule("VP", "VP PP");
        g.createRule("VP", "V NP");
        g.createRule("VP", "eats");
        g.createRule("PP", "P NP");
        g.createRule("NP", "Det N");
        g.createRule("NP", "she");
        g.createRule("V", "eats");
        g.createRule("P", "with");
        g.createRule("N", "fish");
        g.createRule("N", "fork");
        g.createRule("Det", "a");

        // Sorting the non-terminals and terminals
        g.sortNonTerminals();
        g.sortTerminals();

        // Registering the grammar
        a.registerGrammar(g);

        // Testing valid
        testValidate(a, "she eats a fish with a fork");
    }
}
