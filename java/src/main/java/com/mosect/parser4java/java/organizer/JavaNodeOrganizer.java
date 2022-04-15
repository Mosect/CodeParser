package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.organizer.NodeListMaker;
import com.mosect.parser4java.core.organizer.NodeRegion;
import com.mosect.parser4java.java.Constants;
import com.mosect.parser4java.java.node.SentenceNode;
import com.mosect.parser4java.java.node.TypeNode;
import com.mosect.parser4java.java.util.NodeUtils;

import java.util.ArrayList;
import java.util.List;

public class JavaNodeOrganizer {

    /**
     * 区域结尾不匹配
     */
    protected final static int REGION_END_NOT_MATCH = -1;
    /**
     * 区域结尾不匹配，但是消耗了当前位置
     */
    protected final static int REGION_END_NOT_MATCH_AND_CONSUMED = -2;

    protected List<JavaNodeRegion> regions = new ArrayList<>(128);
    protected List<JavaNodeRegion> unclosedRegions = new ArrayList<>(12);
    protected NodeList organizedNodeList;
    protected NodeListMaker nodeListMaker = new NodeListMaker();

    public NodeList getOrganizedNodeList() {
        return organizedNodeList;
    }

    public void organize(NodeList nodeList) {
        organize(nodeList, 0, nodeList.getNodeCount());
    }

    public void organize(NodeList nodeList, int start, int end) {
        onClear();
        onOrganize(nodeList, start, end);
        for (JavaNodeRegion region : regions) {
            if (null == region.getNode()) {
                Node node = createNodeWithRegion(region);
                region.setNode(node);
            }
        }
        organizedNodeList = nodeListMaker.make(nodeList, regions);
    }

    protected void onOrganize(NodeList nodeList, int start, int end) {
        int safeEnd = end < 0 ? nodeList.getNodeCount() : end;
        int offset = start;
        int nodeStart = start;
        while (offset < safeEnd) {
            Node node = nodeList.getNode(offset);

            // 处理未关闭区域，如匹配结束位置，结束此区域
            boolean consumed = false;
            for (int i = unclosedRegions.size() - 1; i >= 0; i--) {
                JavaNodeRegion unclosed = unclosedRegions.get(i);
                int matchEnd = onMatchRegionEnd(unclosed, i, offset, safeEnd);
                if (matchEnd == REGION_END_NOT_MATCH_AND_CONSUMED) {
                    consumed = true;
                    break;
                } else if (matchEnd >= 0) {
                    offset = matchEnd;
                    nodeStart = matchEnd;
                    consumed = true;
                    // 关闭区域
                    closeRegion(i, matchEnd);
                    break;
                }
            }
            if (consumed) {
                JavaNodeRegion unclosed = getLastUnclosedRegion();
                if (unclosed.getResetOffset() >= 0) {
                    // 此区域无效，移除
                    unclosedRegions.remove(unclosedRegions.size() - 1);
                    while (regions.size() > 0) {
                        NodeRegion region = regions.remove(regions.size() - 1);
                        if (region == unclosed) break;
                    }
                    // 恢复重置点
                    offset = unclosed.getResetOffset();
                    nodeStart = unclosed.getResetStart();
                } else {
                    nodeStart = offset + 1;
                }
            } else {
                JavaNodeRegion region = onMatchRegion(nodeList, node, nodeStart, offset);
                if (null != region) {
                    region.setResetStart(nodeStart);
                    // 匹配了新区域
                    addRegion(region);
                    nodeStart = region.getEnd();
                }
                // 进行下一个node匹配
                ++offset;
            }
        }
        // 关闭区间
        closeRegion(0, safeEnd);
    }

