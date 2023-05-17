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
}
