import java.util.ArrayList;

public interface GraphI {
	boolean addEdge(int v, int w, int weight);
    boolean removeEdge(int v, int w);
    int getWeight(int v, int w);
    int getVertexAmount();
    void displayGraph();
    static void generateRandomFullGraph(Graph graph, int maxWeight) {
	};
    
    static ArrayList<Integer> travellingSalesmanBruteForce(Graph graph) {
		return null;
	}
    static ArrayList<Integer> asymetricTravellingSalesmanBranchAndBound(Graph graph){
		return null;
    }
   
	
} 
