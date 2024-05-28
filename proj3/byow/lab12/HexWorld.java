package byow.lab12;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.FilterOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 50;

    private static Random RANDOM = new Random();
    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        Position worldSize = findWorldSize(4);
        ter.initialize(worldSize.x + 4, worldSize.y + 4);
        // initialize tiles
        TETile[][] world = new TETile[worldSize.x + 4][worldSize.y + 4];
        for (int x = 0; x < worldSize.x + 4; x += 1) {
            for (int y = 0; y < worldSize.y + 4; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        List<Position> points = findPoints(4);
        for (Position point : points) {
            addHexagon(world, randomTile(), 4, point.x + 2, point.y + 2);
        }
        // draws the world to the screen
        ter.renderFrame(world);
    }

    public static void addHexagon(TETile[][] world, TETile tile, int size, int x, int y) {
        // 画上半区递增区
        for (int i = 0; i < size; i++) {
            drawLine(x - i, y - i, world, tile, size + 2 * i);
        }
        // 画下班区递减区
        for (int i = 0; i < size; i++) {
            drawLine(x - size + 1 + i, y - size - i, world, tile, size + 2 * (size - 1) - 2 * i);
        }
    }

    private static void drawLine(int start, int height, TETile[][] world, TETile tile, int size) {
        for (int i = 0; i < size; i++) {
            world[i + start][height] = tile;
        }
    }

    public static Position findWorldSize(int hexSize){
        int y = hexSize * 2 * 5;
        int x = (3 * hexSize - 2) * 3 + hexSize * 2;
        return new Position(x, y);
    }

    public static List<Position> findPoints(int hexSize) {
        Position worldSize = findWorldSize(hexSize);
        List<Position> ret = new ArrayList<>();
        int firstX = (3 * hexSize - 2) + hexSize * 2 - 1;
        int firstY = worldSize.y - 1;
        for (int i = 0; i < 5; i++) {
            ret.add(new Position(firstX, firstY - i * 2* hexSize));
        }
        int xgap = 2 * hexSize -1;
        int ygap = hexSize;
        int secondLX = firstX - xgap;
        int secondY = firstY - ygap;
        for (int i = 0; i < 4; i++) {
            ret.add(new Position(secondLX,secondY - i * 2* hexSize));
        }
        int secondRX = firstX + xgap;
        for (int i = 0; i < 4; i++) {
            ret.add(new Position(secondRX,secondY - i * 2* hexSize));
        }

        int thirdLX = secondLX - xgap;
        int thirdY = secondY - ygap;
        for (int i = 0; i < 3; i++) {
            ret.add(new Position(thirdLX, thirdY - i * 2* hexSize));
        }
        int thirdRX = secondRX + xgap;
        for (int i = 0; i < 3; i++) {
            ret.add(new Position(thirdRX, thirdY - i * 2* hexSize));
        }
        return ret;
    }

    public static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void print(TETile[][] map) {
        for (int i = 0; i < map.length; i++) {
            String temp = "";
            for (TETile teTile : map[i]) {
                if (teTile == Tileset.NOTHING) {
                    temp += "0";
                } else  {
                    temp += "1";
                }
            }
            System.out.println(temp);

        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(9);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.AVATAR;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.LOCKED_DOOR;
            case 5: return Tileset.MOUNTAIN;
            case 6: return Tileset.SAND;
            case 7: return Tileset.WATER;
            case 8: return Tileset.TREE;

        }
        return null;
    }


}
