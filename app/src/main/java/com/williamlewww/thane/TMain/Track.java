package com.williamlewww.thane.TMain;

import android.graphics.PointF;

import com.williamlewww.thane.TEngine.TLine;
import com.williamlewww.thane.TEngine.TQuad;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Track {
    public TLine segmentedLine, complementaryLine;
    public TLine startLine;
    List<TQuad> roadQuads = new ArrayList<>();
    List<TQuad> mountainQuadsUpper = new ArrayList<>();
    List<TQuad> mountainQuadsLower = new ArrayList<>();

    Random random;

    int range = 200;
    int spacingX = 350, spacingY = 200;

    public Track() {
        random = new Random();

        segmentedLine = new TLine();
        complementaryLine = new TLine();

        startLine = new TLine();
    }

    PointF[] pointList = new PointF[4];

    public void initialize() {
        generateTrack();
        segmentedLine.initialize();
        complementaryLine.initialize();
        startLine.initialize();

        for (int x = 0; x < (segmentedLine.lineCoords.size() / 3) - 1; x += 1) {
            pointList[0] = new PointF(segmentedLine.lineCoords.get(x * 3), segmentedLine.lineCoords.get((x * 3) + 1));
            pointList[1] = new PointF(segmentedLine.lineCoords.get((x * 3) + 3), segmentedLine.lineCoords.get((x * 3) + 4));
            pointList[2] = new PointF(complementaryLine.lineCoords.get(x * 3), segmentedLine.lineCoords.get((x * 3) + 1));
            pointList[3] = new PointF(complementaryLine.lineCoords.get((x * 3) + 3), segmentedLine.lineCoords.get((x * 3) + 4));
            roadQuads.add(new TQuad(pointList));
        }

        for (int x = 0; x < (segmentedLine.lineCoords.size() / 3) - 1; x += 1) {
            pointList[0] = new PointF(segmentedLine.lineCoords.get(x * 3), segmentedLine.lineCoords.get((x * 3) + 1));
            pointList[1] = new PointF(segmentedLine.lineCoords.get((x * 3) + 3), segmentedLine.lineCoords.get((x * 3) + 4));
            pointList[2] = new PointF(segmentedLine.lineCoords.get(x * 3) + 100, segmentedLine.lineCoords.get((x * 3) + 1));
            pointList[3] = new PointF(segmentedLine.lineCoords.get((x * 3) + 3) + 100, segmentedLine.lineCoords.get((x * 3) + 4));
            mountainQuadsUpper.add(new TQuad(pointList));
            mountainQuadsUpper.get(mountainQuadsUpper.size() - 1).setColor(0.46f, 0.36f, 0.26f);
        }

        for (int x = 0; x < (segmentedLine.lineCoords.size() / 3) - 1; x += 1) {
            pointList[0] = new PointF(complementaryLine.lineCoords.get(x * 3), segmentedLine.lineCoords.get((x * 3) + 1));
            pointList[1] = new PointF(complementaryLine.lineCoords.get((x * 3) + 3), segmentedLine.lineCoords.get((x * 3) + 4));
            pointList[2] = new PointF(complementaryLine.lineCoords.get(x * 3) - 1000, segmentedLine.lineCoords.get((x * 3) + 1));
            pointList[3] = new PointF(complementaryLine.lineCoords.get((x * 3) + 3) - 1000, segmentedLine.lineCoords.get((x * 3) + 4));
            mountainQuadsLower.add(new TQuad(pointList));
            mountainQuadsLower.get(mountainQuadsLower.size() - 1).setColor(0.41f, 0.31f, 0.21f);
        }
    }

    int visibleTrackIndexStart = 0, visibleTrackIndexEnd = 0;
    public void getVisible() {
        while (segmentedLine.lineCoords.get((visibleTrackIndexStart * 3) + 1) < -(Board.rectangle.position.y) - 1250) {
            visibleTrackIndexStart += 1;
        }

        while (segmentedLine.lineCoords.get((visibleTrackIndexEnd * 3) + 1) < -(Board.rectangle.position.y) + 1250) {
            visibleTrackIndexEnd += 1;
        }
    }

    public void reset() {
        visibleTrackIndexStart = 0;
        visibleTrackIndexEnd = 0;
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
        for (int x = visibleTrackIndexStart; x < visibleTrackIndexEnd; x++) {
            roadQuads.get(x).draw(mvpMatrix);
            mountainQuadsUpper.get(x).draw(mvpMatrix);
            mountainQuadsLower.get(x).draw(mvpMatrix);
        }

        segmentedLine.draw(mvpMatrix);
        complementaryLine.draw(mvpMatrix);
        startLine.draw(mvpMatrix);
    }
}
