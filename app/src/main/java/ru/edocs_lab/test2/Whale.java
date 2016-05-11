package ru.edocs_lab.test2;

class Whale extends Cell {
    private static final int CLONE_PERIOD = 8;
    private static final int STARVATION = 3;

    private int mHungerLevel;

    public Whale(long timestamp) {
        super(timestamp);
        mType = Type.WHALE;
        mHungerLevel = 0;
    }

    @Override
    public Cell copy() {
        return new Whale(getLastUpdate());
    }

    @Override
    int getPicture() {
        return R.drawable.orca;
    }

    @Override
    boolean go(Cell cells[][], int r, int c, int direction, long timestamp) {
        super.go(cells, r, c, direction, timestamp);
        boolean moved;
        int new_rc[] = findFirstThing(cells, r, c, Type.PENGUIN);
        if (tryMove(cells, r, c, new_rc[2], CLONE_PERIOD, Type.PENGUIN)) {
            mHungerLevel = 0;
            moved = true;
        } else {
            mHungerLevel++;
            if (mHungerLevel == STARVATION) {
                cells[r][c] = new Empty(mLastUpdate);
                moved = true;
            } else {
                moved = tryMove(cells, r, c, direction, CLONE_PERIOD, Type.EMPTY) || tryClone(cells, r, c, CLONE_PERIOD);
            }
        }
        return moved;
    }
}
