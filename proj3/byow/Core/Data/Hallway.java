package byow.Core.Data;

import java.util.List;

public class Hallway {
    public Room from;

    public Room to;
    public List<Position> paths;

    public Hallway(Room from, Room to, List<Position> paths) {
        this.from = from;
        this.to = to;
        this.paths = paths;
    }

    public Hallway(Room from, Room to) {
        this.from = from;
        this.to = to;
    }
}
