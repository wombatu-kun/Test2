package ru.edocs_lab.test2;

enum Type {PENGUIN, WHALE};

abstract class LifeForm {
    protected int mStepCounter;
    protected long mLastUpdate;
    protected Type mType;

    abstract int getPicture();
    protected abstract LifeForm clone();

    protected LifeForm(long timestamp) {
        mStepCounter = 0;
        mLastUpdate = timestamp;
    }

    public Type getType() { return mType; }

    public long getLastUpdate() { return mLastUpdate; }

    boolean go(LifeForm cells[][], int r, int c, int direction, long timestamp) {
        mStepCounter++;
        mLastUpdate = timestamp;
        return false;
    }

    protected boolean tryFreeMove(LifeForm cells[][], int r, int c, int direction, int clonePeriod) {
        boolean moved = false;
        int new_rc[] = checkDirection(cells, r, c, direction);
        if (new_rc[0]!=r || new_rc[1]!=c) {
            cells[new_rc[0]][new_rc[1]] = cells[r][c];
            moved = true;
            if (mStepCounter == clonePeriod) {
                cells[r][c] = cells[r][c].clone();
                mStepCounter = 0;
            } else {
                cells[r][c] = null;
            }
        } else {
            if (mStepCounter == clonePeriod) {
                new_rc = findEmptySpace(cells, r, c);
                if (new_rc[0] != r || new_rc[1] != c) {
                    cells[new_rc[0]][new_rc[1]] = cells[r][c].clone();
                    moved = true;
                }
                mStepCounter = 0;
            }
        }
        return moved;
    }

    protected int[] checkDirection(LifeForm cells[][], int r, int c, int direction) {
        //проверка: свободна ли клетка cells[r][c] в направлении direction.
        //direction: 0-7, 0 - север, далее - по часовой
        int new_rc[] = new int[2];
        new_rc[0] = r;
        new_rc[1] = c;
        switch(direction) {
            case 0:
                if (r>0 && cells[r-1][c]==null) {
                    new_rc[0] = r-1;
                }
                break;
            case 1:
                if (r>0 && c<(cells[0].length-1) && cells[r-1][c+1]==null) {
                    new_rc[0] = r-1;
                    new_rc[1] = c+1;
                }
                break;
            case 2:
                if (c<(cells[0].length-1) && cells[r][c+1]==null) {
                    new_rc[1] = c+1;
                }
                break;
            case 3:
                if (r<(cells.length-1) && c<(cells[0].length-1) && cells[r+1][c+1]==null) {
                    new_rc[0] = r+1;
                    new_rc[1] = c+1;
                }
                break;
            case 4:
                if (r<(cells.length-1) && cells[r+1][c]==null) {
                    new_rc[0] = r+1;
                }
                break;
            case 5:
                if (r<(cells.length-1) && c>0 && cells[r+1][c-1]==null) {
                    new_rc[0] = r+1;
                    new_rc[1] = c-1;
                }
                break;
            case 6:
                if (c>0 && cells[r][c-1]==null) {
                    new_rc[1] = c-1;
                }
                break;
            case 7:
                if (r>0 && c>0 && cells[r-1][c-1]==null) {
                    new_rc[0] = r-1;
                    new_rc[1] = c-1;
                }
                break;
        }
        return new_rc;
    }

    protected int[] findEmptySpace(LifeForm cells[][], int r, int c) {
        //с севера по часовой проверят все направления, возвращает координаты первой пустой клетки
        int new_rc[] = new int[2];
        int tmp_rc[];
        new_rc[0] = r;
        new_rc[1] = c;
        for(int d=0; d<8; d++) {
            tmp_rc = checkDirection(cells, r, c, d);
            if (tmp_rc[0]!=r || tmp_rc[1]!=c) {
                new_rc[0] = tmp_rc[0];
                new_rc[1] = tmp_rc[1];
                break;
            }
        }
        return new_rc;
    }
}
