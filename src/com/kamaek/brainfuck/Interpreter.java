package com.kamaek.brainfuck;

import com.kamaek.brainfuck.commands.VisitableCommand;

import java.util.List;

public interface Interpreter {
    List<VisitableCommand> prepareCommands(String programCode);
    void execute(List<VisitableCommand> commands);
}
