package com.kamaek.brainfuck.commands;

import com.kamaek.brainfuck.Memory;
import com.kamaek.brainfuck.Visitable;

public interface VisitableCommand extends Visitable {
    void execute(Memory mem);
}
