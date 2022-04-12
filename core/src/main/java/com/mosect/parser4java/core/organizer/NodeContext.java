package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.NodeList;

import java.util.ArrayList;
import java.util.List;

public class NodeContext {

    private final NodeOrganizer organizer;
    private final NodeList nodeList;
    private final int nodeStart;
    private final int nodeEnd;
    private final List<NodeRegion> regions = new ArrayList<>(64);
    private final List<NodeRegion> unclosedRegions = new ArrayList<>(12);

    public NodeContext(NodeOrganizer organizer, NodeList nodeList, int nodeStart, int nodeEnd) {
        this.organizer = organizer;
        this.nodeList = nodeList;
        this.nodeStart = nodeStart;
        this.nodeEnd = nodeEnd;
    }

    public NodeOrganizer getOrganizer() {
        return organizer;
    }

    public NodeList getNodeList() {
        return nodeList;
    }

    public int getNodeStart() {
        return nodeStart;
    }

    public int getNodeEnd() {
        return nodeEnd;
    }

    public void addRegion(NodeRegion region) {
        region.setIndex(regions.size());
        regions.add(region);
        if (!region.isHeadOk() || !region.isTailOk()) {
            region.setUnclosedIndex(unclosedRegions.size());
            unclosedRegions.add(region);
        }
    }

    public int getRegionCount() {
        return regions.size();
    }

    public int getUnclosedRegionCount() {
        return regions.size();
    }

    public NodeRegion getRegion(int index) {
        return regions.get(index);
    }

    public NodeRegion getUnclosedRegion(int index) {
        return unclosedRegions.get(index);
    }

    public NodeList makeNodeList() {
        return null;
    }

    public void removeUnclosedRegion(int unclosedIndex) {
        if (unclosedIndex >= 0 && unclosedIndex < unclosedRegions.size()) {
            while (unclosedRegions.size() > unclosedIndex) {
                unclosedRegions.remove(unclosedRegions.size() - 1);
            }
        }
    }
}
