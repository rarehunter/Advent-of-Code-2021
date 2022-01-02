import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day19_Beacon_Scanner {
    public static void main(String[] args) {
        File file = new File("./inputs/day19/day19.txt");

        try {
            Scanner sc = new Scanner(file);
            List<BeaconScanner> scanners = new ArrayList<>();
            BeaconScanner scanner = new BeaconScanner(); // scanner position is unknown

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals("")) {
                    scanners.add(scanner);
                    continue;
                }

                if (line.contains("scanner")) {
                    String[] tokens = line.split(" ");
                    scanner = new BeaconScanner(); // scanner position is unknown
                    scanner.updateName(tokens[1] + " " + tokens[2]);
                    continue;
                }

                String[] tokens = line.split(",");
                int x = Integer.parseInt(tokens[0]);
                int y = Integer.parseInt(tokens[1]);
                int z = Integer.parseInt(tokens[2]);

                scanner.addBeacon(x,y,z);
            }

            scanners.add(scanner);

            //int part1 = part1(scanners);
            //System.out.println("Part 1 is: " + part1);

            int part2 = part2(scanners);
            System.out.println("Part 2 is: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Returns true if at least 12 of the beacons of the reference scanner and the reoriented, normalized scanner
    // match up exactly. Returns false otherwise.
    private static boolean doScannersMatch(BeaconScanner referenceScanner, BeaconScanner normalizedScanner) {
        int numBeaconsMatch = 0;
        Set<Beacon> matchedBeacons = new HashSet<>();

        for (int i = 0; i < referenceScanner.beacons.size(); i++) {
            Beacon refBeacon = referenceScanner.beacons.get(i);
            for (int j = 0; j < normalizedScanner.beacons.size(); j++) {
                Beacon normalizedBeacon = normalizedScanner.beacons.get(j);
                if ((refBeacon.x-normalizedBeacon.x == refBeacon.y-normalizedBeacon.y) &&
                    (refBeacon.x-normalizedBeacon.x == refBeacon.z-normalizedBeacon.z)) {

                    numBeaconsMatch++;
                    matchedBeacons.add(normalizedBeacon);
                    break;
                }
            }

            if (numBeaconsMatch >= 12) break;
        }

        //System.out.println((numBeaconsMatch >= ) + " and " + matchedBeacons);
        return numBeaconsMatch >= 12;
    }

    // Return a tuple of (x,y,z) representing the difference between Beacons b1 and b2.
    // More specifically, returns the changes in x,y,z needed to make b2 equivalent to b1.
    private static Beacon findBeaconCoordDifference(Beacon b1, Beacon b2) {
        int xDiff;
        int yDiff;
        int zDiff;

        // If the x value of scanner1 is less than that of scanner2,
        // then we need to subtract something from the x's in scanner2's beacons.
        // Otherwise, we need to add something to x's in scanner2's beacons to get to scanner1.
        if (b1.x < b2.x) {
            xDiff = -(b2.x - b1.x);
        } else  {
            xDiff = (b1.x - b2.x);
        }

        if (b1.y < b2.y) {
            yDiff = -(b2.y - b1.y);
        } else  {
            yDiff = (b1.y - b2.y);
        }

        if (b1.z < b2.z) {
            zDiff = -(b2.z - b1.z);
        } else  {
            zDiff = (b1.z - b2.z);
        }

        return new Beacon(xDiff, yDiff, zDiff);
    }

    // Given a scanner and a tuple representing a diff (x,y,z), applies the diff to every
    // beacon in the scanner and returns a new scanner representing the normalized values.
    private static BeaconScanner normalizeScanner(BeaconScanner scanner, Beacon diff) {
        BeaconScanner normalized = new BeaconScanner();
        normalized.updateName(scanner.name);

        // Apply the diff x, y, and z values to every beacon in the scanner
        // to get a normalized beacon position
        for (int i = 0; i < scanner.beacons.size(); i++) {
            Beacon b = scanner.beacons.get(i);
            normalized.addBeacon(b.x + diff.x, b.y + diff.y, b.z + diff.z);
        }

        return normalized;
    }

    // Given a scanner and an id ranging from 0 to 23, returns a new scanner whose
    // beacon positions are one of 24 different orientations of the beacon in 3d space.
    private static BeaconScanner reorientScanner(BeaconScanner scanner, int reorientId) {
        BeaconScanner reorientedScanner = new BeaconScanner();
        reorientedScanner.updateName(scanner.name);

        for (Beacon b : scanner.beacons) {
            int x = b.x;
            int y = b.y;
            int z = b.z;

            if (reorientId == 0) {
                reorientedScanner.addBeacon(x,y,z);
            } else if (reorientId == 1) {
                reorientedScanner.addBeacon(-y,x,z);
            } else if (reorientId == 2) {
                reorientedScanner.addBeacon(-x,-y,z);
            } else if (reorientId == 3) {
                reorientedScanner.addBeacon(y,-x,z);
            } else if (reorientId == 4) {
                reorientedScanner.addBeacon(y,x,-z);
            } else if (reorientId == 5) {
                reorientedScanner.addBeacon(-x,y,-z);
            } else if (reorientId == 6) {
                reorientedScanner.addBeacon(-y,-x,-z);
            } else if (reorientId == 7) {
                reorientedScanner.addBeacon(x,-y,-z);
            } else if (reorientId == 8) {
                reorientedScanner.addBeacon(z,x,y);
            } else if (reorientId == 9) {
                reorientedScanner.addBeacon(x,-z,y);
            } else if (reorientId == 10) {
                reorientedScanner.addBeacon(-x,z,y);
            } else if (reorientId == 11) {
                reorientedScanner.addBeacon(-z,-x,y);
            } else if (reorientId == 12) {
                reorientedScanner.addBeacon(x,z,-y);
            } else if (reorientId == 13) {
                reorientedScanner.addBeacon(-x,-z,-y);
            } else if (reorientId == 14) {
                reorientedScanner.addBeacon(-z,x,-y);
            } else if (reorientId == 15) {
                reorientedScanner.addBeacon(z,-x,-y);
            } else if (reorientId == 16) {
                reorientedScanner.addBeacon(-y,-z,x);
            } else if (reorientId == 17) {
                reorientedScanner.addBeacon(z,-y,x);
            } else if (reorientId == 18) {
                reorientedScanner.addBeacon(y,z,x);
            } else if (reorientId == 19) {
                reorientedScanner.addBeacon(-z,y,x);
            } else if (reorientId == 20) {
                reorientedScanner.addBeacon(z,y,-x);
            } else if (reorientId == 21) {
                reorientedScanner.addBeacon(-y,z,-x);
            } else if (reorientId == 22) {
                reorientedScanner.addBeacon(-z,-y,-x);
            } else if (reorientId == 23) {
                reorientedScanner.addBeacon(y,-z,-x);
            }
        }

        return reorientedScanner;
    }

    // Run an algorithm to find the locations of all the scanners and beacons relative to the first scanner.
    private static ScannerBeaconInfo findScannersAndBeacons(List<BeaconScanner> scanners) {
        // Contains the positions of each beacon and scanner relative to scanner 0
        Set<Beacon> knownBeacons = new HashSet<>();
        Set<BeaconScanner> knownScanners = new HashSet<>();

        // Treat the first scanner as scanner 0, set its position to (0,0,0),
        // and add it to our set of known scanners.
        BeaconScanner scanner0 = scanners.get(0);
        scanner0.updatePosition(0,0,0);
        knownScanners.add(scanner0);
        scanners.remove(scanner0);

        knownBeacons.addAll(scanner0.beacons);

        // If we haven't deduced the position of each scanner, we compare the next unknown scanner
        // with each known scanner.
        while (scanners.size() > 0) {
            BeaconScanner unknownScanner = scanners.remove(0);

            boolean foundMatch = false;

            outerloop:
            for (BeaconScanner referenceScanner : knownScanners) {
                // Try every rotation and direction for the unknown scanner
                for (int orientation = 0; orientation < 24; orientation++) {
                    BeaconScanner reorientedScanner = reorientScanner(unknownScanner, orientation);

                    // For every pair of beacon coordinates between the reference scanner and the unknown scanner,
                    // make the beacon coordinates of unknown scanner match up to that of the reference scanner.
                    for (int i = 0; i < referenceScanner.beacons.size(); i++) {
                        Beacon refBeacon = referenceScanner.beacons.get(i);
                        for (int j = 0; j < reorientedScanner.beacons.size(); j++) {
                            Beacon reorientedBeacon = reorientedScanner.beacons.get(j);
                            Beacon diff = findBeaconCoordDifference(refBeacon, reorientedBeacon);

                            BeaconScanner normalizedScanner = normalizeScanner(reorientedScanner, diff);

                            // Now check if the reference scanner and the normalized scanner
                            // contain at least 12 beacons that match.
                            if (doScannersMatch(referenceScanner, normalizedScanner)) {
                                foundMatch = true;

                                // Set the position of this normalized scanner relative to the reference scanner.
                                normalizedScanner.updatePosition(diff.x, diff.y, diff.z);
                                knownScanners.add(normalizedScanner);
                                knownBeacons.addAll(normalizedScanner.beacons);
                                break outerloop;
                            }
                        }
                    }
                }
            }

            // Found no match for the unknown scanner so add it back into our list
            // in case it matches with known scanners in the future.
            if (!foundMatch)
                scanners.add(unknownScanner);
        }

        return new ScannerBeaconInfo(knownBeacons, knownScanners);
    }

    // Part 1: Runs through the algorithm to find the locations of the scanners and beacons.
    // Returns the number of unique beacons visible by all the given scanners.
    private static int part1(List<BeaconScanner> scanners) {
        ScannerBeaconInfo sbi = findScannersAndBeacons(scanners);
        return sbi.knownBeacons.size();
    }

    // Returns the Manhattan distance between two scanners.
    // Manhattan distance = |x1-x2| + |y1-y2|
    private static int findManhattanDistance(BeaconScanner s1, BeaconScanner s2) {
        return Math.abs(s1.x-s2.x) + Math.abs(s1.y-s2.y) + Math.abs(s1.z-s2.z);
    }

    // Part 2: Runs through the algorithm to find the locations of the scanners and beacons again.
    // Then taking the set of known scanner positions, runs through each pair of scanners and finding
    // the max Manhattan distance between them.
    private static int part2(List<BeaconScanner> scanners) {
        ScannerBeaconInfo sbi = findScannersAndBeacons(scanners);
        int maxDistance = 0;

        for (BeaconScanner s1 : sbi.knownScanners) {
            for (BeaconScanner s2 : sbi.knownScanners) {
                int distance = findManhattanDistance(s1, s2);
                if (distance > maxDistance)
                    maxDistance = distance;
            }
        }

        return maxDistance;
    }

    // Class that bundles the set of known scanners and the set of known beacons.
    static class ScannerBeaconInfo {
        Set<Beacon> knownBeacons = new HashSet<>();
        Set<BeaconScanner> knownScanners = new HashSet<>();

        public ScannerBeaconInfo(Set<Beacon> knownBeacons, Set<BeaconScanner> knownScanners) {
            this.knownBeacons = knownBeacons;
            this.knownScanners = knownScanners;
        }
    }

    // Class that represents a Beacon
    static class Beacon {
        int x;
        int y;
        int z;

        public Beacon(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void updatePosition(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public String toString() {
            return "B(" + this.x + "," + this.y + "," + this.z + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Beacon beacon = (Beacon) o;
            return x == beacon.x && y == beacon.y && z == beacon.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    // Class that represents a Scanner.
    static class BeaconScanner{
        String name;
        int x;
        int y;
        int z;
        List<Beacon> beacons;

        public BeaconScanner() {
            this.name = "";
            this.x = -1;
            this.y = -1;
            this.z = -1;
            this.beacons = new ArrayList<>();

        }
        public BeaconScanner(String name, int x, int y, int z) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
            this.beacons = new ArrayList<>();
        }

        public void addBeacon(int x, int y, int z) {
            Beacon b = new Beacon(x, y, z);
            beacons.add(b);
        }
        public void updateName(String name) { this.name = name; }
        public void updatePosition(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public String toString() {
            return this.name + "(" + this.x + "," + this.y + "," + this.z + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BeaconScanner scanner = (BeaconScanner) o;
            return x == scanner.x && y == scanner.y && z == scanner.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
}
