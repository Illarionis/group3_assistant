package skill;

import org.junit.jupiter.api.Test;

final class PlaceholderSkillTest {
    @Test
    public void testMap() {
        final String[] inputs = {"lecture", "Monday", "8 am", "C0.015"};
        final String[] outputs = {"Monday", "8 am", "Linear Algebra", "Stefan", "C0.015"};
        testMap(
                "What @ do I have on @ at @ in @?", "@", 4,
                "On <> at <> you have <> taught by <> in <>", "<>", 5,
                inputs, outputs,
                "What lecture do I have on Monday at 8 am in C0.015?",
                "On Monday at 8 am you have Linear Algebra taught by Stefan in C0.015"
        );
    }

    public void testMap(String inputTemplate, String inputPlaceholder, int inputPlaceholderCount,
                        String outputTemplate, String outputPlaceholder, int outputPlaceholderCount,
                        String[] inputs, String[] outputs, String input, String output) {
        final var skill = new PlaceholderSkill(
                inputTemplate, inputPlaceholder, inputPlaceholderCount,
                outputTemplate, outputPlaceholder, outputPlaceholderCount
        );
        assert skill.map(inputs, outputs) == null;
        assert skill.getAssociations().get(input).equals(output);
    }

    @Test
    public void testRemove() {
        final String[] inputs = {"lecture", "Monday", "8 am", "C0.015"};
        final String[] outputs = {"Monday", "8 am", "Linear Algebra", "Stefan", "C0.015"};
        testRemove(
                "What @ do I have on @ at @ in @?", "@", 4,
                "On <> at <> you have <> taught by <> in <>", "<>", 5,
                inputs, outputs, "On Monday at 8 am you have Linear Algebra taught by Stefan in C0.015"
        );
    }

    public void testRemove(String inputTemplate, String inputPlaceholder, int inputPlaceholderCount,
                           String outputTemplate, String outputPlaceholder, int outputPlaceholderCount,
                           String[] inputs, String[] outputs, String expected) {
        final var skill = new PlaceholderSkill(
                inputTemplate, inputPlaceholder, inputPlaceholderCount,
                outputTemplate, outputPlaceholder, outputPlaceholderCount
        );

        assert skill.map(inputs, outputs) == null;
        final String s= skill.remove(inputs);
        System.out.println(s);
        assert s.equals(expected);
    }
}
