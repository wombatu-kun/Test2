package ru.edocs_lab.test2;


class Penguin extends LifeForm {
    private static final int CLONE_PERIOD = 3;

    public Penguin(long timestamp) {
        super(timestamp);
        mType = Type.PENGUIN;
    }

    @Override
    public LifeForm clone() {
        return new Penguin(getLastUpdate());
    }

    @Override
    int getPicture() {
        return R.drawable.tux;
    }

    @Override
    boolean go(LifeForm cells[][], int r, int c, int direction, long timestamp) {
        super.go(cells, r, c, direction, timestamp);
        return tryFreeMove(cells, r, c, direction, CLONE_PERIOD);
    }
}