package com.example.dotsandboxes;
/*Implements a very general strategy.
First tries to make a move in a box with three sides,else in a box with one side,no side,
atlast makes a move in a box with two sides*/

import com.example.dotsandboxes.Board;
import com.example.dotsandboxes.Line;

import java.util.ArrayList;
import java.util.Collections;

//TODO: Implement double crossing strategy later and some other game specific techniques and create a medium level player
class EasyPlayer {

    Line getNextMove(Board board) {

        ArrayList<Line> availableMoves = board.getRemainingMoves();
        Collections.shuffle(availableMoves);
        int p=availableMoves.size();
        //for fourth side of a box
        for (int i = 0; i < availableMoves.size(); i++) {
            //create a temporary board because if you refer to the same board changes will not be made
            Board temp = board.getTempBoard();
            if (availableMoves.get(i).isHorizontal)
                temp.setHorizontalLine(availableMoves.get(i).row, availableMoves.get(i).column, 2);
            else temp.setVerticalLine(availableMoves.get(i).row, availableMoves.get(i).column, 2);

            if (temp.player2Score > board.player2Score) return availableMoves.get(i);
        }
        //for second side of a box
        for (int i = 0; i < availableMoves.size(); i++) {
            Board temp = board.getTempBoard();
            Line l = availableMoves.get(i);
            if (l.isHorizontal) {
                temp.setHorizontalLine(l.row, l.column, 2);
                if (l.column < temp.size && l.column > 0)
                    if (temp.markedLinesOfABox(l.row, l.column) != 3 && temp.markedLinesOfABox(l.row, l.column - 1) != 3)
                        return l;
                if (l.column == 0 && temp.markedLinesOfABox(l.row, l.column) != 3)
                    return l;
                if (l.column == temp.size && temp.markedLinesOfABox(l.row, l.column - 1) != 3)
                    return l;
            } else {
                temp.setVerticalLine(l.row, l.column, 2);
                if (l.row < temp.size && l.row > 0)
                    if (temp.markedLinesOfABox(l.row, l.column) != 3 && temp.markedLinesOfABox(l.row - 1, l.column) != 3)
                        return l;
                if (l.row == 0 && temp.markedLinesOfABox(l.row, l.column) != 3)
                    return l;
                if (l.row == temp.size && temp.markedLinesOfABox(l.row - 1, l.column) != 3)
                    return l;
            }
        }
        return availableMoves.get((int) (Math.random() * p));
    }
}
