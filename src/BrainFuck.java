import java.util.*;

public class BrainFuck {

    // вторник 14:00

    // 1. вложенные циклы
    // 2. loop_start/loop_end

    public static void main(String[] args) {
        String bfHelloWorld =
                "xyz++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++" +
                        ".>+.+++++++xyz..+++.>++.<<+++++++++++++++.>.+++." +
                        "------.--------.>+.>.xyz";
        BrainFuck.interpretAndExecute(bfHelloWorld);
    }

    static final int defaultMemSize = 30000;
    static int[] memory = new int[defaultMemSize];
    static int memIndex;
    static int cmdIndex;
    static ArrayList<Command> recentCommands = new ArrayList<Command>();

    public static void interpretAndExecute(String programCode) {
        interpret(programCode);
        execute();
    }

    private static void reset() {
        Arrays.fill(memory, 0);
        memIndex = 0;
        cmdIndex = 0;
        recentCommands.clear();
    }

    private static void interpret(String programCode) {
        reset();
        programCode = Operation.deleteInvalidSymbols(programCode);
        for (int curSymbPos = 0; curSymbPos < programCode.length(); curSymbPos++) {
            recentCommands.add(Operation.getCommand(programCode.charAt(curSymbPos)));
        }
    }

    private static void execute() {
        for (; cmdIndex < recentCommands.size(); cmdIndex++) {
            recentCommands.get(cmdIndex).execute();
        }
    }

    enum Operation implements Command {
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
                    throw new IndexOutOfBoundsException("index is less than 0 in cmd num "
                            + cmdIndex + ", while value: " + memory[0]);
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
                if (memory[memIndex] == 0) {
                    //goto next loop_end command + 1
                    List<Command> subCommands = recentCommands.subList(cmdIndex, recentCommands.size());
                    cmdIndex = subCommands.indexOf(Operation.LOOP_END);
                }
            }
        },
        LOOP_END(']') {
            public void execute() {
                //goto prev loop_start command
                List<Command> subCommands = recentCommands.subList(0, cmdIndex);
                cmdIndex = subCommands.lastIndexOf(Operation.LOOP_START);
                cmdIndex--;
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
                if (!validCharacter(builder.charAt(i))) {
                    builder.deleteCharAt(i);
                    i--;
                }
            }

            return builder.toString();
        }

        private static boolean validCharacter(char ch) {
            for (char validCh: validCharacters)
                if (validCh == ch)
                    return true;
            return false;
        }
    }

    interface Command {
        void execute();
    }
}
