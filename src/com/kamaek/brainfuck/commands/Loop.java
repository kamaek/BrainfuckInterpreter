package com.kamaek.brainfuck.commands;

import com.kamaek.brainfuck.Memory;
import com.kamaek.brainfuck.CommandVisitor;

import java.util.ArrayList;
import java.util.List;

public class Loop implements VisitableCommand {
    private List<VisitableCommand> commands = new ArrayList<VisitableCommand>();

    public Loop(List<VisitableCommand> loopCommands) {
        commands = loopCommands;
    }

    public List<VisitableCommand> getCommands() {
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
            for (VisitableCommand cmd: commands)
                cmd.execute(mem);
    }

}
