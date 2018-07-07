package com.williamlewww.thane.TMain;

import com.williamlewww.thane.TEngine.TLine;

import java.util.Random;

public class Track {
    TLine segmentedLine, complementaryLine;
    TLine startLine;

    Random random;

    int range = 200;
    int spacingX = 300, spacingY = 200;

    public Track() {
        random = new Random();

        segmentedLine = new TLine();
        complementaryLine = new TLine();

        startLine = new TLine();
    }

    public void initialize() {
        generateTrack();
        segmentedLine.initialize();
        complementaryLine.initialize();
        startLine.initialize();
    }

    private void generateTrack() {
        int currentX = 0;

        startLine.addPoint(spacingX, -100);
        startLine.addPoint(-spacingX, -100);

        for (int y = 0; y < 500; y++) {
            segmentedLine.addPoint(spacingX + currentX, (y * spacingY) - 100);
            complementaryLine.addPoint(-spacingX + currentX, (y * spacingY - 100));

            if (y > 0) { currentX += random.nextInt(range + 1 + range) - range; }
        }
    }

    public void draw(float[] mvpMatrix) {
        segmentedLine.draw(mvpMatrix);
        complementaryLine.draw(mvpMatrix);
        startLine.draw(mvpMatrix);
    }
}
