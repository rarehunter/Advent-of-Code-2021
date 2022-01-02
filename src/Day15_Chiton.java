import java.io.File;
import java.io.IOException;
import java.util.*;

class Node {
    private GridPoint point;
    private int weight;

    public Node(GridPoint p, int weight) {
        this.point = p;
        this.weight = weight;
    }

    public GridPoint getPoint() {
        return this.point;
    }

    public int getWeight() {
        return this.weight;
    }

    public String toString() {
        return "(x:" + this.point.x + ", y:" + this.point.y + ", w:" + this.weight + ")";
    }
}

class GridPoint {
    public int x;
    public int y;

    public GridPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        // typecast o to GridPoint so that we can compare data members
        GridPoint gp = (GridPoint) o;

        // Compare x and y values and return accordingly
        return x == gp.x && y == gp.y;
    }
}

class PQNode {
    private GridPoint p;
    private int distance;

    public PQNode(GridPoint p, int distance) {
        this.p = p;
        this.distance = distance;
    }

    public GridPoint getPoint() { return this.p; }
    public int getDistance() { return this.distance; }
    public String toString() {
        return "(x:" + this.p.x + ", y:" + this.p.y + ", d:" + this.distance + ")";
    }
}

class NodeDistanceComparator implements Comparator<PQNode> {
    @Override
    public int compare(PQNode x, PQNode y) {
        if (x.getDistance() < y.getDistance()) {
            return -1;
        }
        if (x.getDistance() > y.getDistance()) {
            return 1;
        }
        return 0;
    }
}

public class Day15_Chiton {
    private static final int SIDE_LENGTH = 100;

    public static void main(String[] args) {
        File file = new File("./inputs/day15/day15.txt");

        try {
            Scanner sc = new Scanner(file);

            // For Part 1:
            //int[][] gridPart1 = initializeGridPart1(sc);
            //Map<GridPoint, List<Node>> adjList = new HashMap<>();
            //constructAdjacencyList(gridPart1, adjList);
            //int part1 = part1(adjList);
            //int part1WithoutAdjList = part1WithoutAdjList(gridPart1);
            //System.out.println("Part 1 (w/o adjList) is: " + part1WithoutAdjList);


            // For Part 2:
            //Map<GridPoint, List<Node>> adjListLarge = new HashMap<>();
            int[][] gridPart2 = initializeGridPart2(sc);
            //constructAdjacencyList(gridPart2, adjListLarge);
            //int part2 = part2(adjListLarge);
            int part2WithoutAdjList = part2WithoutAdjList(gridPart2);
            System.out.println("Part 2 (w/o adjList) is: " + part2WithoutAdjList);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static int[][] initializeGridPart1(Scanner sc) {
        int[][] grid = new int[SIDE_LENGTH][SIDE_LENGTH];

        int row = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            for (int i = 0; i < line.length(); i++) {
                int value = Character.getNumericValue(line.charAt(i));
                grid[row][i] = value;
            }

            row++;
        }

        return grid;
    }

    private static int[][] initializeGridPart2(Scanner sc) {
        int[][] gridLarge = new int[SIDE_LENGTH * 5][SIDE_LENGTH * 5]; // used in part 2

        int row = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            for (int i = 0; i < line.length(); i++) {
                int value = Character.getNumericValue(line.charAt(i));
                gridLarge[row][i] = value;
                gridLarge[row + SIDE_LENGTH][i] = gridLarge[row][i] + 1 > 9 ? 1 : gridLarge[row][i] + 1;
                gridLarge[row + (2*SIDE_LENGTH)][i] = gridLarge[row+SIDE_LENGTH][i] + 1 > 9 ? 1 : gridLarge[row+SIDE_LENGTH][i] + 1;
                gridLarge[row + (3*SIDE_LENGTH)][i] = gridLarge[row+(2*SIDE_LENGTH)][i] + 1 > 9 ? 1 : gridLarge[row+(2*SIDE_LENGTH)][i] + 1;
                gridLarge[row + (4*SIDE_LENGTH)][i] = gridLarge[row+(3*SIDE_LENGTH)][i] + 1 > 9 ? 1 : gridLarge[row+(3*SIDE_LENGTH)][i] + 1;
            }

            row++;
        }

        row = 0;
        while (row < SIDE_LENGTH * 5) {
            for (int i = 0; i < SIDE_LENGTH; i++) {
                gridLarge[row][i + SIDE_LENGTH] = gridLarge[row][i] + 1 > 9 ? 1 : gridLarge[row][i] + 1;
                gridLarge[row][i + (2*SIDE_LENGTH)] = gridLarge[row][i+SIDE_LENGTH] + 1 > 9 ? 1 : gridLarge[row][i+SIDE_LENGTH] + 1;
                gridLarge[row][i + (3*SIDE_LENGTH)] = gridLarge[row][i+(2*SIDE_LENGTH)] + 1 > 9 ? 1 : gridLarge[row][i+(2*SIDE_LENGTH)] + 1;
                gridLarge[row][i + (4*SIDE_LENGTH)] = gridLarge[row][i+(3*SIDE_LENGTH)] + 1 > 9 ? 1 : gridLarge[row][i+(3*SIDE_LENGTH)] + 1;
            }
            row++;
        }

        return gridLarge;
    }

