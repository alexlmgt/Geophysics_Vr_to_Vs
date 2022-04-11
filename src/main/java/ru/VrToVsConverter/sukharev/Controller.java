package ru.VrToVsConverter.sukharev;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField Vp2Field;

    @FXML
    private TextField VpField;

    @FXML
    private TextField VsField;

    @FXML
    private Button btnChoiceFiles;

    @FXML
    private Button btnFindPoisson;

    @FXML
    private Button btnRun;

    @FXML
    private TextField depthField;

    @FXML
    void initialize() {


        assert Vp2Field != null : "fx:id=\"Vp2Field\" was not injected: check your FXML file 'Sample.fxml'.";
        assert VpField != null : "fx:id=\"VpField\" was not injected: check your FXML file 'Sample.fxml'.";
        assert VsField != null : "fx:id=\"VsField\" was not injected: check your FXML file 'Sample.fxml'.";
        assert btnChoiceFiles != null : "fx:id=\"btnChoiceFiles\" was not injected: check your FXML file 'Sample.fxml'.";
        assert btnFindPoisson != null : "fx:id=\"btnFindPoisson\" was not injected: check your FXML file 'Sample.fxml'.";
        assert btnRun != null : "fx:id=\"btnRun\" was not injected: check your FXML file 'Sample.fxml'.";
        assert depthField != null : "fx:id=\"depthField\" was not injected: check your FXML file 'Sample.fxml'.";

    }

}
