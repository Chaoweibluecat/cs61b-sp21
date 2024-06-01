package byow.Core.Data;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.QuickUnionUF;

import java.util.*;
import java.util.stream.Collectors;

public class World {

    public static int MIN_ROOM_SIZE = 3;
    public static int MIN_ROOM_DIS = 3;
    public List<Room> rooms;
    public List<Hallway> hallways;

    List<Position> walls;

    Position lockedDoor;
    Position player;

    int width;
    int height;

    int itTime = 0;
    TETile[][] world;
    Random random;
    TERenderer ter = new TERenderer();

    int roomNum;

    int roomRefWidth;
    int roomRefHeight;


    public World(int width, int height, Random random){
        this.width = width;
        this.height = height;
        this.random = random;
        ter.initialize(width, height);
        world = new TETile[width][height];
        rooms = new ArrayList<>();
        hallways = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        roomNum = random.nextInt(6) + 10;
        // magic , don't ask!
        int roomSizeFactor = (int) (Math.sqrt(roomNum) * 1.3);
        roomRefWidth = width / roomSizeFactor ;
        roomRefHeight = height / roomSizeFactor ;
    }

    public Position randPos(){
        return new Position(random.nextInt(width), random.nextInt(height));
    }

    public Room randomRoom() {
        Position position = randPos();
        while (world[position.x][position.y] != Tileset.NOTHING) {
            position = randPos();
        }
        int x = random.nextInt(roomRefWidth) + MIN_ROOM_SIZE;
        int y = random.nextInt(roomRefHeight) + MIN_ROOM_SIZE;
        return new Room(position, x, y);
    }

    public void initRooms() {
        int roomNum = 0;
        while (roomNum < this.roomNum) {
            Room room = randomRoom();
            if (drawable(room)) {
                draw(room);
                room.id = roomNum++;
                rooms.add(room);
            }
        }
    }

