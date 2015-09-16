package f15.ssui.p1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * The main activity class acting as the controller for the puzzle game. It handles the events for
 * the game layout and implements the game logic.
 *
 * @author Nikola (nbanovic@cs.cmu.edu)
 */
public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        // Find the new game button in the layout.
        Button newGameButton = (Button) findViewById(R.id.newGameButton);

        // Register the onClick listener to detect when the users selects a new game.
        // When the user clicks this button, the game should reset the code to 0 and shuffle the
        // puzzle.
        newGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Reset the score.
                TextView scoreText = (TextView) findViewById(R.id.scoreText);
                // TODO: reset the score to zero here using the scoreText variable.

                // TODO: Put your code that shuffles the puzzle here.
            }
        });

//        setContentView(R.layout.activity_game);
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
}
