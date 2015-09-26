package f15.ssui.p1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private int score = 0; // number of moves taken


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        Button newGameButton = (Button) findViewById(R.id.newGameButton);

        // Register the onClick listener to detect when the users selects a new game.
        // When the user clicks this button, the game should reset the code to 0 and shuffle the
        // puzzle.
        newGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get the score text
                TextView scoreText = (TextView) findViewById(R.id.scoreText);

                // Reset the score to zero
                score = 0;
                scoreText.setText("Score: " + score);

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

        // save order that tiles are currently in
        outState.putIntegerArrayList("imageOrder", gameBoard.getCurrentImageOrder());

        // save where the blank tile is
        // FIXME board should just draw the tile with img# 15, making this unnecessary
//        outState.putInt(gameBoard.getBlankTile().getImgNum());

        // TODO save the score
        // make sure to set score on restore state.

        // save
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        GameBoard gameBoard = (GameBoard) findViewById(R.id.gameBoard);
        gameBoard.setRestoredImageOrder(savedInstanceState.getIntegerArrayList("imageOrder"));
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void incrementScore() {
        TextView scoreText = (TextView) findViewById(R.id.scoreText);

        score++;
        scoreText.setText("Score: " + score);
    }

}
