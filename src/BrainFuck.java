import java.util.ArrayList;
import java.util.Scanner;

public class BrainFuck {

    // вторник 14:00

    // 1. вложенные циклы
    // 2. loop_start/loop_end
    // 3. static fields reinitialization

    public static void main(String[] args) {
        String bfHelloWorld =
                "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++" +
                        ".>+.+++++++..+++.>++.<<+++++++++++++++.>.+++." +
                        "------.--------.>+.>.";
        BrainFuck.interpretAndExecute(bfHelloWorld);
    }

    static final int defaultMemSize = 30000;
    static int[] memory = new int[defaultMemSize];
    static int memPosition = 0;
    static ArrayList<Command> recentCommands = new ArrayList<Command>();

    static void interpret(String programCode) {
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
        for (Command cmd : recentCommands) {
            cmd.execute();
        }
    }

    static void interpretAndExecute(String programCode) {
        interpret(programCode);
        execute();
    }

    enum Operation implements Command {
        PLUS('+') {
            public void execute() {
                memory[memPosition]++;
            }
        },
        MINUS('-') {
            public void execute() {
                memory[memPosition]--;
            }
        },
        NEXT('>') {
            public void execute() {
                if (memPosition >= memory.length)
                    throw new IndexOutOfBoundsException("index is higher than memory size");
                else
                    memPosition++;
            }
        },
        PREVIOUS('<') {
            public void execute() {
                if (memPosition < 0)
                    throw new IndexOutOfBoundsException("index is less than 0");
                else
                    memPosition--;
            }
        },
        PRINT('.') {
            public void execute() {
                System.out.print(Character.toChars(memory[memPosition]));
            }
        },
        INPUT(',') {
            public void execute() {
                Scanner input = new Scanner(System.in);
                memory[memPosition] = Integer.parseInt(input.nextLine());
                input.close();
            }
        },
        LOOP_START('[') {
            public void execute() {
                if (memory[memPosition] != 0) {

                }
            }
        },
        LOOP_END(']') {
            public void execute() {

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
