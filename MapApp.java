/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2017 
// PROJECT:          p5
// FILE:             NavigationGraph.java
// TEAM:    72
// Author: Jonathan Nelson, jnelson33@wisc.edu, jnelson, Lec 001
// Author: David Zhu, dzhu46@wisc.edu, zhu, Lec 002
// Author: Kendra Raczek, raczek@wisc.edu, raczek, Lec 001
// Author: Xinhui Yu, xyu269@wisc.edu, Xinhui Yu, Lec 002
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Driver class that reads/parses the input file and creates NavigationGraph
 * object.
 * 
 * @author Jonathan, Harry, Kendra, David
 *
 */
public class MapApp {

	private NavigationGraph graphObject;

	/**
	 * Constructs a MapApp object
	 * 
	 * @param graph
	 *            NaviagtionGraph object
	 */
	public MapApp(NavigationGraph graph) {
		this.graphObject = graph;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java MapApp <pathToGraphFile>");
			System.exit(1);
		}

		// read the filename from command line argument
		String locationFileName = args[0];
		try {
			NavigationGraph graph = createNavigationGraphFromMapFile(locationFileName);
			MapApp appInstance = new MapApp(graph);
			appInstance.startService();

		} catch (FileNotFoundException e) {
			System.out.println("GRAPH FILE: " + locationFileName + " was not found.");
			System.exit(1);
		} catch (InvalidFileException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

	}

	/**
	 * Displays options to user about the various operations on the loaded graph
	 */
	public void startService() {

		System.out.println("Navigation App");
		Scanner sc = new Scanner(System.in);

		int choice = 0;
		do {
			System.out.println();
			System.out.println("1. List all locations");
			System.out.println("2. Display Graph");
			System.out.println("3. Display Outgoing Edges");
			System.out.println("4. Display Shortest Route");
			System.out.println("5. Quit");
			System.out.print("Enter your choice: ");

			while (!sc.hasNextInt()) {
				sc.next();
				System.out.println("Please select a valid option: ");
			}
			choice = sc.nextInt();

			switch (choice) {
			case 1:
				System.out.println(graphObject.getVertices());
				break;
			case 2:
				System.out.println(graphObject.toString());
				break;
			case 3: {
				System.out.println("Enter source location name: ");
				String srcName = sc.next();
				Location src = graphObject.getLocationByName(srcName);

				if (src == null) {
					System.out.println(srcName + " is not a valid Location");
					break;
				}

				List<Path> outEdges = graphObject.getOutEdges(src);
				System.out.println("Outgoing edges for " + src + ": ");
				for (Path path : outEdges) {
					System.out.println(path);
				}
			}
				break;

			case 4:
				System.out.println("Enter source location name: ");
				String srcName = sc.next();
				Location src = graphObject.getLocationByName(srcName);

				System.out.println("Enter destination location name: ");
				String destName = sc.next();
				Location dest = graphObject.getLocationByName(destName);

				if (src == null || dest == null) {
					System.out.println(srcName + " and/or " + destName + " are not valid Locations in the graph");
					break;
				}

				if (src == dest) {
					System.out.println(srcName + " and " + destName + " correspond to the same Location");
					break;
				}

				System.out.println("Edge properties: ");
				// List Edge Property Names
				String[] propertyNames = graphObject.getEdgePropertyNames();
				for (int i = 0; i < propertyNames.length; i++) {
					System.out.println("\t" + (i + 1) + ": " + propertyNames[i]);
				}
				System.out.println("Select property to compute shortest route on: ");
				int selectedPropertyIndex = sc.nextInt() - 1;

				if (selectedPropertyIndex >= propertyNames.length) {
					System.out.println("Invalid option chosen: " + (selectedPropertyIndex + 1));
					break;
				}

				String selectedPropertyName = propertyNames[selectedPropertyIndex];
				List<Path> shortestRoute = graphObject.getShortestRoute(src, dest, selectedPropertyName);
				for(Path path : shortestRoute) {
					System.out.print(path.displayPathWithProperty(selectedPropertyIndex)+", ");
				}
				if(shortestRoute.size()==0) {
					System.out.print("No route exists");
				}
				System.out.println();

				break;

			case 5:
				break;

			default:
				System.out.println("Please select a valid option: ");
				break;

			}
		} while (choice != 5);
		sc.close();
	}

	/**
	 * Reads and parses the input file passed as argument create a
	 * NavigationGraph object. The edge property names required for
	 * the constructor can be got from the first line of the file
	 * by ignoring the first 2 columns - source, destination. 
	 * Use the graph object to add vertices and edges as
	 * you read the input file.
	 * 
	 * @param graphFilepath
	 *            path to the input file
	 * @return NavigationGraph object
	 * @throws FileNotFoundException
	 *             if graphFilepath is not found
	 * @throws InvalidFileException
	 *             if header line in the file has < 3 columns or 
	 *             if any line that describes an edge has different number of properties 
	 *             	than as described in the header or 
	 *             if any property value is not numeric 
	 */

	public static NavigationGraph createNavigationGraphFromMapFile(String graphFilepath) 
		throws FileNotFoundException, InvalidFileException {
		
			// NavigationGraph with vertices and edges
			if (graphFilepath.equals(" ") || graphFilepath == null)
				throw new FileNotFoundException();
			File file = new File(graphFilepath);
			//Scans in file
			Scanner in = new Scanner(file);
			int i =0;

			String header = in.nextLine();
			String[] headLine = header.split(" ");
			String[] propName = new String[headLine.length -2];
			for (i = 2; i < headLine.length; i++)
				propName[i - 2] = headLine[i];
			if (headLine.length < 3) {
				in.close();
				//Ensures correct file format
				throw new InvalidFileException("The file format is invalid!");
			}
			NavigationGraph graph = new NavigationGraph(propName);
			Location src = null;
			Location dest = null;
			String srcName;
			String destName;
			Path edge;
			boolean t;
			
			//Scans all information
			while (in.hasNextLine()){
				t = true;

				srcName = in.next();
				srcName = srcName.toLowerCase();
				destName = in.next();
				destName = destName.toLowerCase();

				for (GraphNode<Location, Path> a: graph.nodes)
					if (a.getVertexData().getName().equals(srcName)){
						t = false;
						src = graph.getLocationByName(srcName);
					}
				if (t){
					src = new Location(srcName);
					//Transfers to graph
					graph.addVertex(src);
				}
				t = true;
				for (GraphNode<Location, Path> b: graph.nodes)
					if (b.getVertexData().getName().equals(destName)){
						t = false;
						dest = graph.getLocationByName(destName);
					}
				if (t){
					dest = new Location(destName);
					graph.addVertex(dest);
				}
				//Scans information to a graph
				ArrayList<Double> propList = new ArrayList<Double>();
				
				for (int j=0; j < propName.length; j++) {
					//If an edge does not have enough properties in file or property is not numeric
					if (!in.hasNextDouble()) {
						in.close();
						throw new InvalidFileException("The file format is invalid!");
					}
					propList.add(in.nextDouble());
				}			
				//If an edge has too many properties in file
				if (in.hasNextDouble()) {
					in.close();
					throw new InvalidFileException("The file format is invalid!");
				}
				edge = new Path(src,dest, propList);
				graph.addEdge(src, dest, edge);
			}
			//Closes file
			in.close();
			//Returns necessary graph
			return graph;
	}
}
