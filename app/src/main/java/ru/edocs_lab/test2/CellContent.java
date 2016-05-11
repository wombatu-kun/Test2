package ru.edocs_lab.test2;

enum Type {EMPTY, PENGUIN, WHALE}

abstract class CellContent {
    protected int stepCounter;
    protected long lastUpdate;
    protected Type type;

    abstract int getPicture();
    protected abstract CellContent copy();

    protected CellContent(long timestamp) {
        stepCounter = 0;
        lastUpdate = timestamp;
    }

    Type getType() { return type; }

    long getLastUpdate() { return lastUpdate; }

    boolean go(Cell cell, Direction randomDir, long timestamp) {
        stepCounter++;
        lastUpdate = timestamp;
        return false;
    }

    protected boolean tryMove(Cell cell, Direction dir, int copyPeriod, Type targetType) {
        boolean moved = false;
        Cell environment[] = cell.getEnvironment();
        if (isDirectionOk(environment, dir, targetType)) {
            environment[dir.ordinal()].setContent(this);
            moved = true;
            if (stepCounter == copyPeriod) {
                cell.setContent(this.copy());
                stepCounter = 0;
            } else {
                cell.setContent(new Empty(lastUpdate));
            }
        }
        return moved;
    }

    protected boolean tryCopy(Cell environment[], int copyPeriod) {
        boolean copied = false;
        if (stepCounter == copyPeriod) {
            Direction dir = findRightDirection(environment, Type.EMPTY);
            if (dir != null) {
                environment[dir.ordinal()].setContent(this.copy());
                copied = true;
            }
            stepCounter = 0;
        }
        return copied;
    }

    protected boolean isDirectionOk (Cell environment[], Direction dir, Type targetType) {
        if (dir==null || environment[dir.ordinal()] == null) return false;
        CellContent content = environment[dir.ordinal()].getContent();
        if (content.getType() == targetType) {
            return true;
        } else {
            return false;
        }
    }

    protected Direction findRightDirection(Cell environment[], Type targetType) {
        for(Direction d : Direction.values()) {
            if (isDirectionOk(environment, d, targetType)) {
                return d;
            }
        }
        return null;
    }
}
