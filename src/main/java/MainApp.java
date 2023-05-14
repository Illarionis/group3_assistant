import engine.Assistant;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public final class MainApp extends Application {
    final Color[] baseColors = {
            Color.color(0.25, 0.25, 0.25), // Night Mode
            Color.color(0.75, 0.75, 0.75)  // Day Mode
    };

    final Color[] fontColors = {
            Color.WHITE,
            Color.BLACK
    };

    final Color[] highlightColors = {
            Color.color(0.5, 0.5, 0.5, 0.5), // Default Highlight
            Color.color(0.9, 0.1, 0.3, 0.5)  // Close-Cancel
    };

    final Color[] messageColors = {
            Color.rgb(200, 225, 255),
            Color.rgb(200, 255, 225)
    };

    final Color[] overlayColors = {
            baseColors[0].darker(),
            baseColors[1].darker()
    };


    final Background[] highlightBackgrounds = {
            Background.fill(highlightColors[0]),
            Background.fill(highlightColors[1])
    };

    final Background[] messageBackgrounds = {
            Background.fill(messageColors[0]),
            Background.fill(messageColors[1])
    };

    final Background[] overlayBackgrounds = {
            Background.fill(overlayColors[0]),
            Background.fill(overlayColors[1])
    };

    final Insets defaultPadding = new Insets(5.0, 5.0, 5.0, 5.0);

    final String[] colorModeSymbols = {
            Character.toString(9728),   // Sun Symbol
            Character.toString(127769)  // Moon Symbol
    };

    final String[] sceneSelectorItems = {
            "Chat",
            "Editor"
    };

    @Override
    public void start(Stage primaryStage) {
        // Accessing CSS files
        final var scrollPaneCSS       = getClass().getResource("/transparent-scroll-pane.css");
        final var brighterInputCSS    = getClass().getResource("/brighter-input.css");
        final var darkerInputCSS      = getClass().getResource("/darker-input.css");
        final var brighterBoxInputCSS = getClass().getResource("/brighter-box-input.css");
        final var darkerBoxInputCSS   = getClass().getResource("/darker-box-input.css");
        if      (scrollPaneCSS       == null) throw new IllegalStateException("Failed to fetch Scroll Pane CSS file!");
        else if (brighterInputCSS    == null) throw new IllegalStateException("Failed to fetch Brighter Input CSS file!");
        else if (darkerInputCSS      == null) throw new IllegalStateException("Failed to fetch Darker Input CSS file!");
        else if (brighterBoxInputCSS == null) throw new IllegalStateException("Failed to fetch Brighter Box CSS file!");
        else if (darkerBoxInputCSS   == null) throw new IllegalStateException("Failed to fetch Darker Box CSS file!");

        // Preparing CSS
        final String scrollPaneStyle        = scrollPaneCSS.toExternalForm();
        final String brighterInputStyle     = brighterInputCSS.toExternalForm();
        final String darkerInputStyle       = darkerInputCSS.toExternalForm();
        final String brighterBoxInputStyle  = brighterBoxInputCSS.toExternalForm();
        final String darkerBoxInputStyle    = darkerBoxInputCSS.toExternalForm();

        // Preparing title bar
        final Button minimizeStage  = createButton(Character.toString(8212));
        final Button closeStage     = createButton(Character.toString(10005));
        final HBox   stageControls  = encapsulateHorizontally(minimizeStage, closeStage);
        final Button switchColor    = createButton(colorModeSymbols[0]);
        final var    sceneSelector  = createCombobox();
        final HBox   colorControls  = encapsulateHorizontally(switchColor, sceneSelector);
        final Button titleHolder    = createButton("Digital Assistant");
        final HBox   titleBar       = encapsulateHorizontally(colorControls, titleHolder, stageControls);

        // Preparing chat
        final var assistant = new Assistant();
        final VBox chatHistory = new VBox();
        final ScrollPane chatViewport = new ScrollPane(chatHistory);
        final TextField chatInput = createChatInputField(chatHistory, assistant);
        final VBox chatPanel = encapsulateVertically(titleBar, chatViewport, chatInput);
        final Scene chatScene = new Scene(chatPanel, 360, 720);

        // Todo 1: Preparing skill editor
        final VBox editorPanel = encapsulateVertically();
        final Scene editorScene = new Scene(editorPanel, 720, 720);

        // Todo 2: Preparing authorization processes

        // Preparing day-night theme functionality
        final EventHandler<ActionEvent> changeColorHandler = (event -> {
            switchBackground(titleBar, chatInput);
            switchFill(chatScene, editorScene);
            switchFont(minimizeStage, closeStage, switchColor, titleHolder);
            switchFontColor(brighterInputStyle, darkerInputStyle, chatInput);
            switchFontColor(brighterBoxInputStyle, darkerBoxInputStyle, sceneSelector);
            if (Objects.equals(switchColor.getText(), colorModeSymbols[0])) switchColor.setText(colorModeSymbols[1]);
            else switchColor.setText(colorModeSymbols[0]);
        });

        // Assigning button actions
        minimizeStage.setOnAction(event -> primaryStage.setIconified(true));
        closeStage.setOnAction(event -> primaryStage.close());
        switchColor.setOnAction(changeColorHandler);

        // Assigning button highlight events
        setMouseHighlightEvent(highlightBackgrounds[0], minimizeStage, switchColor);
        setMouseHighlightEvent(highlightBackgrounds[1], closeStage);

        // Configuring how scenes are switched using the combo box
        sceneSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue.equals(newValue)) return;
            else if (newValue.equals("Chat")) {
                editorPanel.getChildren().remove(titleBar);
                chatPanel.getChildren().add(0, titleBar);
                primaryStage.setScene(chatScene);
            }
            else {
                chatPanel.getChildren().remove(titleBar);
                editorPanel.getChildren().add(0, titleBar);
                primaryStage.setScene(editorScene);
            }
            primaryStage.centerOnScreen();
        });

        // Setting properties for resizeable priority components
        HBox.setHgrow(titleHolder, Priority.ALWAYS);
        VBox.setVgrow(chatViewport, Priority.ALWAYS);

        // Assigning stage relocation functionality
        relocateOnDrag(primaryStage, titleHolder);

        // Assigning stylesheets
        chatViewport.getStylesheets().add(scrollPaneStyle);

        // Completing configurations-decorations
        chatHistory.setPadding(defaultPadding);
        chatHistory.setSpacing(5.0);

        // Setting components transparent
        setTransparent(sceneSelector, chatPanel, editorPanel);

        // Painting the application to night-mode
        changeColorHandler.handle(null);

        // Configuring the application
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Digital Assistant");
        primaryStage.setScene(chatScene);
        primaryStage.show();
    }

    private Button createButton(String s) {
        final var button = new Button(s);
        button.setMinSize(50, 50);
        button.setMaxWidth(Double.MAX_VALUE);
        setTransparent(button);
        return button;
    }

    private TextField createChatInputField(Pane p, Assistant a) {
        final var field = new TextField();
        field.setPadding(defaultPadding);
        field.setPromptText("Click to type");
        field.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.ENTER || field.getText().isBlank()) return;
            final String userInput = field.getText();
            field.clear();

            final var userMessage = createUserMessage(userInput);
            p.getChildren().add(userMessage);

            final String assistantOutput = a.respond(userInput);
            final var assistantMessage = createSystemMessage(assistantOutput);
            p.getChildren().add(assistantMessage);
        });
        return field;
    }

    private ComboBox<String> createCombobox() {
        final var box = new ComboBox<String>();
        box.getItems().addAll(sceneSelectorItems);
        box.setValue(sceneSelectorItems[0]);
        return box;
    }

    private TextFlow createMessageFlow(String s) {
        final var text = new Text(s);
        final var flow = new TextFlow(text);
        flow.setPadding(defaultPadding);
        flow.setMaxWidth(240);
        return flow;
    }

    private HBox createSystemMessage(String s) {
        final var flow = createMessageFlow(s);
        flow.setBackground(messageBackgrounds[0]);

        final var box = new HBox(flow);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private HBox createUserMessage(String s) {
        final var flow = createMessageFlow(s);
        flow.setBackground(messageBackgrounds[1]);

        final var box = new HBox(flow);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }

    private HBox encapsulateHorizontally(Node... n) {
        final var box = new HBox(n);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private VBox encapsulateVertically(Node... n) {
        final var box = new VBox(n);
        box.setAlignment(Pos.TOP_CENTER);
        return box;
    }

    private void relocateOnDrag(Stage s, Region... r) {
        for (var region : r) {
            final MouseEvent[] history = new MouseEvent[1];
            region.setOnMousePressed(event -> history[0] = event);
            region.setOnMouseDragReleased(event -> history[0] = null);
            region.setOnMouseDragged(event -> {
                double dx = event.getScreenX() - history[0].getScreenX();
                double dy = event.getScreenY() - history[0].getScreenY();
                s.setX(s.getX() + dx);
                s.setY(s.getY() + dy);
                history[0] = event;
            });

        }
    }

    private void setMouseHighlightEvent(Background bg, Button... b) {
        for (var button : b) {
            button.setOnMouseEntered(event -> button.setBackground(bg));
            button.setOnMouseExited(event -> button.setBackground(Background.EMPTY));
        }
    }

    private void setTransparent(Region... r) {
        for (var region : r) {
            region.setBackground(Background.EMPTY);
            region.setBorder(Border.EMPTY);
        }
    }

    private void switchBackground(Region... r) {
        for (var region : r) {
            if (region.getBackground() == overlayBackgrounds[0]) region.setBackground(overlayBackgrounds[1]);
            else region.setBackground(overlayBackgrounds[0]);
        }
    }

    private void switchFill(Scene... s) {
        for (var scene : s) {
            if (scene.getFill() == baseColors[0]) scene.setFill(baseColors[1]);
            else scene.setFill(baseColors[0]);
        }
    }

    private void switchFont(Button... b) {
        for (var button : b) {
            if (button.getTextFill() == fontColors[0]) button.setTextFill(fontColors[1]);
            else button.setTextFill(fontColors[0]);
        }
    }

    private void switchFontColor(String brighterInputStyle, String darkerInputStyle, TextField... f) {
        for (var field : f) {
            final var styles = field.getStylesheets();
            if (styles.remove(brighterInputStyle)) styles.add(darkerInputStyle);
            else styles.add(brighterInputStyle);
        }
    }

    private void switchFontColor(String brightBoxInputStyle, String darkBoxInputStyle, ComboBox<String> box) {
        final var styles = box.getStylesheets();
        if (styles.remove(brightBoxInputStyle)) styles.add(darkBoxInputStyle);
        else styles.add(brightBoxInputStyle);
    }
}