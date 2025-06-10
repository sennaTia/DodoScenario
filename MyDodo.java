import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 *
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.0 -- 20-01-2017
 */
public class MyDodo extends Dodo
{
    private int myNrOfEggsHatched;
    
    public MyDodo() {
        super( EAST );
        myNrOfEggsHatched = 0;
    }

    public void act() {
    }
    
    public void faceSouth(){
        while (getDirection() != SOUTH) {
            turnRight();
        }
    }

    public void faceWest(){
        while (getDirection() != WEST) {
            turnRight();
        }
    }
    
        public void faceNorth(){
        while (getDirection() != NORTH) {
            turnRight();
        }
    }

    public void faceEast(){
        while (getDirection() != EAST) {
            turnRight();
        }
    }

    
    public boolean fenceRight() {
        boolean x;
        turnRight();
        x = fenceAhead();
        turnLeft();
        return x;
    }
    
    public boolean fenceLeft() {
        boolean x;
        turnLeft();
        x = fenceAhead();
        turnRight();
        return x;
    }
    
    public void simpleMaze(){
        while (!onNest()){
            if (!fenceRight()){
                turnRight();
                move();
            } else if (!fenceAhead()) {
                move();
            } else if (!fenceLeft()) {
                turnLeft();
                move();
            } else {
                turn180();
            }
        }
    }
    
        public void goToLocation(int coordX, int coordY){
        if (validCoordinates(coordX, coordY)) {
            while (!locationReached(coordX, coordY)) {
                if (getX() < coordX) {
                    faceEast();
                    move();
                } else if (getX() > coordX) {
                    faceWest();
                    move();
                } if (getY() < coordY) {
                    faceSouth();
                    move();
                } else if (getY() > coordY) {
                    faceNorth();
                    move();
                }
                faceEast();
            }
        }
    }
    
    public boolean locationReached(int x, int y){
        if (getX() == x && getY() == y) {
            return true;
        }
        return false;
    }

    public boolean validCoordinates(int x, int y){
        if (x > getWorld().getWidth()-1 || y > getWorld().getHeight()-1) {
            showError ("Invalid coordinates");
            return false;
        } else {
            return true;
        }
    }
    
        public int countEggsInRow() {
        int totalEggs = 0;
        while (! borderAhead()) {
            if (onEgg()) {
                totalEggs++;
            }
            move();
        }
        if (onEgg()) {
            totalEggs++;
        }
        goBackToStartOfRowAndFaceBack();
        return totalEggs;
    } 
    
        public void layTrailOfEggs(int n) {
        if (n <= 0) {
            System.out.println("Fout: aantal eieren moet groter zijn dan 0.");
            return;
        }
        for (int i = 0; i < n; i++) {
            layEgg();
            move();  
        }
        //layEgg();
    }
    
    

    


    /**
     * Move one cell forward in the current direction.
     * 
     * <P> Initial: Dodo is somewhere in the world
     * <P> Final: If possible, Dodo has moved forward one cell
     *
     */
    public void move() {
        if ( canMove() ) {
            step();
        } else {
            showError( "I'm stuck!" );
        }
    }
    
       public boolean eggToRight(){
        turnRight();
        boolean ahead = eggAhead();
        turnLeft();
        return ahead;
    }
    
    public boolean eggToLeft(){
        turnLeft();
        boolean ahead = eggAhead();
        turnRight();
        return ahead;
    }
    
    public void locateNest(){
        if (!nestAhead()) {
            turnLeft();
        }
        if (!nestAhead()) {
            turnLeft();
        }
        if (!nestAhead()) {
            turnLeft();
        }
        if (!nestAhead()) {
            turnLeft();
        }
    }
    
        public void eggTrailToNest(){
        while (!onNest()) {
            if (eggAhead()){
                move();
                pickUpEgg();
            } else if (eggToRight()) {
                turnRight();
                move();
                pickUpEgg();
            } else if (eggToLeft()) {
                turnLeft();
                move();
                pickUpEgg();
            } else if (!nestAhead()){
                locateNest();
                move();
            } else {
                break;
            }
        }
    }
    
