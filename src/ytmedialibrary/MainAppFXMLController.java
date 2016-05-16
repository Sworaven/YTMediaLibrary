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

/**
 *
 * @author Steven
 */
public class MainAppFXMLController implements Initializable {

    @FXML
    private Button btnYTDownload;
    @FXML
    private TextField txtYTLink;
    @FXML
    private ListView<String> lvVideo = new ListView<String>();
    private ObservableList<String> lvItems = FXCollections.observableArrayList();

    @FXML
    private void GenerateListAction(ActionEvent event) {
        List<String> options = Arrays.asList(
                ".\\lib\\youtube-dl.exe",
                "-i", "-s",
                "--yes-playlist",
                "--dump-json",
                txtYTLink.getText());
        loadList(getProcess(options).getInputStream());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lvVideo.setItems(lvItems);
    }
    
    public Process getProcess(List<String> options)
    {
        ProcessBuilder builder;
        Process process = null;
        
        try {
            builder = new ProcessBuilder().command(options);
            //builder.redirectErrorStream(true);
            process = builder.start();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return process;
    }
    
    public void loadList(InputStream in){
        
        String line;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            while ((line = reader.readLine()) != null) {
                lvItems.add(line.substring(line.indexOf("\"fulltitle\"") + 14, line.indexOf("\"", line.indexOf("\"fulltitle\"") + 14)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String string : lvItems) {
            System.out.println(string);
        }
    }
}
