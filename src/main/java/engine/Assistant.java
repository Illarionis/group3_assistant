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
                System.out.println(m.group(0));
                Boolean foundAll = false;
                ArrayList<String> test = new ArrayList<>();
                try {
                    test.add(m.group(1)); //group 1 = first thing entre parenthese dans la phrase (first placeheolder)
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("-----------------------------ERROR----------------------------------------------------");
                    return s.getOutput();
                }
                System.out.println(test.get(0));
                int i = 2;
                while(!foundAll){
                    try {
                        test.add(m.group(i)); //group 1 = first thing entre parenthese dans la phrase (first placeheolder)
                        System.out.println(m.group(i));
                        i++;
                    }catch(Exception e){
                        foundAll = true;
                        String[] testArray = test.toArray(new String[0]);
                        return s.getOutput(testArray);
                    }
                }


            }


        }
        return DEFAULT_REPLY;
    }
}
