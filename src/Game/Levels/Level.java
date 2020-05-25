package Game.Levels;

import Game.Babka;
import Game.enemies.*;
import Game.interactiveObjects.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.util.ArrayList;

public abstract class Level extends BasicGameState {
    private int id;

    private Babka babka;

    private boolean readyToGoNextLevel = false;
    private Rectangle exitNextLevel;

    private int nextLevelId = 0;



    private Rectangle attackZone;
    private ArrayList<Rectangle> obstacles = new ArrayList<>();
    private ArrayList<Door> doors = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<TapokPick> tapki = new ArrayList<>();
    private ArrayList<Teleport1> teleports = new ArrayList<>();




    @Override
    public int getID() {
        return id;
    }

    protected void addEnemy(Enemy enemy){
        enemies.add(enemy);
    }
    protected void addTapok(TapokPick tapok){
        tapki.add(tapok);
    }

    protected void addTeleport(Teleport1 teleport){
        teleports.add(teleport);
    }

    protected void addBullet(Bullet bullet){
        bullets.add(bullet);
    }

    protected void addObstacle(Rectangle obstacle){
        obstacles.add(obstacle);
    }

    protected void addDoor(Door door){
        doors.add(door);
    }

    protected void setBabka(Babka babka){
        this.babka = babka;
    }

    protected void setExitNextLevel(float x, float y, float width, float height){
        exitNextLevel = new Rectangle(x, y, width, height);
    }


    protected void setId(int id){
        this.id = id;
    }

    protected void setNextLevelId(int id){
        nextLevelId = id;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        initLevel(container, game);
        initAttackZone();
    }

    private void initAttackZone(){
        attackZone = new Rectangle(-50, -50, 50, 50);
    }

