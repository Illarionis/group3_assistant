package engine;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

final class StringSizeComparatorTest {
    @Test
    public void testCriteria() {
        final String[] values = {"1", "12", "123"};
        final List<String> l = new ArrayList<>(List.of(values));
        final var criteria = new StringSizeComparator();
        l.sort(criteria);
        assert l.get(0).equals("123");
        assert l.get(1).equals("12");
        assert l.get(2).equals("1");
    }
}
