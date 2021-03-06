package com.williamlewww.thane.TMain;

import android.graphics.PointF;

import com.williamlewww.thane.MainActivity;
import com.williamlewww.thane.TEngine.TPoint;
import com.williamlewww.thane.TEngine.TRectangle;

public class Board {
    public static TRectangle rectangle;

    TPoint thaneLines;

    float velocity;
    float rollSpeed = 0.02f;

    double turnSpeed = 1.12;

    boolean slide, shutdownSlide;
    boolean turnLeft, turnRight;

    double movementAngle;
    boolean flipped;

    public Board() {
        reset();

        thaneLines = new TPoint();
        thaneLines.initialize();
    }

    public void update() {
        addSpeedFromHill();

        handleLeftTurn();
        handleRightTurn();

        double difference = getAngleDifference();
        handleSlideRight(difference);
        handleSlideLeft(difference);

        moveInDirection(getDirection());
        refreshSlide();
    }

    int speedZoneIndex = 0;
    private void addSpeedFromHill() {
        while (rectangle.position.y > Track.speedZones.get(speedZoneIndex).x) {
            speedZoneIndex += 1;
        }

        velocity += rollSpeed + (rollSpeed * 2 * (Track.speedZones.get(speedZoneIndex).y / 100));
    }

    private void handleLeftTurn() {
        if (MainActivity.action_state > 0) {
            turnLeft = true;

            if (MainActivity.action_state == 1) {
                rectangle.angle -= turnSpeed * 0.75;
                movementAngle -= turnSpeed * 0.75;
            }

            if (MainActivity.action_state == 3) {
                shutdownSlide = true;
                slide = true;

                rectangle.angle -= turnSpeed * 3;
                movementAngle -= turnSpeed * 0.50;
            }
            else {
                if (MainActivity.action_state == 2) {
                    slide = true;

                    rectangle.angle -= turnSpeed * 4;
                    movementAngle -= turnSpeed * 0.42;
                }
                else {
                    if (MainActivity.action_state == 4) {
                        slide = true;

                        rectangle.angle -= turnSpeed * 3;
                        movementAngle -= turnSpeed * 0.85;
                    }
                }
            }
        }
        else {
            turnLeft = false;
        }
    }

    private void handleRightTurn() {
        if (MainActivity.action_state < 0) {
            turnRight = true;

            if (MainActivity.action_state == -1) {
                rectangle.angle += turnSpeed * 0.75;
                movementAngle += turnSpeed * 0.75;
            }

            if (MainActivity.action_state == -3) {
                shutdownSlide = true;
                slide = true;

                rectangle.angle += turnSpeed * 3;
                movementAngle += turnSpeed * 0.50;
            }
            else {
                if (MainActivity.action_state == -2) {
                    slide = true;

                    rectangle.angle += turnSpeed * 4;
                    movementAngle += turnSpeed * 0.42;
                }
                else {
                    if (MainActivity.action_state == -4) {
                        slide = true;

                        rectangle.angle += turnSpeed * 3;
                        movementAngle += turnSpeed * 0.85;
                    }
                }
            }
        }
        else {
            turnRight = false;
        }
    }

    private double getAngleDifference() {
        if (shutdownSlide) { return Math.abs(rectangle.angle - movementAngle) * 1.0; }
        else { return Math.abs(rectangle.angle - movementAngle) * 0.75; }
    }

    private void handleSlideLeft(double difference) {
        if (movementAngle > rectangle.angle) {
            if ((slide == false && turnLeft == false) || turnRight == true) {
                if (turnRight == true) {
                    rectangle.angle += 1;
                }
                rectangle.angle += 3;
            }

            if (movementAngle - rectangle.angle > 5) {
                if (velocity - (difference / 500) < 0) {
                    velocity = 0;
                }
                else {
                    velocity -= (difference / 500);
                }

                if (shutdownSlide) { generateThane(255); }
                else { generateThane(100); }
            }
            else {
                shutdownSlide = false;
            }

            if (slide == true) {
                rectangle.angle += 1.0;
            }

            if (movementAngle - rectangle.angle > 90) {
                movementAngle -= 180;
                flipped = !flipped;
            }
        }
    }

    private void handleSlideRight(double difference) {
        if (movementAngle < rectangle.angle) {
            if ((slide == false && turnRight == false) || turnLeft == true) {
                if (turnLeft == true) {
                    rectangle.angle -= 1;
                }
                rectangle.angle -= 3;
            }

            if (rectangle.angle - movementAngle > 5) {
                if (velocity - (difference / 500) < 0) {
                    velocity = 0;
                }
                else {
                    velocity -= (difference / 500);
                }

                if (shutdownSlide) { generateThane(255); }
                else { generateThane(100); }
            }
            else {
                shutdownSlide = false;
            }

            if (slide == true) {
                rectangle.angle -= 1.0;
            }

            if (rectangle.angle - movementAngle > 90) {
                movementAngle += 180;
                flipped = !flipped;
            }
        }
    }

    private void generateThane(int velocity) {
        thaneLines.addPoint(rectangle.getTopLeft());
        thaneLines.addPoint(rectangle.getTopRight());
        thaneLines.addPoint(rectangle.getBottomRight());
        thaneLines.addPoint(rectangle.getBottomLeft());
    }

    private PointF getDirection() {
        PointF direction = new PointF((float)Math.cos((movementAngle * Math.PI) / 180), (float)Math.sin((movementAngle * Math.PI) / 180));
        return direction;
    }

    private void moveInDirection(PointF direction) {
        if (flipped) {
            rectangle.position.x -= direction.x * velocity;
            rectangle.position.y -= direction.y * velocity;
        }
        else {
            rectangle.position.x += direction.x * velocity;
            rectangle.position.y += direction.y * velocity;
        }
    }

    private void refreshSlide() {
        slide = false;
    }

    public void draw(float[] mvpMatrix) {
        thaneLines.draw(mvpMatrix);
        rectangle.draw(mvpMatrix);
    }

    public boolean handleCollision(PointF pointA, PointF pointB) {
        PointF[] tempPoint = { pointA, pointB };
        if (rectangle.checkCollision(tempPoint) == true) {
            reset();
            return true;
        }

        return false;
    }

    public void reset() {
        rectangle = new TRectangle(new PointF(0,0),100,50, true);
        rectangle.angle = 270;
        movementAngle = 270;

        flipped = false;
        slide = false;
        shutdownSlide = false;
        turnLeft = false;
        turnRight = false;
        velocity = 0.0f;
    }
}
