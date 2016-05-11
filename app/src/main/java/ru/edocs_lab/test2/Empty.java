package ru.edocs_lab.test2;

class Empty extends Cell {

    public Empty(long timestamp) {
        super(timestamp);
        mType = Type.EMPTY;
    }

    @Override
    public Cell copy() {
        return new Empty(getLastUpdate());
    }

    @Override
    int getPicture() {
        return 0;
    }

    @Override
    boolean go(Cell cells[][], int r, int c, int direction, long timestamp) {
        return super.go(cells, r, c, direction, timestamp);
    }
}