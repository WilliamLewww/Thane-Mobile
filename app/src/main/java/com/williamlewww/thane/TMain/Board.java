package com.williamlewww.thane.TMain;

import android.graphics.PointF;

import com.williamlewww.thane.MainActivity;
import com.williamlewww.thane.TEngine.TPoint;
import com.williamlewww.thane.TEngine.TRectangle;

public class Board {
    public TRectangle rectangle;

    TPoint thaneLines;

    float velocity;
    float rollSpeed = 0.02f;

    double pushInterval = 0.7, pushSpeed = 35, pushTimer = 0, pushMax = 150;
    double tuckSpeed = 8;
    double turnSpeed = 1.12;

    boolean slide = false, shutdownSlide = false;
    boolean turnLeft = false, turnRight = false;

    double movementAngle = 0;
    boolean flipped = false;

    public Board() {
        rectangle = new TRectangle(new PointF(0,0),100,50, true);
        rectangle.angle = 270;
        movementAngle = 270;

        thaneLines = new TPoint();
        thaneLines.initialize();
    }

    public void update(float deltaTimeS) {
        velocity += rollSpeed;

        handleLeftTurn();
        handleRightTurn();

        double difference = getAngleDifference();
        handleSlideRight(difference);
        handleSlideLeft(difference);

        moveInDirection(getDirection());
        refreshSlide();
    }

    private void handleLeftTurn() {
        if (MainActivity.action_state > 0) {
            turnLeft = true;

            if (MainActivity.action_state == 3) {
                shutdownSlide = true;
                slide = true;

                rectangle.angle -= turnSpeed * 3;
                movementAngle -= turnSpeed * 0.75;
            }
            else {
                if (MainActivity.action_state == 2) {
                    slide = true;

                    if (shutdownSlide == true) {
                        rectangle.angle -= turnSpeed * 5;
                        movementAngle -= turnSpeed * 0.25;
                    }
                    else {
                        rectangle.angle -= turnSpeed * 6;
                        movementAngle -= turnSpeed * 0.25;
                    }
                }
                else {
                    if (MainActivity.action_state == 4) {
                        slide = true;

                        rectangle.angle -= turnSpeed * 3;
                        movementAngle -= turnSpeed * 1.25;
                    }
                    else {

                        rectangle.angle -= turnSpeed;
                        movementAngle -= turnSpeed;
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

            if (MainActivity.action_state == -3) {
                shutdownSlide = true;
                slide = true;

                rectangle.angle += turnSpeed * 3;
                movementAngle += turnSpeed * 0.75;
            }
            else {
                if (MainActivity.action_state == -2) {
                    slide = true;

                    if (shutdownSlide == true) {
                        rectangle.angle += turnSpeed * 5;
                        movementAngle += turnSpeed * 0.25;
                    }
                    else {
                        rectangle.angle += turnSpeed * 6;
                        movementAngle += turnSpeed * 0.25;
                    }
                }
                else {
                    if (MainActivity.action_state == -4) {
                        slide = true;

                        rectangle.angle += turnSpeed * 3;
                        movementAngle += turnSpeed * 1.25;
                    }
                    else {

                        rectangle.angle += turnSpeed;
                        movementAngle += turnSpeed;
                    }
                }
            }
        }
        else {
            turnRight = false;
        }
    }

    private double getAngleDifference() {
        if (shutdownSlide) { return Math.abs(rectangle.angle - movementAngle) * 1.5; }
        else { return Math.abs(rectangle.angle - movementAngle) * 0.5; }
    }

    private void handleSlideLeft(double difference) {
        if (movementAngle > rectangle.angle) {
            if ((slide == false && turnLeft == false) || turnRight == true) {
                if (turnRight == true) {
                    rectangle.angle += 1;
                }
                rectangle.angle += 2;
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

            rectangle.angle += 1.0;

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
                rectangle.angle -= 2;
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

            rectangle.angle -= 1.0;

            if (rectangle.angle - movementAngle > 90) {
                movementAngle += 180;
                flipped = !flipped;
            }
        }
    }

    private void generateThane(int alpha) {
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
        rectangle.draw(mvpMatrix);
        thaneLines.draw(mvpMatrix);
    }
}
