package com.kamaek.brainfuck.translator;

import com.kamaek.brainfuck.commands.*;

public interface CommandVisitor<T> {
    T visit(Plus plus);
    T visit(Minus minus);
    T visit(NextCell nextCell);
    T visit(PreviousCell previousCell);
    T visit(Print print);
    T visit(Input input);
    T visit(Loop loop);
}
