package ru.VrToVsConverter.sukharev;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static ru.VrToVsConverter.sukharev.Controller.*;

public class Converter {

    List<String> resultList = new ArrayList<>();
    private final double poissonRatio = findPoissonRatio(); // коэффициент Пуассона

    double deltaX = Math.ceil(shearWaveVelocityTopLayer * (0.88 + (0.06 / 0.35) * poissonRatio));

    private double findVelocity(double deltaX, double shearWaveVelocity, double longitudinalWaveVelocity, boolean isFindVr) {
        Map<Double, Double> allRayleighWave = new TreeMap<>();
        double rayleighWavesSum;
        for (int i = 0; i < deltaX / 100; i++) {
            double mathD = 24 - 16 * Math.pow(shearWaveVelocity / longitudinalWaveVelocity, 2);
            double mathDD = 16 * (1 - Math.pow(shearWaveVelocity / longitudinalWaveVelocity, 2));
            if (isFindVr) {
                rayleighWavesSum = Math.abs(Math.pow((deltaX - i) / shearWaveVelocity, 6) - 8 * Math.pow((deltaX - i) / shearWaveVelocity, 4) +
                        mathD * Math.pow((deltaX - i) / shearWaveVelocity, 2) -
                        mathDD);
                allRayleighWave.put(rayleighWavesSum, deltaX - i);
            } else {
                rayleighWavesSum = Math.abs(Math.pow((deltaX + i) / shearWaveVelocity, 6) - 8 * Math.pow((deltaX + i) / shearWaveVelocity, 4) +
                        mathD * Math.pow((deltaX + i) / shearWaveVelocity, 2) -
                        mathDD);
                allRayleighWave.put(rayleighWavesSum, deltaX + i);
            }
        }
        return allRayleighWave.get(allRayleighWave.keySet().toArray()[0]);
    }

    // сложные геофизические формулы для нахождения скорости поперечной волны из скорости Релеевской волны
    void shearWaveVelocityBottomLayer() {

        for (PvsFile file : pvsFilesList) {
            int impactPoint = Integer.parseInt(file.getName());
            double shearWaveVelocityBottomLayer;
            double botLayerDepth;

            for (Map.Entry<Double, Double> values : file.getDispersionResult().entrySet()) {
                double frequency = values.getKey();
                double rayleighVelocity = values.getValue();

                if (frequency < 0 || rayleighVelocity < 0 || rayleighVelocity > longitudinalWaveVelocityTopLayer) continue;

                botLayerDepth = rayleighVelocity / frequency;
                double topRayleighVelocity = findVelocity(deltaX, shearWaveVelocityTopLayer, longitudinalWaveVelocityTopLayer, true);
                double deptCoefficient = topLayerDepth / (rayleighVelocity / frequency);
                double deltaF1 = Math.exp(-1 * Math.sqrt(1 - topRayleighVelocity * topRayleighVelocity / longitudinalWaveVelocityTopLayer /
                        longitudinalWaveVelocityTopLayer) * 2 * Math.PI * deptCoefficient);
                double deltaX1 = Math.exp(-1 * Math.sqrt(1 - topRayleighVelocity * topRayleighVelocity / shearWaveVelocityTopLayer /
                        shearWaveVelocityTopLayer) * 2 * Math.PI * deptCoefficient);
                double deltaU1 = deltaF1 + deltaX1;
                double deltaUpr = deltaU1 / 2;
                double cofDelta = 1 - deltaUpr + deltaUpr * deltaUpr;
                double rayleighWaveVelocityBottomLayer = (topRayleighVelocity * rayleighVelocity * deltaUpr) /
                        (topRayleighVelocity * cofDelta - rayleighVelocity * (cofDelta - deltaUpr));
                double poissonRatioBot = 1 / (0.88 + (0.06 / 0.35) * ((longitudinalWaveVelocityRock * longitudinalWaveVelocityRock -
                        2 * rayleighWaveVelocityBottomLayer * rayleighWaveVelocityBottomLayer) / (2 * longitudinalWaveVelocityRock * longitudinalWaveVelocityRock -
                        2 * rayleighWaveVelocityBottomLayer * rayleighWaveVelocityBottomLayer)));
                double deltaX = Math.ceil(rayleighWaveVelocityBottomLayer * poissonRatioBot);

                shearWaveVelocityBottomLayer = findVelocity(deltaX, rayleighWaveVelocityBottomLayer, longitudinalWaveVelocityRock, false);
                if (!isRealVs(shearWaveVelocityBottomLayer)) continue;

                resultList.add(impactPoint + "," + (-1 * botLayerDepth) + "," + shearWaveVelocityBottomLayer / 1000);
            }
        }
    }

    void writeDatFile() {
        try {
            Files.write(Paths.get(filePath + "\\dataAll.dat"), resultList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    double findPoissonRatio() {
        double poissonRatio = (longitudinalWaveVelocityTopLayer * longitudinalWaveVelocityTopLayer -
                2 * shearWaveVelocityTopLayer * shearWaveVelocityTopLayer) / (2 * longitudinalWaveVelocityTopLayer * longitudinalWaveVelocityTopLayer -
                2 * shearWaveVelocityTopLayer * shearWaveVelocityTopLayer);
        return poissonRatio >= 0.1 ? poissonRatio : -1;
    }

    private boolean isRealVs (double Vs) {
        double rockPoissonRatio = (longitudinalWaveVelocityRock * longitudinalWaveVelocityRock -
                2 * Vs * Vs) / (2 * longitudinalWaveVelocityRock * longitudinalWaveVelocityRock -
                2 * Vs * Vs);
        return rockPoissonRatio >= 0.1;
    }
}
