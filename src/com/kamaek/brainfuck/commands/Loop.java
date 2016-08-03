package com.kamaek.brainfuck.commands;

import com.kamaek.brainfuck.Memory;
import com.kamaek.brainfuck.translator.CommandVisitor;
import com.kamaek.brainfuck.translator.Visitable;

import java.util.ArrayList;
import java.util.List;

public class Loop implements Command {
    private List<Command> commands = new ArrayList<Command>();

    public Loop(List<Command> loopCommands) {
        commands = loopCommands;
    }

    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public void execute(Memory mem) {
        executeLoop(mem);
    }

    @Override
    public <T> T accept(CommandVisitor<T> visitor) {
        return visitor.visit(this);
    }

    private void executeLoop(Memory mem) {
        while (mem.getCurrentCellValue() != 0)
            for (Command cmd: commands)
                cmd.execute(mem);
    }

}
