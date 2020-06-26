package com.javaphysicsengine.api;

import com.javaphysicsengine.api.body.PBody;
import com.javaphysicsengine.api.body.PBoundingBox;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PQuadTree {
    private static final int MAX_DEPTH = 5;
    private PQuadTreeNode node;

    private class PQuadTreeNode {
        PQuadTreeNode topLeft;
        PQuadTreeNode topRight;
        PQuadTreeNode bottomLeft;
        PQuadTreeNode bottomRight;

        List<PBody> bodies;

        PBoundingBox box;
        int numObjects;
        int depth;

        public PQuadTreeNode(PQuadTreeNode topLeft,
                             PQuadTreeNode topRight,
                             PQuadTreeNode bottomLeft,
                             PQuadTreeNode bottomRight,
                             PBoundingBox box,
                             int numObjects,
                             int depth) {
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;

            this.bodies = null;

            this.box = box;
            this.numObjects = numObjects;
            this.depth = depth;
        }

        public PQuadTreeNode(List<PBody> bodies, PBoundingBox box, int numObjects, int depth) {
            this.topLeft = null;
            this.topRight = null;
            this.bottomLeft = null;
            this.bottomRight = null;

            this.bodies = bodies;

            this.box = box;
            this.numObjects = numObjects;
            this.depth = depth;
        }

        public boolean isLeaf() {
            return this.bodies != null;
        }
    }

    public PQuadTree(List<PBody> bodies) {
        this.node = buildTree(bodies, 0);
    }

    private PQuadTreeNode buildTree(List<PBody> bodies, int curDepth) {
        if (bodies.size() == 0) {
            return new PQuadTreeNode(bodies, null, 0, curDepth);

        } else if (curDepth >= MAX_DEPTH){
            // Get the bounding box that wraps around all the objects
            PBoundingBox box = bodies.get(0).getBoundingBox();
            for (int i = 1; i < bodies.size(); i++) {
                box = extendBox(box, bodies.get(i).getBoundingBox());
            }

            return new PQuadTreeNode(bodies, box, bodies.size(), curDepth);

        } else {

            // Get the bounding box that wraps around all the objects
            PBoundingBox box = bodies.get(0).getBoundingBox();
            for (int i = 1; i < bodies.size(); i++) {
                box = extendBox(box, bodies.get(i).getBoundingBox());
            }

            // Divide the objects into 1/2 by the dimension with the largest variance
            List<PBody> topleftBodies = new ArrayList<>();
            List<PBody> toprightBodies = new ArrayList<>();
            List<PBody> bottomleftBodies = new ArrayList<>();
            List<PBody> bottomrightBodies = new ArrayList<>();

            double midXRange = (box.getMaxX() + box.getMinX()) / 2;
            double midYRange = (box.getMaxY() + box.getMinY()) / 2;
            for (PBody body : bodies) {
                PBoundingBox bodyBox = body.getBoundingBox();

                if (bodyBox.getMinX() < midXRange && bodyBox.getMinY() < midYRange) {
                    bottomleftBodies.add(body);
                }

                if (bodyBox.getMinX() < midXRange && bodyBox.getMaxY() >= midYRange) {
                    topleftBodies.add(body);
                }

                if (bodyBox.getMaxX() >= midXRange && bodyBox.getMinY() < midYRange) {
                    bottomrightBodies.add(body);
                }

                if (bodyBox.getMaxX() >= midXRange && bodyBox.getMaxY() >= midYRange) {
                    toprightBodies.add(body);
                }
            }

            boolean shouldDivideBox = topleftBodies.size() < bodies.size() ||
                    toprightBodies.size() < bodies.size() ||
                    bottomleftBodies.size() < bodies.size() ||
                    bottomrightBodies.size() < bodies.size();

            if (shouldDivideBox) {
                PQuadTreeNode topleftTree = buildTree(topleftBodies, curDepth + 1);
                PQuadTreeNode toprightTree = buildTree(toprightBodies, curDepth + 1);
                PQuadTreeNode bottomleftTree = buildTree(bottomleftBodies, curDepth + 1);
                PQuadTreeNode bottomrightTree = buildTree(bottomrightBodies, curDepth + 1);

                return new PQuadTreeNode(
                        topleftTree, toprightTree, bottomleftTree, bottomrightTree, box, bodies.size(), curDepth
                );
            } else {
                return new PQuadTreeNode(bodies, box, bodies.size(), curDepth);
            }
        }
    }

    private PBoundingBox extendBox(PBoundingBox box1, PBoundingBox box2) {
        return new PBoundingBox(
                Math.min(box1.getMinX(), box2.getMinX()),
                Math.max(box1.getMaxX(), box2.getMaxX()),
                Math.min(box1.getMinY(), box2.getMinY()),
                Math.max(box1.getMaxY(), box2.getMaxY())
        );
    }

    public Set<Pair<PBody, PBody>> getPotentialIntersectingBodies() {
        Set<Pair<PBody, PBody>> lst = new HashSet<>();
        getPotentialIntersectingBodies(this.node, lst);

        return lst;
    }

    private void getPotentialIntersectingBodies(PQuadTreeNode curNode, Set<Pair<PBody, PBody>> curPairs) {
        if (curNode.isLeaf()) {
            for (int i = 0; i < curNode.bodies.size(); i++) {
                for (int j = i + 1; j < curNode.bodies.size(); j++) {
                    Pair newPair = new Pair<>(curNode.bodies.get(i), curNode.bodies.get(j));

                    if (!curPairs.contains(newPair)) {
                        curPairs.add(newPair);
                    }
                }
            }

            return;
        }

        getPotentialIntersectingBodies(curNode.topLeft, curPairs);
        getPotentialIntersectingBodies(curNode.topRight, curPairs);
        getPotentialIntersectingBodies(curNode.bottomLeft, curPairs);
        getPotentialIntersectingBodies(curNode.bottomRight, curPairs);
    }
}
