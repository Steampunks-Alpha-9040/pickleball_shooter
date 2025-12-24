package org.firstinspires.ftc.teamcode.util;

import java.util.Arrays;
import java.util.Comparator;

public class KDTree {
    private Node root;

    private class Node {
        double[] data;
        Node left, right;
        boolean splitX;

        Node(double[] data, boolean splitX) {
            this.data = data;
            this.splitX = splitX;
        }
    }

    // Constructor: Builds the tree from your existing 2D array
    public KDTree(double[][] points) {
        this.root = build(points, 0, points.length, true);
    }

    private Node build(double[][] points, int start, int end, boolean splitX) {
        if (start >= end) return null;

        // Sort based on X (index 0) or Y (index 1) to find the median
        int axis = splitX ? 0 : 1;
        Arrays.sort(points, start, end, Comparator.comparingDouble(a -> a[axis]));

        int mid = start + (end - start) / 2;
        Node node = new Node(points[mid], splitX);

        node.left = build(points, start, mid, !splitX);
        node.right = build(points, mid + 1, end, !splitX);

        return node;
    }

    // Search variables
    private double[] bestRow;
    private double bestDistSq;

    public double[] findNearest(double targetX, double targetY) {
        bestDistSq = Double.MAX_VALUE;
        bestRow = null;
        search(root, targetX, targetY);
        return bestRow;
    }

    private void search(Node node, double tx, double ty) {
        if (node == null) return;

        // 1. Check distance to current node
        double dx = node.data[0] - tx;
        double dy = node.data[1] - ty;
        double dSq = dx * dx + dy * dy;

        if (dSq < bestDistSq) {
            bestDistSq = dSq;
            bestRow = node.data;
        }

        // 2. Decide which side of the split the target is on
        double diff = node.splitX ? (tx - node.data[0]) : (ty - node.data[1]);
        Node near = diff < 0 ? node.left : node.right;
        Node far = diff < 0 ? node.right : node.left;

        // 3. Search the "near" side first
        search(near, tx, ty);

        // 4. Pruning: Only check the "far" side if it's physically possible
        // for a point to be closer than our current best distance.
        if (diff * diff < bestDistSq) {
            search(far, tx, ty);
        }
    }
}
