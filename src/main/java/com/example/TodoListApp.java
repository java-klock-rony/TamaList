package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TodoListApp extends Application {

    private ListView<String> listView; // On utilise le type générique ListView<String> pour indiquer que la liste ne
                                       // contient que des chaînes de caractères
    private ObservableList<String> tasks; // On utilise le type générique ObservableList<String> pour indiquer que la
                                          // liste ne contient que des chaînes de caractères

    @Override
    public void start(Stage primaryStage) {
        tasks = FXCollections.observableArrayList(); // Task est une classe qui implémente l'interface ObservableList
        loadTasksFromFile(); // On charge les tâches à partir d'un fichier texte
        // On crée les composants graphiques
        BorderPane root = new BorderPane();
        HBox inputBox = createInputBox();
        listView = createListView();
        Button clearButton = createClearButton();

        root.setTop(inputBox);
        root.setCenter(listView);
        root.setBottom(clearButton);

        Scene scene = new Scene(root, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rorogochi's Todo List");
        primaryStage.show();
    }

    @Override
    public void stop() {
        saveTasksToFile();
    }

    // On aligne les composants graphiques horizontalement
    private HBox createInputBox() {
        TextField taskInput = new TextField();
        Button addButton = new Button("Ajouter");
        addButton.setOnAction(event -> addTask(taskInput.getText()));

        Button editButton = new Button("Éditer");
        editButton.setOnAction(event -> editTask());

        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> deleteTask());

        HBox inputBox = new HBox();
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(taskInput, addButton, editButton, deleteButton);

        return inputBox;
    }

    // On crée une liste de tâches
    private ListView<String> createListView() {
        ListView<String> listView = new ListView<>(tasks);
        listView.setPrefHeight(300);

        return listView;
    }

    // On crée un bouton pour effacer la liste
    private Button createClearButton() {
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(event -> clearTasks());

        return clearButton;
    }

    // On ajoute une tâche à la liste
    private void addTask(String task) {
        if (!task.isEmpty()) {
            tasks.add(task);
        }
    }

    // On édite une tâche de la liste
    private void editTask() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String currentTask = tasks.get(selectedIndex);
            String editedTask = showEditTaskDialog(currentTask);
            if (editedTask != null && !editedTask.isEmpty()) {
                tasks.set(selectedIndex, editedTask);
            }
        }
    }

    // On supprime une tâche de la liste
    private void deleteTask() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            tasks.remove(selectedIndex);
        }
    }

    // On efface la liste
    private void clearTasks() {
        tasks.clear();
    }

    // On crée une boîte de dialogue pour éditer une tâche
    private String showEditTaskDialog(String currentTask) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Éditer la tâche");

        TextField taskInput = new TextField(currentTask);
        taskInput.setPrefWidth(200);

        Button okButton = new Button("OK");
        okButton.setOnAction(event -> {
            dialogStage.close();
        });

        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(10));
        dialogVBox.getChildren().addAll(taskInput, okButton);

        Scene dialogScene = new Scene(dialogVBox);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();

        return taskInput.getText();
    }

    // Méthode pour sauvegarder les tâches dans un fichier texte
    private void saveTasksToFile() {
        try {
            File file = new File("tasks.txt");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String task : tasks) {
                bufferedWriter.write(task);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour charger les tâches à partir d'un fichier texte.
    private void loadTasksFromFile() {
        try {
            File file = new File("tasks.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                tasks.add(line);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // On lance l'application
    public static void main(String[] args) {
        launch(args);
    }
}
