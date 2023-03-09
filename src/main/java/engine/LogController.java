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
    private final int userId;
    private final int chatId;
    private String messageText;
    private boolean isRequest;
    private String fileName;
    private File fileLog;
    private JSONArray messagesArr;

    /**
     * Constructor 1
     *
     * Used for saving
     *
     * @param userId - id of the user
     * @param chatId - id of the chat
     * @param messageText - text to be saved
     * @param isRequest - if true - the message is from the user, else - the message is from the assistant
     */
    public LogController(int userId, int chatId, String messageText, boolean isRequest) {
        this.userId = userId;
        this.chatId = chatId;
        this.messageText = messageText;
        this.isRequest = isRequest;
        setFileName();
        setFileLog();
    }

    /**
     * Constructor 2
     * Used for loading and deleting
     * @param userId - id of the user
     * @param chatId - id of the chat
     */
    public LogController(int userId, int chatId) {
        this.userId = userId;
        this.chatId = chatId;
        setFileName();
        setFileLog();
    }

    /**
     * Saving the message text using this.userId and this.chatId, defined in Constructor 1
     * If you used Constructor 2 - you cannot use save()
     */
    public void save() {
        if(Objects.isNull(messageText)){
            return;
        }
        if (!logExists()) {
            createFile();
        }
        setMessagesArr();

        if (saveMessage()) System.out.println("Saved successfully!");
        else System.out.println("Saving failed :(");
    }

    /**
     * Deletes the chat from the log system by using this.userId and this.chatId
     * Either constructors are applicable
     */
    public void deleteChat() {
        fileLog.delete();
        int newIdIter = this.chatId + 1;
        File next = getFileByChatId(newIdIter);
        File tmp;
        if (!next.exists()) return;

        while (next.exists()) {
            tmp = getFileByChatId(newIdIter - 1);
            next.renameTo(tmp);
            newIdIter++;
            next = getFileByChatId(newIdIter);
        }
    }

    /**
     * Uses this.userId and this.chatId to retrieve the chat log in a form of JSONString
     * @return JSONString of a certain chat
     */
    public String loadString() {
        JSONArray chat = loadJSON();
        if (Objects.isNull(chat)) {
            return "Chat does not exist";
        }
        return chat.toJSONString();
    }

    /**
     * Uses this.userId and this.chatId to retrieve the chat log in a form of JSONArray
     * @return JSONArray of a certain chat
     */
    public JSONArray loadJSON() {
        if (!logExists()) {
            System.out.println("Chat is not in log");
            return new JSONArray();
        }
        return (JSONArray) readJSONfromFile();
    }

    private File getFileByChatId(int id) {
        return new File("log_" + this.userId + "_" + id + ".json");
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

    private void setFileName() {
        this.fileName = "log_" + this.userId + "_" + this.chatId + ".json";
    }

    private void setFileLog() {
        this.fileLog = new File(fileName);
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
}
