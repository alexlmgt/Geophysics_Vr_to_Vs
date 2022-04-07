package ru.VrToVsConverter.sukharev;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static ru.VrToVsConverter.sukharev.Main.longitudinalWaveVelocityTopLayer;
import static ru.VrToVsConverter.sukharev.Main.shearWaveVelocityTopLayer;

public class Converter {

    double poissonRatio = (longitudinalWaveVelocityTopLayer * longitudinalWaveVelocityTopLayer -
            2 * shearWaveVelocityTopLayer * shearWaveVelocityTopLayer) / (2 * longitudinalWaveVelocityTopLayer * longitudinalWaveVelocityTopLayer -
            2 * shearWaveVelocityTopLayer * shearWaveVelocityTopLayer); // коэффициент Пуассона

    double deltaX = Math.ceil(shearWaveVelocityTopLayer * (0.88 + (0.06 / 0.35) * poissonRatio));


    double findReleyVelocity() {
        Map<Double, Double> allWaveReley = new TreeMap<>();
        double waveReleySum;
        for (int i = 0; i < 20; i++) {
            waveReleySum = Math.abs(Math.pow((deltaX - i) / shearWaveVelocityTopLayer, 6) - 8 * Math.pow((deltaX - i) / shearWaveVelocityTopLayer, 4) +
                    (24 - 16 * Math.pow(shearWaveVelocityTopLayer / longitudinalWaveVelocityTopLayer, 2)) * Math.pow((deltaX - i) / shearWaveVelocityTopLayer, 2) -
                    16 * (1 - Math.pow(shearWaveVelocityTopLayer / longitudinalWaveVelocityTopLayer, 2)));
            allWaveReley.put(waveReleySum, deltaX - i);
        }
        System.out.println(allWaveReley.get(allWaveReley.keySet().toArray()[0]));

        return allWaveReley.get(allWaveReley.keySet().toArray()[0]);
    }


}


//    static int longitudinalWaveVelocityTopLayer;
//    static int shearWaveVelocityTopLayer;
//    static double topLayerDepth;
//    static int longitudinalWaveVelocityRock;