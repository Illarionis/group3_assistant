package skill;

import org.junit.jupiter.api.Test;

final class BasicSkillTest {
    @Test
    public void testMap() {
        testMap("Hello world!", "Hello computer!");
    }

    public void testMap(String input, String output) {
        final var skill = new BasicSkill();
        assert skill.map(input, output) == null;
        assert skill.getAssociations().get(input).equals(output);
    }

    @Test
    public void testRemove() {
        testRemove("Good morning teacher!", "Good morning class!");
    }

    public void testRemove(String input, String output) {
        final var skill = new BasicSkill();
        assert skill.map(input, output) == null;
        assert skill.remove(input).equals(output);
    }
}
