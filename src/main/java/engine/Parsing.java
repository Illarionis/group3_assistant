package engine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Parsing {


    /**
     * TO DO :
     * -Add more functionalities
     * -See how complex it is to interract with API's (example : add an appointment in Google Calendar )
     * <p>
     * /**
     * <p>
     * /**
     * Takes a string from user and return the different components (domain, intent and slots)
     * Made for example from Project Manual :  Which lectures are there on <DAY> at <TIME>
     *
     * @return
     */
    public static String[] GetComponentsLectures(String input) {

        String[] splited = input.split("\\s+");
        String[] domain = {splited[5], splited[7]};
        return domain;
    }

    /**
     * Takes a string from user and return the different components (domain, intent and slots)
     * What time is it now?
     *
     * @return
     */
    public static String GetComponentsTime(String input) {

        String[] splited = input.split("\\s+");
        String domain = splited[1];
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }


    //For testing purposes
    public static void main(String[] args) {
        String input = "Which lectures are there on Monday at 5";
        String[] test = GetComponentsLectures(input);
//System.out.println(test[1]);

        String input2 = "What time is it now";
        String test2 = GetComponentsTime(input);
        System.out.println(test2);
    }
}