    private static List<GridPoint> getNeighbors(int[][] grid, GridPoint p) {
        List<GridPoint> neighbors = new ArrayList<>();
        int i = p.x;
        int j = p.y;

        if (i == 0) {
            if (j == 0) { // top left corner
                neighbors.add(new GridPoint(i, j+1));
                neighbors.add(new GridPoint(i+1, j));
            } else if (j == grid[0].length - 1) { // top right corner
                neighbors.add(new GridPoint(i, j-1));
                neighbors.add(new GridPoint(i+1, j));
            } else {
                neighbors.add(new GridPoint(i, j+1));
                neighbors.add(new GridPoint(i, j-1));
                neighbors.add(new GridPoint(i+1, j));
            }
        } else if (i == grid.length - 1) {
            if (j == 0) { // bottom left corner
                neighbors.add(new GridPoint(i, j+1));
                neighbors.add(new GridPoint(i-1, j));
            } else if (j == grid[0].length - 1) { // bottom right corner
                neighbors.add(new GridPoint(i, j-1));
                neighbors.add(new GridPoint(i-1, j));
            } else {
                neighbors.add(new GridPoint(i, j+1));
                neighbors.add(new GridPoint(i, j-1));
                neighbors.add(new GridPoint(i-1, j));
            }
        } else if (j == 0) { // left side
            neighbors.add(new GridPoint(i-1, j));
            neighbors.add(new GridPoint(i+1, j));
            neighbors.add(new GridPoint(i, j+1));
        } else if (j == grid[0].length - 1) { // right side
            neighbors.add(new GridPoint(i-1, j));
            neighbors.add(new GridPoint(i+1, j));
            neighbors.add(new GridPoint(i, j-1));
        } else { // anything in the middle
            neighbors.add(new GridPoint(i-1, j));
            neighbors.add(new GridPoint(i+1, j));
            neighbors.add(new GridPoint(i, j-1));
            neighbors.add(new GridPoint(i, j+1));
        }

        return neighbors;
    }

    private static List<GridPoint> getNeighborsBetter(int[][] grid, GridPoint p) {
        List<GridPoint> neighbors = new ArrayList<>();

        int row = p.x;
        int col = p.y;
        List<GridPoint> directions = new ArrayList<>();
        directions.add(new GridPoint(0, 1)); // represents going right
        directions.add(new GridPoint(0, -1)); // represents going left
        directions.add(new GridPoint(-1, 0)); // represents going up
        directions.add(new GridPoint(1, 0)); // represents going down

        for (GridPoint direction : directions) {
            int newrow = row + direction.x;
            int newcol = col + direction.y;

            // check to make sure that the new point is within the grid boundary
            if (newrow > 0 && newrow < grid.length && newcol > 0 && newcol < grid[0].length) {
                neighbors.add(new GridPoint(newrow, newcol));
            }
        }

        return neighbors;
    }


    private static void constructAdjacencyList(int[][] grid, Map<GridPoint, List<Node>> adjList) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                GridPoint currentPoint = new GridPoint(i, j);

                List<Node> neighborNodes = new ArrayList<>();
                List<GridPoint> neighbors = getNeighbors(grid, currentPoint);

                for (GridPoint neighbor : neighbors) {
                    Node neighborNode = new Node(neighbor, grid[neighbor.x][neighbor.y]);
                    neighborNodes.add(neighborNode);
                }

