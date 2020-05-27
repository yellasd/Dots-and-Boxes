package com.example.dotsandboxes;

import java.util.ArrayList;
import java.util.Collections;

//TODO:more efficient evaluate function and also try to do stg for time complexity

class AdvancedPlayer {

    private EasyPlayer easyPlayer;

    private int evaluate(Board board) {
        int value = 0;

        //based on winner
        if (board.isGameCompleted()) {
            if (board.getWinner() == 2) value = 1000;
            else value = -1000;
        }

        /*//based on no.of boxes with 3 sides available
        if (isMax) value += board.noOfBoxesWithNSides(3);
        else value -= board.noOfBoxesWithNSides(3);*/

        return value;
    }

    Line getNextMove(Board board) {

        ArrayList<Line> possibleMoves = board.getRemainingMoves();
        Collections.shuffle(possibleMoves);
        int totalEdges = 2 * (board.size + 1) * board.size;
        easyPlayer = new EasyPlayer();
        if (possibleMoves.size() >= totalEdges / 4) return easyPlayer.getNextMove(board);
        int bestValue = -32760, k = -1, moveValue, alpha = -10000, beta = 10000, depth = 5;

        for (int i = 0; i < possibleMoves.size(); i++) {
            Line l = possibleMoves.get(i);
            Board tempBoard = board.getTempBoard();
            if (l.isHorizontal) tempBoard.setHorizontalLine(l.row, l.column, 2);
            else tempBoard.setVerticalLine(l.row, l.column, 2);
            if (tempBoard.player2Score == board.player2Score)
                moveValue = alphaBeta(tempBoard, alpha, beta, depth, false);
            else moveValue = alphaBeta(tempBoard, alpha, beta, depth, true);
            if (moveValue >= bestValue) {
                bestValue = moveValue;
                k = i;
            }
        }
        return possibleMoves.get(k);
    }

    private int alphaBeta(Board board, int alpha, int beta, int depth, boolean isMax) {
        ArrayList<Line> possibleMoves = board.getRemainingMoves();

        if (evaluate(board) == 1000 || evaluate(board) == -1000 ) return evaluate(board);

        if (isMax) {
            int bestValue = -10000, moveValue;
            for (int i = 0; i < possibleMoves.size(); i++) {
                Board tempBoard = board.getTempBoard();
                if (possibleMoves.get(i).isHorizontal)
                    tempBoard.setHorizontalLine(possibleMoves.get(i).row, possibleMoves.get(i).column, 2);
                else
                    tempBoard.setVerticalLine(possibleMoves.get(i).row, possibleMoves.get(i).column, 2);
                if (tempBoard.player2Score == board.player2Score)
                    moveValue = alphaBeta(tempBoard, alpha, beta, depth - 1, false);
                else moveValue = alphaBeta(tempBoard, alpha, beta, depth - 1, true);
                bestValue = Math.max(bestValue, moveValue);
                alpha = Math.max(bestValue, alpha);
                if (beta <= alpha) break;
            }
            return bestValue;
        } else {
            int bestValue = 10000, moveValue;
            for (int i = 0; i < possibleMoves.size(); i++) {
                Board tempBoard = board.getTempBoard();
                if (possibleMoves.get(i).isHorizontal)
                    tempBoard.setHorizontalLine(possibleMoves.get(i).row, possibleMoves.get(i).column, 1);
                else
                    tempBoard.setVerticalLine(possibleMoves.get(i).row, possibleMoves.get(i).column, 1);
                if (tempBoard.player1Score == board.player1Score)
                    moveValue = alphaBeta(tempBoard, alpha, beta, depth - 1, true);
                else moveValue = alphaBeta(tempBoard, alpha, beta, depth - 1, false);
                bestValue = Math.min(moveValue, bestValue);
                beta = Math.min(bestValue, beta);
                if (beta <= alpha) break;
            }
            return bestValue;
        }
    }
}
