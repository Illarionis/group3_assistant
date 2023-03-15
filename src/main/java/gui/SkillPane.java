package gui;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public abstract class SkillPane extends VBox {
    private final List<TextField> outputFields;
    private final TextField inputField;
    public SkillPane() {
        outputFields = new ArrayList<>();
        inputField = new TextField();
        /* *
         * Task:
         * Design the skill (editor) pane.
         * */
    }

    public void addOneOutputField() {
        /* *
         * Task:
         * Add a new output field (aka slot) to the slot list.
         * */
    }

    public void removeOneOutputField() {
        /* *
         * Task:
         * Remove the last output field (aka slot) from the slot list if
         * the list size is larger than 1.
         * */
    }

    /**
     * Obtains the delete button of the skill pane.
     *
     * @return The button the user should press to remove the skill locally and from the assistant.
     **/
    public Button getDeleteButton() {
        /* *
         * Task:
         * Return the delete button after the design is completed.
         * */
        return null;
    }

    /**
     * Obtains the save button of the skill pane.
     *
     * @return The button the user should press to save the skill to a file.
     **/
    public Button getSaveButton() {
        /* *
         * Task:
         * Return the save button after the design is completed.
         * */
        return null;
    }

    public List<TextField> getOutputFields() {
        return outputFields;
    }

    public TextField getInputField() {
        return inputField;
    }
}
