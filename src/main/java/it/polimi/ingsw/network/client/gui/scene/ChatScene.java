package it.polimi.ingsw.network.client.gui.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import it.polimi.ingsw.network.client.gui.controllers.ChatController;

import java.util.HashSet;
import java.util.Set;

public class ChatScene {
    private Stage stage;
    private VBox chatArea;
    private TextField inputField;
    private TextField recipientField;
    private ChatController controller;
    private Set<String> messageHistory;
    private String clientName;

    public ChatScene(String clientName) {
        this.clientName = clientName;
        controller = new ChatController(this, clientName);
        stage = new Stage();
        stage.setTitle("Chat");

        chatArea = new VBox(10);
        chatArea.setPadding(new Insets(10));
        chatArea.setStyle("-fx-background-color: #f0f0f0;");

        ScrollPane scrollPane = new ScrollPane(chatArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f0f0f0;");

        recipientField = new TextField();
        recipientField.setPromptText("Recipient (leave empty for public message)");
        recipientField.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px;");

        inputField = new TextField();
        inputField.setPromptText("Enter your message...");
        inputField.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px;");
        inputField.setOnAction(event -> sendMessage());

        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendMessage());
        sendButton.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-background-color: #007bff; -fx-text-fill: white;");

        HBox inputBox = new HBox(10, recipientField, inputField, sendButton);
        inputBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputBox.setPadding(new Insets(10));
        inputBox.setStyle("-fx-background-color: #e0e0e0;");

        VBox vbox = new VBox(10, scrollPane, inputBox);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 400, 500);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
                sendMessage();
                event.consume();
            }
        });

        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY); // Popup style
        stage.setOnCloseRequest(event -> stage.hide()); // Hide window on close

        messageHistory = new HashSet<>();
        controller.start();
    }

    private void sendMessage() {
        String recipient = recipientField.getText().trim();
        String message = inputField.getText().trim();
        if (!message.isEmpty() && !messageHistory.contains(message)) {
            if (!recipient.isEmpty()) {
                controller.sendPrivateMessage(recipient, message);
            } else {
                controller.sendMessageToServer(message);
            }
            appendMessage(clientName + ": " + message, true);
            inputField.clear();
            recipientField.clear();
        }
    }

    public void appendMessage(String message, boolean sentByUser) {
        if (sentByUser || (!message.startsWith(clientName + ": ") && !messageHistory.contains(message))) {
            HBox messageBox = new HBox();
            Label messageLabel = new Label(message);
            messageLabel.setWrapText(true);
            messageLabel.setPadding(new Insets(5, 10, 5, 10));
            messageLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px;");

            if (sentByUser) {
                messageBox.setAlignment(Pos.CENTER_RIGHT);
                messageLabel.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-border-radius: 10; -fx-background-radius: 10;");
            } else {
                messageBox.setAlignment(Pos.CENTER_LEFT);
                messageLabel.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: black; -fx-border-radius: 10; -fx-background-radius: 10;");
            }

            messageBox.getChildren().add(messageLabel);
            chatArea.getChildren().add(messageBox);
            messageHistory.add(message);
        }
    }

    public void appendMessage(String message) {
        appendMessage(message, false);
    }

    public void show() {
        stage.show();
    }

}
