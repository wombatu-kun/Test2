package ru.edocs_lab.test2;

class Empty extends CellContent {

    public Empty(long timestamp) {
        super(timestamp);
        type = Type.EMPTY;
    }

    @Override
    public CellContent copy() {
        return new Empty(getLastUpdate());
    }

    @Override
    int getPicture() {
        return 0;
    }

    @Override
    boolean go(Cell cell, Direction dir, long timestamp) {
        return super.go(cell, dir, timestamp);
    }
}