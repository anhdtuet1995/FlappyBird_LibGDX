package com.patent.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.patent.flappybird.FlappyBird;
import com.patent.flappybird.sprites.Bird;
import com.patent.flappybird.sprites.Tube;


public class PlayState extends State {

    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -30;
    private static final int GAMEOVER_GAP = 50;

    private Bird bird;
    private Texture bg;
    private Array<Tube> tubes;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private int point = 0;

    private BitmapFont score;
    private Texture playAgain;
    private Texture gameOver;
    private boolean isGameOver = false;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        score = new BitmapFont(false);
        score.setColor(Color.WHITE);
        bird = new Bird(50, 300);
        playAgain = new Texture("playbtn.png");
        gameOver = new Texture("gameover.png");
        bg = new Texture("bg.png");
        camera.setToOrtho(false, (float) FlappyBird.WIDTH / 2, (float) FlappyBird.HEIGHT / 2);

        ground = new Texture("ground.png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);

        tubes = new Array<>();
        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            if (isGameOver) {
                isGameOver = false;
                gsm.set(new PlayState(gsm));
            } else {
                bird.jump();
            }

        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        if (isGameOver) return;
        updateGround();
        bird.update(dt);
        camera.position.x = bird.getPosition().x + 80;

        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);
            if (camera.position.x - (camera.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + (Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT);
            }

            if (tube.collides(bird.getBounds())) {
                isGameOver = true;
            }

            if (tube.increasePoint(bird.getBounds())) {
                point++;
            }
        }

        if (bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
            isGameOver = true;
        }
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth / 2), 0);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBottomTube().x, tube.getPosBottomTube().y);
        }
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        score.draw(sb, "" + point, camera.position.x, 400);
        if (isGameOver) {
            float gameOverY = camera.position.y + (float) gameOver.getHeight() / 2 + (float) GAMEOVER_GAP / 2;
            sb.draw(gameOver, camera.position.x - (float) gameOver.getWidth() / 2, gameOverY);
            sb.draw(playAgain, camera.position.x - (float) playAgain.getWidth() / 2, gameOverY - (float) GAMEOVER_GAP - (float) playAgain.getHeight() / 2);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        ground.dispose();
        score.dispose();
        playAgain.dispose();
        gameOver.dispose();
        for (Tube tube: tubes) {
            tube.dispose();
        }
        System.out.println("Play State Disposed");
    }

    private void updateGround() {
        if (camera.position.x - camera.viewportWidth / 2 > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }
        if (camera.position.x - camera.viewportWidth / 2 > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }
}
