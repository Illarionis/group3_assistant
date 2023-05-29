package engine;
import com.swabunga.spell.event.*;
import com.swabunga.spell.engine.*;
import java.io.*;
import java.util.*;

public class JazzyTest1 implements SpellCheckListener {

    SpellChecker checker;
    ArrayList<String> misspelled;

    public JazzyTest1(String input) {
        createDictionary();
        StringWordTokenizer tokenizer = new StringWordTokenizer(input);

        misspelled = new ArrayList<>();

        checker.addSpellCheckListener(this);
        checker.checkSpelling(tokenizer);

        for (String word : misspelled) {
            System.out.println("misspelled: " + word);
        }
    }

    private void createDictionary() {
        File dict = new File("src/main/resources/english.0");
        try {
            checker = new SpellChecker(new SpellDictionaryHashMap(dict));
        } catch (FileNotFoundException e) {
            System.err.println("Dictionary File " + dict + " not found! " + e);
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("IO problem: " + ex);
            System.exit(2);
        }
    }

    public void spellingError(SpellCheckEvent event) {
        event.ignoreWord(true);
        misspelled.add(event.getInvalidWord());
    }

    public List<String> getMisspelledWords() {
        return misspelled;
    }

    public static void main(String[] args) {
        String input="This ise not verry englisch";
        new JazzyTest1(input);
    }

}