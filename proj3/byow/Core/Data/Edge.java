package byow.Core.Data;

public class Edge {
    public Room a;
    public Room b;
    public int weight;

    public Edge(Room a, Room b, int weight) {
        this.a = a;
        this.b = b;
        this.weight = weight;
    }
}
