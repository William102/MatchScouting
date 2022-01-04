package com.example.opal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.opal.Data.Teams;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    int i = 1;

    HashMap<String, String> teamNames = new HashMap<>();
    HashMap<String, Double> averagePoints = new HashMap<>();
    HashMap<String, Integer> teamPoints = new HashMap<>();
    ArrayList<HashMap<String, String>> Data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 1; i < 100; i++) {
            createTeam(i, "Team #" + i);
        }
        ListView listview = findViewById(R.id.teams_list);
        DatabaseReference reference = database.getReference().child("Teams");

        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, Data, R.layout.list_view,
                new String[]{"Team", "TeamData"},
                new int[]{R.id.textTeam, R.id.textTeamData});
        listview.setAdapter(adapter);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Data.clear();
                teamNames.clear();
                teamPoints.clear();
                averagePoints.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Teams post = data.getValue(Teams.class);
                    teamNames.put(data.getKey(), post.getName());
                    teamPoints.put(data.getKey(), post.getPoints());
                    averagePoints.put(data.getKey(), post.getAveragePoints());
                }
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(teamPoints);
                        int rank = 1;

                        for (Map.Entry<String, Integer> map : sortTeams()) {
                            String key = map.getKey();
                            HashMap<String, String> finalMap = new HashMap<>();
                            finalMap.put("Team", map.getKey());
                            finalMap.put("TeamData",
                                    "Name: " + teamNames.get(key) +
                                            "   Ranking: " + rank +
                                            "   Points: " + teamPoints.get(key) +
                                            "   Point Average: " + averagePoints.get(key));
                            Data.add(finalMap);
                            System.out.println(map.getKey());
                            reference.child(key + "/ranking").setValue(rank);
                            rank = rank + 1;
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, 100);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        };
        reference.addValueEventListener(listener);

        Button button = findViewById(R.id.match_fragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTeam(701, "RoboVikes");
            }
        });
    }

    public void createTeam(Integer id, String name) {
        teamPoints.put(String.valueOf(id), 0);
        Random rand = new Random();
        int points = rand.nextInt(1000);
        double pointAverage = points / 4.0;
        DatabaseReference teams = database.getReference().child("Teams");
        teams.child(id + "/name").setValue(name);
        teams.child(id + "/ranking").setValue(1);
        teams.child(id + "/averagePoints").setValue(pointAverage);
        teams.child(id + "/averageBalls").setValue(0);
        teams.child(id + "/points").setValue(points);
    }

    public List<Map.Entry<String, Integer>> sortTeams() {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(teamPoints.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        return list;
    }
}
