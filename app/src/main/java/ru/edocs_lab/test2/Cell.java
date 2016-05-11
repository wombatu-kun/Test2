package ru.edocs_lab.test2;

enum Type {EMPTY, PENGUIN, WHALE}

abstract class Cell {
    protected int mStepCounter;
    protected long mLastUpdate;
    protected Type mType;

    abstract int getPicture();
    protected abstract Cell copy();

    protected Cell(long timestamp) {
        mStepCounter = 0;
        mLastUpdate = timestamp;
    }

    Type getType() { return mType; }

    long getLastUpdate() { return mLastUpdate; }

    boolean go(Cell cells[][], int r, int c, int direction, long timestamp) {
        mStepCounter++;
        mLastUpdate = timestamp;
        return false;
    }

    protected boolean tryMove(Cell cells[][], int r, int c, int direction, int clonePeriod, Type target) {
        boolean moved = false;
        int new_rc[] = checkDirection(cells, r, c, direction, target);
        if (new_rc[0]!=r || new_rc[1]!=c) {
            cells[new_rc[0]][new_rc[1]] = cells[r][c];
            moved = true;
            if (mStepCounter == clonePeriod) {
                cells[r][c] = cells[r][c].copy();
                mStepCounter = 0;
            } else {
                cells[r][c] = new Empty(mLastUpdate);
            }
        }
        return moved;
    }

    protected boolean tryClone(Cell cells[][], int r, int c, int clonePeriod) {
        boolean cloned = false;
        if (mStepCounter == clonePeriod) {
            int new_rc[] = findFirstThing(cells, r, c, Type.EMPTY);
            if (new_rc[0] != r || new_rc[1] != c) {
                cells[new_rc[0]][new_rc[1]] = cells[r][c].copy();
                cloned = true;
            }
            mStepCounter = 0;
        }
        return cloned;
    }

    protected int[] checkDirection(Cell cells[][], int r, int c, int direction, Type contentType) {
        int new_rc[] = new int[2];
        new_rc[0] = r;
        new_rc[1] = c;
        switch(direction) {
            case 0:
                if (r>0 && cells[r-1][c].getType()==contentType) {
                    new_rc[0] = r-1;
                }
                break;
            case 1:
                if (r>0 && c<(cells[0].length-1) && cells[r-1][c+1].getType()==contentType) {
                    new_rc[0] = r-1;
                    new_rc[1] = c+1;
                }
                break;
            case 2:
                if (c<(cells[0].length-1) && cells[r][c+1].getType()==contentType) {
                    new_rc[1] = c+1;
                }
                break;
            case 3:
                if (r<(cells.length-1) && c<(cells[0].length-1) && cells[r+1][c+1].getType()==contentType) {
                    new_rc[0] = r+1;
                    new_rc[1] = c+1;
                }
                break;
            case 4:
                if (r<(cells.length-1) && cells[r+1][c].getType()==contentType) {
                    new_rc[0] = r+1;
                }
                break;
            case 5:
                if (r<(cells.length-1) && c>0 && cells[r+1][c-1].getType()==contentType) {
                    new_rc[0] = r+1;
                    new_rc[1] = c-1;
                }
                break;
            case 6:
                if (c>0 && cells[r][c-1].getType()==contentType) {
                    new_rc[1] = c-1;
                }
                break;
            case 7:
                if (r>0 && c>0 && cells[r-1][c-1].getType()==contentType) {
                    new_rc[0] = r-1;
                    new_rc[1] = c-1;
                }
                break;
        }
        return new_rc;
    }

    protected int[] findFirstThing(Cell cells[][], int r, int c, Type contentType) {
        //с севера по часовой проверяет все направления, возвращает координаты первой клетки нужного типа
        int new_rc[] = new int[3];
        int tmp_rc[];
        new_rc[0] = r;
        new_rc[1] = c;
        for(int d=0; d<8; d++) {
            tmp_rc = checkDirection(cells, r, c, d, contentType);
            if (tmp_rc[0]!=r || tmp_rc[1]!=c) {
                new_rc[0] = tmp_rc[0];
                new_rc[1] = tmp_rc[1];
                new_rc[2] = d; //направление
                break;
            }
        }
        return new_rc;
    }
}
