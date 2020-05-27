package com.example.dotsandboxes;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    //to store board data
    private static final int PLAYER1 = 1;
    private static final int PLAYER2 = 2;//player2 is computer
    private static final int PLAYER3 = 3;
    private static final int NOT_MARKED = 0;
    int currentPlayer = 1;

    int size = 3; //size of the board(3*3)->9 boxes
    int player1Score = 0, player2Score = 0, player3Score = 0;//scores of both players->boxes count

    int[][] box;
    int[][] horizontalLine;
    int[][] verticalLine;

    Board(int size) {
        this.size = size;

        //allocate memory
        box = new int[size][size];
        horizontalLine = new int[size][size + 1];
        verticalLine = new int[size + 1][size];

        //assign elements
        for (int[] row : box)
            Arrays.fill(row, 0);
        for (int[] row : horizontalLine)
            Arrays.fill(row, 0);
        for (int[] row : verticalLine)
            Arrays.fill(row, 0);
    }

    ArrayList<Line> getRemainingMoves() {
        ArrayList<Line> remainingMoves = new ArrayList<>(0);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size + 1; j++) {
                if (horizontalLine[i][j] == NOT_MARKED)
                    remainingMoves.add(new Line(i, j, true));
                if (verticalLine[j][i] == NOT_MARKED)
                    remainingMoves.add(new Line(j, i, false));
            }
        return remainingMoves;
    }

    int getWinner() {
        if (player1Score > player2Score && player1Score > player3Score) return PLAYER1;
        else if (player2Score > player1Score && player2Score > player3Score) return PLAYER2;
        else if (player3Score > player1Score && player3Score > player2Score) return PLAYER3;
        else return 0;
    }

    Board getTempBoard() {
        Board tempBoard = new Board(size);

        tempBoard.player1Score = player1Score;
        tempBoard.player2Score = player2Score;

        for (int i = 0; i < size; i++)
            if (size + 1 >= 0)
                System.arraycopy(horizontalLine[i], 0, tempBoard.horizontalLine[i], 0, size + 1);

        for (int i = 0; i < size + 1; i++)
            System.arraycopy(verticalLine[i], 0, tempBoard.verticalLine[i], 0, size);

        for (int i = 0; i < size; i++)
            System.arraycopy(box[i], 0, tempBoard.box[i], 0, size);

        return tempBoard;
    }

    ArrayList<Point> setHorizontalLine(int i, int j, int who) {
        horizontalLine[i][j] = who;
        ArrayList<Point> temp = new ArrayList<>();
        //need to check for top and bottom
        //for bottom box
        if (j < size) {
            if (verticalLine[i][j] != NOT_MARKED && verticalLine[i + 1][j] != NOT_MARKED && horizontalLine[i][j + 1] != NOT_MARKED) {
                box[i][j] = who;
                temp.add(new Point(i, j));
                if (who == PLAYER1) player1Score++;
                else if (who == PLAYER2) player2Score++;
                else if (who == PLAYER3) player3Score++;
            }
        }
        //for top box
        if (j > 0) {
            if (horizontalLine[i][j - 1] != NOT_MARKED && verticalLine[i][j - 1] != NOT_MARKED && verticalLine[i + 1][j - 1] != NOT_MARKED) {
                box[i][j - 1] = who;
                temp.add(new Point(i, j - 1));
                if (who == PLAYER2) player2Score++;
                else if (who == PLAYER1) player1Score++;
                else if (who == PLAYER3) player3Score++;
            }
        }
        return temp;
    }

    ArrayList<Point> setVerticalLine(int i, int j, int who) {
        verticalLine[i][j] = who;
        ArrayList<Point> temp = new ArrayList<>();
        //for left
        if (i > 0) {
            if (verticalLine[i - 1][j] != NOT_MARKED && horizontalLine[i - 1][j] != NOT_MARKED && horizontalLine[i - 1][j + 1] != NOT_MARKED) {
                box[i - 1][j] = who;
                temp.add(new Point(i - 1, j));
                if (who == PLAYER1) player1Score++;
                else if (who == PLAYER2) player2Score++;
                else if (who == PLAYER3) player3Score++;
            }
        }
        //for right box
        if (i < size) {
            if (horizontalLine[i][j] != NOT_MARKED && horizontalLine[i][j + 1] != NOT_MARKED && verticalLine[i + 1][j] != NOT_MARKED) {
                box[i][j] = who;
                temp.add(new Point(i, j));
                if (who == PLAYER2) player2Score++;
                else if(who==PLAYER1) player1Score++;
                else if(who==PLAYER3) player3Score++;
            }
        }
        return temp;
    }


    //returns no of sides of a box that are marked
    int markedLinesOfABox(int i, int j) {
        int markedLines = 0;
        if (horizontalLine[i][j] != NOT_MARKED) markedLines++;
        if (horizontalLine[i][j + 1] != NOT_MARKED) markedLines++;
        if (verticalLine[i][j] != NOT_MARKED) markedLines++;
        if (verticalLine[i + 1][j] != NOT_MARKED) markedLines++;
        return markedLines;
    }

    boolean isGameCompleted() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (box[i][j] == NOT_MARKED) return false;

        return true;
    }

    int noOfBoxesWithNSides(int n) {
        int x = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (markedLinesOfABox(i, j) == n) x++;
            }
        return x;
    }

    int noOfBoxesMarkedByPlayer(int who) {
        int x = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (box[i][j] == who) x++;
        return x;
    }
}
