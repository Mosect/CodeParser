package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.Node;

public interface NodeRegionHandler {

    int getRegionStart(NodeOrganizer organizer, NodeContext context, NodeRegion parent, int start, int offset);

    Node createNode(NodeOrganizer organizer, NodeContext context, NodeRegion parent, int start, int offset);

    boolean isRegionEnd(NodeOrganizer organizer, NodeContext context, NodeRegion region, int offset);
}
