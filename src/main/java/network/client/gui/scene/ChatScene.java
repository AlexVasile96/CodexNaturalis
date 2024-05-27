package network.client.gui.scene;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChatScene {
    private Stage stage;
    private TextArea chatArea;
    private TextField inputField;

    public ChatScene() {
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
                chatArea.appendText("You: " + message + "\n");
                inputField.clear();
                // Invia il messaggio al server qui
                sendMessageToServer(message);
            }
        });

        VBox vbox = new VBox(chatArea, inputField, sendButton);
        Scene scene = new Scene(vbox, 300, 400);

        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY); // Popup style
        stage.setOnCloseRequest(event -> stage.hide()); // Hide window on close
    }

    private void sendMessageToServer(String message) {
        // Logica per inviare il messaggio al server
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

    public void appendMessage(String message) {
        chatArea.appendText(message + "\n");
    }
}

