package ru.edocs_lab.test2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class MainFragment extends Fragment {

    private AppMgr mAppMgr;
    private ImageAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mAppMgr = AppMgr.get(getActivity());
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mAdapter = new ImageAdapter(getActivity(),
                (dm.widthPixels / AppMgr.COLUMNS) - 1, (int)(dm.heightPixels - dm.density*80) / AppMgr.ROWS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setNumColumns(mAppMgr.COLUMNS);
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mAppMgr.isRunning()) {
                    mAppMgr.go(mAdapter);
                } else {
                    Toast.makeText(getActivity(), R.string.grid_press_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnRestart = (Button) v.findViewById(R.id.restart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mAppMgr.isRunning()) {
                    mAppMgr.restart();
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), R.string.restart_press_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    public void onBackPressed() {
        if (mAppMgr.isRunning()) {
            Toast.makeText(getActivity(), R.string.back_press_msg, Toast.LENGTH_SHORT).show();
        } else {
            mAppMgr.restart();
            getActivity().finish();
        }
    }
}
