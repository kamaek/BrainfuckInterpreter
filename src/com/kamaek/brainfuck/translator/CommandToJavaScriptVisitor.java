package com.kamaek.brainfuck.translator;

import com.kamaek.brainfuck.CommandVisitor;
import com.kamaek.brainfuck.commands.*;

public class CommandToJavaScriptVisitor implements CommandVisitor<String> {

    @Override
    public String visit(Plus plus) {
        return "memory[memIndex]++;";
    }

    @Override
    public String visit(Minus minus) {
        return "memory[memIndex]--;";
    }

    @Override
    public String visit(NextCell nextCell) {
        return "memIndex++;";
    }

    @Override
    public String visit(PreviousCell previousCell) {
        return "memIndex--;";
    }

    @Override
    public String visit(Print print) {
        return "document.write(String.fromCharCode(memory[memIndex]));";
    }

    @Override
    public String visit(Input input) {
        return "memory[memIndex] = prompt(\"input value: \");";
    }

    @Override
    public String visit(Loop loop) {
        StringBuilder strBuilder = new StringBuilder("while (memory[memIndex] != 0) {\n");

        for (VisitableCommand cmd: loop.getCommands()) {
            strBuilder.append(cmd.accept(this));
            strBuilder.append("\n");
        }
        strBuilder.append("};");

        return strBuilder.toString();
    }
}