                adjList.put(currentPoint, neighborNodes);
            }
        }
    }

    private static void print2DArray(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    private static void initializeDataStructures(Map<GridPoint, Integer> distances, int gridLength) {
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                GridPoint current = new GridPoint(i, j);
                int distance = (i == 0 && j == 0) ? 0 : Integer.MAX_VALUE;
                distances.put(current, distance);
            }
        }
    }

    // Part 1: Given an adjacency list of a graph, runs Dijkstra's algorithm on it.
    // Source node is top left (0,0) and destination node is bottom right (gridLength-1, gridLength-1).
    // We need:
    // - a set to hold visited points
    // - a priority queue (PQ) of (node, distance) pairs which tells us which node to visit
    // next based on its minimum distance so far.
    // - a dictionary (distances) to store the distances so far between the source node
    // and all other nodes.
    // While the PQ is not empty:
    // 1. get the next smallest distance node from the PQ that hasn't been visited yet.
    // 2. add that node to the visited set
    // 3. get the neighbors of the node and for each neighbor:
    // 3.1 if dist[current] + weight[current,neighbor] < dist[neighbor], update dist[neighbor]
    // to the new minimum value.
    // 3.2 add the neighbor to the PQ.
    private static int part1(Map<GridPoint, List<Node>> adjList) {
        Set<GridPoint> visited = new HashSet<>();

        // Tracks the distance between the source point (0,0) to all other points
        Map<GridPoint, Integer> distances = new HashMap<>();
        initializeDataStructures(distances, SIDE_LENGTH);

        // Create a priority queue of (node, distance) pairs with an initial capacity and a comparator
        // to give priority to nodes with smallest distances so far
        Comparator<PQNode> nodeDistanceComparator = new NodeDistanceComparator();
        PriorityQueue<PQNode> queue = new PriorityQueue<>(SIDE_LENGTH * 2, nodeDistanceComparator);
        queue.add(new PQNode(new GridPoint(0, 0), 0));

        while (!queue.isEmpty()) {
            PQNode current = queue.poll();
            GridPoint currentPoint = current.getPoint();

            if (visited.contains(currentPoint)) continue;

            // Add the point to our visited set indicating that this vertex
            // has been visited.
            visited.add(currentPoint);

            List<Node> neighbors = adjList.get(currentPoint);

            // For each neighbor, if the distance of the source node to the current node (dist[current]) plus
            // the weight of the edge from the current node to the neighbor (weight[current, neighbor])
            // is less than the best recorded distance of the source node to the neighbor (dist[neighbor]) already,
            // we update the distance to the neighbor to the new minimum distance value.
            // Otherwise, no updates are made.
            // In other words, we update if: dist[current] + weight[current,neighbor] < dist[neighbor]
            for (Node neighbor : neighbors) {
                GridPoint neighborPoint = neighbor.getPoint();
                int newDistance = distances.get(currentPoint) + neighbor.getWeight();
                if (newDistance < distances.get(neighborPoint)) {
                    distances.put(neighborPoint, newDistance);
                    queue.add(new PQNode(neighborPoint, newDistance));
                }
            }
        }

        return distances.get(new GridPoint(SIDE_LENGTH - 1, SIDE_LENGTH - 1));
    }

    // Part 2: The same thing as Part 1 except the adjacencyList is much larger
    private static int part2(Map<GridPoint, List<Node>> adjList) {
        Set<GridPoint> visited = new HashSet<>();

        // Tracks the distance between the source point (0,0) to all other points
        Map<GridPoint, Integer> distances = new HashMap<>();
        initializeDataStructures(distances, SIDE_LENGTH * 5);

        // Create a priority queue of (node, distance) pairs with an initial capacity and a comparator
        // to give priority to nodes with smallest distances so far
        Comparator<PQNode> nodeDistanceComparator = new NodeDistanceComparator();
        PriorityQueue<PQNode> queue = new PriorityQueue<>(SIDE_LENGTH * 5 * 2, nodeDistanceComparator);
        queue.add(new PQNode(new GridPoint(0, 0), 0));

        while (!queue.isEmpty()) {
            PQNode current = queue.poll();
            GridPoint currentPoint = current.getPoint();

            if (visited.contains(currentPoint)) continue;

            // Add the point to our visited set indicating that this vertex
            // has been visited.
            visited.add(currentPoint);

            List<Node> neighbors = adjList.get(currentPoint);

            // For each neighbor, if the distance of the source node to the current node (dist[current]) plus
            // the weight of the edge from the current node to the neighbor (weight[current, neighbor])
            // is less than the best recorded distance of the source node to the neighbor (dist[neighbor]) already,
            // we update the distance to the neighbor to the new minimum distance value.
            // Otherwise, no updates are made.
            // In other words, we update if: dist[current] + weight[current,neighbor] < dist[neighbor]
            for (Node neighbor : neighbors) {
                GridPoint neighborPoint = neighbor.getPoint();
                int newDistance = distances.get(currentPoint) + neighbor.getWeight();
                if (newDistance < distances.get(neighborPoint)) {
                    distances.put(neighborPoint, newDistance);
                    queue.add(new PQNode(neighborPoint, newDistance));
                }
            }
        }

        return distances.get(new GridPoint(SIDE_LENGTH * 5 - 1, SIDE_LENGTH * 5 - 1));
    }

    ////////////////////////////////////////////////////////////////////////////////

    // Part 1 (revisited): Instead of calculating an adjacency list in our main function,
    // we can simply use a int[][] grid to get the adjacent cells and the edge weights.
    // Other than this, it is the same as Part 1.
    private static int part1WithoutAdjList(int[][] grid) {
        Set<GridPoint> visited = new HashSet<>();

        // Tracks the distance between the source point (0,0) to all other points
        Map<GridPoint, Integer> distances = new HashMap<>();
        initializeDataStructures(distances, SIDE_LENGTH);

        // Create a priority queue of (node, distance) pairs with an initial capacity and a comparator
        // to give priority to nodes with smallest distances so far
        Comparator<PQNode> nodeDistanceComparator = new NodeDistanceComparator();
        PriorityQueue<PQNode> queue = new PriorityQueue<>(SIDE_LENGTH * 2, nodeDistanceComparator);
        queue.add(new PQNode(new GridPoint(0, 0), 0));

        while (!queue.isEmpty()) {
            PQNode current = queue.poll();
            GridPoint currentPoint = current.getPoint();

            if (visited.contains(currentPoint)) continue;

            // Add the point to our visited set indicating that this vertex
            // has been visited.
            visited.add(currentPoint);

            List<GridPoint> neighbors = getNeighborsBetter(grid, currentPoint);

            // For each neighbor, if the distance of the source node to the current node (dist[current]) plus
            // the weight of the edge from the current node to the neighbor (weight[current, neighbor])
            // is less than the best recorded distance of the source node to the neighbor (dist[neighbor]) already,
            // we update the distance to the neighbor to the new minimum distance value.
            // Otherwise, no updates are made.
            // In other words, we update if: dist[current] + weight[current,neighbor] < dist[neighbor]
            for (GridPoint neighbor : neighbors) {
                int newDistance = distances.get(currentPoint) + grid[neighbor.x][neighbor.y];
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    queue.add(new PQNode(neighbor, newDistance));
                }
            }
        }

        return distances.get(new GridPoint(SIDE_LENGTH - 1, SIDE_LENGTH - 1));
    }

    // Part 2 (revisited): Instead of calculating an adjacency list in our main function,
    // we can simply use a int[][] grid to get the adjacent cells and the edge weights.
    // Other than this, it is the same as Part 1 just with a much larger int[][] grid
    private static int part2WithoutAdjList(int[][] grid) {
        Set<GridPoint> visited = new HashSet<>();

        // Tracks the distance between the source point (0,0) to all other points
        Map<GridPoint, Integer> distances = new HashMap<>();
        initializeDataStructures(distances, SIDE_LENGTH * 5);

        // Create a priority queue of (node, distance) pairs with an initial capacity and a comparator
        // to give priority to nodes with smallest distances so far
        Comparator<PQNode> nodeDistanceComparator = new NodeDistanceComparator();
        PriorityQueue<PQNode> queue = new PriorityQueue<>(SIDE_LENGTH * 5 * 2, nodeDistanceComparator);
        queue.add(new PQNode(new GridPoint(0, 0), 0));

        while (!queue.isEmpty()) {
            PQNode current = queue.poll();
            GridPoint currentPoint = current.getPoint();

            if (visited.contains(currentPoint)) continue;

            // Add the point to our visited set indicating that this vertex
            // has been visited.
            visited.add(currentPoint);

            List<GridPoint> neighbors = getNeighbors(grid, currentPoint);

            // For each neighbor, if the distance of the source node to the current node (dist[current]) plus
            // the weight of the edge from the current node to the neighbor (weight[current, neighbor])
            // is less than the best recorded distance of the source node to the neighbor (dist[neighbor]) already,
            // we update the distance to the neighbor to the new minimum distance value.
            // Otherwise, no updates are made.
            // In other words, we update if: dist[current] + weight[current,neighbor] < dist[neighbor]
            for (GridPoint neighbor : neighbors) {
                int newDistance = distances.get(currentPoint) + grid[neighbor.x][neighbor.y];
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    queue.add(new PQNode(neighbor, newDistance));
                }
            }
        }

        return distances.get(new GridPoint(SIDE_LENGTH * 5 - 1, SIDE_LENGTH * 5 - 1));
    }
}
