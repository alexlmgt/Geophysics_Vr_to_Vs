package ru.VrToVsConverter.sukharev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static ru.VrToVsConverter.sukharev.Main.filesList;
import static ru.VrToVsConverter.sukharev.Main.pvsFilesList;

public class PvsFile {
    private String name;
    private Map<Double, Double> dispersionResult;

    public PvsFile(String name, Map<Double, Double> dispersionResult) {
        this.name = name;
        this.dispersionResult = dispersionResult;
    }

    public String getName() {
        return name;
    }

    public Map<Double, Double> getDispersionResult() {
        return dispersionResult;
    }

    public void setDispersionResult(Map<Double, Double> dispersionResult) {
        this.dispersionResult = dispersionResult;
    }

    static void readFile() {
        for (String file : filesList) {
            String fileName = file.substring(file.lastIndexOf('\\') + 1, file.lastIndexOf('.'));
            Map<Double, Double> dispersionResult = new TreeMap<>();
            PvsFile pvsFile = new PvsFile(fileName, dispersionResult);
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String str;

                while ((str = reader.readLine()) != null) {
                    String[] box = str.trim().split(" ");
                    if (box.length == 4) {
                        Double frequency = Double.parseDouble(box[0]);
                        Double velocity = Double.parseDouble(box[1]);
                        dispersionResult.put(frequency, velocity);
                    }
                }
                pvsFile.setDispersionResult(dispersionResult);

            } catch (IOException e) {
                e.printStackTrace();
            }
            pvsFilesList.add(pvsFile);
        }

    }


}
