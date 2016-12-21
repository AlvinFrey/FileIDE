package fr.fileide;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private TreeView<String> treeView;

    @FXML
    private Label messageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        messageLabel.setText("Bonjour ici Albert Reporter ! ");
        File currentDir = new File("src/fr/fileide/");
        findFiles(currentDir,null);

    }

    private void findFiles(File dir, TreeItem<String> parent) {

        TreeItem root = new TreeItem<>(dir.getName());

        Image image = new Image("fr/fileide/folder.png");
        root.setGraphic(new ImageView((Image) image));

        root.setExpanded(true);

        File[] files = dir.listFiles();

        for (File file : files) {

            if (file.isDirectory()) {
                findFiles(file,root);
            } else {
                root.getChildren().add(new TreeItem<>(file.getName()));
            }

        }

        if(parent==null){
            treeView.setRoot(root);
        } else {
            parent.getChildren().add(root);
        }

    }

}
