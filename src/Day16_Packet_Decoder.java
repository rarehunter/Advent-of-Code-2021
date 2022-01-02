import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day16_Packet_Decoder {
    private static final int TYPE_ID_SUM = 0;
    private static final int TYPE_ID_PRODUCT = 1;
    private static final int TYPE_ID_MIN = 2;
    private static final int TYPE_ID_MAX = 3;
    private static final int TYPE_ID_LITERAL = 4;
    private static final int TYPE_ID_GREATER_THAN = 5;
    private static final int TYPE_ID_LESS_THAN = 6;
    private static final int TYPE_ID_EQUAL_TO = 7;

    public static void main(String[] args) {
        File file = new File("./inputs/day16/day16.txt");
        Map<Character, String> hexToBin = generateHexToBinMap();

        try {
            Scanner sc = new Scanner(file);
            String line = "";

            while (sc.hasNextLine()) {
                line = sc.nextLine();
            }

            // We can estimate the amount of bits this hex string will take so that
            // we can set the initial capacity of the BitSet
            BitSet bs = new BitSet(line.length() * 4);

            // Iterate through the hex string, converting each character to its binary equivalent
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                String binary = hexToBin.get(c);

                bs.set(4*i, binary.charAt(0) == '1');
                bs.set(4*i+1, binary.charAt(1) == '1');
                bs.set(4*i+2, binary.charAt(2) == '1');
                bs.set(4*i+3, binary.charAt(3) == '1');
            }

            //int part1 = part1(bs);
            //System.out.println("Part 1 is: " + part1);

            long part2 = part2(bs);
            System.out.println("Part 2 is: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void printBitSet(BitSet bs) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bs.length();  i++) {
            sb.append(bs.get(i) == true ? 1: 0 );
        }

        System.out.println(sb.toString());
    }

    private static Map<Character, String> generateHexToBinMap() {
        Map<Character, String> hexToBin = new HashMap<>();
        hexToBin.put('0', "0000");
        hexToBin.put('1', "0001");
        hexToBin.put('2', "0010");
        hexToBin.put('3', "0011");
        hexToBin.put('4', "0100");
        hexToBin.put('5', "0101");
        hexToBin.put('6', "0110");
        hexToBin.put('7', "0111");
        hexToBin.put('8', "1000");
        hexToBin.put('9', "1001");
        hexToBin.put('A', "1010");
        hexToBin.put('B', "1011");
        hexToBin.put('C', "1100");
        hexToBin.put('D', "1101");
        hexToBin.put('E', "1110");
        hexToBin.put('F', "1111");
        return hexToBin;
    }

    private static int readVersion(BitSet bs, int packetStartIndex) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            boolean bit = bs.get(packetStartIndex+i);
            sb.append(bit ? "1" : "0");
        }

        return Integer.parseInt(sb.toString(), 2);
    }

    private static int readTypeId(BitSet bs, int packetStartIndex) {
        StringBuilder sb = new StringBuilder();

        for (int i = 3; i < 6; i++) {
            boolean bit = bs.get(packetStartIndex+i);
            sb.append(bit ? "1" : "0");
        }

        return Integer.parseInt(sb.toString(), 2);
    }

    // Literal value packets encode a single binary number.
    private static PacketData readLiteralValuePacket(BitSet bs, int packetStartIndex) {
        StringBuilder sb = new StringBuilder();
        int lastBitIndex = 0;

        // Iterate through the literal value one chunk at a time (chunk size of 5)
        // The first bit of a chunk is a prefix bit. Each chunk is prefixed by a 1 except for the last
        // chunk which is prefixed by 0.
        for (int i = 6; i < bs.size(); i += 5) {
            boolean prefixBit = bs.get(i + packetStartIndex);
            boolean bit1 = bs.get(i+1 + packetStartIndex);
            boolean bit2 = bs.get(i+2 + packetStartIndex);
            boolean bit3 = bs.get(i+3 + packetStartIndex);
            boolean bit4 = bs.get(i+4 +  packetStartIndex);

            sb.append(bit1 ? "1" : "0");
            sb.append(bit2 ? "1" : "0");
            sb.append(bit3 ? "1" : "0");
            sb.append(bit4 ? "1" : "0");

            // Our prefix bit is 0 so we need to end after this chunk
            if (!prefixBit) {
                lastBitIndex = i+4 + packetStartIndex;
                break;
            }
        }

        int version = readVersion(bs, packetStartIndex);
        long literalValue = Long.parseLong(sb.toString(), 2);
        return new PacketData(version, lastBitIndex, literalValue);
    }

    private static Long calculateValue(int typeId, List<Long> values) {
        long calculatedValue = 0;

        if (typeId == TYPE_ID_SUM) {
            calculatedValue = values.stream().mapToLong(a -> a).sum();
        } else if (typeId == TYPE_ID_PRODUCT) {
            long product = 1;
            for (Long value : values) {
                product *= value;
            }
            calculatedValue = product;
        } else if (typeId == TYPE_ID_MIN) {
            calculatedValue = values.stream().mapToLong(a -> a).min().orElse(Long.MAX_VALUE);
        } else if (typeId == TYPE_ID_MAX) {
            calculatedValue = values.stream().mapToLong(a -> a).max().orElse(Long.MIN_VALUE);
        } else if (typeId == TYPE_ID_GREATER_THAN) {
            if (values.get(0) > values.get(1)) calculatedValue = 1L;
            else calculatedValue = 0L;
        } else if (typeId == TYPE_ID_LESS_THAN) {
            if (values.get(0) < values.get(1)) calculatedValue = 1L;
            else calculatedValue = 0L;
        } else if (typeId == TYPE_ID_EQUAL_TO) {
            if (values.get(0).equals(values.get(1))) calculatedValue = 1L;
            else calculatedValue = 0L;
        }

        return calculatedValue;
    }

    private static PacketData processSubpacketsUsingTotalLength(BitSet bs, int version, int packetStartIndex, int typeId) {
        int versionSum = 0;
        int endingIndex = 0;

        StringBuilder sb = new StringBuilder();
        for (int i = packetStartIndex + 8; i < packetStartIndex + 22; i++) {
            sb.append(bs.get(i) ? "1" : "0");
        }
        int totalLength = Integer.parseInt(sb.toString(), 2);

        int cumulativeLengthOfPrevPackets = 0;
        List<Long> subpacketValues = new ArrayList<>();

        while (totalLength > 0) {
            PacketData pd = readPacket(bs, packetStartIndex+22+cumulativeLengthOfPrevPackets);
            int lengthOfPreviousPacket = pd.endingIndex - (packetStartIndex+22+cumulativeLengthOfPrevPackets) + 1;
            versionSum += pd.versionSum;
            endingIndex = pd.endingIndex;
            cumulativeLengthOfPrevPackets += lengthOfPreviousPacket;
            totalLength -= lengthOfPreviousPacket;
            subpacketValues.add(pd.value);
        }

        long calculatedValue = calculateValue(typeId, subpacketValues);

        return new PacketData(versionSum + version, endingIndex, calculatedValue);
    }

    private static PacketData processSubpacketsUsingPacketCount(BitSet bs, int version, int packetStartIndex, int typeId) {
        int versionSum = 0;
        int endingIndex = 0;

        StringBuilder sb = new StringBuilder();
        for (int i = packetStartIndex + 8; i < packetStartIndex + 18; i++) {
            sb.append(bs.get(i) ? "1" : "0");
        }

        int numSubpackets = Integer.parseInt(sb.toString(), 2);

        int cumulativeLengthOfPrevPackets = 0;
        List<Long> subpacketValues = new ArrayList<>();
        while (numSubpackets > 0) {
            PacketData pd = readPacket(bs, packetStartIndex+18+cumulativeLengthOfPrevPackets);
            int lengthOfPreviousPacket = pd.endingIndex - (packetStartIndex+18+cumulativeLengthOfPrevPackets) + 1;
            versionSum += pd.versionSum;
            endingIndex = pd.endingIndex;
            cumulativeLengthOfPrevPackets += lengthOfPreviousPacket;
            subpacketValues.add(pd.value);

            numSubpackets--;
        }

        long calculatedValue = calculateValue(typeId, subpacketValues);

        return new PacketData(versionSum + version, endingIndex, calculatedValue);
    }

    // Packets with type id != 4
    private static PacketData readOperatorPacket(BitSet bs, int packetStartIndex, int typeId) {
        int version = readVersion(bs, packetStartIndex);

        int lengthTypeId = bs.get(packetStartIndex + 6) ? 1 : 0;

        // Next 15 bits are a number that represents the total length
        // in bits of the sub-packets contained by this packet.
        if (lengthTypeId == 0) {
            return processSubpacketsUsingTotalLength(bs, version, packetStartIndex, typeId);
        } else { // Next 11 bits represents the number of sub-packets.
            return processSubpacketsUsingPacketCount(bs, version, packetStartIndex, typeId);
        }
    }

    private static PacketData readPacket(BitSet bs, int packetStartIndex) {
        int typeId = readTypeId(bs, packetStartIndex);

        PacketData pd;

        // For typeId 4, the packet contains a literal value.
        // Otherwise, it is an operator packet which can contain sub-packets.
        if (typeId == TYPE_ID_LITERAL) {
            pd = readLiteralValuePacket(bs, packetStartIndex);
        } else {
            pd = readOperatorPacket(bs, packetStartIndex, typeId);
        }

        return new PacketData(pd.versionSum, pd.endingIndex, pd.value);
    }

    // Part 1
    private static int part1(BitSet bs) {
        PacketData pd =  readPacket(bs, 0);
        return pd.versionSum;
    }

    // Part 2
    private static long part2(BitSet bs) {
        PacketData pd = readPacket(bs, 0);
        return pd.value;
    }

    static class PacketData {
        public int versionSum;
        public int endingIndex;
        public long value;

        public PacketData(int versionSum, int endingIndex, long value) {
            this.versionSum = versionSum;
            this.endingIndex = endingIndex;
            this.value = value;
        }
    }
}
