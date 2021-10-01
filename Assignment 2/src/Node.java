import java.util.ArrayList;

/**
 * Class representing a Node
 * The same node class is used in both Dijkstra and A*,
 * and it contains the methods to calculate the distance to other nodes.
 */
public class Node implements Comparable {

    private int nodeNum;
    private int cost = 1000000000; // The initial value is to be considered infinite
    private int priority = 1000000000; // The initial value is to be considered infinite
    private double directDistance = 0.0;
    private Node previousNode;
    private ArrayList<Edge> outgoingEdgeList = new ArrayList<>();
    private boolean expanded = false;
    private boolean discovered = false;
    private boolean directDistanceCalculated = false;

    private double row = 0.0;       // x
    private double column = 0.0;    // y

    public Node(int nodeNum){
        this.nodeNum = nodeNum;
    }

    /**
     * Method for calculating the distance between two points along the surface of a sphere (like the Earth)
     * This method is used by A* to calculate the direct distance to the goal node, and thus setting the priority
     * of expanding this node.
     *
     * @param node the node that we want to calculate the distance to.
     * @return The calculated direct distance to the given node.
     */
    public double calculateDirectDistanceToNode(Node node){

        // Method for calculating the distance between two points along the surface of a sphere (like the Earth)

        directDistanceCalculated = true;

        setDirectDistance(Math.sqrt((node.column - column) * (node.column - column) + (node.row - row) * (node.row - row)));

        return directDistance;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int time) {
        this.cost = time;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(int nodeNum) {
        this.nodeNum = nodeNum;
    }

    public ArrayList<Edge> getOutgoingEdgeList() {
        return outgoingEdgeList;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public double getRow() {
        return row;
    }

    public void setRow(double row) {
        this.row = row;
    }

    public double getColumn() {
        return column;
    }

    public void setColumn(double column) {
        this.column = column;
    }

    public boolean hasDirectdistanceCalculated() {
        return directDistanceCalculated;
    }

    public void setDirectDistanceCalculated(boolean directDistanceCalculated){
        this.directDistanceCalculated = directDistanceCalculated;
    }

    public double getDirectDistance() {
        return directDistance;
    }

    public void setDirectDistance(double directDistance) {
        this.directDistance = directDistance;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    @Override
    public int compareTo(Object o) {

        Node otherNode;
        if (o instanceof Node){

            otherNode = (Node) o;
            return priority - otherNode.getPriority();
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {

        return obj instanceof Node && ((Node) obj).nodeNum == this.nodeNum;

    }

    @Override
    public String toString() {
        return "" + nodeNum + " lat: " + row + " long: " + column + " Prev. node: " + previousNode;
    }
}
