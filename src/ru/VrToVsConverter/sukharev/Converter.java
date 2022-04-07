package ru.VrToVsConverter.sukharev;

import java.util.Map;
import java.util.TreeMap;

import static ru.VrToVsConverter.sukharev.Main.*;

public class Converter {

    double poissonRatio = (longitudinalWaveVelocityTopLayer * longitudinalWaveVelocityTopLayer -
            2 * shearWaveVelocityTopLayer * shearWaveVelocityTopLayer) / (2 * longitudinalWaveVelocityTopLayer * longitudinalWaveVelocityTopLayer -
            2 * shearWaveVelocityTopLayer * shearWaveVelocityTopLayer); // коэффициент Пуассона

    double deltaX = Math.ceil(shearWaveVelocityTopLayer * (0.88 + (0.06 / 0.35) * poissonRatio));

    double findVelocity(double deltaX, double shearWaveVelocity, double longitudinalWaveVelocity, boolean isFindVr) {
        Map<Double, Double> allWaveReley = new TreeMap<>();
        double waveReleySum;
        for (int i = 0; i < deltaX / 100; i++) {
            if (isFindVr) {
                waveReleySum = Math.abs(Math.pow((deltaX - i) / shearWaveVelocity, 6) - 8 * Math.pow((deltaX - i) / shearWaveVelocity, 4) +
                        (24 - 16 * Math.pow(shearWaveVelocity / longitudinalWaveVelocity, 2)) * Math.pow((deltaX - i) / shearWaveVelocity, 2) -
                        16 * (1 - Math.pow(shearWaveVelocity / longitudinalWaveVelocity, 2)));
            } else {
                waveReleySum = Math.abs(Math.pow((deltaX + i) / shearWaveVelocity, 6) - 8 * Math.pow((deltaX + i) / shearWaveVelocity, 4) +
                        (24 - 16 * Math.pow(shearWaveVelocity / longitudinalWaveVelocity, 2)) * Math.pow((deltaX + i) / shearWaveVelocity, 2) -
                        16 * (1 - Math.pow(shearWaveVelocity / longitudinalWaveVelocity, 2)));
            }


            allWaveReley.put(waveReleySum, deltaX - i);
        }
        return allWaveReley.get(allWaveReley.keySet().toArray()[0]);
    }

    double topReleyVelocity = findVelocity(deltaX, shearWaveVelocityTopLayer, longitudinalWaveVelocityTopLayer, true);

    // сложные геофизические формулы для нахождения скорости поперечной волны из Релеевской скорости и частоты волны
    void shearWaveVelocityBottomLayer() {
        for (PvsFile file : pvsFilesList) {
            int impactPoint = Integer.parseInt(file.getName());

            double shearWaveVelocityBottomLayer;
            double botLayerDepth;

            for (Map.Entry<Double, Double> values : file.getDispersionResult().entrySet()) {
                double frequency = values.getKey();
                double releyVelocity = values.getValue();
                botLayerDepth = releyVelocity / frequency;
                double deptCoefficient = topLayerDepth / (releyVelocity / frequency);
                double deltaF1 = Math.exp(-1 * Math.sqrt(1 - topReleyVelocity * topReleyVelocity / longitudinalWaveVelocityTopLayer /
                        longitudinalWaveVelocityTopLayer) * 2 * Math.PI * deptCoefficient);
                double deltaX1 = Math.exp(-1 * Math.sqrt(1 - topReleyVelocity * topReleyVelocity / shearWaveVelocityTopLayer /
                        shearWaveVelocityTopLayer) * 2 * Math.PI * deptCoefficient);
                double deltaU1 = deltaF1 + deltaX1;
                double deltaUpr = deltaU1 / 2;
                double coefDdelta = 1 - deltaUpr + deltaUpr * deltaUpr;
                double releyWaveVelocityBottomLayer = (topReleyVelocity * releyVelocity * deltaUpr) /
                        (topReleyVelocity * coefDdelta - releyVelocity * (coefDdelta - deltaUpr));

                double poissonRatioBot = 1 / (0.88 + (0.06 / 0.35) * ((longitudinalWaveVelocityRock * longitudinalWaveVelocityRock -
                        2 * releyWaveVelocityBottomLayer * releyWaveVelocityBottomLayer) / (2 * longitudinalWaveVelocityRock * longitudinalWaveVelocityRock -
                        2 * releyWaveVelocityBottomLayer * releyWaveVelocityBottomLayer)));
                double deltaX = Math.ceil(releyWaveVelocityBottomLayer * poissonRatioBot);

                shearWaveVelocityBottomLayer = findVelocity(deltaX, releyWaveVelocityBottomLayer, longitudinalWaveVelocityRock, false);

                System.out.println(impactPoint + " " + (-1 * botLayerDepth) + " " + shearWaveVelocityBottomLayer);

            }
        }
    }
}


//    static int longitudinalWaveVelocityTopLayer;
//    static int shearWaveVelocityTopLayer;
//    static double topLayerDepth;
//    static int longitudinalWaveVelocityRock;