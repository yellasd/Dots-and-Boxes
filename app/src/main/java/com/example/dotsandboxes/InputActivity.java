package com.example.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class InputActivity extends AppCompatActivity {
    int players = 1;
    int gridsize = 4;
    int difficultyLevel=0;
    RadioGroup noOfPlayers, gridSize, difficulty;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        intent = new Intent(this, GameActivity.class);

        Toast.makeText(getApplicationContext(), "Please select number of players and grid size!!", Toast.LENGTH_SHORT).show();
        noOfPlayers = findViewById(R.id.players);
        gridSize = findViewById(R.id.gridSize);
        difficulty=findViewById(R.id.difficulty);
        noOfPlayers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.single) {
                    players=1;
                }
                if (checkedId == R.id.two) {
                    players=2;
                }
                if (checkedId == R.id.three) {
                    players=3;
                }
            }
        });
        gridSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.four)
                    gridsize=4;
                if (checkedId == R.id.five)
                    gridsize=5;
                if (checkedId == R.id.six)
                    gridsize=6;
            }
        });
        difficulty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.easy)
                    difficultyLevel=0;
                if(checkedId==R.id.hard)
                    difficultyLevel=1;
            }
        });
    }

    public void go(View view) {
        intent.putExtra("PLAYERS",players);
        intent.putExtra("DIFFICULTY",difficultyLevel);
        intent.putExtra("GRID_SIZE",gridsize);
        startActivity(intent);
    }
}
