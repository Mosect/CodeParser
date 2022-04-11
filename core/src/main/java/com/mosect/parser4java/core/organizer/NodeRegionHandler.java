package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.Node;

public interface NodeRegionHandler {

    enum EndState {
        NONE,
        CLOSED,
        CLOSED_AND_CONSUMED
    }

    String getName();

    int getRegionStart(NodeContext context, NodeRegion parent, int start, int offset);

    Node createNode(NodeContext context, NodeRegion parent, int start, int offset);

    EndState getRegionEnd(NodeContext context, NodeRegion region, int offset);

    int getForceEndIndex(NodeContext context, NodeRegion region);

    void onRegionAdded(NodeContext context, NodeRegion region);

    void onRegionClosed(NodeContext context, NodeRegion region);
}
