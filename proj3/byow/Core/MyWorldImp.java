package byow.Core;

import byow.Core.Data.World;
import byow.InputDemo.KeyboardInputSource;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class MyWorldImp {

    public static int width = 40;
    public static int height = 40;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        KeyboardInputSource inputSource = new KeyboardInputSource();
        showMenu();
        char ins1 = inputSource.getNextKey();
        long seed = 1;
        if (ins1 == 'n' || ins1 == 'N') {
            seed = getSeed(inputSource);
        }
        World world = new World(80, 50, new Random(seed));
        world.initRooms();
        world.initHallWays();
        world.genWalls();
        world.genDoor();
        world.render();
        while (inputSource.possibleNextInput()) {
            char nextKey = inputSource.getNextKey();
            world.handleInput(Character.toString(nextKey));
        }

    }

    private static void showMenu() {
        StdDraw.setCanvasSize(width * 16, height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(width / 2.0, height * 0.65, "CS61B by ChovyBlueCat");
        StdDraw.text(width / 2.0, height / 2.0, "welcome");
        StdDraw.text(width / 2.0, height * 0.35, "New Game (n)");
        StdDraw.show();
    }

    private static long getSeed(KeyboardInputSource inputSource) {
        drawFrame(width / 2.0, height * 0.65, "enter seed, stop with 'S' ");
        String seedStr = "";
        char cur = inputSource.getNextKey();
        while (cur != 'S' && cur != 's') {
            seedStr = seedStr + cur;
            cur = inputSource.getNextKey();
            drawFrame("your seed" + seedStr);
        }
        return Long.parseLong(seedStr.toString());
    }

    public static void drawFrame(String s) {
        drawFrame(width / 2.0, height / 2.0, s);
    }

    public static void drawFrame(double x, double y, String s) {
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(x, y, s);
        StdDraw.show();
    }

}