    protected abstract void initLevel(GameContainer container, StateBasedGame game) throws SlickException;

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        renderLevel(container, game, g);
        drawDoors(g);
        drawEnemies(g);
        drawBullets(g);
        drawTapki(g);
        drawBabka();
    }

    private void drawBabka() throws SlickException {
        if ( babka.animationSlide()) {
            babka.getAnimation().draw(babka.getX()-25, babka.getY());
        }else{
            babka.getAnimation().draw(babka.getX(), babka.getY());
        }
    }

    private void drawDoors(Graphics graphics) {
        for(Door door : doors){
            if (!door.isBroken()) {
                door.getImageDoor(graphics).draw(  door.getX(),   door.getY());
            }
        }
    }

    private void drawEnemies(Graphics graphics) {
        for(int i = 0; i<enemies.size(); i++) {
            if (enemies.get(i) instanceof Doctor) {
                Doctor doctor = (Doctor) enemies.get(i);
                if (doctor.isAlive()) {
                    doctor.getAnimation(graphics).draw(  doctor.getX(),  doctor.getY());
                }
            } else if (enemies.get(i) instanceof Coronavirus) {
                Coronavirus corona = (Coronavirus) enemies.get(i);
                if (corona.isAlive()) {
                    graphics.setColor(Color.blue);
                    corona.getAnimation(graphics).draw(corona.getX()-5, corona.getY()-5);
                }
            } else if (enemies.get(i) instanceof CoronaSmall) {
                CoronaSmall coronaS = (CoronaSmall) enemies.get(i);
                if (coronaS.isAlive()) {
                    coronaS.getAnimation(graphics).draw(coronaS.getX(), coronaS.getY(),25,25);
                }
            } else if (enemies.get(i) instanceof Turrel) {
                Turrel turrel = (Turrel) enemies.get(i);
                if (turrel.isAlive()) {
                    turrel.getImageTurrel(graphics).draw(turrel.getX(), turrel.getY(),75,80);

                }
            }
        }

        drawBullets(graphics);


    }

    private void drawBullets(Graphics graphics){
        if (!bullets.isEmpty()) {
            for (int i = 0; i<bullets.size(); i++ ) {
                if(bullets.get(i) instanceof Injection){
                    Injection bullet = (Injection) bullets.get(i);;
                    if (bullet.isPresent()) {
                        bullet.getImageInjection(graphics).draw(bullet.getX(), bullet.getY());
                    }
                }
                else if(bullets.get(i) instanceof TapokThrow){
                    TapokThrow bullet = (TapokThrow) bullets.get(i);;
                    if (bullet.isPresent()) {
                        bullet.getImageInjection(graphics).draw(bullet.getX(), bullet.getY());
                    }
                }

            }
        }
    }

    private void drawTapki(Graphics graphics){
        if (!tapki.isEmpty()) {
            for (TapokPick i : tapki) {
                if (i.isPresent()) {
                    i.getAnimation(graphics).draw(i.getX(), i.getY());
                }
            }
        }
    }

    protected abstract void renderLevel(GameContainer container, StateBasedGame game, Graphics g);

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        updateBabka(delta, container);
        checkForAttack(container);
        updateLevel(container,game,delta);
        updateEnemies(delta);
        updateBullets();
        updateTapki();
        updateTeleport(container);
        if(container.getInput().isKeyPressed(Input.KEY_ESCAPE)){
            game.enterState(0,new FadeOutTransition(), new FadeInTransition());
        }

        if(readyToGoNextLevel && babka.intersects(exitNextLevel)){
            System.out.println("Our id"+id+" Next level id:"+nextLevelId);
            game.enterState(nextLevelId, new FadeOutTransition(), new FadeInTransition());
        }

        if(container.getInput().isKeyPressed(Input.KEY_R)) restart(container, game);

    }

    protected void restart(GameContainer container, StateBasedGame game) throws SlickException {
        readyToGoNextLevel = false;
        enemies = new ArrayList<>();
        obstacles = new ArrayList<>();
        doors = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        tapki = new ArrayList<>();
        teleports = new ArrayList<>();
        initLevel(container, game);
        initAttackZone();
    }

    protected void addEnemy(ArrayList<Enemy> enemies){
        this.enemies.addAll(enemies);
    }

    private void updateTeleport(GameContainer container){
        for(Teleport1 teleport: teleports){
            if(container.getInput().isKeyDown(Input.KEY_ENTER))babka = teleport.teleport(babka, container);
        }
    }

    private void updateBabka(int delta, GameContainer container) throws SlickException {
        babka.update(1, delta);
        for (Rectangle obstacle : obstacles) {
            babka.checkForCollision(obstacle);
        }
        for (Door door : doors){
            if(!door.isBroken())babka.checkForCollision(door);
        }
        babka.controls(container);

        if(babka.isReadyToShoot(container)) bullets.add(babka.shoot(container));
    }

    private void updateEnemies(int delta) throws SlickException {
        ArrayList<Rectangle> all = new ArrayList<>();
        all.addAll(obstacles);
        all.addAll(doors);

        for(int i = 0; i<enemies.size(); i++){
            if(enemies.get(i) instanceof Doctor && enemies.get(i).isAlive()){
                Doctor doctor = (Doctor) enemies.get(i);
                doctor.update(delta, all);
                if (doctor.isReadyToShoot()) bullets.add(doctor.shoot(babka));

                for (Rectangle obstacle : obstacles) {
                    doctor.checkForCollisionWall(obstacle);
                }

                for(Rectangle door: doors){
                    doctor.checkForCollisionWall(door);
                }
                doctor.checkForCollisionBabka(babka);
            }
            else if(enemies.get(i) instanceof Coronavirus && enemies.get(i).isAlive()){
                Coronavirus corona = (Coronavirus) enemies.get(i);
                if(corona.isAlive()) {
                    corona.update(all);
                    for (Rectangle obstacle : obstacles) {
                        corona.checkForCollisionWall(obstacle);
                    }
                    corona.checkForCollisionBabka(babka);
                    if(babka.intersects(corona)&&corona.isAlive()) babka.die();
                }
            }
            else if(enemies.get(i) instanceof CoronaSmall && enemies.get(i).isAlive()){
                CoronaSmall coronaS = (CoronaSmall) enemies.get(i);
                if(coronaS.isAlive()) {
                    coronaS.update();
                    coronaS.checkForCollisionBabka(babka);
                    if(babka.intersects(coronaS)&&coronaS.isAlive()) babka.die();
                }
            }
            else if(enemies.get(i) instanceof Turrel && enemies.get(i).isAlive()){
                Turrel turrel = (Turrel) enemies.get(i);
                turrel.update(delta);
                turrel.checkForCollisionBabka(babka);
                if(turrel.isReadyToShoot(babka)) bullets.add(turrel.shoot());
            }
            else {
                enemies.remove(i);
                i--;
            }

        }

    }

    private void updateTapki(){
        if (tapki.size()!=0) {
            for (TapokPick tapok : tapki) {
                if (tapok.isPresent()) {
                    tapok.checkForCollision(babka);
                }
            }
            for (int i = 0; i < tapki.size(); i++) {
                TapokPick tapok = tapki.get(i);
                if (tapok.isPresent() == false) tapki.remove(i);
            }
        }
    }

    private void updateBullets(){
        for (int i = 0; i< bullets.size(); i++) {
            if(bullets.get(i) instanceof Injection && bullets.get(i).isPresent()){
                Injection j = (Injection) bullets.get(i);
                j.update();
                for (Rectangle obstacle : obstacles) {
                    j.checkForCollision(obstacle);
                }
                for (Rectangle obstacle : doors) {
                    j.checkForCollision(obstacle);
                }
                if(j.isReflected()) {
                    for (int d = 0; d < enemies.size(); d++) {
                        if (enemies.get(d) instanceof Doctor) {
                            Doctor doctor = (Doctor) enemies.get(d);
                            if (j.intersects(doctor)) {
                                doctor.die();
                                j.disappear();
                            }
                        }
                    }
                }
                if(j.intersects(babka)) babka.die();
            }
            else if(bullets.get(i) instanceof TapokThrow && bullets.get(i).isPresent()){
                TapokThrow tapok = (TapokThrow) bullets.get(i);
                tapok.update();
                for (Rectangle obstacle : obstacles) {
                    tapok.checkForCollision(obstacle);
                }
                for (Rectangle obstacle : doors) {
                    tapok.checkForCollision(obstacle);
                }
                for (int d = 0; d < enemies.size(); d++) {
                    if(!(enemies.get(d) instanceof Turrel) && tapok.intersects((Shape) enemies.get(d))&&tapok.isPresent()){
                        enemies.get(d).die();
                        tapok.disappear();
                    }
                }
            }
            else {
                bullets.remove(i);
                i--;
            }
        }
    }

    protected abstract void  updateLevel(GameContainer container, StateBasedGame game, int delta);

    private void checkForAttack(GameContainer container) {
        attackZone = babka.getHitZone(container);
        checkForAttackDoors();
        checkForAttackEnemies();

    }

    private void checkForAttackDoors(){
        for(int i = 0; i<doors.size(); i++){
            Door door = doors.get(i);
            if(attackZone.intersects(door)) door.broke() ;
            if(door.isBroken()) doors.remove(i);
        }
    }

    private void checkForAttackEnemies(){
        for(int i = 0; i<enemies.size(); i++){
            if(enemies.get(i).isAlive() && attackZone.intersects( (Rectangle) enemies.get(i))){
                enemies.get(i).die();
            }
        }

    }

    protected boolean isReadyToGoNextLevel(){
        return readyToGoNextLevel;
    }

    protected int getQuantityOfEnemiesAlive(){
        return enemies.size();
    }

    protected int getQuantityOfTapoksLeftOnLevel(){
        return tapki.size();
    }

    protected void giveBabkeTapok(){
        babka.giveTapok();
    }

    protected void giveBabkeTapok(int q){
        babka.giveTapok(q);
    }

    protected Babka getBabka(){
        return babka;
    }

    protected void setReadyToGoNextLevel(boolean readyToGoNextLevel){
        this.readyToGoNextLevel = true;
    }
}
