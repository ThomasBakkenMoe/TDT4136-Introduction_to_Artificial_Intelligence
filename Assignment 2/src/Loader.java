import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class for loading edges and nodes from files into arrays.
 */
public class Loader {

    public ArrayList<Node> loadNodes(String filename) throws Exception{

        String currentLine = "";
        FileInputStream fileInputStream = new FileInputStream(filename);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        ArrayList<int[]> intArrays = new ArrayList<>();
        ArrayList<Node> nodeList = new ArrayList<>();

        // Load all values from CSV into a List of int arrays
        // The index of the list represents row, the index of each array represents a column.

        while (true) {
            currentLine = dataInputStream.readLine();
            if (currentLine == null) {
                break;
            }

            String[] lineArray = currentLine.split(",");
            int[] intArray = new int[lineArray.length];

            for (int i = 0; i < lineArray.length; i++) {
                intArray[i] = Integer.parseInt(lineArray[i]);
            }
            intArrays.add(intArray);
        }

        int currentNodeNumber = 0;

        // i = row
        // j = column
        for (int i = 0; i < intArrays.size(); i++) {
            for (int j = 0; j < intArrays.get(i).length; j++) {

                Node newNode = new Node(currentNodeNumber);
                newNode.setRow(i);
                newNode.setColumn(j);
                newNode.setCost(intArrays.get(i)[j]);

                if ( (i-1 > -1) && (j-1 > -1) ) {

                    //Connect the current node with the one above it(if there is a node
                    if (intArrays.get(i-1)[j] > 0) {
                        newNode.getConnectingNodesList().add(nodeList.get(j + (intArrays.get(i).length * (i-1))));
                        nodeList.get(j + (intArrays.get(i).length * (i-1))).getConnectingNodesList().add(newNode);
                    }

                    if (intArrays.get(i)[j-1] > 0) {
                        newNode.getConnectingNodesList().add(nodeList.get((j-1) + (intArrays.get(i).length * i)));
                        nodeList.get((j-1) + (intArrays.get(i).length * i)).getConnectingNodesList().add(newNode);
                    }
                }
                nodeList.add(newNode);
                currentNodeNumber++;
            }
        }

        return nodeList;



        /*
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int numberOfNodes = Integer.parseInt(bufferedReader.readLine().replaceAll("\\s", ""));

        Node[] nodeArray = new Node[numberOfNodes];

        String currentLine = "";
        Node newNode;

        for (int i = 0; i < numberOfNodes; i++) {
            currentLine = bufferedReader.readLine();
            String[] currentLineArray = currentLine.trim().split("\\s+");
            newNode = new Node(Integer.parseInt(currentLineArray[0]));
            newNode.setRow(Double.parseDouble(currentLineArray[1]));
            newNode.setColumn(Double.parseDouble(currentLineArray[2]));

            nodeArray[i] = newNode;
        }

        bufferedReader.close();
        fileReader.close();

        return nodeArray;

         */
    }
}