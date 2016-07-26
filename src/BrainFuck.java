import java.util.*;

public class BrainFuck {

    public static void main(String[] args) {
        String bfHelloWorld = "xyz++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>--xyz"
                + "-.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.xyz";
        BrainFuck bf = new BrainFuck();
        bf.interpretAndExecute(bfHelloWorld);
        //System.out.print(BrainFuck.getJSCode(bfHelloWorld));
    }

    class Memory {
        private static final int defaultMemSize = 30000;
        private int[] memory = new int[defaultMemSize];
        private int memIndex;

        Memory() {
            memIndex = 0;
        }

        private void reset() {
            Arrays.fill(memory, 0);
            memIndex = 0;
        }

        public void incrementCurrentCell() {
            memory[memIndex]++;
        }

        public void decrementCurrentCell() {
            memory[memIndex]--;
        }

        public int getCurrentCellValue() {
            return memory[memIndex];
        }

        public void setCurrentCellValue(int val) {
            memory[memIndex] = val;
        }

        public void toNextCell() {
            if (memIndex >= memory.length)
                throw new IndexOutOfBoundsException("index is higher than memory size");
            else
                memIndex++;
        }

        public void toPrevCell() {
            if (memIndex < 0)
                throw new IndexOutOfBoundsException("index is less than 0");
            else
                memIndex--;
        }
    }

    Memory memory = new Memory();

    public void interpretAndExecute(String programCode) {
        ArrayList<Operation> commands = interpret(programCode);
        execute(commands);
    }

    private ArrayList<Operation> interpret(String programCode) {
        ArrayList<Operation> commands;
        programCode = Operation.deleteInvalidSymbols(programCode);
        commands = new ArrayList<Operation>(programCode.length());
        for (int curSymbPos = 0; curSymbPos < programCode.length(); curSymbPos++) {
            commands.add(Operation.getOperation(programCode.charAt(curSymbPos)));
        }
        return commands;
    }

    private void execute(ArrayList<Operation> commands) {
        memory.reset();
        for (Operation op: commands) {
            op.execute(memory);
        }
    }

    private enum Operation implements Command, JSable {
        PLUS('+') {
            public void execute(Memory mem) {
                mem.incrementCurrentCell();
            }
            public String getJSRepresentation() {
                return "memory[memIndex]++;";
            }
        },
        MINUS('-') {
            public void execute(Memory mem) {
                mem.decrementCurrentCell();
            }
            public String getJSRepresentation() {
                return "memory[memIndex]--;";
            }
        },
        NEXT('>') {
            public void execute(Memory mem) {
                mem.toNextCell();
            }
            public String getJSRepresentation() {
                return "memIndex++;";
            }
        },
        PREVIOUS('<') {
            public void execute(Memory mem) {
                mem.toPrevCell();
            }
            public String getJSRepresentation() {
                return "memIndex--;";
            }
        },
        PRINT('.') {
            public void execute(Memory mem) {
                System.out.print(Character.toChars(mem.getCurrentCellValue()));
            }
            public String getJSRepresentation() {
                return "document.write(String.fromCharCode(memory[memIndex]));";
            }
        },
        INPUT(',') {
            public void execute(Memory mem) {
                Scanner input = new Scanner(System.in);
                mem.setCurrentCellValue(Integer.parseInt(input.nextLine()));
                input.close();
            }
            public String getJSRepresentation() {
                return "memory[memIndex] = prompt(\"input value: \");";
            }
        },
        LOOP_START('[') {
            public void execute(Memory mem) {
                if (mem.getCurrentCellValue() == 0)
                    toAnotherLoopEnd(Operation.LOOP_START);
            }
            public String getJSRepresentation() {
                return "while (memory[memIndex] != 0) {";
            }
        },
        LOOP_END(']') {
            public void execute(Memory mem) {
                if (mem.getCurrentCellValue() != 0)
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


        //set operationIndex to the correct state
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
                operationIndex += direction;
                if (operations.get(operationIndex) == to) {
                    matching--;
                    if (matching == 0)
                        break;
                }
                if (operations.get(operationIndex) == from)
                    matching++;
            }
        }
    }

    interface Command {
        void execute(Memory mem);
    }

    interface JSable {
        String getJSRepresentation();
    }
}


