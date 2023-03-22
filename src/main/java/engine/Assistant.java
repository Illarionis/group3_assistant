package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Assistant {
    private static final String[] DEFAULT_REPLY = {
            "Could not understand your message.",
            "Did you perhaps forget to define the skill?",
            "Or have you made a typing mistake?"
    };
    private final List<Skill> skillList = new ArrayList<>();

    public void addSkill(Skill s) {
        skillList.add(s);
    }

    public void removeSkill(Skill s) {
        skillList.remove(s);
    }

    public String[] respond(String msg) {
        for (Skill s : skillList) {
            final Pattern p = Pattern.compile(s.getInput());
            Matcher m = p.matcher(msg);
            if (m.find()) {
                try{

                String test = m.group(1); //group 1 = first thing entre parenthese dans la phrase (first placeheolder)
                System.out.println(test);
                return s.getOutput(test);}
                catch(Exception e){
                    e.printStackTrace();
                    System.out.println("-----------------------------ERROR----------------------------------------------------");
                    return s.getOutput();
                }

            }


        }
        return DEFAULT_REPLY;
    }
}
