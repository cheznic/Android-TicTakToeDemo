package com.whimsygames.tictaktoedemo;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private enum Turn {PUMPKIN, SANTA, BLANK}

    private Turn turn = Turn.PUMPKIN;

    private boolean winner;

    private int[][][] winningSequences = {
            {{0, 0}, {0, 1}, {0, 2}},
            {{1, 0}, {1, 1}, {1, 2}},
            {{2, 0}, {2, 1}, {2, 2}},
            {{0, 0}, {1, 0}, {2, 0}},
            {{0, 1}, {1, 1}, {2, 1}},
            {{0, 2}, {1, 2}, {2, 2}},
            {{0, 0}, {1, 1}, {2, 2}},
            {{0, 2}, {1, 1}, {2, 0}}
    };

    private Turn[][] moves = new Turn[3][3];

    private ImageView[][] clickedImages = new ImageView[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        for (int i = 0; i < moves.length; i++) {
            for (int ii = 0; ii < moves[i].length; ii++) {
                moves[i][ii] = Turn.BLANK;
            }
        }

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.clear);
        for (ImageView[] imageRow : clickedImages) {
            for (ImageView image : imageRow) {
                if (image != null) {
                    image.setImageDrawable(drawable);
                }
            }
        }

        for (int i = 0 ; i < clickedImages.length ; i++) {
            for (int ii = 0 ; ii < clickedImages[i].length ; ii++) {
                clickedImages[i][ii] = null;
            }
        }

        this.winner = false;
    }

    protected void onImageClick(View view) {
        if (winner)
            return;

        int id = view.getId();
        String name = getResources().getResourceName(id);
        name = name.substring(name.length() - 2);

        int row = Integer.parseInt(name.substring(0, 1));
        int col = Integer.parseInt(name.substring(1, 2));

        if (moves[row][col] == Turn.BLANK) {
            moves[row][col] = turn;
        } else {
            return;
        }

        ImageView imageView = (ImageView) view;
        int[][] winningImages;

        Drawable drawable;
        if (turn.equals(Turn.PUMPKIN)) {
            drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.pumpkin);
            winningImages = checkIfWinner(row, col, turn);
            turn = Turn.SANTA;
        } else {
            drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.santa);
            winningImages = checkIfWinner(row, col, turn);
            turn = Turn.PUMPKIN;
        }

        imageView.setImageDrawable(drawable);
        clickedImages[row][col] = imageView;
        if (winningImages != null)
            celebrate(winningImages);
    }

    private void celebrate(int[][] cells) {
        String sequence = "";
        for (int[] cell : cells) {
            sequence += " " + cell[0] + "," + cell[1];
        }
        Log.i("info", sequence);

        for (int[] cell : cells) {
            clickedImages[cell[0]][cell[1]].animate().rotation(1080).setDuration(3000);
        }
    }

    private int[][] checkIfWinner(int row, int col, Turn turn) {
        boolean winner;
        int[] clickedCell = new int[]{row, col};

        for (int[][] winningSequence : winningSequences) {
            for (int[] cell : winningSequence) {

                if (cell[0] == clickedCell[0] && cell[1] == clickedCell[1]) {

                    winner = true;

                    for (int[] c : winningSequence)
                        if (moves[c[0]][c[1]] != turn)
                            winner = false;

                    if (winner) {
                        this.winner = true;
                        return winningSequence;
                    }
                }
            }
        }

        return null;
    }

    @SuppressWarnings("unused")
    protected void onPlayAgainClick(View view) {
        init();
    }
}
