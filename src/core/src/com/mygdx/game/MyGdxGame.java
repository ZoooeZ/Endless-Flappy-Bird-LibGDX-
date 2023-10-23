package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;

	int flapState = 0;
	int flapInterval = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	BitmapFont font;

	//ShapeRenderer shapeRenderer;
	Circle birdCircle;
	Rectangle[] toptubeRectangles;
	Rectangle[] bottomtubeRectangles;

	Texture bottomtube;
	Texture toptube;

	int gap = 800;

	Random rand;

	float maxOffset;
	float tubeVelocity = 3;
	int numberOfTubes = 4;
	float[] tubeOffset = new float[numberOfTubes];
	float[] tubeX = new float[numberOfTubes];
	float distanceBetweenTubes;

	int score = 0;
	int scoringTube = 0;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");

		birds = new Texture[3];
		birds[0] = new Texture("bird1.png");
		birds[1] = new Texture("bird2.png");
		birds[2] = new Texture("bird3.png");

		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(8);


		birdY = Gdx.graphics.getHeight()/2 - birds[flapState].getHeight()/2;

		bottomtube = new Texture("bottomtube.png");
		toptube = new Texture("toptube.png");
		rand = new Random();
		maxOffset = Gdx.graphics.getHeight()/2 - toptube.getHeight()/2 - 100;
		toptubeRectangles = new Rectangle[numberOfTubes];
		bottomtubeRectangles = new Rectangle[numberOfTubes];

		distanceBetweenTubes = Gdx.graphics.getWidth()/2;

		for (int i = 0; i < numberOfTubes; i++){
			tubeOffset[i] = (rand.nextFloat() - 0.5f)* maxOffset * 2;
			tubeX[i] = Gdx.graphics.getWidth()/2 - toptube.getWidth()/2 +Gdx.graphics.getWidth() + i*distanceBetweenTubes;
			toptubeRectangles[i] = new Rectangle();
			bottomtubeRectangles[i] = new Rectangle();

		}




	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState != 0) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2){
				score++;
				Gdx.app.log("Score:",String.valueOf(score) );
				if (scoringTube < numberOfTubes - 1){
					scoringTube++;
				}else {
					scoringTube = 0;
				}
			}

			if (Gdx.input.justTouched()){
				velocity = -20;
			}
			for (int i = 0; i< numberOfTubes; i++) {
				if(tubeX[i]< -bottomtube.getWidth()){
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (rand.nextFloat() - 0.5f)* maxOffset * 2;

				}else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}
				batch.draw(bottomtube, tubeX[i], 0, bottomtube.getWidth() * 3, bottomtube.getHeight() * 2 + tubeOffset[i]);
				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], toptube.getWidth() * 3, toptube.getHeight() * 2 + 400);
				toptubeRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth()*3,toptube.getHeight() * 2 + 400);
				bottomtubeRectangles[i] = new Rectangle(tubeX[i],0,bottomtube.getWidth() * 3,bottomtube.getHeight() * 2 + tubeOffset[i]);
			}

			velocity ++;
			birdY -= velocity;
			if (birdY < 0){
				birdY = 0;
			}


		}else {
			if (Gdx.input.justTouched()){
				gameState = 1;
			}
		}
		//飞起来
		if (flapInterval < 4) {
			flapInterval++;
		} else {
			flapInterval = 0;
			if (flapState == 0) {
				flapState = 1;
			} else if (flapState == 1) {
				flapState = 2;
			} else if (flapState == 2) {
				flapState = 0;
			}
		}


		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY,birds[flapState].getWidth()*2,birds[flapState].getHeight()*2);
		font.draw(batch,String.valueOf(score),100,200);

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for (int i = 0; i< numberOfTubes; i++){
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth()*3,toptube.getHeight() * 2 + 400);
			//shapeRenderer.rect(tubeX[i],0,bottomtube.getWidth() * 3,bottomtube.getHeight() * 2 + tubeOffset[i]);

			if (Intersector.overlaps(birdCircle,toptubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomtubeRectangles[i])){

				Gdx.app.log("collision","kapooooooski");
			}
		}

		//shapeRenderer.end();

		batch.end();

	}
}
