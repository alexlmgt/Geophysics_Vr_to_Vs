package ru.VrToVsConverter.sukharev;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller {

    static List<File> filesList = new ArrayList<>();
    static List<PvsFile> pvsFilesList = new ArrayList<>();
    static double longitudinalWaveVelocityTopLayer;
    static double shearWaveVelocityTopLayer;
    static double topLayerDepth;
    static double longitudinalWaveVelocityRock;
    static String filePath;

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
    private Label errorLabel;

    @FXML
    private TextField poissonRatioField;

    @FXML
    private AnchorPane myWindow;

    @FXML
    private ListView<String> filesNameField;

    @FXML
    void initialize() {
        btnFindPoisson.setOnAction(actionEvent -> {
            try {
                Converter converter = new Converter();
                errorLabel.setVisible(false);

                longitudinalWaveVelocityTopLayer = Double.parseDouble(VpField.getText());
                shearWaveVelocityTopLayer = Double.parseDouble(VsField.getText());

                double poissonRatio = converter.findPoissonRatio();
                poissonRatioField.setText(String.format("%.4g%n", poissonRatio));
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setVisible(true);
                errorLabel.setText("Ошибка ввода данных");
            }
        });

        btnChoiceFiles.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pvs Files", "*.pvs"));
            try {
                errorLabel.setVisible(false);
                filesList = fileChooser.showOpenMultipleDialog(myWindow.getScene().getWindow());
                filePath = filesList.get(0).getParent();

                ObservableList<String> itemList = FXCollections.observableArrayList();
                for(File ele : filesList) {
                    itemList.add(ele.getAbsolutePath());
                }
                filesNameField.setStyle("-fx-font-size: 10px;");
                filesNameField.setItems(itemList);


            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setVisible(true);
                errorLabel.setText("Ошибка выбора данных");
            }
        });

        btnRun.setOnAction(actionEvent -> {
            try {
                Converter converter = new Converter();
                errorLabel.setVisible(false);

                longitudinalWaveVelocityTopLayer = Double.parseDouble(VpField.getText());
                shearWaveVelocityTopLayer = Double.parseDouble(VsField.getText());
                longitudinalWaveVelocityRock = Double.parseDouble(Vp2Field.getText());
                topLayerDepth = Double.parseDouble(depthField.getText());

                PvsFile pvsFile = new PvsFile();
                pvsFile.readFile();

                converter.shearWaveVelocityBottomLayer();
                converter.writeDatFile();
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setVisible(true);
                errorLabel.setText("Ошибка!!!");
            }
        });
    }
}
