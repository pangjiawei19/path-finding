package com.mt.server.pathfinding.map;

import com.mt.server.pathfinding.util.ImageUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pangjiawei
 * @created 2021-08-15 16:59:07
 */
public class MapManager {

    public static final String MAP_KEY_TEST = "test";
    public static final String MAP_KEY_ZELDA = "zelda";
    public static final String[] MAP_KEY_LIST = {MAP_KEY_TEST, MAP_KEY_ZELDA};

    private static final Map<String, MapData> mapDataMap = new HashMap<>();

    public static void init() {
        try {
            for (String mapKey : MAP_KEY_LIST) {
                NodeType[][] testArray = ImageUtil.readPNG(mapKey + ".png");
                MapData testMapData = new MapData(testArray);
                mapDataMap.put(mapKey, testMapData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MapData getMapData(String mapKey) {
        return mapDataMap.get(mapKey);
    }
}
