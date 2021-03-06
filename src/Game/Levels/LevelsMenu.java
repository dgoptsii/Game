package Game.Levels;

import Game.*;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class LevelsMenu  extends BasicGameState {

    private  String path = SetupGame.path;
    private Image background,level1,level2,level3,level4,level5, main;
    private MouseOverArea level1MOA,level2MOA,level3MOA,level4MOA,level5MOA,mainMOA;
    private Music music;

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        background = new Image(SetupGame.path+"menu_background_game_won.png");
        level1 = new Image(SetupGame.path+"level_1.png");
        level2 = new Image(SetupGame.path+"level_2.png");
        level3 = new Image(SetupGame.path+"level_3.png");
        level4 = new Image(SetupGame.path+"level_4.png");
        level5 = new Image(SetupGame.path+"level_5.png");

        level1MOA = new MouseOverArea(container, level1, 400, 100);
        level2MOA = new MouseOverArea(container, level2, 400, 200);
        level3MOA = new MouseOverArea(container, level3, 400, 300);
        level4MOA = new MouseOverArea(container, level4, 400, 400);
        level5MOA = new MouseOverArea(container, level5, 400, 500);


        main = new Image(path+"main_menu.png");
        mainMOA = new MouseOverArea(container, main,50,600);
        music = SetupGame.levelMusic;
}

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        background.draw(0,0,1100,700);
        level1.draw(385,100,300,80);
        level2.draw(400,200,300,80);
        level3.draw(400,300,300,80);
        level4.draw(400,400,300,80);
        level5.draw(400,500,300,80);
        main.draw(50,600, 300,40);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if(level1MOA.isMouseOver()&&Mouse.isButtonDown(0)){
            game.getState(2).init(container, game);
            game.enterState(2, new FadeOutTransition(),new FadeInTransition()); //level1
            SetupGame.level1Music.loop();
            SetupGame.level1Music.setVolume(0.2f);
        }
        if(level2MOA.isMouseOver()&&Mouse.isButtonDown(0)){
            game.getState(3).init(container, game);
            game.enterState(3, new FadeOutTransition(),new FadeInTransition()); //level2
            SetupGame.level2Music.loop();
            SetupGame.level2Music.setVolume(0.2f);
        }
        if(level3MOA.isMouseOver()&&Mouse.isButtonDown(0)){
            game.getState(4).init(container, game);
            game.enterState(4, new FadeOutTransition(),new FadeInTransition()); //level3
            SetupGame.level3Music.loop();
            SetupGame.level3Music.setVolume(0.2f);
        }
        if(level4MOA.isMouseOver()&&Mouse.isButtonDown(0)){
            game.getState(5).init(container, game);
            game.enterState(5, new FadeOutTransition(),new FadeInTransition()); //level4
            SetupGame.level4Music.loop();
            SetupGame.level4Music.setVolume(0.2f);
        }
        if(level5MOA.isMouseOver()&&Mouse.isButtonDown(0)){
            game.getState(6).init(container, game);
            game.enterState(6, new FadeOutTransition(),new FadeInTransition()); //level5
            SetupGame.level5Music.loop();
            SetupGame.level5Music.setVolume(0.2f);
        }
        if(container.getInput().isKeyDown(Input.KEY_ESCAPE)){
            game.enterState(0, new FadeOutTransition(),new FadeInTransition()); //main menu
        }
        if(mainMOA.isMouseOver()&& Mouse.isButtonDown(0)){

            game.enterState(0, new FadeOutTransition(), new FadeInTransition()); //if level5 then winner


        }
    }
}
