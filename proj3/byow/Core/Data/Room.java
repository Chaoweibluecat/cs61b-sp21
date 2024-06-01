package byow.Core.Data;

import java.util.*;

public class Room {

    int id;
    Position start;
    int width;
    int height;

    Set<Position> edgePoints;

    public Room(Position start, int width, int height) {
        this.start = start;
        this.width = width;
        this.height = height;
    }

    public int xStart() {
        return start.x;
    }
    public int xEnd() {
        return start.x + width - 1;
    }

    public int yStart() {
        return start.y;
    }

    public int yEnd() {
        return start.y + height - 1;
    }

    public List<Position> nearestEdgePair(Room other) {
        int min = Integer.MAX_VALUE;
        Position start = null;
        Position end = null;
        Set<Position> startPosList = getEdgePositions();
        for (Position startPos : startPosList) {
            Position endPoint = other.nearestPoint(startPos);
            if (endPoint.manhattanDistance(startPos) < min) {
                min = endPoint.manhattanDistance(startPos);
                start = startPos;
                end = endPoint;
            }
        }
        List<Position> res = new ArrayList<>();
        res.add(start);
        res.add(end);
        return res;

    }

    public Set<Position> getEdgePositions(){
        if (edgePoints != null) {
            return edgePoints;
        }
        Set<Position> positions = new HashSet<Position>();
        for (int i = xStart(); i <= xEnd(); i++) {
            positions.add(new Position(i, yStart()));
            positions.add(new Position(i, yEnd()));
        }

        for (int i = yStart(); i <= yEnd(); i++) {
            positions.add(new Position(xStart(), i));
            positions.add(new Position(xEnd(), i));
        }

        edgePoints = positions;
        return edgePoints;
    }

    public int manhattanDistance(Room other) {
        int manhattanDistance = Integer.MAX_VALUE;
        Set<Position> targets = other.getEdgePositions();
        for (Position edgePosition : getEdgePositions()) {
            for (Position target : targets) {
                if (edgePosition.manhattanDistance(target) < manhattanDistance) {
                    manhattanDistance = edgePosition.manhattanDistance(target);
                }
            }
        }
        return manhattanDistance;
    }

    public Position nearestPoint(Position position) {
        int min = Integer.MAX_VALUE;
        Position res = null;
        Set<Position> edgePositions = getEdgePositions();
        for (Position edgePosition : edgePositions) {
            if (position.manhattanDistance(edgePosition) < min) {
                min = position.manhattanDistance(edgePosition);
                res = edgePosition;
            }
        }
        return res;
    }

    public int manhattanDistance(Position position) {
        return nearestPoint(position).manhattanDistance(position);
    }

    public boolean isCorner(Position position) {
        return (position.x == xEnd() && position.y == yEnd()) ||
                (position.x == xStart() && position.y == yEnd()) ||
                (position.x == xStart() && position.y == yStart()) ||
                (position.x == xEnd() && position.y == yStart());
    }


}
