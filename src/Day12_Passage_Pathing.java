import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day12_Passage_Pathing {
    private static Map<String, List<String>> adjList;

    public static void main(String[] args) {
        adjList = new HashMap<>();
        File file = new File("./inputs/day12/day12.txt");

        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] tokens = line.split("-");
                String cave1 = tokens[0].trim();
                String cave2 = tokens[1].trim();

                // Add an edge from cave1 to cave2 and an edge from cave2 to cave1.
                // Adds edge to an existing list or creates a new list of one doesn't already exist.
                adjList.computeIfAbsent(cave1, k -> new ArrayList<>()).add(cave2);
                adjList.computeIfAbsent(cave2, k -> new ArrayList<>()).add(cave1);

                // Ensure that the "end" cave is considered terminal and there are no outgoing edges from it.
                // Also ensure that the "start" cave has no incoming edges

                adjList.remove("end");
                for (String cave : adjList.keySet()) {
                    List<String> neighbors = adjList.get(cave);
                    neighbors.remove("start");
                    adjList.put(cave, neighbors);
                }


            }

            Set<String> smallCaveVisited = new HashSet<>(); // Set to keep track of the small caves we've visited
            List<String> path = new ArrayList<>(); // Holds the path so far
            path.add("start");
            smallCaveVisited.add("start");

            // For Part 1:
            int pathCount = 0;
            pathCount = part1Recursive("start", "end", smallCaveVisited, path, pathCount);
            System.out.println("Part 1 (recursive) is: " + pathCount);

            // For Part 2:
            int pathCount2 = 0;
            Map<String, Integer> smallCavesTracker = new HashMap<>();

            pathCount2 = part2Recursive(
                    "start",
                    "end",
                    smallCavesTracker,
                    path,
                    pathCount2);
            System.out.println("Part 2 (recursive) is: " + pathCount2);

            System.out.println("Part 1 (iterative) is: " + part1Iterative());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Returns true if the given cave is a small cave.
    // Returns false otherwise.
    // It is sufficient to just check if the first character is lowercase.
    private static boolean isSmallCave(String cave) {
        return Character.isLowerCase(cave.charAt(0));
    }

    // Part 1 - Recursive backtracking algorithm. Each small cave can be visited at most once.
    private static int part1Recursive(String curr, String dest, Set<String> smallCavesVisited, List<String> path, int pathCount) {
        // If we've reached our destination, increment the path and return it.
        if (curr.equals(dest)) {
            pathCount += 1;
            return pathCount;
        }

        if (isSmallCave(curr)) {
            smallCavesVisited.add(curr); // mark the current cave as visited
        }

        List<String> neighbors = adjList.get(curr);

        for (String neighbor : neighbors) {
            // If we haven't visited this small cave yet...
            if (!smallCavesVisited.contains(neighbor)) {
                path.add(neighbor);
                pathCount = part1Recursive(neighbor, dest, smallCavesVisited, path, pathCount);
                path.remove(path.size() - 1);
            }
        }

        smallCavesVisited.remove(curr);

        return pathCount;
    }

    // Returns true if no 2s are found in the cave tracker map
    // Returns false otherwise
    private static boolean noTwos(Map<String, Integer> smallCavesTracker) {
        for (Integer i : smallCavesTracker.values()) {
            if (i == 2) {
                return false;
            }
        }

        return true;
    }

    // Returns true if the given cave can be visited (map does not contain it, or it has
    // only been visited once and there is no other cave that was visited twice).
    // Returns false otherwise.
    private static boolean caveCanBeVisited(Map<String, Integer> smallCavesTracker, String cave) {
        if (!smallCavesTracker.containsKey(cave)) {
            return true;
        }

        if (smallCavesTracker.get(cave) == 1 && noTwos(smallCavesTracker)) {
            return true;
        }

        return false;
    }

    // Part 2 - Recursive backtracking algorithm. A single small cave can be visited at most twice.
    // All other small caves can be visited at most once.
    private static int part2Recursive(
            String source,
            String dest,
            Map<String, Integer> smallCavesTracker,
            List<String> path,
            int pathCount) {

        // If we've reached our destination, increment the path and return it.
        if (source.equals(dest)) {
            pathCount += 1;
            return pathCount;
        }

        if (isSmallCave(source)) {
            // mark the current cave as visited by incrementing the number of times we've visited it
            if (smallCavesTracker.containsKey(source)) {
                Integer numVisits = smallCavesTracker.get(source);
                smallCavesTracker.put(source, numVisits + 1);
            } else {
                smallCavesTracker.put(source, 1);
            }
        }

        List<String> neighbors = adjList.get(source);

        for (String neighbor : neighbors) {
            // We can visit this cave if it is the first time we've done so OR
            // if we've already visited it once but no other caves have been visited twice.
            // Only one small cave can be visited twice. All others have to be once.
            if (caveCanBeVisited(smallCavesTracker, neighbor)) {
                path.add(neighbor);
                pathCount = part2Recursive(neighbor, dest, smallCavesTracker, path, pathCount);
                path.remove(path.size() - 1);
            }
        }

        if (smallCavesTracker.containsKey(source)) {
            Integer numVisits = smallCavesTracker.get(source);
            if (numVisits > 1) {
                smallCavesTracker.put(source, numVisits - 1);
            } else {
                smallCavesTracker.remove(source);
            }
        }

        return pathCount;
    }

    private static int part1Iterative() {
        int count = 0;
        Set<String> smallCavesVisited = new HashSet<>();
        smallCavesVisited.add("start");

        Stack<String> stack = new Stack<>();
        stack.push("start");

        // We need another stack to keep track of the state of the visited set
        // at various parts in the process
        Stack<Set<String>> visitedState = new Stack<>();
        visitedState.push(smallCavesVisited);

        while (!stack.isEmpty()) {
            String currentCave = stack.pop();
            Set<String> visited = visitedState.pop();

            if (currentCave.equals("end")) {
                count++;
                continue;
            }

            List<String> neighbors = adjList.get(currentCave);

            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    // Need to create a new set to capture the state
                    // of the set at this point in time
                    Set<String> newVisited = new HashSet<String>();
                    newVisited.addAll(visited);

                    if (isSmallCave(neighbor)) {
                        newVisited.add(neighbor);
                    }
                    stack.push(neighbor);
                    visitedState.push(newVisited);
                }
            }
        }

        return count;
    }
}
