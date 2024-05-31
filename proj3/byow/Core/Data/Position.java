package byow.Core.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
    }

    public List<Position> getNeighbours(){
        List<Position> res = new ArrayList<>();
        res.add(new Position(x, y+1));
        res.add(new Position(x, y-1));
        res.add(new Position(x+1, y));
        res.add(new Position(x-1, y));
        return res;
    }

    public List<Position> getEightNeighbours(){
        List<Position> res = new ArrayList<>();
        res.add(new Position(x, y+1));
        res.add(new Position(x, y-1));
        res.add(new Position(x+1, y));
        res.add(new Position(x-1, y));
        res.add(new Position(x+1, y+1));
        res.add(new Position(x-1, y+1));
        res.add(new Position(x+1, y-1));
        res.add(new Position(x-1, y-1));

        return res;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int manhattanDistance(Position other) {
        return Math.abs(other.x - x) + Math.abs(other.y - y);
    }
}
