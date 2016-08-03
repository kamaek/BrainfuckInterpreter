package com.kamaek.brainfuck;

import com.kamaek.brainfuck.commands.Command;

import java.util.List;

public interface Interpreter {
    List<Command> prepareCommands(String programCode);
    void execute(List<Command> commands);
}
