package com.williamlewww.thane.TMain;

public class Joiner {
    Board board;
    Track track;

    public Joiner() {

    }

    public void initialize() {
        board = new Board();
        track = new Track();
        track.initialize();
    }

    public void update() {
        track.getVisible();
        board.update();

        board.handleCollision(track.startLine.getPointReverse(0), track.startLine.getPointReverse(1));
        for (int x = 0; x < (track.segmentedLine.lineCoords.size() / 3) - 1; x += 1) {
            if (board.handleCollision(track.segmentedLine.getPointReverse(x), track.segmentedLine.getPointReverse(x + 1)) ||
                board.handleCollision(track.complementaryLine.getPointReverse(x), track.complementaryLine.getPointReverse(x + 1))) {
                track.reset();
            }
        }
    }

    public void draw(float[] mvpMatrix) {
        track.draw(mvpMatrix);
        board.draw(mvpMatrix);
    }
}
