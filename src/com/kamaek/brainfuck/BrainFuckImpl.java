package com.kamaek.brainfuck;

import com.kamaek.brainfuck.commands.*;
import com.kamaek.brainfuck.translator.ToJavaScriptTranslator;
import com.kamaek.brainfuck.translator.Translator;

import java.util.*;

public class BrainFuckImpl implements Interpreter {

    public static void main(String[] args) {
        String bfHelloWorld = "xyz++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>--xyz"
                + "-.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.xyz";

        Interpreter brainFuck = new BrainFuckImpl();
        Translator translator = new ToJavaScriptTranslator();

        List<VisitableCommand> commands = brainFuck.prepareCommands(bfHelloWorld);
        brainFuck.execute(commands);
        System.out.print(translator.translate(commands));
    }

    @Override
    public void execute(List<VisitableCommand> commands) {
        executeCommands(commands);
    }

    @Override
    public List<VisitableCommand> prepareCommands(String programCode) {
        return parseCode(programCode);
    }

    private void executeCommands(List<VisitableCommand> commands) {
        Memory memory = new Memory();
        for (VisitableCommand cmd: commands) {
            cmd.execute(memory);
        }
    }

    private List<VisitableCommand> parseCode(String programCode) {
        List<VisitableCommand> commands = new ArrayList<VisitableCommand>();

        for (int curSymbPos = 0; curSymbPos < programCode.length(); curSymbPos++) {
            switch (programCode.charAt(curSymbPos)) {
                case '+':
                    commands.add(new Plus());
                    break;
                case '-':
                    commands.add(new Minus());
                    break;
                case '>':
                    commands.add(new NextCell());
                    break;
                case '<':
                    commands.add(new PreviousCell());
                    break;
                case '.':
                    commands.add(new Print());
                    break;
                case ',':
                    commands.add(new Input());
                    break;
                case '[':
                    int loopStart = curSymbPos + 1;
                    int loopEnd;

                    for (int matching = 1; matching != 0;) {
                        curSymbPos++;
                        if (programCode.charAt(curSymbPos) == '[') {
                            matching++;
                        }
                        else if (programCode.charAt(curSymbPos) == ']') {
                            matching--;
                        }
                    }
                    loopEnd = curSymbPos;

                    List<VisitableCommand> loopCommands = parseCode(programCode.substring(loopStart, loopEnd));
                    commands.add(new Loop(loopCommands));
                    break;
            }
        }

        return commands;
    }

}


