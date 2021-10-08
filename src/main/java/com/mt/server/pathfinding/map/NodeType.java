package com.mt.server.pathfinding.map;

/**
 * @author pangjiawei
 * @created 2021-08-11 22:36:56
 */
public enum NodeType {

    Normal(255, 236, 179), // #ffecb3
    None(0, 0, 0), // #ffffff
    Wall(255, 179, 0), // #ffb300
    Water(35, 150, 243), // #2396f3
    Mountain(0, 255, 0), // # 00ff00
    ;

    private static final int PIXEL_DIFF = 5;

    private static final NodeType[] values = NodeType.values();

    public static NodeType valueOf(int ordinal) {
        return values[ordinal];
    }

    public static NodeType getByRGB(int r, int g, int b) {
        for (NodeType nodeType : values) {
            // RGB 偏差处理
            if (Math.abs(nodeType.r - r) <= PIXEL_DIFF
                    && Math.abs(nodeType.g - g) <= PIXEL_DIFF
                    && Math.abs(nodeType.b - b) <= PIXEL_DIFF) {
                return nodeType;
            }
        }
        throw new IllegalArgumentException("can not find NodeType, r:" + r + ",g:" + g + ",b:" + b);
    }

    private final int r;
    private final int g;
    private final int b;

    NodeType(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public boolean canMoveOn() {
        return this == Normal;
    }
}
