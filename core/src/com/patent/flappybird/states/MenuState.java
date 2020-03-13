package com.patent.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.patent.flappybird.FlappyBird;

public class MenuState extends State {

    private Texture background;
    private Texture playBtn;
    private BitmapFont font;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, (float) FlappyBird.WIDTH / 2, (float) FlappyBird.HEIGHT / 2);
        background = new Texture("bg.png");
        playBtn = new Texture("playbtn.png");
        font = new BitmapFont();
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        font.setColor(Color.BLACK);
        font.draw(sb, "Hello", 250, 250);
        sb.draw(background, 0, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT);
        float xPlayBtn = camera.position.x - (float) playBtn.getWidth() / 2;
        float yPlayBtn = camera.position.y;
        sb.draw(playBtn, xPlayBtn, yPlayBtn);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        System.out.println("Menu State Disposed");
    }
}
