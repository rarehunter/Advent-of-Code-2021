import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Day18_Snailfish {
    public static void main(String[] args) {
        File file = new File("./inputs/day18/day18.example.txt");

        try {
            Scanner sc = new Scanner(file);
            List<Node> nodes = new ArrayList<>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Node newNode = constructNodes(line);
                nodes.add(newNode);
            }

            Node n = constructNodes("[[[[[9,8],1],2],3],4]");
            Node parentNode = findParentNodeNestedWithinFourPairs(n);
            explode(parentNode);
            System.out.println(n);

//            Node n = constructNodes("[7,[6,[5,[4,[3,2]]]]]");
//            Node n1 = new Node(n, 15);
//            System.out.println(n1);
//
//            System.out.println(reduceSnailFishNumber(n1));

            //int part1 = part1(nodes);
            //System.out.println("Part 1 is: " + part1);

            int part2 = part2();
            System.out.println("Part 2 is: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Given the string representation of a Snailfish Pair (which we'll call a Node),
    // constructs the Node object.
    private static Node constructNodes(String line) {
        Stack<Object> stack = new Stack<>();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            // If we encounter any other character but a closing bracket, we add it to the stack.
            // If we encounter a closing bracket, we pop the top 4 elements of the stack
            // which should be: a right node, a comma, a left node, and an open bracket.
            if (c != ']') {
                stack.push(c);
            }
            else {
                Object rightObject = stack.pop();
                stack.pop(); // pop the comma
                Object leftObject = stack.pop();
                stack.pop(); // pop the left bracket

                Node newNode = new Node(leftObject, rightObject);
/*
                // If it's just a single number, we want to create a node of it
                if (!(rightObject instanceof Node)) {
                    Character charRep = (Character)rightObject;
                    Node rightNode = new Node( null, null);
                    newNode.addRight(rightNode);
                } else {
                    newNode.addRight((Node)rightObject);
                }

                // If it's just a single number, we want to create a node of it
                if (!(leftObject instanceof Node)) {
                    Character charRep = (Character)leftObject;
                    Node leftNode = new Node(Character.toString(charRep), null, null);
                    newNode.addLeft(leftNode);
                } else {
                    newNode.addLeft((Node)leftObject);
                }
*/
                stack.push(newNode);
            }
        }

        // At this point, stack should contain a single node representing the whole tree.
        // Pop it and return it.
        return (Node)stack.pop();
    }

    private static Node findNodeNestedWithinFourPairs(Node n, int depth) {
        if (depth >= 4)
            return n;

        if (n.leftChild instanceof Node) {
            Node leftResult = findNodeNestedWithinFourPairs((Node)n.leftChild, depth+1);
            if (leftResult != null)
                return leftResult;
        }

        if (n.rightChild instanceof Node) {
            Node rightResult = findNodeNestedWithinFourPairs((Node)n.rightChild, depth+1);
            if (rightResult != null)
                return rightResult;
        }

        return null;
    }

    private static Node findParentNodeNestedWithinFourPairs(Node n) {
        return findNodeNestedWithinFourPairs(n, 1);
    }

    private static boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private static Node findNodeGreaterThanNine(Node n) {
        if (isParsable(n.leftChild.toString())) {
            int leftInt = Integer.parseInt(n.leftChild.toString());
            if (leftInt >= 9)
                return n;
        } else if (isParsable(n.rightChild.toString())) {
            int rightInt = Integer.parseInt(n.rightChild.toString());
            if (rightInt >= 9)
                return n;
        } else {
            Node leftResult = findNodeGreaterThanNine((Node)n.leftChild);
            if (leftResult != null)
                return leftResult;

            Node rightResult = findNodeGreaterThanNine((Node)n.rightChild);
            if (rightResult != null)
                return rightResult;
        }

        return null;
    }

    private static Node findLeftmostNodeInRightSubtree(Node parent) {
        Node n = (Node)parent.rightChild;

        while (!isParsable(n.leftChild.toString())) {
            n = (Node)n.leftChild;
        }

        return n;
    }

    private static Node findRightmostNodeInLeftSubtree(Node parent) {
        Node n = (Node)parent.leftChild;

        while (!isParsable(n.rightChild.toString())) {
            n = (Node)n.rightChild;
        }

        return n;
    }

    private static void explode(Node parentOfNodeToExplode) {
        if (parentOfNodeToExplode == null) return;

        int leftValue, rightValue;
        if (parentOfNodeToExplode.leftChild instanceof Node) {
            System.out.println("Left is node");
            leftValue = Integer.parseInt(((Node)parentOfNodeToExplode.leftChild).leftChild.toString());
            rightValue = Integer.parseInt(((Node)parentOfNodeToExplode.leftChild).rightChild.toString());
            parentOfNodeToExplode.leftChild = 0;
        } else if (parentOfNodeToExplode.rightChild instanceof Node) {
            System.out.println("Right is node");

            leftValue = Integer.parseInt(((Node)parentOfNodeToExplode.rightChild).leftChild.toString());
            rightValue = Integer.parseInt(((Node)parentOfNodeToExplode.rightChild).rightChild.toString());
            parentOfNodeToExplode.rightChild = 0;
        } else {
            // could not find the node to explode
            return;
        }

        System.out.println(parentOfNodeToExplode);

        // The left value is added to the first number to the left of the exploding node (if any)
        // The right value is added to the first number to the right of the exploding node (if any).
        // The exploding node is replaced with 0.

        // Add the right value to the left-most number in the right subtree
        if (isParsable(parentOfNodeToExplode.rightChild.toString())) {
            int leftMostValue = Integer.parseInt(parentOfNodeToExplode.rightChild.toString());
            parentOfNodeToExplode.rightChild = rightValue + leftMostValue;
        } else {
            Node leftMostNode = findLeftmostNodeInRightSubtree(parentOfNodeToExplode);
            if (leftMostNode != null) {
                int leftMostValue = Integer.parseInt(leftMostNode.leftChild.toString());
                leftMostNode.leftChild = rightValue + leftMostValue;
            }
        }

        // Add the left value to the right-most number in the left subtree
        if (isParsable(parentOfNodeToExplode.leftChild.toString())) {
            int rightMostValue = Integer.parseInt(parentOfNodeToExplode.leftChild.toString());
            parentOfNodeToExplode.leftChild = leftValue + rightMostValue;
        } else {
            Node rightMostNode = findRightmostNodeInLeftSubtree(parentOfNodeToExplode);

            if (rightMostNode != null) {
                int rightMostValue = Integer.parseInt(rightMostNode.rightChild.toString());
                rightMostNode.rightChild = leftValue + rightMostValue;
            }
        }

    }

    // Given a node to split that has left and right parts, check if the left or the right
    // contains a single integer that can be split. Replace that integer with a new Node.
    private static void split(Node nodeToSplit) {
        if (nodeToSplit == null) return;

        if (isParsable(nodeToSplit.leftChild.toString())) {
            int value = Integer.parseInt(nodeToSplit.leftChild.toString());
            if (value > 9) {
                int newleftvalue = (int)Math.floor((double)value / 2);
                int newrightvalue = (int)Math.ceil((double)value / 2);
                nodeToSplit.leftChild = new Node(newleftvalue, newrightvalue);
            }
        } else if (isParsable(nodeToSplit.rightChild.toString())) {
            int value = Integer.parseInt(nodeToSplit.rightChild.toString());
            if (value > 9) {
                int newleftvalue = (int)Math.floor((double)value / 2);
                int newrightvalue = (int)Math.ceil((double)value / 2);
                nodeToSplit.rightChild = new Node(newleftvalue, newrightvalue);
            }
        }
    }

    // Given a node representing a snail fish number, reduces it, and returns the result.
    // To reduce a snail fish number, these criteria are checked in order and repeated until no criteria applies:
    // 1. If any pair is nested inside of four pairs, the leftmost such pair explodes.
    // 2. If any regular number is 10 or greater, the leftmost such regular number splits.
    // If no action applies, the snail fish number is reduced.
    private static Node reduceSnailFishNumber(Node n) {
        while (true) {
            Node parentNode = findParentNodeNestedWithinFourPairs(n);
            if (parentNode != null) {
                explode(parentNode);
                continue;
            }

            Node leftPair2 = findNodeGreaterThanNine(n);
            if (leftPair2 != null) {
                split(leftPair2);
                continue;
            }

            break;
        }

        return n;
    }

    // Given two nodes representing snail fish numbers, adds them together and returns the result.
    private static Node addSnailFishNumbers(Node n1, Node n2) {
        Node added = new Node(n1, n2);
        return reduceSnailFishNumber(added);
    }

    // Part 1
    private static int part1(List<Node> nodes) {
        Node result;
        result = nodes.get(0);

        for (int i = 1; i < nodes.size(); i++) {
            Node added = addSnailFishNumbers(result, nodes.get(i));
            result = added;
        }

        return 0;
    }



    private static int part2() {
        return 0;
    }

    static class Node {
        Object leftChild;
        Object rightChild;
        Node parent;

        public Node(Object leftChild, Object rightChild) {
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        public void addLeft(Object leftChild) { this.leftChild = leftChild; }
        public void addRight(Object rightChild) { this.rightChild = rightChild; }
        public String toString() { return "[" + this.leftChild + "," + this.rightChild + "]"; }
    }
}
