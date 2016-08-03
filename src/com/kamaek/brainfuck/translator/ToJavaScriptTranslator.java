package com.kamaek.brainfuck.translator;

import com.kamaek.brainfuck.commands.VisitableCommand;

import java.util.List;

public class ToJavaScriptTranslator implements Translator {

    @Override
    public String translate(List<VisitableCommand> commands) {
        StringBuilder stringBuilder = new StringBuilder(
                "memory = new Array(" + 30000 + ");\n"
                        + "memory.fill(0);\n"
                        + "memIndex = 0;\n");

        for (VisitableCommand cmd: commands) {
            stringBuilder.append(cmd.accept(new CommandToJavaScriptVisitor()));
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

}
