package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeContext {

    private final NodeOrganizer organizer;
    private final List<? extends Node> source;
    private final List<NodeRegion> regions = new ArrayList<>(64);
    private final List<NodeRegion> unclosedRegions = new ArrayList<>(12);

    public NodeContext(NodeOrganizer organizer, List<? extends Node> source) {
        this.organizer = organizer;
        this.source = source;
    }

    public NodeOrganizer getOrganizer() {
        return organizer;
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
        region.getHandler().onRegionAdded(this, region);
    }

    void addUnclosedRegion(NodeRegion region) {
        region.setUnclosedIndex(unclosedRegions.size());
        unclosedRegions.add(region);
    }

    /**
     * 关闭区域
     *
     * @param unclosedIndex    未关闭
     * @param end              节点结束位置
     * @param onClosedCallback 关闭回调
     */
    public void closeRegion(int unclosedIndex, int end, OnClosedCallback onClosedCallback) {
        while (unclosedRegions.size() > unclosedIndex) {
            NodeRegion region = unclosedRegions.remove(unclosedRegions.size() - 1);
            region.setEnd(end);
            region.setUnclosedIndex(-1);
            if (null != onClosedCallback) {
                region.getHandler().onRegionClosed(this, region);
                onClosedCallback.onRegionClosed(region);
            }
        }
    }

    public interface OnClosedCallback {
        void onRegionClosed(NodeRegion region);
    }
}
