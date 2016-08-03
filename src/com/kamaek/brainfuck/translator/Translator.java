package com.kamaek.brainfuck.translator;

import com.kamaek.brainfuck.commands.VisitableCommand;

import java.util.List;

public interface Translator {
    String translate(List<VisitableCommand> commands);
}
