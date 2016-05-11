package ru.edocs_lab.test2;

class Penguin extends Cell {
    private static final int CLONE_PERIOD = 3;

    public Penguin(long timestamp) {
        super(timestamp);
        mType = Type.PENGUIN;
    }

    @Override
    public Cell copy() {
        return new Penguin(getLastUpdate());
    }

    @Override
    int getPicture() {
        return R.drawable.tux;
    }

    @Override
    boolean go(Cell cells[][], int r, int c, int direction, long timestamp) {
        super.go(cells, r, c, direction, timestamp);
        return tryMove(cells, r, c, direction, CLONE_PERIOD, Type.EMPTY) || tryClone(cells, r, c, CLONE_PERIOD);
    }
}