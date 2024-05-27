package network.client.gui.scene;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import network.client.gui.controllers.ChatController;

public class ChatScene {
    private Stage stage;
    private TextArea chatArea;
    private TextField inputField;
    private ChatController controller;

    public ChatScene(String clientName) {
        controller = new ChatController(this, clientName);
        stage = new Stage();
        stage.setTitle("Chat");

        chatArea = new TextArea();
        chatArea.setEditable(false);

        inputField = new TextField();
        inputField.setPromptText("Enter your message...");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> {
            String message = inputField.getText();
            if (!message.isEmpty()) {
                controller.sendMessageToServer(message);
                inputField.clear();
            }
        });

        VBox vbox = new VBox(chatArea, inputField, sendButton);
        Scene scene = new Scene(vbox, 300, 400);

        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY); // Popup style
        stage.setOnCloseRequest(event -> stage.hide()); // Hide window on close

        controller.start();
    }

    public void appendMessage(String message) {
        if (!chatArea.getText().contains(message)) {
            chatArea.appendText(message + "\n");
        }
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    public boolean isShowing() {
        return stage.isShowing();
    }
}
