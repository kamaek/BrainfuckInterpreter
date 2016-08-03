package com.kamaek.brainfuck;

public class Memory {
    private static final int defaultMemSize = 30000;

    private final int[] memory = new int[defaultMemSize];
    private int memIndex;

    public void incrementCurrentCell() {
        memory[memIndex]++;
    }

    public void decrementCurrentCell() {
        memory[memIndex]--;
    }

    public int getCurrentCellValue() {
        return memory[memIndex];
    }

    public void setCurrentCellValue(int val) {
        memory[memIndex] = val;
    }

    public void toNextCell() {
        if (memIndex >= memory.length)
            throw new IndexOutOfBoundsException("index is higher than memory size");
        else
            memIndex++;
    }

    public void toPrevCell() {
        if (memIndex < 0)
            throw new IndexOutOfBoundsException("index is less than 0");
        else
            memIndex--;
    }
}
