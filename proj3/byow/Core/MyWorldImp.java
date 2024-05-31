package byow.Core;

import byow.Core.Data.World;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.lab12.HexWorld;

import java.util.List;
import java.util.Random;

public class MyWorldImp {
    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        World world = new World(80, 50, new Random());
        world.initRooms();
        world.initHallWays();
        world.genWalls();
        world.Render();

    }

}
