package com.kamaek.brainfuck.translator;

import com.kamaek.brainfuck.commands.Command;

import java.util.List;

public interface Translator {
    String translate(List<Command> commands);
}
