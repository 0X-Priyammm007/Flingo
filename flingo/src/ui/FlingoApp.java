package ui;

import auth.UserService;
import auth.User;
import flashcards.FlashcardService;
import flashcards.Flashcard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.List;

public class FlingoApp extends Application {
    private Stage primaryStage;
    private UserService userService = new UserService();
    private FlashcardService flashcardService = new FlashcardService();
    private User loggedInUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showLoginScreen();
        primaryStage.setTitle("Flingo - Language Flashcards");
        primaryStage.show();
    }

    private void showLoginScreen() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(30));
        Label title = new Label("Welcome to Flingo!");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #3b5998;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");
        Label message = new Label();

        HBox buttons = new HBox(10, loginBtn, registerBtn);

        root.getChildren().addAll(title, usernameField, passwordField, buttons, message);

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            User user = userService.login(username, password);
            if (user != null) {
                loggedInUser = user;
                showDashboard();
            } else {
                message.setText("Login failed. Try again.");
            }
        });

        registerBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (userService.register(username, password)) {
                message.setText("Registered! Now login.");
            } else {
                message.setText("Registration failed.");
            }
        });

        Scene scene = new Scene(root, 350, 250);
        primaryStage.setScene(scene);
    }

    private void showDashboard() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        Label title = new Label("Flingo Flashcards Dashboard");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        root.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);

        // Flashcard List
        ListView<String> flashcardList = new ListView<>();
        updateFlashcardList(flashcardList, "EN_HI");

        // Controls
        TextField frontField = new TextField();
        frontField.setPromptText("Word or Sentence");
        TextField backField = new TextField();
        backField.setPromptText("Translation");
        ComboBox<String> directionBox = new ComboBox<>();
        directionBox.getItems().addAll("EN_HI", "HI_EN");
        directionBox.setValue("EN_HI");

        Button addBtn = new Button("Add Flashcard");
        Label msg = new Label();

        HBox addRow = new HBox(10, frontField, backField, directionBox, addBtn);
        addRow.setAlignment(Pos.CENTER);

        VBox center = new VBox(12, flashcardList, addRow, msg);
        center.setPadding(new Insets(10));
        root.setCenter(center);

        // Language direction switch
        directionBox.setOnAction(e -> updateFlashcardList(flashcardList, directionBox.getValue()));

        addBtn.setOnAction(e -> {
            String front = frontField.getText().trim();
            String back = backField.getText().trim();
            String dir = directionBox.getValue();
            if (front.isEmpty() || back.isEmpty()) {
                msg.setText("Enter both word and translation!");
                return;
            }
            flashcardService.addFlashcard(loggedInUser.getUserId(), front, back, dir);
            msg.setText("Flashcard added!");
            frontField.clear();
            backField.clear();
            updateFlashcardList(flashcardList, dir);
        });

        // Logout
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            loggedInUser = null;
            showLoginScreen();
        });
        root.setBottom(logoutBtn);
        BorderPane.setAlignment(logoutBtn, Pos.CENTER_RIGHT);

        Scene scene = new Scene(root, 550, 400);
        primaryStage.setScene(scene);
    }

    private void updateFlashcardList(ListView<String> listView, String direction) {
        if (loggedInUser == null) return;
        List<Flashcard> cards = flashcardService.getFlashcards(loggedInUser.getUserId(), direction);
        listView.getItems().clear();
        for (Flashcard card : cards) {
            listView.getItems().add(card.getFrontText() + "  ➔  " + card.getBackText());
        }
    }
}