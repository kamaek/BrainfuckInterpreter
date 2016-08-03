package com.kamaek.brainfuck.commands;

import com.kamaek.brainfuck.Memory;
import com.kamaek.brainfuck.translator.Visitable;

public interface Command extends Visitable {
    void execute(Memory mem);
}
