package com.example.dotsandboxes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GameView extends View {

    private static final int NOT_MARKED = 0;
   // private static final int PLAYER1 = 1;
    //private static final int PLAYER2 = 2;
    //private static final int PLAYER3 = 3;

    private Paint[] playerLinePaint, playerBoxPaint;
    ArrayList<Turn> moves;
    GameActivity gameActivity;
    Board board;
    private Paint dotPaint;
    private PointF[][] dot;
    int size = 4;
    int players = 2;
    int[][] horLine, verLine, boxes;
    int turn=0;

    public GameView(Context context, int players, int grid_size) {
        super(context);
        this.players = players;
        size = grid_size;
        board = new Board(size);//new board
        moves=new ArrayList<>();

        playerLinePaint=new Paint[4];
        playerBoxPaint=new Paint[4];

        //configure paints
        playerLinePaint[1] = new Paint();
        playerLinePaint[1].setColor(Color.parseColor("#FB0577"));
        playerLinePaint[1].setStyle(Paint.Style.STROKE);
        playerLinePaint[1].setStrokeWidth(getResources().getDisplayMetrics().density * 4f);

        playerLinePaint[2] = new Paint();
        playerLinePaint[2].setColor(Color.parseColor("#1565C0"));
        playerLinePaint[2].setStyle(Paint.Style.STROKE);
        playerLinePaint[2].setStrokeWidth(getResources().getDisplayMetrics().density * 4f);

        playerLinePaint[3] = new Paint();
        playerLinePaint[3].setColor(Color.parseColor("#00E676"));
        playerLinePaint[3].setStyle(Paint.Style.STROKE);
        playerLinePaint[3].setStrokeWidth(getResources().getDisplayMetrics().density * 4f);

        playerLinePaint[0] = new Paint();
        playerLinePaint[0].setColor(Color.parseColor("#ffffff"));
        playerLinePaint[0].setStyle(Paint.Style.STROKE);
        playerLinePaint[0].setStrokeWidth(getResources().getDisplayMetrics().density * 2f);

        playerBoxPaint[0] = new Paint();
        playerBoxPaint[0].setStyle(Paint.Style.FILL);
        playerBoxPaint[0].setColor(Color.parseColor("#757575"));

        playerBoxPaint[1] = new Paint();
        playerBoxPaint[1].setStyle(Paint.Style.FILL);
        playerBoxPaint[1].setColor(Color.parseColor("#FEB4D6"));

        playerBoxPaint[2] = new Paint();
        playerBoxPaint[2].setStyle(Paint.Style.FILL);
        playerBoxPaint[2].setColor(Color.parseColor("#64B5F6"));

        playerBoxPaint[3] = new Paint();
        playerBoxPaint[3].setStyle(Paint.Style.FILL);
        playerBoxPaint[3].setColor(Color.parseColor("#B9F6CA"));

        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setColor(Color.parseColor("#ffffff"));

        //initialize dot array
        dot = new PointF[size + 1][size + 1];
        for (int i = 0; i < size + 1; i++)
            for (int j = 0; j < size + 1; j++)
                dot[i][j] = new PointF(0, 0);

        horLine = new int[size][size + 1];
        verLine = new int[size + 1][size];
        boxes = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size + 1; j++)
                horLine[i][j] = NOT_MARKED;

        for (int i = 0; i < size + 1; i++)
            for (int j = 0; j < size; j++)
                verLine[i][j] = NOT_MARKED;

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                boxes[i][j] = NOT_MARKED;

    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);

        for (int i = 0; i < size + 1; i++)
            for (int j = 0; j < size + 1; j++) {
                dot[i][j].x = 26 + i * (float) (width - 52) / (float) (size);
                dot[i][j].y = 26 + j * (float) (width - 52) / (float) (size);
            }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBoxes(canvas);
        drawLines(canvas);
        drawDots(canvas);
    }

    private void drawDots(Canvas canvas) {
        for (int i = 0; i < size + 1; i++)
            for (int j = 0; j < size + 1; j++)
                canvas.drawCircle(dot[i][j].x, dot[i][j].y, 10, dotPaint);
    }

    private void drawLines(Canvas canvas) {
        //horizontal
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size + 1; j++) {
                    canvas.drawLine(dot[i][j].x, dot[i][j].y, dot[i + 1][j].x, dot[i + 1][j].y, playerLinePaint[horLine[i][j]]);
            }

        //vertical
        for (int i = 0; i < size + 1; i++)
            for (int j = 0; j < size; j++) {
                    canvas.drawLine(dot[i][j].x, dot[i][j].y, dot[i][j + 1].x, dot[i][j + 1].y, playerLinePaint[verLine[i][j]]);
            }
    }

    private void drawBoxes(Canvas canvas) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                    canvas.drawRect(dot[i][j].x, dot[i][j].y, dot[i + 1][j].x, dot[i][j + 1].y, playerBoxPaint[boxes[i][j]]);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return false;

        PointF current = new PointF();
        current.x = event.getX();
        current.y = event.getY();

        for (int i = 0; i < size + 1; i++)
            for (int j = 0; j < size; j++) {
                RectF tempVertical = new RectF(dot[i][j].x - 24, dot[i][j].y + 10, dot[i][j].x + 24, dot[i][j + 1].y - 10);
                if (tempVertical.contains(current.x, current.y) && verLine[i][j] == NOT_MARKED && !isGameCompleted()) {
                    verticalLineManager(i, j, board.currentPlayer);
                    updateScores();
                    invalidate();
                    if (isGameCompleted()) endGame();
                    return true;
                }
            }

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size + 1; j++) {
                RectF tempHorizontal = new RectF(dot[i][j].x + 10, dot[i][j].y - 24, dot[i + 1][j].x - 10, dot[i][j].y + 24);
                if (tempHorizontal.contains(current.x, current.y) && horLine[i][j] == NOT_MARKED && !isGameCompleted()) {
                    horizontalLineManager(i,j,board.currentPlayer);
                    updateScores();
                    invalidate();
                    if (isGameCompleted()) endGame();
                    return true;
                }
            }

        return false;
    }

    private void endGame() {
        gameActivity.displayWinner(board.getWinner());
    }

    void updateScores() {
        gameActivity.player1Score.setText(getResources().getString(R.string.score1, board.player1Score));
        gameActivity.player2Score.setText(getResources().getString(R.string.score2, board.player2Score));
        if(players==3){
            gameActivity.player3Score.setText(getResources().getString(R.string.score3,board.player3Score));
            gameActivity.player1Score.setText(getResources().getString(R.string.score1, board.player1Score));
            gameActivity.player2Score.setText(getResources().getString(R.string.score2, board.player2Score));
        }
    }

    public boolean isGameCompleted() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (boxes[i][j] == NOT_MARKED) return false;

        return true;
    }

    private void horizontalLineManager(int i, int j, int who) {
        turn++;
        ArrayList<Point> temp;
        horLine[i][j] = who;
        temp = board.setHorizontalLine(i, j, who);
        for (int p = 0; p < temp.size(); p++)
            boxes[temp.get(p).x][temp.get(p).y] = who;
        if (temp.size() == 0 && players==3) {
            if(board.currentPlayer==1)board.currentPlayer=2;
            else if(board.currentPlayer==2)board.currentPlayer=3;
            else if(board.currentPlayer==3)board.currentPlayer=1;
        }
        if (temp.size() == 0 && players==2) {
            if(board.currentPlayer==1)board.currentPlayer=2;
            else if(board.currentPlayer==2)board.currentPlayer=1;
        }
        Turn t=new Turn(new Line(i,j,true),who);
        t.boxes=temp;
        moves.add(t);
    }

    private void verticalLineManager(int i, int j, int who) {
        turn++;
        ArrayList<Point> temp;
        verLine[i][j] = who;
        temp = board.setVerticalLine(i, j, who);
        for (int p = 0; p < temp.size(); p++)
            boxes[temp.get(p).x][temp.get(p).y] = who;
        if (temp.size() == 0 && players==3) {
            if(board.currentPlayer==1)board.currentPlayer=2;
            else if(board.currentPlayer==2)board.currentPlayer=3;
            else if(board.currentPlayer==3)board.currentPlayer=1;
        }
        if (temp.size() == 0 && players==2) {
            if(board.currentPlayer==1)board.currentPlayer=2;
            else if(board.currentPlayer==2)board.currentPlayer=1;
        }
        Turn t=new Turn(new Line(i,j,false),who);
        t.boxes=temp;
        moves.add(t);
    }
}
