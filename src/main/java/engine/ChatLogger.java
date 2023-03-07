package engine;

/*
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
*/

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class ChatLogger {
    /*
    public static final String FILE_NAME = "chat_log.json";
    private int userId;
    private int conversationId;
    private String userName;
    private String msgText;
    private boolean isRequest;

    private JSONArray userArr;

    public ChatLogger(int userId, String userName, int conversationId, String msgText, boolean isRequest) {
        this.userId = userId;
        this.conversationId = conversationId;
        this.userName = userName;
        this.msgText = msgText;
        this.isRequest = isRequest;
        this.userArr = getUserArr();
    }

    public ChatLogger(int userId, int conversationId, String msgText, boolean isRequest) {
        this.userId = userId;
        this.conversationId = conversationId;
        this.msgText = msgText;
        this.isRequest = isRequest;
        this.userArr = getUserArr();
    }

    public void save() {
        if (!logExists()) {
            compileNewLog();
        }
        if (!userExists()) {
            addUser();
        }
        if (!conversationExists()) {
            addConversation();
        }
        saveMessage();
    }

    *//*
            ->

    save()
        ?
    log exists
    N ->

    compileLog()

    Y ->?
    conversation exists
    N ->

    compileConversation()

    Y ->

    saveMessageStandard()
     *//*

    // TODO: Deal with user doesnt exist
    // TODO: compileUser could be useful in compileNewLog
    public void compileNewLog() {
        JSONArray conversationsArr = new JSONArray();
        conversationsArr.add(compileConversationJSON());

        JSONObject user = compileUserJSON();
        user.put("conversations", conversationsArr);

        JSONArray usersArr = new JSONArray();
        usersArr.add(user);

        if (writeJSONinFile(usersArr)) {
            System.out.println("Basic log structure saved successfully");
            return;
        }
        System.out.println("Basic log structure saving failed");
    }

    public void addUser() {
        userArr.add(compileUserJSON());
    }

    public void addConversation() {
        JSONArray conversationsArr = getConversationsArr();
        JSONObject conversationNew = compileConversationJSON();
        conversationsArr.add(conversationNew);

        JSONObject user = getUser();
        user.put("conversations", conversationsArr);

        ((JSONObject) userArr.get(userId)).put("conversations", conversationsArr);
    }

    private boolean saveMessage() {
        JSONArray messagesArr = getMessagesArr();
        messagesArr.add(compileMessageJSON(messagesArr.size()));

        return writeJSONinFile(userArr);
    }

    private JSONObject compileMessageJSON(int messageId) {
        LocalDateTime dateTime = LocalDateTime.now();
        JSONObject message = new JSONObject();
        message.put("messageId", messageId);
        message.put("messageText", msgText);
        message.put("isRequest", isRequest);
        message.put("dateTime", dateTime.toString());
        return message;
    }

    private JSONObject compileConversationJSON() {
        JSONObject conversation = new JSONObject();
        conversation.put("conversationId", conversationId);
        conversation.put("enabled", true);
        conversation.put("messages", new JSONArray());
        return conversation;
    }

    private JSONObject compileUserJSON() {
        JSONObject user = new JSONObject();
        user.put("userId", userId);
        user.put("userName", userName);
        user.put("conversations", new JSONArray());
        return user;
    }

    private boolean logExists() {
        if (Objects.isNull(userArr) || userArr.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean userExists() {
        for (int i = 0; i < userArr.size(); i++) {
            JSONObject conversation = (JSONObject) userArr.get(i);
            if (Integer.parseInt(conversation.get("userId").toString()) == this.userId)
                return true;
        }
        return false;
    }

    private boolean conversationExists() {
        return this.conversationId < getConversationsArr().size();

//        JSONArray conversationsArr = getConversationsArr();

//        try{
//            getConversation();
//        }
//        catch (IndexOutOfBoundsException e){
//            return false;
//        }
//        return true;

//        for (int i = 0; i < conversationsArr.size(); i++) {
//            JSONObject conversation = (JSONObject) conversationsArr.get(i);
//            if (Integer.parseInt(conversation.get("conversationId").toString()) == conversationId)
//                return true;
//        }
//        return false;
    }

    private JSONArray getUserArr() {
        JSONArray usersArr = readJSONfromFile();
        return usersArr;
    }

    private JSONObject getUser() {
        JSONObject user = (JSONObject) this.userArr.get(this.userId);
        return user;
    }

    private JSONArray getConversationsArr() {
        JSONArray conversations = (JSONArray) getUser().get("conversations");
        return conversations;
    }

    private JSONObject getConversation() {
        JSONObject conversation = (JSONObject) getConversationsArr().get(this.conversationId);
        return conversation;
    }

    private JSONArray getMessagesArr() {
        JSONArray messages = (JSONArray) getConversation().get("messages");
        return messages;
    }


    public JSONArray readJSONfromFile() {
        JSONArray users;
        try {
            FileReader file = new FileReader(FILE_NAME);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(file);
            users = (JSONArray) obj;
            return users;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean writeJSONinFile(JSONAware json) {
        try {
            FileWriter file = new FileWriter(FILE_NAME);
            file.write(json.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static String getFormattedDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return formatter.format(dateTime);
    }

    public static void main(String[] args) {
        ChatLogger logger = new ChatLogger(0, "John", 0, "helloooo", true);
//        ChatLogger logger = new ChatLogger(0, "John", 1, "olaaaa", true);

//        if (logger.compileNewLog()) {
//            System.out.println("great success!");
//        } else {
//            System.out.println("great failure!");
//        }
        logger.save();


        JSONArray users = logger.readJSONfromFile();
        System.out.println(users.toString());
    }

    */

}
