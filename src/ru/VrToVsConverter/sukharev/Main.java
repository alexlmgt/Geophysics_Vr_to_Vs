package ru.VrToVsConverter.sukharev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


// C:\Users\ASuharev\IdeaProjects\Для тестов\180094.pvs

public class Main {
    static List<String> filesList = new ArrayList<>();
    static List<PvsFile> pvsFilesList = new ArrayList<>();
    static double longitudinalWaveVelocityTopLayer;
    static double shearWaveVelocityTopLayer;
    static double topLayerDepth;
    static double longitudinalWaveVelocityRock;

    public static void main(String[] args) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Введите скорость продольной волны (Vp1) для верхнего слоя > ");
            longitudinalWaveVelocityTopLayer = Integer.parseInt(reader.readLine().trim());

            System.out.print("Введите скорость поперечной волны (Vs) для верхнего слоя > ");
            shearWaveVelocityTopLayer = Integer.parseInt(reader.readLine().trim());

            System.out.print("Введите толщину верхнего слоя > ");
            topLayerDepth = Double.parseDouble(reader.readLine().trim());

            System.out.print("Введите скорость продольной волны (Vp2) для породы > ");
            longitudinalWaveVelocityRock = Integer.parseInt(reader.readLine().trim());

            String str;
            System.out.println("Введите путь до файла > ");
            while (!(str = reader.readLine()).equals("exit")) {
                System.out.println("Введите путь до файла > ");
                filesList.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        new PvsFile().readFile();


        Converter converter = new Converter();
        converter.shearWaveVelocityBottomLayer();
    }


}
