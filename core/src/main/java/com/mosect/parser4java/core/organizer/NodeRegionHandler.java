package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.Node;

public interface NodeRegionHandler {

    enum EndState {
        NONE,
        CLOSED,
        CLOSED_AND_CONSUMED
    }

    String getName();

    int getRegionStart(NodeOrganizer organizer, NodeContext context, NodeRegion parent, int start, int offset);

    Node createNode(NodeOrganizer organizer, NodeContext context, NodeRegion parent, int start, int offset);

    EndState getRegionEnd(NodeOrganizer organizer, NodeContext context, NodeRegion region, int offset);

    int getForceEndIndex(NodeOrganizer organizer, NodeContext context, NodeRegion region);
}
