package engine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parsing2 {
    static List<String> patterns = new ArrayList<String>();
    static List<String> actionsList = new ArrayList<String>();
    static List<String> responses = new ArrayList<String>();

    public static void ParseString(String input, String actions, String output){
        String target = "*";
        String replacement = "([a-zA-Z0-9]*)"; //Regex for a word, which can contains lower or upper case or numbers
        String inputProcessed = input.replace(target, replacement);
        patterns.add(inputProcessed);
        actionsList.add(actions);
        String outputProcessed = output.replace(target, "%s"); //points to where the variable should be replaced
        responses.add(outputProcessed);
    }



    public static String chatSkill(String input) {

        for (int i=0; i<patterns.size(); i++) { //patterns : "which class do I have on *?"
            final Pattern p = Pattern.compile(patterns.get(i));
            Matcher m = p.matcher(input);
            if (m.find()) {
                String test = m.group(1); //group 1 = first thing entre parenthese dans la phrase (first placeheolder)
                final Pattern pat = Pattern.compile("([a-zA-Z0-9]*) - ([a-zA-Z0-9]*)");
                m = pat.matcher(actionsList.get(i));
                while(m.find()) {
                    if(m.group(1).equals(test)){
                        String response = String.format(responses.get(i), m.group(2));
                        return response;
                    }
                    return "Variable not recognized"; //if variable wasnt inputed (Classes on Monday ? Didnt say Monday - Maths)
                }
            }
        }
        return "No pattern found";
    }
    public static void main(String[] args){
        ParseString("what classes do I have on *", "Monday - math, Tuesday - English", "You have * on that day");
        String response = chatSkill("what classes do I have on Monday");
        System.out.println(response);
    }
}