package com.eecs.radar;

import processing.core.PApplet;

public class RadarGraph extends PApplet {
    private float[] distances;
    private int numAngles;
    private float angleIncrement;

    public void settings() {
        size(400, 400);
    }

    public void setup() {
        distances = new float[360];
        numAngles = 360;
        angleIncrement = radians(1); // Adjust this value for angle precision
    }

    public void draw() {
        background(255);
        translate(width / 2, height / 2); // Move origin to center

        // Draw the radar lines
        for (int i = 0; i < numAngles; i++) {
            float distance = distances[i];
            float x = distance * cos(angleIncrement * i);
            float y = distance * sin(angleIncrement * i);
            line(0, 0, x, y);
        }

        // Draw obstacles (representing distances)
        fill(255, 0, 0); // Red color for obstacles
        for (int i = 0; i < numAngles; i++) {
            float distance = distances[i];
            float x = distance * cos(angleIncrement * i);
            float y = distance * sin(angleIncrement * i);
            ellipse(x, y, 5, 5);
        }
    }

    public void updateDistances(float[] newDistances) {
        if (newDistances.length == numAngles) {
            distances = newDistances;
        }
    }

    // Add the init() method
    public void init() {
        String[] processingArgs = {"RadarGraph"};
        RadarGraph radarGraph = new RadarGraph();
        PApplet.runSketch(processingArgs, radarGraph);
    }
}
