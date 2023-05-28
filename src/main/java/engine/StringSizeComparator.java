package engine;

import java.util.Comparator;

/**
 * Provides a comparator to sort strings based on their length from the longest to the smallest.
 **/
public final class StringSizeComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o2.length() - o1.length();
    }
}
