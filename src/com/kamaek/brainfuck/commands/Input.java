package com.kamaek.brainfuck.commands;

import com.kamaek.brainfuck.Memory;
import com.kamaek.brainfuck.translator.CommandVisitor;
import com.kamaek.brainfuck.translator.Visitable;

import java.util.Scanner;

public class Input implements Command {

    @Override
    public void execute(Memory mem) {
        Scanner input = new Scanner(System.in);
        mem.setCurrentCellValue(Integer.parseInt(input.nextLine()));
        input.close();
    }

    @Override
    public <T> T accept(CommandVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
