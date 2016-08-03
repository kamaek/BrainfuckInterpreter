package com.kamaek.brainfuck.commands;

import com.kamaek.brainfuck.Memory;
import com.kamaek.brainfuck.translator.CommandVisitor;
import com.kamaek.brainfuck.translator.Visitable;

public class Plus implements Command {

    @Override
    public void execute(Memory mem) {
        mem.incrementCurrentCell();
    }

    @Override
    public <T> T accept(CommandVisitor<T> visitor) {
        return visitor.visit(this);
    }
}