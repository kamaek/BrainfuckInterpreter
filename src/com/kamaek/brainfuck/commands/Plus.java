package com.kamaek.brainfuck.commands;

import com.kamaek.brainfuck.Memory;
import com.kamaek.brainfuck.CommandVisitor;

public class Plus implements VisitableCommand {

    @Override
    public void execute(Memory mem) {
        mem.incrementCurrentCell();
    }

    @Override
    public <T> T accept(CommandVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
