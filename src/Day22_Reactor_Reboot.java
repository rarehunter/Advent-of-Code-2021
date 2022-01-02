import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day22_Reactor_Reboot {
    public static void main(String[] args) {
        File file = new File("./inputs/day22/day22.example3.txt");

        try {
            Scanner sc = new Scanner(file);
            List<Step> instructions = new ArrayList<>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] tokens = line.trim().split(" ");
                boolean on = tokens[0].equals("on");
                String[] bounds = tokens[1].split(",");
                String[] xBounds = bounds[0].substring(2).split("[.][.]");
                String[] yBounds = bounds[1].substring(2).split("[.][.]");
                String[] zBounds = bounds[2].substring(2).split("[.][.]");
                int xLow = Integer.parseInt(xBounds[0]);
                int xHigh = Integer.parseInt(xBounds[1]);
                int yLow = Integer.parseInt(yBounds[0]);
                int yHigh = Integer.parseInt(yBounds[1]);
                int zLow = Integer.parseInt(zBounds[0]);
                int zHigh = Integer.parseInt(zBounds[1]);

                Step s = new Step(on, xLow, xHigh, yLow, yHigh, zLow, zHigh);
                instructions.add(s);
            }

            int part1 = part1(instructions);
            System.out.println("Part 1 is: " + part1);

            long part2 = part2(instructions);
            System.out.println("Part 2 is: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Part 1: Count all the "on" cubes using a brute force way by storing each cube into a set.
    // We only consider bounds between -50 and 50.
    private static int part1(List<Step> instructions) {
        Set<Cube> onCubes = new HashSet<>();

        for (Step s : instructions) {
            for (int x = s.xLow; x <= s.xHigh; x++) {
                if (x < -50 || x > 50) continue;
                for (int y = s.yLow; y <= s.yHigh; y++) {
                    if (y < -50 || y > 50) continue;

                    for (int z = s.zLow; z <= s.zHigh; z++) {
                        if (z < -50 || z > 50) continue;

                        Cube c = new Cube(x,y,z);

                        if (s.on) onCubes.add(c);
                        else onCubes.remove(c);
                    }
                }
            }
        }

        return onCubes.size();
    }

    // Returns the range values of intersection between the given ranges r1 and r2.
    // Only calculates along one dimension.
    // Returns null if no intersection
    private static Range intersect1D(Range r1, Range r2) {
        if (r2.low > r1.high || r1.low > r2.high)
            return null; // no intersection

        int lowIntersect = Math.max(r1.low, r2.low);
        int highIntersect = Math.min(r1.high, r2.high);
        return new Range(lowIntersect, highIntersect);
    }

    // Returns a cuboid representing the intersection of cuboids c1 and c2.
    // Returns null if there is no intersection.
    private static Cuboid cuboidIntersect(Cuboid c1, Cuboid c2, int on) {
        Range xIntersect = intersect1D(c1.xRange, c2.xRange);
        Range yIntersect = intersect1D(c1.yRange, c2.yRange);
        Range zIntersect = intersect1D(c1.zRange, c2.zRange);

        if (xIntersect == null || yIntersect == null || zIntersect == null)
            return null;

        return new Cuboid(xIntersect, yIntersect, zIntersect, on);
    }

    // Part 2: This is done by implementing cube intersections as the inclusion-exclusion principle.
    // https://www.reddit.com/r/adventofcode/comments/rmaeh0/2021_day_22_suggestions_to_optimize_my_code/
    private static long part2(List<Step> instructions) {
        // Keeps track of all the cuboids represented by the instructions (both off and on)
        Set<Cuboid> visited = new HashSet<>();

        // For every instruction, construct a Cuboid object.
        for (Step s : instructions) {
            Range xRangeCurrent = new Range(s.xLow, s.xHigh);
            Range yRangeCurrent = new Range(s.yLow, s.yHigh);
            Range zRangeCurrent = new Range(s.zLow, s.zHigh);
            Cuboid current = new Cuboid(xRangeCurrent, yRangeCurrent, zRangeCurrent, s.on ? 1 : 0);

            Set<Cuboid> newCuboids = new HashSet<>();

            // For "on" instructions, we add the cuboid formed by the current instruction.
            if (s.on) {
                newCuboids.add(current);
            }

            // Compare the current cuboid to every other cuboid and intersection we've seen.
            for (Cuboid c : visited) {
                // We add in a cuboid representing the intersection and flip the "on" bit to
                // account for double counting cubes in intersection regions.
                Cuboid intersection = cuboidIntersect(c, current, -c.on);
                if (intersection != null) {
                    newCuboids.add(intersection);
                }
            }

            // Union
            visited.addAll(newCuboids);
        }

        // Finally, sum up all the areas, including subtracting and adding back in regions for double-counting.
        long numCubesOn = 0L;
        for (Cuboid c : visited) {
            numCubesOn += c.on * c.getVolume();
        }

        return numCubesOn;
    }

    // Class that represents a range of low and high values denoting the bounds of an axis of a cuboid.
    static class Range {
        int low;
        int high;

        public Range(int low, int high) {
            this.low = low;
            this.high = high;
        }

        public String toString() { return "(" + this.low + "," + this.high + ")"; }
    }

    // Class that represents a Cuboid object. A cuboid is made up of three ranges: one for x, y, and z.
    // It also contains an int denoting whether that cuboid is "on" and a method to calculate the volume.
    static class Cuboid {
        Range xRange;
        Range yRange;
        Range zRange;
        int on;

        public Cuboid (Range xRange, Range yRange, Range zRange, int on) {
            this.xRange = xRange;
            this.yRange = yRange;
            this.zRange = zRange;
            this.on = on;
        }

        public long getVolume() {
            return (long) (xRange.high-xRange.low+1) * (yRange.high-yRange.low+1) * (zRange.high-zRange.low+1);
        }

        public String toString() {
            return "x=" + this.xRange + ", y=" + this.yRange + ", z=" + this.zRange;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cuboid c = (Cuboid) o;
            return xRange.equals(c.xRange) && yRange.equals(c.yRange) && zRange.equals(c.zRange);
        }

        @Override
        public int hashCode() {
            return Objects.hash(xRange, yRange, zRange);
        }
    }

    // Class that represents an integer cube at (x,y,z).
    // A cube can either be on or off.
    static class Cube {
        int x;
        int y;
        int z;

        public Cube(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public String toString() { return "(" + this.x + "," + this.y + "," + this.z + ")"; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cube c = (Cube) o;
            return x == c.x && y == c.y && z == c.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    // Class that represents a line of instruction from the input text file.
    static class Step {
        boolean on;
        int xLow;
        int xHigh;
        int yLow;
        int yHigh;
        int zLow;
        int zHigh;

        public Step(boolean on, int xLow, int xHigh, int yLow, int yHigh, int zLow, int zHigh) {
            this.on = on;
            this.xLow = xLow;
            this.xHigh = xHigh;
            this.yLow = yLow;
            this.yHigh = yHigh;
            this.zLow = zLow;
            this.zHigh = zHigh;
        }

        public String toString() {
            return "(" + this.on + ": " + this.xLow + ".." + this.xHigh + ", " + this.yLow + ".." + this.yHigh + ", " + this.zLow + ".." + this.zHigh + ")";
        }
    }
}
