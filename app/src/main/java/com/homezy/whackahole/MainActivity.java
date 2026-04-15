package com.homezy.whackamole;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GridLayout grid;
    ImageView[] holes = new ImageView[9];

    TextView scoreText, lifeText;
    Button startBtn;

    int score = 0;
    int lives = 3;
    int activeIndex = -1;

    boolean gameRunning = false;

    Handler handler = new Handler();
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = findViewById(R.id.grid);
        scoreText = findViewById(R.id.scoreText);
        lifeText = findViewById(R.id.lifeText);
        startBtn = findViewById(R.id.startBtn);

        createGrid();

        startBtn.setOnClickListener(v -> {
            startGame();
            gameRunning = true;
        });
    }

    void createGrid() {
        for (int i = 0; i < 9; i++) {

            ImageView hole = new ImageView(this);
            hole.setBackgroundResource(R.drawable.hole_bg);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 250;
            params.height = 250;
            params.setMargins(20, 20, 20, 20);

            hole.setLayoutParams(params);

            final int index = i;

            hole.setOnClickListener(v -> {

                if (!gameRunning) return;

                // tap animation
                hole.setScaleX(0.8f);
                hole.setScaleY(0.8f);

                hole.postDelayed(() -> {
                    hole.setScaleX(1f);
                    hole.setScaleY(1f);
                }, 100);

                if (index == activeIndex) {
                    score++;
                    scoreText.setText("Score: " + score);

                    hole.setImageDrawable(null);
                    activeIndex = -1;
                } else {
                    lives--;
                    updateLives();

                    if (lives == 0) {
                        gameRunning = false;
                        scoreText.setText("Game Over! Score: " + score);
                    }
                }
            });

            holes[i] = hole;
            grid.addView(hole);
        }
    }

    void startGame() {
        score = 0;
        lives = 3;

        scoreText.setText("Score: 0");
        updateLives();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (gameRunning) {
                    showMole();
                    handler.postDelayed(this, 800);
                }
            }
        }, 800);
    }

    void showMole() {

        for (ImageView hole : holes) {
            hole.setImageDrawable(null);
        }

        activeIndex = random.nextInt(9);
        holes[activeIndex].setImageResource(R.drawable.mole);
    }

    void updateLives() {
        String hearts = "";
        for (int i = 0; i < lives; i++) {
            hearts += "❤️";
        }
        lifeText.setText(hearts);
    }
}