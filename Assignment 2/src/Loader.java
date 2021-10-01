import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Class for loading edges and nodes from files into arrays.
 */
public class Loader {

    public Node[] loadNodes(String filename) throws Exception{
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
    }
}