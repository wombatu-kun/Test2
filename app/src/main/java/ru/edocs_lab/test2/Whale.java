package ru.edocs_lab.test2;

class Whale extends LifeForm {
    private static final int CLONE_PERIOD = 8;
    private static final int STARVATION = 3;

    private int mHungerLevel;

    public Whale(long timestamp) {
        super(timestamp);
        mType = Type.WHALE;
        mHungerLevel = 0;
    }

    @Override
    public LifeForm clone() {
        return new Whale(getLastUpdate());

    }

    @Override
    int getPicture() {
        return R.drawable.orca;
    }

    @Override
    boolean go(LifeForm cells[][], int r, int c, int direction, long timestamp) {
        super.go(cells, r, c, direction, timestamp);
        boolean moved = false;
        int new_rc[] = findFood(cells, r, c);
        if (new_rc[0] != r || new_rc[1] != c) {
            cells[new_rc[0]][new_rc[1]] = cells[r][c];
            mHungerLevel = 0;
            moved = true;
            if (mStepCounter == CLONE_PERIOD) {
                cells[r][c] = new Whale(timestamp);
                mStepCounter = 0;
            } else {
                cells[r][c] = null;
            }
        } else {
            mHungerLevel++;
            if (mHungerLevel == STARVATION) {
                cells[r][c] = null;
                moved = true;
            } else {
                moved = tryFreeMove(cells, r, c, direction, CLONE_PERIOD);
            }
        }
        return moved;
    }

    private int[] findFood(LifeForm cells[][], int r, int c) {
        int new_rc[] = new int[2];
        new_rc[0] = r;
        new_rc[1] = c;
        int r_max = cells.length-1;
        int c_max = cells[0].length-1;
        if (r>0 && cells[r-1][c]!=null && cells[r-1][c].getType()!=mType) {
            new_rc[0] = r-1;
            return new_rc;
        }
        if (r>0 && c<c_max && cells[r-1][c+1]!=null && cells[r-1][c+1].getType()!=mType) {
            new_rc[0] = r-1;
            new_rc[1] = c+1;
            return new_rc;
        }
        if (c<c_max && cells[r][c+1]!=null && cells[r][c+1].getType()!=mType) {
            new_rc[1] = c+1;
            return new_rc;
        }
        if (r<r_max && c<c_max && cells[r+1][c+1]!=null && cells[r+1][c+1].getType()!=mType) {
            new_rc[0] = r+1;
            new_rc[1] = c+1;
            return new_rc;
        }
        if (r<r_max && cells[r+1][c]!=null && cells[r+1][c].getType()!=mType) {
            new_rc[0] = r+1;
            return new_rc;
        }
        if (r<r_max && c>0 && cells[r+1][c-1]!=null && cells[r+1][c-1].getType()!=mType) {
            new_rc[0] = r+1;
            new_rc[1] = c-1;
            return new_rc;
        }
        if (c>0 && cells[r][c-1]!=null && cells[r][c-1].getType()!=mType) {
            new_rc[1] = c-1;
            return new_rc;
        }
        if (r>0 && c>0 && cells[r-1][c-1]!=null && cells[r-1][c-1].getType()!=mType) {
            new_rc[0] = r-1;
            new_rc[1] = c-1;
        }
        return new_rc;
    }
}
