package ru.VrToVsConverter.sukharev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Main extends Application {
    static List<String> filesList = new ArrayList<>();
    static List<PvsFile> pvsFilesList = new ArrayList<>();
    static double longitudinalWaveVelocityTopLayer;
    static double shearWaveVelocityTopLayer;
    static double topLayerDepth;
    static double longitudinalWaveVelocityRock;
    static String filePath;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Sample.fxml")));
        stage.setTitle("Vp to Vs Converter");
        stage.setScene(new Scene(root, 600, 400));
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {

        //работа в командной строке
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            // написать условия для входных данных!
            System.out.print("Введите скорость продольной волны (Vp1) для верхнего слоя > ");
            longitudinalWaveVelocityTopLayer = Integer.parseInt(reader.readLine().trim());

            System.out.print("Введите скорость поперечной волны (Vs) для верхнего слоя > ");
            shearWaveVelocityTopLayer = Integer.parseInt(reader.readLine().trim());

            System.out.print("Введите толщину верхнего слоя > ");
            topLayerDepth = Double.parseDouble(reader.readLine().trim());

            System.out.print("Введите скорость продольной волны (Vp2) для породы > ");
            longitudinalWaveVelocityRock = Integer.parseInt(reader.readLine().trim());

            String str;
            System.out.println("Введите путь до файлов, в конце введите \"end\" > ");
            while (!(str = reader.readLine()).equals("end")) {
                filePath = str.substring(0, str.lastIndexOf('\\') + 1);
                filesList.add(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        new PvsFile().readFile();
        Converter converter = new Converter();
        converter.shearWaveVelocityBottomLayer();
        converter.writeDatFile();

    }


}