    /**
     * Test if Dodo can move forward, (there are no obstructions
     *    or end of world in the cell in front of her).
     * 
     * <p> Initial: Dodo is somewhere in the world
     * <p> Final:   Same as initial situation
     * 
     * @return boolean true if Dodo can move (no obstructions ahead)
     *                 false if Dodo can't move
     *                      (an obstruction or end of world ahead)
     */
    public boolean canMove() {
        if ( borderAhead() || fenceAhead()){
            return false;
        } else {
            return true;
        }
    }
    
    public void pickUpGrainsAndPrintCoordinates() {
    while (canMove()) {
        checkForGrain();
        move();
    }
    checkForGrain();
}

public void checkForGrain() {
    Grain grain = (Grain)getOneObjectAtOffset(0, 0, Grain.class);
    if (grain != null) {
        System.out.println("Graan gevonden op (" + getX() + ", " + getY() + ")");
        getWorld().removeObject(grain);
    }
}

public void stepOneCellBackwards () {
    turn180 ();
    move ();
    turn180 ();
}

    /**
     * Hatches the egg in the current cell by removing
     * the egg from the cell.
     * Gives an error message if there is no egg
     * 
     * <p> Initial: Dodo is somewhere in the world. There is an egg in Dodo's cell.
     * <p> Final: Dodo is in the same cell. The egg has been removed (hatched).     
     */    
    public void hatchEgg () {
        if ( onEgg() ) {
            pickUpEgg();
            myNrOfEggsHatched++;
        } else {
            showError( "There was no egg in this cell" );
        }
    }
    
    /**
     * Returns the number of eggs Dodo has hatched so far.
     * 
     * @return int number of eggs hatched by Dodo
     */
    public int getNrOfEggsHatched() {
        return myNrOfEggsHatched;
    }
    
    /**
     * Move given number of cells forward in the current direction.
     * 
     * <p> Initial:   
     * <p> Final:  
     * 
     * @param   int distance: the number of steps made
     */
    public void jump( int distance ) {
        int nrStepsTaken = 0;               // set counter to 0
        while ( nrStepsTaken < distance ) { // check if more steps must be taken  
            move();                         // take a step
            nrStepsTaken++;                 // increment the counter
        }
    }

    /**
     * Walks to edge of the world printing the coordinates at each step
     * 
     * <p> Initial: Dodo is on West side of world facing East.
     * <p> Final:   Dodo is on East side of world facing East.
     *              Coordinates of each cell printed in the console.
     */

    public void walkToWorldEdgePrintingCoordinates( ){
        
            while (!borderAhead()) {
            
            move();
        }
    }

    /**
     * Test if Dodo can lay an egg.
     *          (there is not already an egg in the cell)
     * 
     * <p> Initial: Dodo is somewhere in the world
     * <p> Final:   Same as initial situation
     * 
     * @return boolean true if Dodo can lay an egg (no egg there)
     *                 false if Dodo can't lay an egg
     *                      (already an egg in the cell)
     */

    public boolean canLayEgg( ){
        if( onEgg() ){
             return false;
        }else{
            return true;
        }
    }  
    
    public void turn180() {
        turnRight();
        turnRight();
    
    }
    
    public void climbOverFence() {
        turnLeft();
        move();
        turnRight();
        move();
        move();
        turnRight();
        move();
        turnLeft();
    }
    
    public void walkToWorldEdgeClimbingOverFences() {
          while (!isAtWorldEdge()) {
        if (isFenceInFront()) {
            climbOverFence();
        } else {
            move();
        }
    }
    }
    
    public boolean isAtWorldEdge() {
    return getX() == getWorld().getWidth() - 1;
}

public boolean isFenceInFront() {
    Actor fence = getOneObjectAtOffset(1, 0, Fence.class);
    return fence != null;
}
    
    public void goBackToStartOfRowAndFaceBack() {
    turn180();
    while (canMove()) {
        move();
    }
    turn180();
}
    
    public boolean grainAhead() {
        move();
        if (onGrain()) {
            turn180();
            move();
            turn180();
            return true;
        }
        
        else {
            turn180();
            move();
            turn180();
            return false;
        }
    }
    
    public void goToEgg () {
        while (!onEgg()) {
        move();
    }
    } 
}
            
        
    

