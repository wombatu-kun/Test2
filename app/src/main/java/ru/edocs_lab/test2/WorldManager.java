package ru.edocs_lab.test2;

import android.os.AsyncTask;
import android.widget.BaseAdapter;

import java.util.Random;

public class WorldManager {
    public static final int COLUMNS = 10;
    public static final int ROWS = 15;
    private static final int PAUSE = 150;
    private static final int P_PERCENT = 50;
    private static final int W_PERCENT = 5;

    private static WorldManager sWorldManager;
    private Cell mCells[][];
    private Random mRnd;
    private boolean inProcess;

    private WorldManager() {
        mRnd = new Random(System.currentTimeMillis());
        restart();
    }

    static WorldManager get() {
        if (sWorldManager == null) sWorldManager = new WorldManager();
        return sWorldManager;
    }

    Cell getCellContent(int pos) {
        return mCells[pos/COLUMNS][pos%COLUMNS];
    }

    void restart() {
        inProcess = false;
        mCells = new Cell[ROWS][COLUMNS];
        for(int r=0; r<ROWS; r++) {
            for(int c=0; c<COLUMNS; c++) {
                mCells[r][c] = new Empty(0);
            }
        }
        generateLife(new Penguin(0), COLUMNS*ROWS*P_PERCENT/100);
        generateLife(new Whale(0), COLUMNS*ROWS*W_PERCENT/100);
    }

    private void generateLife(Cell life, int number) {
        int r, c;
        for(int n=number; n>0; n--) {
            do {
                r = mRnd.nextInt(ROWS);
                c = mRnd.nextInt(COLUMNS);
            } while (mCells[r][c].getType() != Type.EMPTY);
            mCells[r][c] = life.copy();
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
                    if (mCells[r][c].getType() != Type.EMPTY) {
                        if (timestamp != mCells[r][c].getLastUpdate()) {
                            int direction = mRnd.nextInt(8);
                            if (mCells[r][c].go(mCells, r, c, direction, timestamp)) {
                                publishProgress(null);
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
