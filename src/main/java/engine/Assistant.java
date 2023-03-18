package engine;

import java.util.ArrayList;
import java.util.List;

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
            if (s.getInput().equals(msg)) {
                return s.getOutput();
            }
        }
        return DEFAULT_REPLY;
    }
}
