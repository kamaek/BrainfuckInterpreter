import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Scanner;

public class BrainFuck {

    // вторник 14:00

    public static void main(String[] args) {
        String bfHelloWorld =
                "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++" +
                        ".>+.+++++++..+++.>++.<<+++++++++++++++.>.+++." +
                        "------.--------.>+.>.";
        BrainFuck.interpretAndExecute(bfHelloWorld);
    }

    static final int defaultMemSize = 30000;
    static int[] memory = new int[defaultMemSize];
    static int curPos = 0;
    static ArrayList<Command> recentCommands = new ArrayList<Command>();

    static void interpret(String programCode) {
        //recentCommands.clear();
        for (int curSymbPos = 0; curSymbPos < programCode.length(); curSymbPos++) {
            switch (programCode.charAt(curSymbPos)) {
                case '+':
                    recentCommands.add(Operation.PLUS);
                    break;
                case '-':
                    recentCommands.add(Operation.MINUS);
                    break;
                case '<':
                    recentCommands.add(Operation.PREVIOUS);
                    break;
                case '>':
                    recentCommands.add(Operation.NEXT);
                    break;
                case '.':
                    recentCommands.add(Operation.PRINT);
                    break;
                case ',':
                    recentCommands.add(Operation.INPUT);
                    break;
                case '[':
                    recentCommands.add(Operation.LOOP_START);
                    break;
                case ']':
                    recentCommands.add(Operation.LOOP_END);
                    break;
            }
        }
    }

    static void execute() {
        for (Command cmd : recentCommands)
            cmd.execute();
    }

    static void interpretAndExecute(String programCode) {
        interpret(programCode);
        execute();
    }

    enum Operation implements Command {
        PLUS('+') {
            public void execute() {
                memory[curPos]++;
            }
        },
        MINUS('-') {
            public void execute() {
                memory[curPos]--;
            }
        },
        NEXT('>') {
            public void execute() {
                if (curPos == memory.length)
                    throw new IndexOutOfBoundsException("index is higher than memory size");
                else
                    curPos++;
            }
        },
        PREVIOUS('<') {
            public void execute() {
                if (curPos == 0)
                    throw new IndexOutOfBoundsException("index is less than 0");
                else
                    curPos--;
            }
        },
        PRINT('.') {
            public void execute() {
                System.out.print(memory[curPos]);
            }
        },
        INPUT(',') {
            public void execute() {
                Scanner input = new Scanner(System.in);
                memory[curPos] = Integer.parseInt(input.nextLine());
                input.close();
            }
        },
        LOOP_START('[') {
            public void execute() {
                if (memory[curPos] == 0) {
                    //curSymbPos = programCode.indexOf(']', curSymbPos);
                }
            }
        },
        LOOP_END(']') {
            public void execute() {
                recentCommands.
                //curSymbPos = programCode.lastIndexOf('[', curSymbPos);
                //curSymbPos--;
            }
        };

        char op;
        Operation(char op) {
            this.op = op;
        }
    }

    interface Command {
        void execute();
    }
}

/*
class Plus implements Command {
        public void execute() {
            memory[curPos]++;
        }
    }

    class Minus implements Command {
        public void execute() {
            memory[curPos]--;
        }
    }

    class Next implements Command {
        public void execute() {
            if (curPos == memory.length)
                throw new IndexOutOfBoundsException("index is higher than memory size");
            else
                curPos++;
        }
    }

    class Previous implements Command {
        public void execute() {
            if (curPos == 0)
                throw new IndexOutOfBoundsException("index is less than 0");
            else
                curPos--;
        }
    }

    class Print implements Command {
        public void execute() {
            System.out.print(Character.toChars(memory[curPos]));
        }
    }

    class Input implements Command {
        public void execute() {
            Scanner input = new Scanner(System.in);
            memory[curPos] = Integer.parseInt(input.nextLine());
            input.close();
        }
    }

    class LoopStart implements Command {
        public void execute() {
            if (memory[curPos] == 0) {
                curSymbPos = programCode.indexOf(']', curSymbPos);
            }
        }
    }

    class LoopEnd implements Command {
        public void execute() {
            curSymbPos = programCode.lastIndexOf('[', curSymbPos);
            curSymbPos--;
        }
    }
 */