    public boolean drawable(Room room) {
        Position start = room.start;
        if (start.x + room.width + MIN_ROOM_DIS >= this.width || start.y + room.height + MIN_ROOM_DIS >= this.height
         || start.x < MIN_ROOM_DIS || start.y < MIN_ROOM_DIS) {
            return false;
        }
        for (int i = start.x - MIN_ROOM_DIS; i < start.x + room.width + MIN_ROOM_DIS; i++) {
            for (int j = start.y - MIN_ROOM_DIS; j < start.y + room.height + MIN_ROOM_DIS; j++) {
                if (world[i][j] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    public void draw(Room room) {
        Position start = room.start;
        for (int i = start.x; i < start.x + room.width; i++) {
            for (int j = start.y; j < start.y + room.height; j++) {
                if (i==start.x || i==start.x + room.width -1 || j == start.y || j==start.y + room.height -1) {
                    world[i][j] = Tileset.FLOOR;
                } else {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    public void initHallWays() {
        List<Edge> edges = new ArrayList<>();
        QuickUnionUF union = new QuickUnionUF(roomNum);
        for (int i = 0; i < rooms.size(); i++) {
            for (int j = i + 1; j < rooms.size(); j++) {
                edges.add(new Edge(rooms.get(i), rooms.get(j), rooms.get(i).manhattanDistance(rooms.get(j))));
            }
        }
        List<Edge> sortedEdges = edges.stream().sorted(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.weight - o2.weight;
            }
        }).collect(Collectors.toList());

        List<Edge> resultEdge = new ArrayList<>();
        for (Edge edge : sortedEdges) {
            if (!union.connected(edge.a.id, edge.b.id)) {
                resultEdge.add(edge);
                union.union(edge.a.id, edge.b.id);
            }
        }
        generateHallWays(resultEdge);
    }

    public void genWalls() {
        Set<Position> walls = new HashSet<>();
        for (Room room : rooms) {
            Set<Position> edgePositions = room.getEdgePositions();
            for (Position edgePoint : edgePositions) {
                List<Position> neighbours = edgePoint.getEightNeighbours();
                for (Position neighbour : neighbours) {
                    if (world[neighbour.x][neighbour.y] == Tileset.NOTHING){
                        walls.add(neighbour);
                        world[neighbour.x][neighbour.y] = Tileset.WALL;
                    }
                }
            }
        }
        for (Hallway hallway : hallways) {
            for (Position path : hallway.paths) {
                List<Position> neighbours = path.getEightNeighbours();
                for (Position neighbour : neighbours) {
                    if (world[neighbour.x][neighbour.y] == Tileset.NOTHING){
                        walls.add(neighbour);
                        world[neighbour.x][neighbour.y] = Tileset.WALL;
                    }
                }

            }
        }
        this.walls = new ArrayList<>(walls);
    }

    private void generateHallWays(List<Edge> resultEdges) {
        for (Edge edge : resultEdges) {
            Room from = edge.a;
            Room to = edge.b;
            List<Position> positions = from.nearestEdgePair(to);
            Set<Position> targetPoints = to.getEdgePositions();
            Position start = positions.get(0);
            Position current = start;
            Set<Position> visited = new HashSet<>();
            visited.add(start);
            Hallway hallway = new Hallway(from, to);
            while (true) {
                itTime++;
                if (itTime > 10000000) {
                    throw new IllegalStateException("we fucked up");
                }
                List<Position> availableMoves = availableMoves(current, visited, targetPoints);
                final Position tempCur = current;
                if (availableMoves.stream().anyMatch(targetPoints::contains)) {
                    hallway.paths = new ArrayList<>(visited);
                    break;
                }
                List<Position> badChoices = availableMoves.stream().filter(move -> to.manhattanDistance(move) >= to.manhattanDistance(tempCur)).collect(Collectors.toList());
                if (availableMoves.isEmpty()) {
                    visited.clear();
                    current = start;
                } else {
                    availableMoves.removeIf(badChoices::contains);
                    int take;
                    if (availableMoves.size() > 0) {
                        take = random.nextInt(availableMoves.size());
                        current = availableMoves.get(take);
                    } else {
                        take = random.nextInt(badChoices.size());
                        current = badChoices.get(take);
                    }
                    visited.add(current);
                }
            }
            hallways.add(hallway);
            for (Position path : hallway.paths) {
                world[path.x][path.y] = Tileset.MOUNTAIN;
            }
        }
    }

    private List<Position> availableMoves(Position position, Set<Position> visited, Set<Position> targets){
        List<Position> res = position.getNeighbours();
        res.removeIf(pos -> {
            return visited.contains(pos) || pos.x >= width || pos.x <= 0 || pos.y <= 0 || pos.y >= height ||
                    ((world[pos.x][pos.y] != Tileset.NOTHING) && !targets.contains(pos));
        });
        return res;
    }
    public void render() {
        ter.renderFrame(world);
    }

    public void genDoor(){
        int wallIdx = random.nextInt(walls.size());
        while (!legalWall(walls.get(wallIdx))) {
            wallIdx = random.nextInt(walls.size());
        }
        lockedDoor = walls.get(wallIdx);
        world[lockedDoor.x][lockedDoor.y] = Tileset.LOCKED_DOOR;
        for (Position neighbour : lockedDoor.getNeighbours()) {
            if (world[neighbour.x][neighbour.y] != Tileset.LOCKED_DOOR
                    && world[neighbour.x][neighbour.y] != Tileset.NOTHING
                    && world[neighbour.x][neighbour.y] != Tileset.WALL) {
                player = neighbour;
                break;
            }
        }
        world[player.x][player.y] = Tileset.AVATAR;
    }

    private boolean legalWall(Position pos){
        List<Position> neighbours = pos.getNeighbours();
        int nothing = 0;
        int something = 0;
        int wall = 0;
        for (Position neighbour : neighbours) {
            if (world[neighbour.x][neighbour.y] == Tileset.NOTHING){
                nothing++;
            } else if (world[neighbour.x][neighbour.y] == Tileset.WALL){
                wall++;
            } else {
                something++;
            }
        }
        return nothing == 1 && wall == 2 && something == 1;
    }


    public void handleInput(String ins) {
        char c = ins.charAt(0);
        Position next = player;
        if (c == 'w' || c == 'W') {
            next = player.getUp();
        } else if (c == 's' || c == 'S') {
            next = player.getDown();
        } else if (c == 'a' || c == 'A') {
            next = player.getLeft();
         }else if (c == 'd' || c == 'D') {
            next = player.getRight();
        }
        if (legalMove(next)) {
            world[player.x][player.y] = Tileset.GRASS;
            player = next;
            world[player.x][player.y] = Tileset.AVATAR;
            render();
        }
    }

    private boolean legalMove(Position position) {
       return world[position.x][position.y] != Tileset.LOCKED_DOOR
                && world[position.x][position.y] != Tileset.NOTHING
                && world[position.x][position.y] != Tileset.WALL;
    }


}
