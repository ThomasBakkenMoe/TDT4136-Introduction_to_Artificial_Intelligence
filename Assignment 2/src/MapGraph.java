import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.PriorityQueue;

public class MapGraph {

    int nodesChecked = 0;

    /**
     * Method to reset all the nodes in the nodeArray. Used if we want to run both A* and Dijkstra
     * @param nodeArray
     */
    public void reset(Node[] nodeArray){

        for (Node node:nodeArray) {
            node.setPriority(1000000000);
            node.setCost(1000000000);
            node.setDiscovered(false);
            node.setExpanded(false);
            node.setDirectDistanceCalculated(false);
            node.setDirectDistance(0.0);
            node.setPreviousNode(null);

            nodesChecked = 0;
        }
    }

    /**
     * A find the shortest path method that can use either the dijkstra of A* algorithm, based on a boolean parameter.
     * This method times the algorithm and contains the main running loop.
     * @param startingNode
     * @param goalNode
     * @param outputFileName
     * @param dijkstra Whether or not the dijkstra algorithm is to be used. If false: A* is used.
     * @throws Exception
     */
    public void FindShortestPath(Node startingNode, Node goalNode, String outputFileName, boolean dijkstra) throws Exception{

        // Date objects used to time the algorithm
        Date start = new Date();
        Date slutt;

        Node currentNode;
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(startingNode);
        startingNode.setCost(0);
        int iteration = 0;
        while(true){

            currentNode = priorityQueue.poll();

            if (currentNode == null){
                System.out.println("Current node is null! Iteration: " + iteration);
            }

            if (currentNode != goalNode){
                expandNode(currentNode, goalNode, priorityQueue, dijkstra);
            }else{
                System.out.println("Checked " + nodesChecked + " nodes.");
                break;
            }

            iteration++;
        }

        slutt = new Date();
        System.out.println("Time before print: " + (double) (slutt.getTime()-start.getTime()) + "ms");

        printResult(startingNode, goalNode, outputFileName);
    }

    private void expandNode(Node node, Node goalNode, PriorityQueue<Node> priorityQueue, boolean dijkstra){

        node.setExpanded(true);

        for (Node connectingNode: node.getConnectingNodesList()) {

            nodesChecked++;

            if (connectingNode.getCost() > node.getCost()){
                connectingNode.setCost(node.getCost());
                connectingNode.setPreviousNode(node);
            }else{
                continue;
            }

            if (dijkstra){

                connectingNode.setPriority(connectingNode.getCost());

            }else {

                if (!connectingNode.hasDirectdistanceCalculated()) {
                    connectingNode.calculateDirectDistanceToNode(goalNode);
                }
                connectingNode.setPriority((int) connectingNode.getDirectDistance() + connectingNode.getCost());
            }

            if (!connectingNode.isExpanded()){
                if (connectingNode.isDiscovered()){
                    priorityQueue.remove(connectingNode);
                }

                connectingNode.setDiscovered(true);
                priorityQueue.add(connectingNode);
            }
        }
    }

    /**
     * Method that prints the result of a shortest path algorithm to two files.
     * The first is a .txt file containing travel time, distance, amount of nodes visited, and a list of lat/long for all the visited nodes.
     * The second is a .csv file containing the lat/long of all the nodes visited. The name of the .csv file is outputFileName_nodesOnly.csv.
     *
     * @param startingNode The location where the search started.
     * @param goalNode The destination of the search.
     * @param outputFileName The name (without extensions) of the output files.
     * @throws Exception
     */
    private void printResult(Node startingNode, Node goalNode, String outputFileName) throws Exception{
        Node currentNode = goalNode;
        Node prevNode;

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName + ".txt"));

        int totalCost = goalNode.getCost();
        int amountOfNodes = 0;

        ArrayList<Node> nodePathList = new ArrayList<>();

        while (currentNode != startingNode){

            nodePathList.add(currentNode);

            prevNode = currentNode.getPreviousNode();

            if (prevNode == null){
                System.out.println("PrevNode is null!!!");
            }

            amountOfNodes++;

            currentNode = prevNode;
        }

        bufferedWriter.write("Travel cost");
        bufferedWriter.newLine();
        bufferedWriter.write(totalCost);
        bufferedWriter.newLine();
        bufferedWriter.write("Nodes traversed: " + amountOfNodes);
        bufferedWriter.newLine();
        bufferedWriter.write("row,column");
        bufferedWriter.newLine();

        for (int i = nodePathList.size() - 1; i >= 0; i--) {

            bufferedWriter.write(nodePathList.get(i).getRow() + "," + nodePathList.get(i).getColumn());
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        /* Print CSV with the nodes only */
        bufferedWriter = new BufferedWriter(new FileWriter(outputFileName + "_nodesOnly.csv"));

        bufferedWriter.write("latitude,longitude");
        bufferedWriter.newLine();

        for (int i = nodePathList.size() - 1; i >= 0; i--) {

            bufferedWriter.write(nodePathList.get(i).getRow() + "," + nodePathList.get(i).getColumn());
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
    }

    public static void main(String[] args) throws Exception{

        int fromNode = 30236; // Reykjavík, the capital of Iceland
        int toNode = 8136; // Akureyri, a town in the north of Iceland

        String nodeFilepath = "nodesIceland.txt";
        String edgeFilepath = "edgesIceland.txt";

        MapGraph mapGraph = new MapGraph();
        Loader loader = new Loader();

        System.out.println("Reading nodes...");

        Node[] nodeArray = loader.loadNodes(nodeFilepath);

        System.out.println("Beginning program...");

        System.out.println("Djikstra");
        mapGraph.FindShortestPath(nodeArray[fromNode], nodeArray[toNode], "outputDijkstra", true);

        mapGraph.reset(nodeArray);

        System.out.println("A*");
        mapGraph.FindShortestPath(nodeArray[fromNode], nodeArray[toNode], "outputAStar", false);
    }
}