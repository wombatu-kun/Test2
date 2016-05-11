package ru.edocs_lab.test2;

class Whale extends CellContent {
    private static final int COPY_PERIOD = 8;
    private static final int STARVATION = 3;

    private int hungerLevel;

    public Whale(long timestamp) {
        super(timestamp);
        type = Type.WHALE;
        hungerLevel = 0;
    }

    @Override
    public CellContent copy() {
        return new Whale(getLastUpdate());
    }

    @Override
    int getPicture() {
        return R.drawable.orca;
    }

    @Override
    boolean go(Cell cell, Direction randomDir, long timestamp) {
        super.go(cell, randomDir, timestamp);
        boolean moved;
        Cell environment[] = cell.getEnvironment();
        Direction dir = findRightDirection(environment, Type.PENGUIN);
        if (tryMove(cell, dir, COPY_PERIOD, Type.PENGUIN)) {
            hungerLevel = 0;
            moved = true;
        } else {
            hungerLevel++;
            if (hungerLevel == STARVATION) {
                cell.setContent(new Empty(lastUpdate));
                moved = true;
            } else {
                moved = tryMove(cell, randomDir, COPY_PERIOD, Type.EMPTY) || tryCopy(environment, COPY_PERIOD);;
            }
        }
        return moved;
    }
}
