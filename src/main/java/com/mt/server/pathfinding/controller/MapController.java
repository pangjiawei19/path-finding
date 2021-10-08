package com.mt.server.pathfinding.controller;

import com.mt.server.pathfinding.map.AStar;
import com.mt.server.pathfinding.map.Direction;
import com.mt.server.pathfinding.map.MapData;
import com.mt.server.pathfinding.map.MapManager;
import com.mt.server.pathfinding.map.Node;
import com.mt.server.pathfinding.map.Point;
import com.mt.server.pathfinding.map.User;
import com.mt.server.pathfinding.util.MapUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author pangjiawei
 * @created 2021-08-15 17:19:56
 */
@RestController
@RequestMapping("/map")
public class MapController {

    private final AtomicInteger idGenerator = new AtomicInteger();
    private final Map<Integer, User> userMap = new HashMap<>();

    @GetMapping("/data/{mapKey}")
    public Object getMapData(@PathVariable String mapKey) {
        Map<String, Object> result = new HashMap<>();
        result.put("exist", false);
        MapData mapData = MapManager.getMapData(mapKey);
        if (mapData != null) {
            result.put("exist", true);
            result.put("array", mapData.getMapArrayOrdinal());
        }
        return result;
    }

    @GetMapping(value = "/login/{mapKey}")
    public User login(@PathVariable String mapKey) {
        int id = idGenerator.incrementAndGet();
        MapData mapData = MapManager.getMapData(mapKey);
        if (mapData == null) {
            return null;
        }
//        Point randomPoint = mapData.randomCanMoveOnPoint();
        Point randomPoint = new Point(0, 0); // 起点出生
        User user = new User(id, mapKey, randomPoint.getCol(), randomPoint.getRow());
//        userMap.put(id, user);
        return user;
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/findPath")
    public List<Point> findPath(@RequestBody Map<String, Object> body) {
        String mapKey = (String) body.get("mapKey");
        String directionText = (String) body.get("direction");
        Direction direction = Direction.valueOf(directionText);
        boolean mergePath = Boolean.parseBoolean((String) body.get("mergePath"));

        Map<String, Integer> startMap = (Map<String, Integer>) body.get("start");
        Map<String, Integer> endMap = (Map<String, Integer>) body.get("end");
        MapData mapData = MapManager.getMapData(mapKey);
        if (mapData == null) {
            return List.of();
        }
        Node startNode = mapData.getNodeByColAndRow(startMap.get("col"), startMap.get("row"));
        Node endNode = mapData.getNodeByColAndRow(endMap.get("col"), endMap.get("row"));
        List<Node> path = AStar.find(mapData, direction, startNode, endNode);
        if (mergePath) {
            path = MapUtil.mergePath(path);
        }
        return path.stream().map(Node::toPoint).collect(Collectors.toList());
    }
}
