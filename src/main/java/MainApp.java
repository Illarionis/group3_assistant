import engine.*;
import gui.Chat;
import gui.LogIn;
import gui.Overview;
import gui.SkillEditor;
import io.DataReader;
import io.DataWriter;
import io.DirectoryGenerator;
import io.DirectoryReader;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MainApp extends Application {
    private static final String ACCOUNT_DIRECTORY = "data/accounts";
    private static final String CHAT_DIRECTORY = "data/chats";
    private static final String SKILL_DIRECTORY = "data/skills";
    private final DataReader dataReader = new DataReader();
    private final DataWriter dataWriter = new DataWriter();
    private final DirectoryGenerator directoryGenerator = new DirectoryGenerator();
    private final DirectoryReader directoryReader = new DirectoryReader();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Preparing directories.
        directoryGenerator.generate(ACCOUNT_DIRECTORY);
        directoryGenerator.generate(CHAT_DIRECTORY);
        directoryGenerator.generate(SKILL_DIRECTORY);

        // Loading accounts to the memory
        final List<Account> accountList = getAccounts();

        // Getting the authorization handlers
        final AuthorizationHandler registrationHandler = createRegistrationHandler(primaryStage, accountList);
        final AuthorizationHandler loginHandler = createLoginHandler(primaryStage, accountList);

        final LogIn root = new LogIn(registrationHandler, loginHandler);
        final Scene s = new Scene(root, 600, 600);
        primaryStage.setScene(s);
        primaryStage.setTitle("Digital Assistant");
        primaryStage.show();
    }

    /* *
     * Remark:
     * All methods below are sorted alphabetically by name,
     * */

    /**
     * Creates an authorization handler that is responsible for handling login events.
     *
     * @param primaryStage The stage that gets passed to the application start(Stage) method.
     * @param accountList  The list of accounts obtained from the getAccounts() method.
     * @return An authorization handler that loads the user session whenever an account has logged in successfully.
     **/
    private AuthorizationHandler createLoginHandler(Stage primaryStage, List<Account> accountList) {
        return (username, password) -> {
            final Account a = findAccount(username, accountList);
            if (a != null && a.getPassword().equals(password)) {
                loadUserSession(primaryStage, a.getId());
            }
            return false;
        };
    }

    /**
     * Creates an authorization handler that is responsible for handling registration events.
     *
     * @param primaryStage The stage that gets passed to the application start(Stage) method.
     * @param accountList  The list of accounts obtained from the getAccounts() method.
     * @return An authorization handler that loads the user session whenever an account has been registered successfully.
     **/
    private AuthorizationHandler createRegistrationHandler(Stage primaryStage, List<Account> accountList) {
        return (username, password) -> {
            if (findAccount(username, accountList) == null) {
                // Creating and saving the new account
                final String accountId = UUID.randomUUID().toString();
                final Account a = new Account(accountId, username, password);
                final File accountFile = Path.of(ACCOUNT_DIRECTORY, accountId).toFile();
                dataWriter.write(accountFile, a.toString());

                // Loading user session
                loadUserSession(primaryStage, accountId);
                return true;
            }
            return false;
        };
    }

    /**
     * Establishes the interactive link between the front-end class Chat and the back-end class Assistant.
     *
     * @param accountId The id of the account for which the link between the assistant and the chat should be made.
     * @param a         The assistant that should be interacting with the user.
     * @param c         The chat in which the assistant and user should be interacting with each other.
     **/
    private void establishConnections(String accountId, Assistant a, Chat c) {
        final var inputField = c.getMessageField();
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                final String input = inputField.getText();
                if (input.isBlank()) {
                    return;
                }
                c.newMessage(input, accountId);
                inputField.clear();

                final String[] responses = a.respond(input);
                for (String s : responses) {
                    c.newMessage(s, "Assistant");
                }
            }
        });
    }

    /**
     * Establishes the data preservation and deletion link between the front-end classes Chat and Overview.
     *
     * @param f The file that should contain the chat data.
     * @param o The overview that should display the chat.
     * @param c The chat that should be linked with the overview and file.
     **/
    private void establishConnections(File f, Overview o, Chat c) {
        // Adding the chat to the overview
        final EventHandler<ActionEvent> chatRemovalHandler = o.add(c, c.getTitleTracker());

        // Defining how the chat gets deleted
        c.getDeleteButton().setOnAction(event -> {
            if (f.delete() || !f.exists()) {
                chatRemovalHandler.handle(event);
            }
        });

        // Defining how the chat gets saved
        c.getSaveButton().setOnAction(event -> dataWriter.write(f, c.toString()));
    }

    /**
     * Establishes the data preservation and deletion link for the front-end classes SkillEditor and Overview and
     * the back-end class Assistant.
     *
     * @param f The file that should contain the skill data.
     * @param a The assistant that should be linked with the skill data.
     * @param o The overview that should display the skill.
     * @param e The skill that should be linked with the assistant and overview.
     **/
    private void establishConnections(File f, Assistant a, Overview o, SkillEditor e) {
        // Adding the skill (editor) to the overview and assistant.
        final EventHandler<ActionEvent> skillRemovalHandler = o.add(e, e.getTitleTracker());
        a.addSkill(e);

        // Defining how the skill (editor) gets deleted
        e.getDeleteButton().setOnAction(event -> {
            if (f.delete()) {
                a.removeSkill(e);
                skillRemovalHandler.handle(event);
            }
        });

        // Defining how the skill (editor) gets saved
        e.getSaveButton().setOnAction(event -> dataWriter.write(f, e.toString()));
    }

    /**
     * Finds an account by the username from a list of accounts.
     *
     * @param username    The username the account should have.
     * @param accountList A list of accounts to search from.
     * @return The account with the provided username, if the usernames are equal. <br>
     *         Null, otherwise.
     **/
    private Account findAccount(String username, List<Account> accountList) {
        for (Account a : accountList) {
            if (a.getUsername().equals(username)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Obtains the accounts that have been stored locally.
     *
     * @return The list of accounts that have been stored.
     **/
    private List<Account> getAccounts() {
        final List<Account> l = new ArrayList<>();
        for (File f : directoryReader.getFiles(ACCOUNT_DIRECTORY)) {
            final String accountData = dataReader.read(f);
            final Account a = Account.accountOf(accountData);
            l.add(a);
        }
        return l;
    }

    /**
     * Creates an event handler that is responsible for creating a new chat whenever
     * a new chat event occurs.
     *
     * @param accountId The id of the account for which a new chat will be created.
     * @param a         The assistant the user will be interacting with.
     * @param o         The overview that should be displaying the new chat.
     * @return An event handler that can be invoked to create a new chat for the provided parameters.
     **/
    private EventHandler<ActionEvent> getNewChatEventHandler(String accountId, Assistant a, Overview o) {
        return event -> {
            final String randomId = UUID.randomUUID().toString();
            final File f = Path.of(CHAT_DIRECTORY, accountId, randomId).toFile();
            final Chat c = new Chat();
            establishConnections(f, o, c);
            establishConnections(accountId, a, c);
        };
    }

    /**
     * Creates an event handler that is responsible for creating a new skill whenever
     * a new skill event occurs.
     *
     * @param accountId The id of the account for which a new skill will be created.
     * @param a         The assistant the user will be interacting with.
     * @param o         The overview that should be displaying the new skill.
     * @return An event handler that can be invoked to create a new skill for the provided parameters.
     **/
    private EventHandler<ActionEvent> getNewSkillEventHandler(String accountId, Assistant a, Overview o) {
        return event -> {
            final String randomId = UUID.randomUUID().toString();
            final File f = Path.of(SKILL_DIRECTORY, accountId, randomId).toFile();
            final SkillEditor e = new SkillEditor();
            establishConnections(f, a, o, e);
        };
    }

    /**
     * Loads all chats associated with an account that have been stored locally.
     *
     * @param accountId The id of the account for which the chats should be loaded.
     * @param a         The assistant the user will be interacting with.
     * @param o         The overview that should be displaying the chats.
     **/
    private void loadChats(String accountId, Assistant a, Overview o) {
        final String chatDirectoryOfAccount = Path.of(CHAT_DIRECTORY, accountId).toString();
        directoryGenerator.generate(chatDirectoryOfAccount);

        for (File f : directoryReader.getFiles(chatDirectoryOfAccount)) {
            final String s = dataReader.read(f);
            final Chat c = new Chat(s);
            establishConnections(f, o, c);
            establishConnections(accountId, a, c);
        }
    }

    /**
     * Loads all skills associated with an account that have been stored locally.
     *
     * @param accountId The id of the account for which the skills should be loaded.
     * @param a         The assistant the user will be interacting with.
     * @param o         The overview that should be displaying the skills.
     **/
    private void loadSkills(String accountId, Assistant a, Overview o) {
        final String skillDirectoryOfAccount = Path.of(SKILL_DIRECTORY, accountId).toString();
        directoryGenerator.generate(skillDirectoryOfAccount);

        for (File f : directoryReader.getFiles(skillDirectoryOfAccount)) {
            final String s = dataReader.read(f);
            final SkillEditor e = new SkillEditor(s);
            establishConnections(f, a, o, e);
        }
    }

    /**
     * Loads the user session on the primary stage.
     *
     * @param primaryStage The stage that gets passed to the application start(Stage) method.
     * @param accountId    The id of the account for which the session should be loaded.
     **/
    private void loadUserSession(Stage primaryStage, String accountId) {

        // Defining an assistant that is used globally during user session.
        final Assistant a = new Assistant();
        System.out.println("-------------Assistant loaded------------");

        // Creating and configuring the chat overview.
        final Overview chatOverview = new Overview("CONVERSATIONS", "CHAT");
        chatOverview.setEventHandler(getNewChatEventHandler(accountId, a, chatOverview));
        chatOverview.setPrefSize(1920, 1080);
        System.out.println("-------------chat overview------------");

        // Creating and configuring the skill overview
        final Overview skillOverview = new Overview("SKILL LIST", "SKILL EDITOR");
        skillOverview.setEventHandler(getNewSkillEventHandler(accountId, a, skillOverview));
        skillOverview.setPrefSize(1920, 1080);
        System.out.println("-------------skill overview------------");

        // Loading existing data
        loadChats(accountId, a, chatOverview);
        System.out.println("-------------chats loaded------------");
        loadSkills(accountId, a, skillOverview);
        System.out.println("-------------skills loaded------------");

        // Preparing the scene root (aka user session)
        final HBox root = new HBox();
        root.getChildren().addAll(skillOverview, chatOverview);
        root.setAlignment(Pos.CENTER);

        System.out.println("-------------scene loaded------------");
        final Scene s = new Scene(root, 1280, 960);
        primaryStage.setScene(s);
        primaryStage.centerOnScreen();
        if (!primaryStage.isShowing()) {
            primaryStage.show();
        }
        primaryStage.setMaximized(true);
        System.out.println("-------------scene shown------------");
    }
}
