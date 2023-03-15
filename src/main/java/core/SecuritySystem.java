package core;

import design.DataFile;
import engine.Account;
import gui.AuthorizationPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.UUID;


/**
 * Provides the one and only obstacle the user has to go through
 * in order to access the digital assistant >.>
 **/
public final class SecuritySystem extends AuthorizationPane {
    private static final Database ACCOUNTS = new Database("/.data/accounts");
    /**
     * Creates the security system.
     *
     * @param s The stage that should show the user session on a successful authorization.
     **/
    public SecuritySystem(Stage s) {
        final DataFile[] accountFiles = ACCOUNTS.getFiles("");
        final Account[] registered = new Account[accountFiles.length];
        for (int i = 0; i < accountFiles.length; i++) {
            final String snapshot = accountFiles[i].getData();
            registered[i] = new Account(snapshot);
        }

        final Button signInButton = getSignInButton();
        signInButton.setOnAction(clickEvent -> {
            final String username = getUsernameField().getText();
            final String password = getPasswordField().getText();
            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                return;
            }
            for (Account a : registered) {
                if (a.authorize(username, password)) {
                    showSession(s, a.getId());
                    return;
                }
            }
        });

        final Button signUpButton = getSignUpButton();
        signUpButton.setOnAction(clickEvent -> {
            final String username = getUsernameField().getText();
            final String password = getPasswordField().getText();
            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                return;
            }
            for (Account a : registered) {
                if (a.getUsername().equals(username)) {
                    return;
                }
            }
            final String newAccountId = UUID.randomUUID().toString();
            final Account newAccount = new Account(newAccountId, username, password);
            final DataFile f = ACCOUNTS.createNewFile(newAccountId);
            if (f != null) {
                f.storeData(newAccount.snapshot());
                showSession(s, newAccountId);
            }
        });
    }

    private void showSession(Stage s, String accountId) {
        final Session sceneRoot = new Session(accountId);
        final Scene userSession = new Scene(sceneRoot, 600, 600);

        s.setScene(userSession);
        if (!s.isShowing()) {
            s.show();
        }
    }
}
