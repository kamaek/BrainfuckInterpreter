package com.kamaek.brainfuck;

public interface Visitable {
    <T> T accept(CommandVisitor<T> visitor);
}
