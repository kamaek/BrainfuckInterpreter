import java.util.*;

public class BrainFuck {

    public static void main(String[] args) {
        String bfHelloWorld = "xyz++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>--xyz"
                + "-.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.xyz";
        BrainFuck.interpretAndExecute(bfHelloWorld);
    }

    private static final int defaultMemSize = 30000;
    private static int[] memory = new int[defaultMemSize];
    private static int memIndex;
    private static int cmdIndex;
    private static ArrayList<Command> commands = new ArrayList<Command>();

    public static void interpretAndExecute(String programCode) {
        interpret(programCode);
        execute();
    }

    private static void reset() {
        Arrays.fill(memory, 0);
        memIndex = 0;
        cmdIndex = 0;
        commands.clear();
    }

    private static void interpret(String programCode) {
        reset();
        programCode = Operation.deleteInvalidSymbols(programCode);
        for (int curSymbPos = 0; curSymbPos < programCode.length(); curSymbPos++) {
            commands.add(Operation.getCommand(programCode.charAt(curSymbPos)));
        }
    }

    private static void execute() {
        for (; cmdIndex < commands.size(); cmdIndex++) {
            commands.get(cmdIndex).execute();
        }
    }

    private enum Operation implements Command {
        PLUS('+') {
            public void execute() {
                memory[memIndex]++;
            }
        },
        MINUS('-') {
            public void execute() {
                memory[memIndex]--;
            }
        },
        NEXT('>') {
            public void execute() {
                if (memIndex >= memory.length)
                    throw new IndexOutOfBoundsException("index is higher than memory size");
                else
                    memIndex++;
            }
        },
        PREVIOUS('<') {
            public void execute() {
                if (memIndex < 0)
                    throw new IndexOutOfBoundsException("index is less than 0");
                else
                    memIndex--;
            }
        },
        PRINT('.') {
            public void execute() {
                System.out.print(Character.toChars(memory[memIndex]));
            }
        },
        INPUT(',') {
            public void execute() {
                Scanner input = new Scanner(System.in);
                memory[memIndex] = Integer.parseInt(input.nextLine());
                input.close();
            }
        },
        LOOP_START('[') {
            public void execute() {
                if (memory[memIndex] == 0)
                    toAnotherLoopEnd(Operation.LOOP_START);
            }
        },
        LOOP_END(']') {
            public void execute() {
                if (memory[memIndex] != 0)
                    toAnotherLoopEnd(Operation.LOOP_END);
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

        private static Command getCommand(char ch) {
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
            //indicate direction (forward - 1 or backward - -1) for movement among commands
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
                if (BrainFuck.commands.get(cmdIndex) == to) {
                    matching--;
                    if (matching == 0)
                        break;
                }
                if (BrainFuck.commands.get(cmdIndex) == from)
                    matching++;
            }
        }
    }

    interface Command {
        void execute();
    }
}
