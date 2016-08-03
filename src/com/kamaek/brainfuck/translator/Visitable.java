package com.kamaek.brainfuck.translator;

public interface Visitable {
    <T> T accept(CommandVisitor<T> visitor);
}
