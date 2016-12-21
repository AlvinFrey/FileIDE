package fr.fileide;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    Image folderIcon = new Image("fr/fileide/resources/icons/folder.png");
    Image fileIcon = new Image("fr/fileide/resources/icons/file.png");

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextArea textArea;

    @FXML
    private Label messageLabel;

    private String fileDirectory = "src/fr/fileide/";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        messageLabel.setText("FileIDE");
        File currentDir = new File(fileDirectory);
        findFiles(currentDir, null);

        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getClickCount() == 2) {

                    TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

                    if (selectedItem.getChildren().isEmpty()) {

                        StringBuilder pathBuilder = new StringBuilder();
                        for (TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
                             item != null; item = item.getParent()) {

                            pathBuilder.insert(0, item.getValue());
                            pathBuilder.insert(0, "/");
                        }
                        String path = pathBuilder.toString();

                        path = "src/fr/" + path;

                        openFile(selectedItem, path);

                    } else {

                        System.out.println("EN PRINCIPE, c'est un dossier donc balek");

                    }

                }

            }
        });

    }

    public void openFile(TreeItem<String> selectedItem, String path) {

        try {

            final String fileContent = new String(Files.readAllBytes(Paths.get(path)));

            final Tab tab = new Tab(selectedItem.getValue());
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            TextArea textArea = new TextArea();
            tab.setContent(textArea);
            textArea.setText(fileContent);
            textArea.requestFocus();

            messageLabel.setText("Vous venez d'ouvrir le fichier : " + selectedItem.getValue());

            textArea.textProperty().addListener((observable, oldValue, newValue) -> {

                tab.setText(selectedItem.getValue() + "*");
                tab.setStyle("-fx-background-color: #ef5350;");

            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void findFiles(File dir, TreeItem<String> parent) {

        TreeItem<String> root = new TreeItem<>(dir.getName());

        root.setExpanded(true);

        File[] files = dir.listFiles();

        for (File file : files) {

            if (file.isDirectory()) {
                root.setGraphic(new ImageView((Image) folderIcon));
                findFiles(file,root);
            } else {
                root.getChildren().add(new TreeItem<>(file.getName(), new ImageView(fileIcon)));
            }

        }

        if(parent==null){
            treeView.setRoot(root);
        } else {
            parent.getChildren().add(root);
            root.setGraphic(new ImageView((Image) folderIcon));
        }

    }

    public void newFile(ActionEvent actionEvent) {

        TextInputDialog dialog = new TextInputDialog("untitled");
        dialog.setTitle("Nom du fichier");
        dialog.setHeaderText("Veuillez choisir un nom pour votre nouveau fichier");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {

            final Tab tab = new Tab(result.get() + "*");
            tab.setStyle("-fx-background-color: #ef5350;");
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            TextArea textArea = new TextArea();
            tab.setContent(textArea);
            textArea.requestFocus();

            messageLabel.setText("Vous venez d'ouvrir un nouveau fichier nommé : " + result.get());

        }

    }

}
