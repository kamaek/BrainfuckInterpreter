import java.util.*;

public class BrainFuck {

    public static void main(String[] args) {
        String bfHelloWorld = "xyz++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>--xyz"
                + "-.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.xyz";
        BrainFuck.interpretAndExecute(bfHelloWorld);
        System.out.print(BrainFuck.getJSCode(bfHelloWorld));
    }

    private static final int defaultMemSize = 30000;
    private static int[] memory = new int[defaultMemSize];
    private static int memIndex;
    private static int cmdIndex;
    private static ArrayList<Operation> operations = new ArrayList<Operation>();

    public static void interpretAndExecute(String programCode) {
        interpret(programCode);
        execute();
    }

    public static String getJSCode(String programCode) {
        interpret(programCode);
        StringBuilder code = new StringBuilder(
                "memory = new Array(" + defaultMemSize + ");\n"
                        + "memory.fill(0);\n"
                        + "memIndex = 0;\n");

        for (JSable obj: operations) {
            code.append(obj.getJSRepresentation());
            code.append("\n");
        }

        return code.toString();
    }

    private static void reset() {
        Arrays.fill(memory, 0);
        memIndex = 0;
        cmdIndex = 0;
        operations.clear();
    }

    private static void interpret(String programCode) {
        reset();
        programCode = Operation.deleteInvalidSymbols(programCode);
        for (int curSymbPos = 0; curSymbPos < programCode.length(); curSymbPos++) {
            operations.add(Operation.getOperation(programCode.charAt(curSymbPos)));
        }
    }

    private static void execute() {
        for (; cmdIndex < operations.size(); cmdIndex++) {
            operations.get(cmdIndex).execute();
        }
    }

    private enum Operation implements Command, JSable {
        PLUS('+') {
            public void execute() {
                memory[memIndex]++;
            }
            public String getJSRepresentation() {
                return "memory[memIndex]++;";
            }
        },
        MINUS('-') {
            public void execute() {
                memory[memIndex]--;
            }
            public String getJSRepresentation() {
                return "memory[memIndex]--;";
            }
        },
        NEXT('>') {
            public void execute() {
                if (memIndex >= memory.length)
                    throw new IndexOutOfBoundsException("index is higher than memory size");
                else
                    memIndex++;
            }
            public String getJSRepresentation() {
                return "memIndex++;";
            }
        },
        PREVIOUS('<') {
            public void execute() {
                if (memIndex < 0)
                    throw new IndexOutOfBoundsException("index is less than 0");
                else
                    memIndex--;
            }
            public String getJSRepresentation() {
                return "memIndex--;";
            }
        },
        PRINT('.') {
            public void execute() {
                System.out.print(Character.toChars(memory[memIndex]));
            }
            public String getJSRepresentation() {
                return "document.write(String.fromCharCode(memory[memIndex]));";
            }
        },
        INPUT(',') {
            public void execute() {
                Scanner input = new Scanner(System.in);
                memory[memIndex] = Integer.parseInt(input.nextLine());
                input.close();
            }
            public String getJSRepresentation() {
                return "memory[memIndex]++;";
            }
        },
        LOOP_START('[') {
            public void execute() {
                if (memory[memIndex] == 0)
                    toAnotherLoopEnd(Operation.LOOP_START);
            }
            public String getJSRepresentation() {
                return "while (memory[memIndex] != 0) {";
            }
        },
        LOOP_END(']') {
            public void execute() {
                if (memory[memIndex] != 0)
                    toAnotherLoopEnd(Operation.LOOP_END);
            }
            public String getJSRepresentation() {
                return "};";
            }
        };

        private final char opSymb;
        private static final Map<Character, Operation> commands = new HashMap<Character, Operation>();
        private static final char[] validCharacters;

        static {
            for (Operation op: Operation.values()) {
                commands.put(op.opSymb, op);
            }
            validCharacters = new char[Operation.values().length];
            for (int i = 0; i < Operation.values().length; i++)
                validCharacters[i] = Operation.values()[i].opSymb;
        }

        private Operation(char symb) {
            this.opSymb = symb;
        }

        private static Operation getOperation(char ch) {
            return commands.get(ch);
        }

        private static String deleteInvalidSymbols(String programCode) {
            StringBuilder builder = new StringBuilder(programCode);

            for (int i = 0; i < builder.length(); i++) {
                if (!isValidCharacter(builder.charAt(i))) {
                    builder.deleteCharAt(i);
                    i--;
                }
            }

            return builder.toString();
        }

        private static boolean isValidCharacter(char ch) {
            for (char validCh: validCharacters)
                if (validCh == ch)
                    return true;
            return false;
        }


        //set cmdIndex to the correct state
        private static void toAnotherLoopEnd(Operation from) {
            if (!(from.equals(Operation.LOOP_END) || from.equals(Operation.LOOP_START)))
                throw new IllegalArgumentException("only Operation.LOOP_END or Operation.LOOP_START allowed");

            //function invoked when current command is LOOP_END or LOOP_START
            //so already there is one matching
            int matching = 1;
            //indicate direction (forward - 1 or backward - -1) for movement among operations
            int direction;
            Operation to;

            if (from == Operation.LOOP_START) {
                to = Operation.LOOP_END;
                direction = 1;
            } else {
                to = Operation.LOOP_START;
                direction = -1;
            }

            while (true) {
                cmdIndex += direction;
                if (BrainFuck.operations.get(cmdIndex) == to) {
                    matching--;
                    if (matching == 0)
                        break;
                }
                if (BrainFuck.operations.get(cmdIndex) == from)
                    matching++;
            }
        }
    }

    interface Command {
        void execute();
    }

    interface JSable {
        String getJSRepresentation();
    }
}
