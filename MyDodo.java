import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 *
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.0 -- 20-01-2017
 */
public class MyDodo extends Dodo
{
    private int myNrOfEggsHatched;
    
    // Maakt een dodo die naar het oosten kijkt en begint met 0 eieren
    public MyDodo() {
        super( EAST );
        myNrOfEggsHatched = 0;
    }
    
    // Telt alle eieren in de wereld op
    public int totalEggsInWorld(){
        goToLocation(0,0);
        boolean end = false;
        int totalEggs = 0;
        while (end==false) {
            totalEggs = totalEggs + countEggsInRow();
            goBackToStartOfRowAndFaceBack();
            turnRight();
            if (borderAhead()) {
                end = true;
                break;
            }
            move();
            turnLeft();
        }
        turnLeft();
        return totalEggs;
    }
    
    // Zoekt welke rij de meeste eieren heeft
    public int mostEggsInRow(){
        boolean end = false;
        int mostEggs = 0;
        int rowEggs = 0;
        int row = 0;
        int mostEggsRow = 0;
        int startX = getX();
        int startY = getY();
        goToLocation(0,0);
        while (end==false) {
            row++;
            rowEggs = countEggsInRow();
            if (rowEggs >= mostEggs) {
                mostEggs = rowEggs;
                mostEggsRow = row;
            }
            goBackToStartOfRowAndFaceBack();
            turnRight();
            if (borderAhead()) {
                end = true;
                break;
            }
            move();
            turnLeft();
        }
        goToLocation(startX,startY);
        turnLeft();
        return mostEggsRow;
    }
    
    // Legt eieren in een driehoekvorm in de wereld
    public void monumentOfEggs() {
        int row = 0;
        boolean done = false;
        while (!done) {
            if (!validCoordinates(0, row)) {
                done = true;
                break;
            }
            goToLocation(0, row);
            faceEast();
            int column = 0;
            while (true) {
                if (!validCoordinates(column, row)) {
                    break;
                }
                if (column <= row) {
                    if (canLayEgg()) {
                        layEgg();
                    }
                }
                if (!borderAhead()) {
                    move();
                    column++;
                } else {
                    break;
                }
            }
            row++;
        }
    }
    
    // Legt eieren waarbij het aantal per rij steeds verdubbelt
    public void monumentOfEggs2() {
        int eggsInRow = 1;
        int startX = getX();
        int startY = getY();
        int worldWidth = getWorld().getWidth();
        int worldHeight = getWorld().getHeight();
        int row = 0;
        while (startY + row < worldHeight && startX + eggsInRow - 1 < worldWidth) {
            goToLocation(startX, startY + row);
            faceEast();
            int layed = 0;
            while (layed < eggsInRow && !borderAhead()) {
                if (canLayEgg()) {
                    layEgg();
                }
                layed++;
                if (layed < eggsInRow && !borderAhead()) {
                    move();
                }
            }
            eggsInRow *= 2;
            row++;
        }
    }
    
    // Maakt een piramide van eieren met steeds meer eieren per rij
    public void stablePyramidOfEggs() {
        int stappenNaarLinks = 2;
        int eierenLeggen = 1;
        while (getY() < getWorld().getHeight() - 1) {
            layTrailOfEggs(eierenLeggen);
            eierenLeggen += 2;
            for (int w = 0; w < stappenNaarLinks; w++) {
                if (getX() != 0) {
                    stepOneCellBackwards();
                } else {
                    return;
                }
            }
            stappenNaarLinks += 2;
            faceSouth();
            if (canMove()) {
                move();
            } else {
                return;
            }
            faceEast();
        }
        layTrailOfEggs(eierenLeggen);
    }

    // Wordt steeds uitgevoerd (nu leeg)
    public void act() {
    }
    
    // Laat de dodo naar het zuiden kijken
    public void faceSouth(){
        while (getDirection() != SOUTH) {
            turnRight();
        }
    }

    // Laat de dodo naar het westen kijken
    public void faceWest(){
        while (getDirection() != WEST) {
            turnRight();
        }
    }
    
    // Laat de dodo naar het noorden kijken
    public void faceNorth(){
        while (getDirection() != NORTH) {
            turnRight();
        }
    }

