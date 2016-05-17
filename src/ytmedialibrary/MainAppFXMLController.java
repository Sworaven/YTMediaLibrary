/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ytmedialibrary;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author Steven
 */
public class MainAppFXMLController implements Initializable {

    @FXML
    private AnchorPane apRoot;
    @FXML
    private Button btnYTDownload;
    @FXML
    private TextField txtYTLink;
    @FXML
    private ListView<String> lvVideo = new ListView<String>();
    private ObservableList<String> lvItems = FXCollections.observableArrayList();

    @FXML
    private ComboBox cbxPaths;
    @FXML
    private Button btnDirectoryChooser;
    @FXML
    private TextArea rtfConsole;

    @FXML
    private void GenerateListAction(ActionEvent event) {
        outDebug("GenerateListAction started.");
        List<String> options = Arrays.asList(
                ".\\lib\\youtube-dl.exe",
                "-i", "--skip-download",
                "--yes-playlist",
                "--dump-json",
                txtYTLink.getText());
        loadList(getProcess(options).getInputStream());
        outDebug("GenerateListAction finished.");
    }

    @FXML
    private void DirectoryChooserAction(ActionEvent event) {
        chooseDirectory((Stage) apRoot.getScene().getWindow() , System.getProperty("user.dir"));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        outDebug("initialize started.");
        lvVideo.setItems(lvItems);
        outDebug("initialize finished.");
    }

    public Process getProcess(List<String> options) {
        outDebug("getProcess started.");
        ProcessBuilder builder;
        Process process = null;

        try {
            builder = new ProcessBuilder().command(options);
            //builder.redirectErrorStream(true);
            process = builder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        outDebug("getProcess finished.");
        return process;
    }

    public void loadList(InputStream in) {
        outDebug("loadList started.");
        List<String> lines = new ArrayList<>();
        String line;
        String strInput = in.toString();
        int count = 1;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            while ((line = reader.readLine()) != null) {
                outDebug("_Processing videos - #" + count);
                count++;
                lines.add(line);
                outDebug(" .. done");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String l : lines) {
            lvItems.add(l.substring(l.indexOf("\"fulltitle\"") + 14,
                    l.indexOf("\"", l.indexOf("\"fulltitle\"") + 14)));
        }
        outDebug("loadList finished.");
    }

    private void chooseDirectory(Stage stage, String pathField) {
        DirectoryChooser chooser = new DirectoryChooser();
        
        chooser.setTitle("Save files to folder");
        chooser.setInitialDirectory(new File(pathField));
        
        File selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            pathField = selectedDirectory.getAbsolutePath();
        }
        
        cbxPaths.getItems().add(pathField);
    }

    public void outDebug(String message) {

        if (message.startsWith("_")) {
            System.out.print("[DEBUG]: " + message.substring(1));
            rtfConsole.appendText("[DEBUG]: " + message.substring(1));
        } else {
            if (!message.startsWith(" .. ")) {
                message = "[DEBUG]: " + message;
            }
            System.out.println(message);
            rtfConsole.appendText(message + "\n");
        }
    }
}
