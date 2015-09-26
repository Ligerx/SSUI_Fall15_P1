package f15.ssui.p1;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private int score = 0; // number of moves taken


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        // Register the onClick listener to detect when the users selects a new game.
        // When the user clicks this button, the game should reset the code to 0 and shuffle the
        // puzzle.
        Button newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get the score text
                TextView scoreText = (TextView) findViewById(R.id.scoreText);

                // Reset the score to zero
                updateScore(0);

                // Shuffle the puzzle
                GameBoard gameBoard = (GameBoard) findViewById(R.id.gameBoard);
                gameBoard.shuffler.shuffleBoard();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //// my data
        GameBoard gameBoard = (GameBoard) findViewById(R.id.gameBoard);

        // save order that tiles are currently in - blank tile case is handled
        outState.putIntegerArrayList("imageOrder", gameBoard.getCurrentImageOrder());

        // save score
        outState.putInt("score", score);

        // super
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        GameBoard gameBoard = (GameBoard) findViewById(R.id.gameBoard);
        gameBoard.setRestoredImageOrder(savedInstanceState.getIntegerArrayList("imageOrder"));

        // update score in memory and on the UI
        updateScore(savedInstanceState.getInt("score"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.show_solution) {
            showSolution();
        }
        else if(id == R.id.show_last_move) {
            showLastMove();
        }

        return super.onOptionsItemSelected(item);
    }


    public void showSolution() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        FrameLayout solution = (FrameLayout) inflater.inflate(R.layout.solution_popup, null);

        // Get full solution image from gameBoard
        GameBoard gameBoard = (GameBoard) findViewById(R.id.gameBoard);
        Bitmap image = gameBoard.imageSplitter.editedImage;

        // Set the edited image onto the layout
        ((ImageView) solution.getChildAt(0)).setImageBitmap(image);

        // Make popup a percentage of the screen size
        int windowHeight = getResources().getDisplayMetrics().widthPixels;
        int windowWidth = getResources().getDisplayMetrics().heightPixels;

        final PopupWindow popup = new PopupWindow(solution, (int) (windowHeight * 0.8), (int) (windowWidth * 0.8), true);

        // set background color, also automatically makes it close on outside click
        popup.setBackgroundDrawable(new ColorDrawable(0xcc000000));


        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popup.dismiss();
            }
        });

        popup.showAtLocation(findViewById(R.id.activity_game), Gravity.CENTER, 0, 0);
    }

    public void showLastMove() {
        // TODO Didn't get around to implementing this one
    }



    public void incrementScore() {
        updateScore(score + 1);
    }

    public void updateScore(int newScore) {
        TextView scoreText = (TextView) findViewById(R.id.scoreText);

        score = newScore;
        scoreText.setText("Score: " + score);
    }

}
