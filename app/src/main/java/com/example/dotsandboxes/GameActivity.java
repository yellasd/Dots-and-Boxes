package com.example.dotsandboxes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    TextView player1Score, player2Score, player3Score;
    int players = 1, grid_size = 4, difficulty = 0;
    GameView gameView;
    MediaPlayer player;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        player=MediaPlayer.create(this,R.raw.music_dotsnboxes);
        player.setLooping(true);
        player.start();


        Intent intent = getIntent();
        players = intent.getIntExtra("PLAYERS", 2);
        grid_size = intent.getIntExtra("GRID_SIZE", 4);
        difficulty = intent.getIntExtra("DIFFICULTY", 0);

        player1Score = findViewById(R.id.player1Score);
        player2Score = findViewById(R.id.player2Score);

        if (players == 3) {
            player3Score = findViewById(R.id.player2Score);
            player2Score = findViewById(R.id.playerExtraScore);
            player2Score.setVisibility(View.VISIBLE);
        }

        LinearLayout layout = findViewById(R.id.layout);
        if (players == 1) {
            BoardView boardView = new BoardView(this, 1, 4, difficulty);
            boardView.gameActivity = this;
            boardView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(boardView);

            TextView tv = findViewById(R.id.textView8);
            tv.setText("You");
            tv = findViewById(R.id.textView7);
            tv.setText("Computer");

        } else {
            gameView = new GameView(getApplicationContext(), players, grid_size);
            gameView.gameActivity = this;
            ImageButton imageButton = findViewById(R.id.undo);
            imageButton.setVisibility(View.VISIBLE);
            gameView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(gameView);

            if (players == 3) {
                TextView tv = findViewById(R.id.textView8);
                tv.setText(R.string.player1);
                tv = findViewById(R.id.textView9);
                tv.setVisibility(View.VISIBLE);
                tv.setText(R.string.player2);
                tv = findViewById(R.id.textView7);
                tv.setText(R.string.player3);
            }

            if (players == 2) {
                TextView tv = findViewById(R.id.textView8);
                tv.setText(R.string.player1);
                tv = findViewById(R.id.textView7);
                tv.setText(R.string.player2);
            }
        }
    }

    public void newGame() {
        Intent k = new Intent(getApplicationContext(), GameActivity.class);
        k.putExtra("PLAYERS", players);
        k.putExtra("GRID_SIZE", grid_size);
        k.putExtra("DIFFICULTY", difficulty);
        player.release();
        finish();
        overridePendingTransition(0, 0);
        startActivity(k);
    }

    public void displayWinner(int winner) {
        String msg = "";
        if (players == 1) {
            if (winner == 1) msg = "Congratulations, You won!!";
            else if (winner == 2) msg = "Computer won!!";
            else if (winner == 0) msg = "I see, it's a tie!";
        } else {
            if (winner == 0) msg = "I see, it's a tie!";
            else if (winner == 1) msg = "Player 1 won!!";
            else if (winner == 2) msg = "Player 2 won!!";
            else if (winner == 3) msg = "Player 3 won!!";
        }
        new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(msg).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                newGame();
            }
        }).show();
    }

    public void undo(View view) {
        int size = gameView.moves.size();
        if (size == 0) return;
        //should undo (size-1)th move
        int toUndo = size - 1;
        Turn t = gameView.moves.get(toUndo);
        if (t.line.isHorizontal)
            gameView.horLine[t.line.row][t.line.column] = 0;
        else gameView.verLine[t.line.row][t.line.column] = 0;

        if (t.line.isHorizontal)
            gameView.board.horizontalLine[t.line.row][t.line.column] = 0;
        else gameView.board.verticalLine[t.line.row][t.line.column] = 0;

        for (int p = 0; p < t.boxes.size(); p++) {
            gameView.boxes[t.boxes.get(p).x][t.boxes.get(p).y] = 0;
            if(t.who==1)gameView.board.player1Score--;
            if(t.who==2)gameView.board.player2Score--;
            if(t.who==3)gameView.board.player3Score--;

        }
        for (int p = 0; p < t.boxes.size(); p++) {
            gameView.board.box[t.boxes.get(p).x][t.boxes.get(p).y] = 0;
        }

        gameView.board.currentPlayer = t.who;

        gameView.moves.remove(toUndo);
        gameView.invalidate();
        gameView.updateScores();
    }
}
