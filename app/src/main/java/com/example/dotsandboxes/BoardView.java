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

import com.example.dotsandboxes.Board;

import java.util.ArrayList;
import java.util.Collections;

public class BoardView extends View {

    private static final int NOT_MARKED = 0;
    private static final int PLAYER1 = 1;
    private static final int PLAYER2 = 2;
    private static final int PLAYER3 = 3;

    private Paint player1linePaint, player2linePaint, linePaint;
    private Paint boxPaint, player1boxPaint, player2boxPaint;
    GameActivity gameActivity;
    private Board board;
    private Paint dotPaint;
    private PointF[][] dot;
    int size = 4;
    int players = 1;
    int difficulty = 0;
    //private int currentPlayer = 1;
    private boolean isPlayer1 = true;
    private int[][] horLine, verLine, boxes;
    private EasyPlayer easyPlayer;
    private AdvancedPlayer advancedPlayer;

    public BoardView(Context context, int players, int grid_size, int difficulty) {
        super(context);
        this.players = players;
        size = grid_size;
        if (players == 1) size = 4;
        this.difficulty = difficulty;

        board = new Board(size);//new board
        easyPlayer = new EasyPlayer();
        advancedPlayer = new AdvancedPlayer();

        //configure paints
        player1linePaint = new Paint();
        player1linePaint.setColor(Color.parseColor("#FB0577"));
        player1linePaint.setStyle(Paint.Style.STROKE);
        player1linePaint.setStrokeWidth(getResources().getDisplayMetrics().density * 4f);

        player2linePaint = new Paint();
        player2linePaint.setColor(Color.parseColor("#1565C0"));
        player2linePaint.setStyle(Paint.Style.STROKE);
        player2linePaint.setStrokeWidth(getResources().getDisplayMetrics().density * 4f);

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#ffffff"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(getResources().getDisplayMetrics().density * 2f);

        boxPaint = new Paint();
        boxPaint.setStyle(Paint.Style.FILL);
        boxPaint.setColor(Color.parseColor("#757575"));

        player1boxPaint = new Paint();
        player1boxPaint.setStyle(Paint.Style.FILL);
        player1boxPaint.setColor(Color.parseColor("#FEB4D6"));

        player2boxPaint = new Paint();
        player2boxPaint.setStyle(Paint.Style.FILL);
        player2boxPaint.setColor(Color.parseColor("#64B5F6"));

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

    public BoardView(Context context, @Nullable AttributeSet attrs) {
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
                if (horLine[i][j] == NOT_MARKED)
                    canvas.drawLine(dot[i][j].x, dot[i][j].y, dot[i + 1][j].x, dot[i + 1][j].y, linePaint);
                else if (horLine[i][j] == PLAYER1)
                    canvas.drawLine(dot[i][j].x, dot[i][j].y, dot[i + 1][j].x, dot[i + 1][j].y, player1linePaint);
                else if (horLine[i][j] == PLAYER2)
                    canvas.drawLine(dot[i][j].x, dot[i][j].y, dot[i + 1][j].x, dot[i + 1][j].y, player2linePaint);
            }

        //vertical
        for (int i = 0; i < size + 1; i++)
            for (int j = 0; j < size; j++) {
                if (verLine[i][j] == NOT_MARKED)
                    canvas.drawLine(dot[i][j].x, dot[i][j].y, dot[i][j + 1].x, dot[i][j + 1].y, linePaint);
                else if (verLine[i][j] == PLAYER1)
                    canvas.drawLine(dot[i][j].x, dot[i][j].y, dot[i][j + 1].x, dot[i][j + 1].y, player1linePaint);
                else if (verLine[i][j] == PLAYER2)
                    canvas.drawLine(dot[i][j].x, dot[i][j].y, dot[i][j + 1].x, dot[i][j + 1].y, player2linePaint);
            }
    }

    private void drawBoxes(Canvas canvas) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (boxes[i][j] == NOT_MARKED)
                    canvas.drawRect(dot[i][j].x, dot[i][j].y, dot[i + 1][j].x, dot[i][j + 1].y, boxPaint);
                else if (boxes[i][j] == PLAYER1)
                    canvas.drawRect(dot[i][j].x, dot[i][j].y, dot[i + 1][j].x, dot[i][j + 1].y, player1boxPaint);
                else if (boxes[i][j] == PLAYER2)
                    canvas.drawRect(dot[i][j].x, dot[i][j].y, dot[i + 1][j].x, dot[i][j + 1].y, player2boxPaint);
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
                if (tempVertical.contains(current.x, current.y) && verLine[i][j] == NOT_MARKED && isPlayer1 && !isGameCompleted()) {
                    setEnabled(false);
                    verticalLineManager(i, j, PLAYER1);
                    updateScores();

                    while (!isPlayer1 && !isGameCompleted()) {
                        Line l;
                        if (difficulty == 1) l = advancedPlayer.getNextMove(board);
                        else l = easyPlayer.getNextMove(board);
                        if (l.isHorizontal)
                            horizontalLineManager(l.row, l.column, PLAYER2);
                        else verticalLineManager(l.row, l.column, PLAYER2);
                    }
                    updateScores();
                    invalidate();
                    setEnabled(true);
                    if (isGameCompleted()) endGame();
                    return true;
                }
            }

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size + 1; j++) {
                RectF tempHorizontal = new RectF(dot[i][j].x + 10, dot[i][j].y - 24, dot[i + 1][j].x - 10, dot[i][j].y + 24);
                if (tempHorizontal.contains(current.x, current.y) && horLine[i][j] == NOT_MARKED && isPlayer1 && !isGameCompleted()) {
                    setEnabled(false);
                    horizontalLineManager(i, j, PLAYER1);
                    updateScores();

                    while (!isPlayer1 && !isGameCompleted()) {
                        Line l;
                        if (difficulty == 1) l = advancedPlayer.getNextMove(board);
                        else l = easyPlayer.getNextMove(board);
                        if (l.isHorizontal)
                            horizontalLineManager(l.row, l.column, PLAYER2);
                        else verticalLineManager(l.row, l.column, PLAYER2);
                    }
                    updateScores();
                    invalidate();
                    setEnabled(true);
                    if (isGameCompleted()) endGame();
                    return true;
                }
            }
        return super.onTouchEvent(event);
    }

    private void endGame() {
        gameActivity.displayWinner(board.getWinner());
    }

    private void updateScores() {
        gameActivity.player1Score.setText(getResources().getString(R.string.score1, board.player1Score));
        gameActivity.player2Score.setText(getResources().getString(R.string.score2, board.player2Score));
    }

    private void horizontalLineManager(int i, int j, int who) {
        ArrayList<Point> temp;
        horLine[i][j] = who;
        temp = board.setHorizontalLine(i, j, who);
        for (int p = 0; p < temp.size(); p++)
            boxes[temp.get(p).x][temp.get(p).y] = who;
        if (temp.size() == 0) isPlayer1 = !isPlayer1;
    }

    private void verticalLineManager(int i, int j, int who) {
        ArrayList<Point> temp;
        verLine[i][j] = who;
        temp = board.setVerticalLine(i, j, who);
        for (int p = 0; p < temp.size(); p++)
            boxes[temp.get(p).x][temp.get(p).y] = who;
        if (temp.size() == 0) isPlayer1 = !isPlayer1;
    }

    public boolean isGameCompleted() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (boxes[i][j] == NOT_MARKED) return false;

        return true;
    }
}
