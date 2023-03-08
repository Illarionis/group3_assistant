package engine;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;


public class LogController {
    private int userId;
    private int conversationId;
    private String messageText;
    private boolean isRequest;
    private String fileName;
    private File fileLog;
    private JSONArray messagesArr;

    public LogController(int userId, int conversationId, String messageText, boolean isRequest) {
        this.userId = userId;
        this.conversationId = conversationId;
        this.messageText = messageText;
        this.isRequest = isRequest;
        setFileName();
        this.fileLog = new File(fileName);
    }

    public LogController(int userId, int conversationId){
        this.userId = userId;
        this.conversationId = conversationId;
        setFileName();
        this.fileLog = new File(fileName);
    }

    public void save() {
        if (!logExists()) {
            createFile();
        }
        setMessagesArr();

        if (saveMessage()) System.out.println("Saved successfully!");
        else System.out.println("Saving failed :(");
    }

    public String getConversationString(){
        JSONArray conversation = getConversationJSON();
        if (Objects.isNull(conversation)) {
            return "Conversation does not exist";
        }
        return conversation.toJSONString();
    }

    public JSONArray getConversationJSON(){
        if(!logExists()){
            System.out.println("Conversation is not in log");
            return new JSONArray();
        }
        return getConversationFromFile();
    }

    private JSONArray getConversationFromFile(){
        return (JSONArray) readJSONfromFile();
    }

    private boolean saveMessage() {
        LocalDateTime dateTime = LocalDateTime.now();
        JSONObject newMessage = new JSONObject();
        newMessage.put("messageId", this.messagesArr.size());
        newMessage.put("messageText", this.messageText);
        newMessage.put("isRequest", this.isRequest);
        newMessage.put("dateTime", dateTime.toString());

        this.messagesArr.add(newMessage);
        return writeJSONinFile(this.messagesArr);
    }

    private void setMessagesArr() {
        this.messagesArr = (JSONArray) readJSONfromFile();
    }

    private void setFileName(){
        this.fileName = "log_" + this.userId + "_" + this.conversationId + ".json";
    }

    private boolean logExists() {
        return (fileLog.exists() && !fileLog.isDirectory());
    }

    private boolean createFile() {
        try {
            JSONArray msgArrTmp = new JSONArray();
            fileLog.createNewFile();
            return writeJSONinFile(msgArrTmp);
        } catch (IOException e) {
            System.out.println("<EXCEPTION> createFile: " + e.getMessage());
            return false;
        }
    }

    private boolean writeJSONinFile(JSONAware json) {
        try {
            FileWriter file = new FileWriter(fileLog);
            file.write(json.toJSONString());
            file.close();
        } catch (IOException e) {
            System.out.println("<EXCEPTION> writeJSONinFile: " + e.getMessage());
            return false;
        }
        return true;
    }

    private JSONAware readJSONfromFile() {
        JSONAware json;
        try {
            FileReader file = new FileReader(fileLog);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(file);
            json = (JSONAware) obj;
            return json;
        } catch (IOException | ParseException e) {
            System.out.println("<EXCEPTION> readJSONfromFile: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
//        LogController logger = new LogController(0, 0, "helloooo", true);
//        LogController logController = new LogController(1, 0, "olaaaa", true);
//        logController.save();
//
//        System.out.println(new LogController(1, 0).getConversationString());

    }
}