    /**
     * 添加区域
     *
     * @param region 区域
     */
    protected void addRegion(JavaNodeRegion region) {
        regions.add(region);
        onRegionAdded(region);
        if (unclosedRegions.size() > 0) {
            JavaNodeRegion unclosedRegion = unclosedRegions.get(unclosedRegions.size() - 1);
            if (isBrother(unclosedRegion, region)) {
                // 兄弟区间关系，需要关闭之前区间
                closeRegion(unclosedRegions.size() - 1, region.getStart());
            } else {
                // 父子区间关系
                region.setParent(unclosedRegion);
            }
        }
        if (!region.isClosed()) {
            // 区域未关闭
            unclosedRegions.add(region);
        }
    }

    /**
     * 关闭区间
     *
     * @param unclosedIndex 未关闭的区间下标
     * @param end           结束位置
     */
    protected void closeRegion(int unclosedIndex, int end) {
        while (unclosedRegions.size() > unclosedIndex) {
            int index = unclosedRegions.size() - 1;
            JavaNodeRegion region = unclosedRegions.remove(index);
            region.setEnd(end);
            region.setClosed(true);
            onRegionClosed(region, index);
        }
    }

    /**
     * 判断当前是否匹配了新区域
     *
     * @param nodeList 节点列表
     * @param node     节点
     * @param start    节点开始位置
     * @param offset   当前节点偏移位置
     * @return 如匹配区域，需要返回区域对象
     */
    protected JavaNodeRegion onMatchRegion(NodeList nodeList, Node node, int start, int offset) {
        return null;
    }

    /**
     * 匹配区域结尾
     *
     * @param region        区域
     * @param unclosedIndex 为关闭区域下标
     * @param offset        需要处理的node偏移量
     * @param maxEnd        区域最大结束位置
     * @return 区域的结束位置
     */
    protected int onMatchRegionEnd(JavaNodeRegion region, int unclosedIndex, int offset, int maxEnd) {
        return REGION_END_NOT_MATCH_AND_CONSUMED;
    }

    /**
     * 区域被添加时触发
     *
     * @param region 区域
     */
    protected void onRegionAdded(JavaNodeRegion region) {
    }

    /**
     * 区域被关闭时触发
     *
     * @param region        区域
     * @param unclosedIndex 未关闭区间的下标
     */
    protected void onRegionClosed(JavaNodeRegion region, int unclosedIndex) {
    }

    /**
     * 判断是否为兄弟区间
     *
     * @param region1 区间1
     * @param region2 区间2
     * @return true，兄弟区间；false，父子区间
     */
    protected boolean isBrother(JavaNodeRegion region1, JavaNodeRegion region2) {
        return false;
    }

    /**
     * 清空缓存
     */
    protected void onClear() {
        organizedNodeList = null;
        unclosedRegions.clear();
    }

    /**
     * 获取最后一个未关闭区间
     *
     * @return 未关闭区间
     */
    protected JavaNodeRegion getLastUnclosedRegion() {
        if (unclosedRegions.size() > 0) {
            return unclosedRegions.get(unclosedRegions.size() - 1);
        }
        return null;
    }

    protected JavaNodeRegion createTypeNodeRegion(String classType, NodeList src, int start, int offset) {
        TypeNode node = new TypeNode(Constants.NODE_TYPE, classType);
        int safeStart = NodeUtils.trimStartOnlyKeyword(null, src, start, offset);
        return new JavaNodeRegion(node, src, safeStart, offset + 1);
    }

    protected JavaNodeRegion createKeywordSentence(String sentenceType, NodeList src, int offset) {
        SentenceNode node = new SentenceNode(Constants.NODE_SENTENCE, sentenceType);
        return new JavaNodeRegion(node, src, offset, offset + 1);
    }

    protected JavaNodeRegion createUnknownSentence(NodeList src, int start, int offset) {
        int safeStart = NodeUtils.trimStart(src, start, offset);
        SentenceNode node = new SentenceNode(Constants.NODE_SENTENCE, Constants.SENTENCE_TYPE_UNKNOWN);
        return new JavaNodeRegion(node, src, safeStart, offset + 1);
    }

    protected Node createNodeWithRegion(JavaNodeRegion region) {
        throw new IllegalStateException("Unsupported region state: " + region.getState());
    }
}