    // Laat de dodo naar het oosten kijken
    public void faceEast(){
        while (getDirection() != EAST) {
            turnRight();
        }
    }


    
    // Controleert of er een hek aan de rechterkant staat
public boolean fenceRight() {
    boolean x;
    turnRight();
    x = fenceAhead();
    turnLeft();
    return x;
}

// Controleert of er een hek aan de linkerkant staat
public boolean fenceLeft() {
    boolean x;
    turnLeft();
    x = fenceAhead();
    turnRight();
    return x;
}

// Loopt een simpel doolhof uit tot bij het nest
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

// Gaat naar een bepaalde plek in de wereld (x, y)
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

// Controleert of dodo op positie (x, y) staat
public boolean locationReached(int x, int y){
    if (getX() == x && getY() == y) {
        return true;
    }
    return false;
}

// Controleert of de coördinaten geldig zijn binnen de wereld
public boolean validCoordinates(int x, int y){
    if (x > getWorld().getWidth()-1 || y > getWorld().getHeight()-1) {
        showError ("Invalid coordinates");
        return false;
    } else {
        return true;
    }
}

// Telt hoeveel eieren er in de huidige rij liggen
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

// Legt een rij van n eieren en loopt mee naar voren
public void layTrailOfEggs(int n) {
    if (n <= 0) {
        System.out.println("Fout: aantal eieren moet groter zijn dan 0.");
        return;
    }
    for (int i = 0; i < n; i++) {
        layEgg();
        move();  
    }
}

// Verplaatst de dodo 1 stap vooruit als dat kan
public void move() {
    if ( canMove() ) {
        step();
    } else {
        showError( "I'm stuck!" );
    }
}

// Checkt of er een ei rechts van de dodo ligt
public boolean eggToRight(){
    turnRight();
    boolean ahead = eggAhead();
    turnLeft();
    return ahead;
}

// Checkt of er een ei links van de dodo ligt
public boolean eggToLeft(){
    turnLeft();
    boolean ahead = eggAhead();
    turnRight();
    return ahead;
}

// Draait de dodo totdat hij naar het nest kijkt
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

    
     // Verzamelt eieren die op de weg naar het nest liggen
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

// Checkt of Dodo naar voren kan lopen (geen hek of grens)
public boolean canMove() {
    if ( borderAhead() || fenceAhead()){
        return false;
    } else {
        return true;
    }
}

// Loopt vooruit en pakt alle korrels op die je tegenkomt
public void pickUpGrainsAndPrintCoordinates() {
    while (canMove()) {
        checkForGrain();
        move();
    }
    checkForGrain();
}

// Controleert of er korrels op de plek zijn en haalt ze weg
public void checkForGrain() {
    Grain grain = (Grain)getOneObjectAtOffset(0, 0, Grain.class);
    if (grain != null) {
        System.out.println("Graan gevonden op (" + getX() + ", " + getY() + ")");
        getWorld().removeObject(grain);
    }
}

// Loopt één stap achteruit
public void stepOneCellBackwards () {
    turn180 ();
    move ();
    turn180 ();
}

// Laat een ei in de huidige cel uitkomen (verwijdert ei)
public void hatchEgg () {
    if ( onEgg() ) {
        pickUpEgg();
        myNrOfEggsHatched++;
    } else {
        showError( "There was no egg in this cell" );
    }
}

// Geeft het aantal uitgekomen eieren terug
public int getNrOfEggsHatched() {
    return myNrOfEggsHatched;
}

// Springt een aantal stappen vooruit
public void jump( int distance ) {
    int nrStepsTaken = 0;               
    while ( nrStepsTaken < distance ) {  
        move();                         
        nrStepsTaken++;                 
    }
}

// Loopt naar de rand van de wereld en print steeds de positie
public void walkToWorldEdgePrintingCoordinates( ){
    while (!borderAhead()) {
        move();
    }
}

// Checkt of Dodo een ei kan leggen (staat er al een ei?)
public boolean canLayEgg( ){
    if( onEgg() ){
         return false;
    }else{
        return true;
    }
}

// Draait Dodo 180 graden om
public void turn180() {
    turnRight();
    turnRight();
}

// Klimt over een hek door een bepaald patroon te lopen
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

// Loopt naar wereldrand en klimt over hekken waar nodig
public void walkToWorldEdgeClimbingOverFences() {
    while (!isAtWorldEdge()) {
        if (isFenceInFront()) {
            climbOverFence();
        } else {
            move();
        }
    }
}

// Checkt of Dodo bij de rand van de wereld is
public boolean isAtWorldEdge() {
    return getX() == getWorld().getWidth() - 1;
}

// Checkt of er een hek direct voor Dodo staat
public boolean isFenceInFront() {
    Actor fence = getOneObjectAtOffset(1, 0, Fence.class);
    return fence != null;
}

// Gaat terug naar het begin van de rij en kijkt de andere kant op
public void goBackToStartOfRowAndFaceBack() {
    turn180();
    while (canMove()) {
        move();
    }
    turn180();
}

// Checkt of er graan vlak voor Dodo ligt (loopt een stap naar voren)
public boolean grainAhead() {
    move();
    if (onGrain()) {
        turn180();
        move();
        turn180();
        return true;
    } else {
        turn180();
        move();
        turn180();
        return false;
    }
}

// Gaat naar een ei toe als Dodo er nog niet op staat
public void goToEgg () {
    while (!onEgg()) {
        move();
    }
} 
}

        
    

