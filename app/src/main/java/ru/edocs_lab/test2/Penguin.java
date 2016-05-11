package ru.edocs_lab.test2;

class Penguin extends CellContent {
    private static final int COPY_PERIOD = 3;

    public Penguin(long timestamp) {
        super(timestamp);
        type = Type.PENGUIN;
    }

    @Override
    public CellContent copy() { return new Penguin(getLastUpdate()); }

    @Override
    int getPicture() {
        return R.drawable.tux;
    }

    @Override
    boolean go(Cell cell, Direction randomDir, long timestamp) {
        super.go(cell, randomDir, timestamp);
        return tryMove(cell, randomDir, COPY_PERIOD, Type.EMPTY) || tryCopy(cell.getEnvironment(), COPY_PERIOD);
    }
}