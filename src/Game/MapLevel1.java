package Game;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;


public class MapLevel1 extends BasicGameState {
    private Babka babka;
    private Rectangle terrain;
    private Rectangle platform2;
    private Rectangle leftWall,rightWall,firstFloor,secondFloor,roof;
    private Image background,wall,wallpaper,window,sofa,table,wardrobe,cupboard,nightstand,rockingChair,  doorDown,  doorUp;
    private SpriteSheet leftW,rightW,firstF,secondF,roofF, wallpaper1;
    private Rectangle attackZone;
    private Coronavirus corona;
    private Doctor doctor;
    private Teleport upperTeleport, lowerTeleport;

    private Door door;

    private ArrayList<Injection> injections = new ArrayList<Injection>();
    private int timePassed;


    private static final int GROUND=80;
    private int wallW =10;
    private int floorH =225, floorW =900;
    private int x_offset=100;

    private String path = "/Users/dgoptsii/Game/pictures/";

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

        babka = new Babka(900, 300, 50, 50);

        initDoors();
        initEnemies();
        initAttackZone();
        initWalls();
    }

    private void initAttackZone(){
        attackZone = new Rectangle(-50, -50, 50, 50);
    }

    private void initWalls()throws SlickException {
        background = new Image(path+"backg.jpg");

        terrain = new Rectangle(0, SetupGame.height-10, SetupGame.width, 10);
        platform2 = new Rectangle(0, 0, 20, SetupGame.height);

        wall = new Image(path+"wall.jpg");
        leftWall = new Rectangle(x_offset- wallW,SetupGame.height- floorH *2, wallW, floorH *2);
        rightWall = new Rectangle(SetupGame.width-x_offset,SetupGame.height- floorH *2-80, wallW, floorH *2);
        firstFloor = new Rectangle(x_offset,SetupGame.height- wallW, floorW, wallW);
        secondFloor = new Rectangle(x_offset,SetupGame.height- floorH - wallW, floorW, wallW);
        roof = new Rectangle(x_offset,SetupGame.height- floorH *2- wallW *2, floorW, wallW *2);

        leftW = new SpriteSheet(wall,10,10);
        rightW = new SpriteSheet(wall,10,10);
        firstF = new SpriteSheet(wall,10,10);
        secondF = new SpriteSheet(wall,10,10);
        roofF = new SpriteSheet(wall,50,50);

        wallpaper = new Image(path+"wallpaper.jpg");
        wallpaper1 = new SpriteSheet(wallpaper,100,100);

        window = new Image(path+"window.jpg");
        sofa = new Image(path+"sofa.png");
        table = new Image(path+"table.png");
        wardrobe = new Image(path+"wardrobe.png");
        cupboard = new Image(path+"cupboard.png");
        nightstand = new Image(path+"nightstand.png");
        rockingChair = new Image(path+"rocking chair.png");
    }

    private void initDoors()throws SlickException{
        //  door = new Door(550,SetupGame.height-200, 20,100);
        doorDown = new Image(path+"door.jpg");
        doorUp = new Image(path+"door.jpg");
        upperTeleport = new Teleport(200,330,80,135);
        lowerTeleport = new Teleport(200,555,80,135);
    }
    private void initEnemies() throws SlickException {

        doctor = new Doctor(750, 350);
        corona = new Coronavirus(650, 400);
        corona.setSpace(150);
        corona.setVisionHorizontal(150);
        corona.setVisionVertical(150);
        corona.setNotVisionHorizontal(200);
        corona.setNotVisionVertical(150);

        doctor.setSpace(150);
        doctor.setVisionHorizontal(350);
        doctor.setVisionVertical(150);
        doctor.setNotVisionHorizontal(400);
        doctor.setNotVisionVertical(150);


    }




    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {



        //drawDoors(graphics);

        graphics.fill(platform2);
        graphics.fill(terrain);
        background.draw(0,0,1100,700);

        wall.startUse();
        for(int a=x_offset; a<SetupGame.width-x_offset; a+=10) {
            firstF.getSubImage(0, 20, 50, 50).drawEmbedded(a, SetupGame.height - wallW, 10, 10);
            secondF.getSubImage(0, 20, 50, 50).drawEmbedded(a, SetupGame.height- floorH - wallW, 10, 10);
            roofF.getSubImage(0,20,100,70).drawEmbedded(a,SetupGame.height- floorH *2- wallW *2,10,20);
        }
        for(int a = SetupGame.height; a>SetupGame.height-2* wallW -2* floorH; a-=10){
            leftW.getSubImage(0,20,50,50).drawEmbedded(x_offset- wallW,a,10,10);
            rightW.getSubImage(0,20,50,50).drawEmbedded(SetupGame.width-x_offset,a,10,10);
        }
        wall.endUse();

        wallpaper.startUse();
        for(int a=x_offset; a<SetupGame.width-x_offset; a+=10){
            wallpaper1.getSubImage(0,0,333,850).drawEmbedded(a,SetupGame.height-floorH,10,floorH-wallW);
            wallpaper1.getSubImage(0,0,333,850).drawEmbedded(a,SetupGame.height-floorH*2,10,floorH-wallW);
        }
        wallpaper.endUse();

        window.draw(800,300,100,100);
        window.draw(500,300,100,100);
        window.draw(800,550,100,100);
        window.draw(500,550,100,100);
        doorUp.draw(200,330,80,135);
        doorDown.draw(200,555,80,135);
        sofa.draw(600,365,200,100);
        nightstand.draw(540,415,60,50);
        nightstand.draw(800,415,60,50);
        table.draw(625,630,150,60);
        wardrobe.draw(350,500,130,193);
        cupboard.draw(320,300,150,100);
        rockingChair.draw(110,620,70,70);

        graphics.setColor(Color.pink);
        graphics.fill(babka);
        graphics.setColor(Color.black);
        graphics.setColor(Color.yellow);
        graphics.fill(attackZone);

        drawEnenmies(graphics);

    }


    private void drawDoors(Graphics graphics) {
        if(!door.isBroken()) {
            graphics.setColor(Color.blue);
            graphics.fill(door);
        }
    }

    private void drawEnenmies(Graphics graphics){
        if(corona.isAlive()) {
            graphics.setColor(Color.green);
            graphics.fill(corona);
        }
        if(doctor.isAlive()) {
            graphics.setColor(Color.cyan);
            graphics.fill(doctor);
        }
        if (injections.isEmpty() == false) {
            for (Injection i : injections) {
                if (i.isPresent()) {
                    graphics.setColor(Color.red);
                    graphics.fill(i);
                }
            }
        }


    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {



        babka.update(1);
        babka.checkForCollision(terrain);
        babka.checkForCollision(platform2);
        babka.checkForCollision(rightWall);
        babka.checkForCollision(leftWall);
        babka.checkForCollision(firstFloor);
        babka.checkForCollision(secondFloor);
        babka.checkForCollision(roof);
        babka.controls(gameContainer);

        doctor.update();
        doctor.checkForCollision(terrain, false, true);
        doctor.checkForCollision(platform2, false, true);
        doctor.checkForCollision(rightWall, false, true);
        doctor.checkForCollision(leftWall, false, true);
        doctor.checkForCollision(firstFloor, false, true);
        doctor.checkForCollision(secondFloor, false, true);
        doctor.checkForCollision(roof, false, true);
        doctor.checkForCollision(babka, true, true);

        corona.update();
        corona.checkForCollision(terrain, false, false);
        corona.checkForCollision(platform2, false, false);
        corona.checkForCollision(rightWall, false, false);
        corona.checkForCollision(leftWall, false, false);
        corona.checkForCollision(firstFloor, false, false);
        corona.checkForCollision(secondFloor, false, false);
        corona.checkForCollision(roof, false, false);
        corona.checkForCollision(babka, true, true);

        timePassed += delta;
        if (timePassed>600){
            timePassed=0;
            doctorShoot();
        }
        if (injections.isEmpty() == false) {
            for (Injection j : injections) {
                j.update();
                j.checkForCollision(terrain);
                j.checkForCollision(platform2);
                j.checkForCollision(rightWall);
                j.checkForCollision(leftWall);
                j.checkForCollision(firstFloor);
                j.checkForCollision(secondFloor);
                j.checkForCollision(roof);
            }
        }
        checkForAttack(gameContainer);


        //babka.teleport(gameContainer,200,330,80,135,0,100); //upper door
        //babka.teleport(gameContainer,200,555,80,135,0,-300); //lower door
        babka.goInTeleport(gameContainer,upperTeleport,0,100);
        babka.goInTeleport(gameContainer,lowerTeleport,0,-300);
    }

    private void checkForAttack(GameContainer container){
        //if babka`s x > mouse x then draw attackZone on right
        //else if babka`s x < mouse x then draw attackZone on left+
        if(container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)||container.getInput().isKeyPressed(Input.KEY_F)){
            if(container.getInput().getMouseX()>babka.getX()){
                attackZone.setX(babka.getX()+babka.getWidth());
                attackZone.setY(babka.getY());
            }
            else if(container.getInput().getMouseX()<babka.getX()){
                attackZone.setX(babka.getX()-babka.getWidth());
                attackZone.setY(babka.getY());
            }
        }
        else {
            attackZone = new Rectangle(-50, -50, 50, 50);
        }


        if (attackZone.intersects(corona)) corona.die();
        // if(attackZone.intersects(door)) door.broke();*/
        if (attackZone.intersects(doctor)) doctor.die();

    }
    private void doctorShoot() throws SlickException {
        if (doctor.isBabkaNoticed()&&doctor.isAlive()) {
            if (doctor.babkaIsToRight()){
                Injection newInj= doctor.shoot();
                newInj.setRight();
                injections.add(newInj);
            }else{
                Injection newInj= doctor.shoot();
                newInj.setLeft();
                injections.add(newInj);
            }

        }
    }

}
