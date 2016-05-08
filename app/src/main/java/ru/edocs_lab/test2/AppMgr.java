package ru.edocs_lab.test2;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

import java.util.Random;

public class AppMgr {
    public static final int COLUMNS = 10;
    public static final int ROWS = 15;
    private static final int PAUSE = 150;
    private static final int P_PERCENT = 50;
    private static final int W_PERCENT = 5;

    private static AppMgr sAppMgr;
    private Context mAppContext;
    private LifeForm mCells[][];
    private Random mRnd;
    private boolean inProcess;

    private AppMgr(Context appContext) {
        mAppContext = appContext;
        mRnd = new Random(System.currentTimeMillis());
        restart();
    }

    public static AppMgr get(Context c) {
        if (sAppMgr == null) sAppMgr = new AppMgr(c.getApplicationContext());
        return sAppMgr;
    }

    public LifeForm getCellContent(int pos) {
        return mCells[pos/COLUMNS][pos%COLUMNS];
    }

    public void restart() {
        inProcess = false;
        mCells = new LifeForm[ROWS][COLUMNS];
        generateLife(new Penguin(0), COLUMNS*ROWS*P_PERCENT/100);
        generateLife(new Whale(0), COLUMNS*ROWS*W_PERCENT/100);
    }

    private void generateLife(LifeForm life, int number) {
        int row, col;
        for(int p=number; p>0; p--) {
            do {
                row = mRnd.nextInt(ROWS);
                col = mRnd.nextInt(COLUMNS);
            } while (mCells[row][col]!=null);
            mCells[row][col] = life.clone();
        }
    }

    public boolean isRunning() { return inProcess; }

    public void go(BaseAdapter adapter) {
        inProcess = true;
        new DemonstrateStepTask().execute(adapter);
    }

    private class DemonstrateStepTask extends AsyncTask<BaseAdapter, Void, Void> {
        private BaseAdapter adapter;
        @Override
        protected Void doInBackground(BaseAdapter... params) {
            adapter = params[0];
            long timestamp = System.currentTimeMillis();
            for(int i=0; i<ROWS; i++) {
                for(int j=0; j<COLUMNS; j++) {
                    if (mCells[i][j] != null) {
                        if (timestamp != mCells[i][j].getLastUpdate()) {
                            int direction = mRnd.nextInt(8);
                            if (mCells[i][j].go(mCells, i, j, direction, timestamp)) {
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
