package com.williamlewww.thane.TMain;

import com.williamlewww.thane.TEngine.TLine;

public class Track {
    TLine segmentedLine;

    public Track() {
        segmentedLine = new TLine();
    }

    public void initialize() {
        generateTrack();
        segmentedLine.initialize();
    }

    private void generateTrack() {
        for (int y = 0; y < 500; y++) {
            segmentedLine.addPoint(-50 + (25 * (y % 2)), y * 100);
        }
    }

    public void draw(float[] mvpMatrix) {
        segmentedLine.draw(mvpMatrix);
    }
}
