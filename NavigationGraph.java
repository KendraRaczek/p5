import java.util.ArrayList;
import java.util.List;

public class NavigationGraph implements GraphADT<Location, Path> {

	//TODO: Implement all methods of GraphADT
	
	private ArrayList<GraphNode<Location, Path>> nodes;
	private int size;
	
	public NavigationGraph(String[] edgePropertyNames) {
		
		this.nodes = new ArrayList<GraphNode<Location, Path>>();
		this.size = 0;
	}
	
	
	
	public void addVertex(Location vertex) {
		
	}

	public void addEdge(Location src, Location dest, Path edge) {
		
	}
	
	public List<Location> getVertices() {
		
	}
	
	public Path getEdgeIfExists(Location src, Location dest) {
		
	}
	
	public List<Path> getOutEdges(Location src) {
		
	}
	
	public List<Location> getNeighbors(Location vertex) {
		
	}
	
	public List<Path> getShortestRoute(Location src, Location dest, String edgePropertyName) {
		
	}
	
	public String[] getEdgePropertyNames() {
		
	}
	
	public String toString() {
		
	}
	
	/**
	 * Returns a Location object given its name
	 * 
	 * @param name
	 *            name of the location
	 * @return Location object
	 */
	public Location getLocationByName(String name) {
		return null; //TODO: implement correctly. 
	}

}
