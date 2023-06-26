package nlp;
import com.swabunga.spell.event.*;
import com.swabunga.spell.engine.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Jazzy  {
    private final SpellDictionaryHashMap dictionary;

    public Jazzy() {
        try {
            final var file  = new File("src/main/resources/english.0");
            dictionary = new SpellDictionaryHashMap(file);
        } catch (Exception e) {
            System.out.println("Failed to load spell checker due to exception " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks the spelling of each word in a string.
     *
     * @param in The string for which the spelling should be checked.
     * @return All misspelled words in the string.
     **/
    public List<String> checkSpelling(String in) {
        final List<String> misspelled = new ArrayList<>();
        final var spellChecker = new SpellChecker(dictionary);
        spellChecker.addSpellCheckListener(event -> {
            event.ignoreWord(true);
            misspelled.add(event.getInvalidWord());
        });

        final var tokenizer = new StringWordTokenizer(in);
        spellChecker.checkSpelling(tokenizer);

        return misspelled;
    }
}