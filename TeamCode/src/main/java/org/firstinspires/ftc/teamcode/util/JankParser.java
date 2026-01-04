package org.firstinspires.ftc.teamcode.util;

public class JankParser {
    public static int findClosestPointIndex(double tx, double ty) {
        //finds index of closest point

        double[][] points = ShootingPoints.points;

        int closestIndex = 0;
        double minDistSq = Float.MAX_VALUE;

        for (int i = 0; i < points.length; i++) {
            double dx = points[i][0] - tx;
            double dy = points[i][1] - ty;
            double distSq = dx * dx + dy * dy;

            if (distSq < minDistSq) {
                //this if statment removes positions that are not valid from being considerd closest points. will code into robot eventually.
                if (points[i][2] == 0 && points[i][3] == 0 && points[i][4] == 0 && points[i][5] == 0) {
                    return -1;
                } else {
                    minDistSq = distSq;
                    closestIndex = i;
                }
            }
        }
        return closestIndex;
    }

}
