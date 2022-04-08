package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeContext {

    private final List<? extends Node> source;
    private final List<NodeRegion> regions = new ArrayList<>(64);
    private final List<NodeRegion> unclosedRegions = new ArrayList<>(12);

    public NodeContext(List<? extends Node> source) {
        this.source = source;
    }

    public List<? extends Node> getSource() {
        return source;
    }

    public int getRegionCount() {
        return regions.size();
    }

    public NodeRegion getRegion(int index) {
        return regions.get(index);
    }

    public int getUnclosedRegionCount() {
        return unclosedRegions.size();
    }

    public NodeRegion getUnclosedRegion(int index) {
        return unclosedRegions.get(index);
    }

    void addRegion(NodeRegion region) {
        region.setIndex(regions.size());
        regions.add(region);
    }

    void addUnclosedRegion(NodeRegion region) {
        region.setUnclosedIndex(unclosedRegions.size());
        unclosedRegions.add(region);
    }

    void removeLastUncloseRegions(int count) {
        int size = Math.max(0, unclosedRegions.size() - count);
        while (unclosedRegions.size() > size) {
            unclosedRegions.remove(unclosedRegions.size() - 1);
        }
    }

    List<NodeRegion> getRegions() {
        return regions;
    }
}
