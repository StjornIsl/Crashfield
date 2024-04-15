package com.tryout;

import java.util.Scanner;

public class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        boolean hasBoost = true;
        int maxSpeed = 100;
        int minSpeed = 0;
        int boostDistanceThreshold = 4895;
        int boostAngleThreshold = 1;

        // game loop
        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            int nextCheckpointX = in.nextInt(); // x position of the next check point
            int nextCheckpointY = in.nextInt(); // y position of the next check point
            int nextCheckpointDist = in.nextInt(); // distance to the next checkpoint
            int nextCheckpointAngle = in.nextInt(); // angle between your pod orientation and the direction of the next checkpoint
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            // You have to output the target position
            // followed by the power (0 <= thrust <= 100)
            // i.e.: "x y thrust"

            float distanceToOpponent = (float) Math.sqrt(Math.pow(opponentX - x, 2) + Math.pow(opponentY - y, 2));
            float opponentToCheckpointDistance = (float) Math.sqrt(Math.pow(nextCheckpointX - opponentX, 2) + Math.pow(nextCheckpointY - opponentY, 2));

            int thrust;

            if (shouldBoost(nextCheckpointDist, nextCheckpointAngle, hasBoost, boostDistanceThreshold, boostAngleThreshold)) {
                System.out.println(nextCheckpointX + " " + nextCheckpointY + " BOOST");
                hasBoost = false;
                continue;
            }

            if (opponentIsCloserToNextCheckpoint(opponentToCheckpointDistance, nextCheckpointDist)
                    && opponentIsCloseToMe(distanceToOpponent)
                    && calculateAngleTo(nextCheckpointX, nextCheckpointY, x, y) < 1
                    && nextCheckpointDistIsLessThan4000(nextCheckpointDist)) {
                thrust = attack(opponentX, opponentY, x, y);
            }

            thrust = calculateThrust(nextCheckpointAngle, maxSpeed, minSpeed);

            System.out.println(nextCheckpointX + " " + nextCheckpointY + " " + thrust);
        }
    }

    private static boolean nextCheckpointDistIsLessThan4000(int nextCheckpointDist) {
        return nextCheckpointDist < 4000;
    }

    private static int calculateThrust(int nextCheckpointAngle, int maxSpeed, int minSpeed) {

        int thrust;

        if (Math.abs(nextCheckpointAngle) > 90) {
            thrust = minSpeed;
            ;
        } else {
            if (Math.abs(nextCheckpointAngle) > 75) {
                thrust = 85;
            } else {
                thrust = maxSpeed;
            }
        }
        return thrust;
    }

    private static boolean shouldBoost(int nextCheckpointDist, int nextCheckpointAngle, boolean hasBoost, int boostDistanceThreshold, int boostAngleThreshold) {

        return hasBoost && nextCheckpointDist > boostDistanceThreshold && Math.abs(nextCheckpointAngle) < boostAngleThreshold;
    }

    private static int attack(int targetX, int targetY, int myX, int myY) {
        int thrust;
        float angleToTarget = calculateAngleTo(targetX, targetY, myX, myY);

        if (Math.abs(angleToTarget) > 75.0) {
            thrust = 85;
        } else {
            thrust = 100;
        }
        return thrust;
    }

    private static boolean opponentIsCloserToNextCheckpoint(float opponentToCheckpointDistance, int nextCheckpointDist) {
        return opponentToCheckpointDistance < nextCheckpointDist;
    }

    private static boolean opponentIsCloseToMe(float distanceToOpponent) {
        return distanceToOpponent < 800.0;
    }

    private static float calculateAngleTo(int targetX, int targetY, int myX, int myY) {

        int deltaX = targetX - myX;
        int deltaY = targetY - myY;

        float angleToTarget = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

        if (angleToTarget < 0) {
            angleToTarget += 360;
        }

        return angleToTarget;
    }

}
