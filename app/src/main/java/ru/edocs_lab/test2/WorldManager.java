package ru.edocs_lab.test2;

import android.os.AsyncTask;
import android.widget.BaseAdapter;

import java.util.Random;

enum Direction {NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST}

public class WorldManager {
    public static final int COLUMNS = 10;
    public static final int ROWS = 15;
    private static final int PAUSE = 150;
    private static final int P_PERCENT = 50;
    private static final int W_PERCENT = 5;
    private static final int FAILS_LIMIT = ROWS * COLUMNS;
    private static final Direction DIRECTIONS[] = Direction.values();


    private static WorldManager worldManager;
    private Cell cells[][];
    private Random random;
    private boolean inProcess;

    private WorldManager() {
        random = new Random(System.currentTimeMillis());
        createCells();
        generateEnvironments();
        restart();
    }

    static WorldManager get() {
        if (worldManager == null) worldManager = new WorldManager();
        return worldManager;
    }

    CellContent getCellContent(int pos) {
        return cells[pos/COLUMNS][pos%COLUMNS].getContent();
    }

    void createCells() {
        cells = new Cell[ROWS][COLUMNS];
        for(int r=0; r<ROWS; r++) {
            for (int c=0; c<COLUMNS; c++) {
                cells[r][c] = new Cell();
            }
        }
    }

    private void generateEnvironments() {
        Cell environment[];
        int shift[][] = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
        for(int r=0; r<ROWS; r++) {
            for (int c=0; c<COLUMNS; c++) {
                environment = new Cell[DIRECTIONS.length];
                for(int i=0; i<DIRECTIONS.length; i++){
                    environment[i] = getEnvCell(r, c, shift[i]);
                }
                cells[r][c].setEnvironment(environment);
            }
        }
    }

    private Cell getEnvCell(int r, int c, int shift[]) {
        int envR = r+shift[0];
        int envC = c+shift[1];
        if (envR>-1 && envR<ROWS && envC>-1 && envC<COLUMNS) {
            return cells[envR][envC];
        } else {
            return null;
        }
    }

    void restart() {
        inProcess = false;
        for(int r=0; r<ROWS; r++) {
            for(int c=0; c<COLUMNS; c++) {
                cells[r][c].setContent(new Empty(0));
            }
        }
        generateLife(new Penguin(0), COLUMNS*ROWS*P_PERCENT/100);
        generateLife(new Whale(0), COLUMNS*ROWS*W_PERCENT/100);
    }

    private void generateLife(CellContent life, int number) {
        int r, c;
        for(int n=number; n>0; n--) {
            int attempt = FAILS_LIMIT;
            do {
                attempt--;
                r = random.nextInt(ROWS);
                c = random.nextInt(COLUMNS);
            } while (cells[r][c].getContent().getType()!=Type.EMPTY && attempt>0);
            if (attempt>0) {
                cells[r][c].setContent(life.copy());
            }
        }
    }

    boolean isRunning() { return inProcess; }

    void go(BaseAdapter adapter) {
        inProcess = true;
        new DemonstrateStepTask().execute(adapter);
    }

    private class DemonstrateStepTask extends AsyncTask<BaseAdapter, Void, Void> {
        private BaseAdapter adapter;
        @Override
        protected Void doInBackground(BaseAdapter... params) {
            adapter = params[0];
            long timestamp = System.currentTimeMillis();
            for(int r=0; r<ROWS; r++) {
                for(int c=0; c<COLUMNS; c++) {
                    if (cells[r][c].getContent().getType() != Type.EMPTY) {
                        if (timestamp != cells[r][c].getContent().getLastUpdate()) {
                            int randomDirection = random.nextInt(DIRECTIONS.length);
                            if (cells[r][c].go(DIRECTIONS[randomDirection], timestamp)) {
                                publishProgress();
                                try {
                                    Thread.sleep(PAUSE);
                                } catch (InterruptedException e) { /*shit happens*/ }
                            }
                        }
                    }
                }
            }
            return null;
        }
        public void onProgressUpdate(Void... params) {
            adapter.notifyDataSetChanged();
        }
        @Override
        protected void onPostExecute(Void param) {
            inProcess = false;
        }
    }
}
