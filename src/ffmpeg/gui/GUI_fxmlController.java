/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffmpeg.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Volk
 */
public class GUI_fxmlController implements Initializable {
    
    Stage stage;
    /*
     * elements from the fxml file 
     */
    @FXML
    private Label fileSelected;
    
    @FXML
    private TextField newFileNameTF;
    
    @FXML
    private ChoiceBox<String> fileExtensionsDDM;
    
    @FXML
    private Label statusReportL;
    
    private String directoryFromFile;
    private File fileToTransform;
    /**
     * DocumentBuilder used to parse xml files.
     */
    private DocumentBuilder builder;
    
    /**
     * Method that prompts the user to select a file. May be used Repeatedly
     * @param event 
     */
    @FXML
    private void handleFileSelection(ActionEvent event){
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        
        FileChooser fileChooser = new FileChooser();
        fileToTransform = fileChooser.showOpenDialog(stage);
        fileSelected.setText(fileToTransform.getAbsolutePath());
    }
    
    /**
     * Method that calls on ffmpeg, given the the program itself is installed and added to the system path.
     * @param event 
     */
    @FXML
    private void handleFileTransformation(ActionEvent event){
        
        statusReportL.setText("Converting file");
        
        directoryFromFile = fileToTransform.getParent();
        try {
            Process callToFFmpeg = new ProcessBuilder("ffmpeg", "-i", fileSelected.getText(), directoryFromFile+"\\"+newFileNameTF.getText()+fileExtensionsDDM.getValue()).start();
        } catch (Exception e){
            Logger.getAnonymousLogger().log(Level.SEVERE, "Couldn't call ffmpeg.\n" + "Stack trace is:" + e.getStackTrace().toString());
        } finally {
            statusReportL.setText("Conversion complete");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            /*
            We load the xml file containing all the file types compatible with ffmpeg.
            Currently, only 6 file types are supported by this application.
            */
            builder = documentBuilderFactory.newDocumentBuilder();
            InputStream is =  this.getClass().getResourceAsStream("/ffmpeg/gui/FileTypes.xml");
            Document xmlFile = builder.parse(is);
            // We add each element from the xml into the choicebox
            for(int index = 0; index < xmlFile.getElementsByTagName("FileType").getLength(); index++){
                fileExtensionsDDM.getItems().add(xmlFile.getElementsByTagName("FileType").item(index).getTextContent());
            }
            
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(GUI_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
