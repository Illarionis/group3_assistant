package gui;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Provides the authorization pane, which is the graphical component
 * responsible for providing the sign-in and sign-up options.
 **/
public abstract class AuthorizationPane extends Pane {
    public AuthorizationPane() {
        /* *
         * Task:
         * Design the pane with at least 2 buttons: one to sign-up and the other sign-in.
         * And naturally have some form input fields for the username and password.
         * */
    }

    protected Button getSignUpButton() {
        /* *
         * Task:
         * Return the button the user should press in order to sign-up,
         * after having filled an account name and password.
         * */
        return null;
    }

    protected Button getSignInButton() {
        /* *
         * Task:
         * Return the button the user should press in order to sign-in,
         * after having filled a username and password.
         * */
        return null;
    }

    protected TextField getPasswordField() {
        /* *
         * Task:
         * After the overall design of the pane is done,
         * Return the text field responsible for the password input
         * */
        return null;
    }

    protected TextField getUsernameField() {
        /* *
         * Task:
         * After the overall design of the pane is done,
         * Return the text field responsible for the username input
         * */
        return null;
    }
}
