package com.mt.server.pathfinding.util;

import com.mt.server.pathfinding.map.NodeType;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author pangjiawei
 * @created 2021-08-15 16:21:55
 */
public class ImageUtil {

    public static final String PNG_PATH = "data/";

    public static NodeType[][] readPNG(String pngFileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(PNG_PATH + pngFileName);
        InputStream inputStream = classPathResource.getInputStream();
        BufferedImage bi = ImageIO.read(inputStream);
        int width = bi.getWidth();
        int height = bi.getHeight();
        NodeType[][] mapDataArray = new NodeType[height][];

        for (int i = 0; i < height; i++) {
            mapDataArray[i] = new NodeType[width];
            for (int j = 0; j < width; j++) {
                int pixel = bi.getRGB(j, i);
                int r = (pixel & 0xff0000) >> 16;
                int g = (pixel & 0xff00) >> 8;
                int b = (pixel & 0xff);
                mapDataArray[i][j] = NodeType.getByRGB(r, g, b);
            }
        }

        return mapDataArray;
    }
}
