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
    private static final int HEIGHT_CORRECTION = 80;

    private WorldManager worldManager;
    private ImageAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        worldManager = WorldManager.get();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        adapter = new ImageAdapter(getActivity(),
                (dm.widthPixels / WorldManager.COLUMNS) - 1, (int)(dm.heightPixels - dm.density*HEIGHT_CORRECTION) / WorldManager.ROWS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.gridview);
        gridView.setNumColumns(WorldManager.COLUMNS);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!worldManager.isRunning()) {
                    worldManager.go(adapter);
                } else {
                    Toast.makeText(getActivity(), R.string.grid_press_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnRestart = (Button) v.findViewById(R.id.restart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!worldManager.isRunning()) {
                    worldManager.restart();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), R.string.restart_press_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    public void onBackPressed() {
        if (worldManager.isRunning()) {
            Toast.makeText(getActivity(), R.string.back_press_msg, Toast.LENGTH_SHORT).show();
        } else {
            worldManager.restart();
            getActivity().finish();
        }
    }
}
