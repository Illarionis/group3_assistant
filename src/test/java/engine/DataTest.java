package engine;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

public class DataTest {
    @Test
    public void testCleanly() {
        final File dir = new File(".test");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Failed to create directory to test the class Data");
        }
        final Data d = new Data(dir);
        final String s = "Hello world!!!";
        d.store("test", "test.txt", s);

        final File[] files = d.read("test");
        assert files.length == 1;
        assert Objects.equals(d.read(files[0]), s);

        assert d.delete(files[0]);
        assert d.delete("test");
        assert d.delete();
    }
}
