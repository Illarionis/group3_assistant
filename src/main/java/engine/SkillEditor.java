package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class SkillEditor {

    private void addToTextFile(List<String> info) {
        ArrayList<Integer> slotCounts = new ArrayList<>();

        // declarations
        Scanner ifsInput;
        String sFile;

        // initializations
        ifsInput = null;
        sFile = "";

        // attempts to create scanner for file
        try {
            ifsInput = new Scanner(new File("Skills.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File Doesn't Exist");
            return;
        }

        for (int i = 0; i < info.size(); i++) {
            if (i == 0) {
                // goes line by line and concatenates the elements of the file into a string
                while (ifsInput.hasNextLine()) {
                    sFile = ifsInput.nextLine();
                    if (sFile.equals(info.get(0))) {
                        System.out.println("Question already exists.");
                        return;
                    }
                }

                try {
                    File file = new File("Skills.txt");
                    PrintWriter writer = new PrintWriter(file);
                    writer.write(info.get(0));
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String input = info.get(i);
                int countActivities = 0;

                for (int j = 0; j < input.length(); j++) {
                    char compare = input.charAt(j);
                    char comma = ',';
                    if (compare == comma) {
                        countActivities++;
                    }
                }
                slotCounts.add(countActivities);
            }
        }
        if (slotCounts.size() % 2 != 0) return;

        int halfway = slotCounts.size() / 2;
        for (int i = 1; i < info.size() - halfway + 1; i++) {
            if (Objects.equals(slotCounts.get(i - 1), slotCounts.get(i - 1 + halfway))) {
                try {
                    File file = new File("Skills.txt");
                    PrintWriter writer = new PrintWriter(file);
                    writer.write(info.get(i));
                    writer.write(info.get(i + halfway));
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Amount of activities don't match. Please try again.");
            }
        }
    }

    public static void main(String[] args) {


    }
}
