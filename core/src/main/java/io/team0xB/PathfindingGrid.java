package io.team0xB;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

// Have to figure out how to centre this grid around the player and move it with the player

public class PathfindingGrid {
    // creates a grid for flow field pathfinding

    private int gridWidth;
    private Vector2 targetPosition;
    private int gridHeight;
    private float screenResolution;
    private final PathfindingCell[][] flowfield;

    /**
     * A flowfield pathfinding grid
     * Start of each square is top left and grid (0,0) is also top left
     * @param screenWidth float width of GameScreen
     * @param screenHeight float height of GameScreen
     * @param screenResolution float size of individual vector squares (how detailed pathfinding will be.
     *                         Smaller values will result in higher pathfinding detail)
     */
    public PathfindingGrid(float screenWidth, float screenHeight, float screenResolution) {

        this.screenResolution = screenResolution;
        this.gridWidth = (int) Math.ceil(screenWidth / screenResolution);
        this.gridHeight = (int) Math.ceil(screenHeight / screenResolution);
        this.flowfield = new PathfindingCell[this.gridHeight][this.gridWidth];

        // pad grid with 0-Vectors and set the cost of each cell to max
        for (int y = 0; y < flowfield.length; y++) {
            for (int x = 0; x < flowfield[y].length; x++) {
                // the coordinates have to be relative to the players position
                flowfield[y][x] = new PathfindingCell(x, y);
            }
        }
    }

    /**
     * @param targetPos The coordinates of the pathfinding target (should always be the player.)
     */
    public void calculateFlow(Vector2 targetPos) {
        this.targetPosition = targetPos;
        // must translate positions so that target is 0,0
        // then translate  grid so that 0,0 top left (no-negative numbers in an array)

//        Vector2 zeroTranslation = targetPos.scl(-1); // adding negative vector to any position


        //BFS for initial flowfield
//        int targetX = (int) Math.floor(targetPos.x / screenResolution);
//        int targetY = (int) Math.floor(targetPos.y / screenResolution);


        int targetX = 0;
        int targetY = 0;

        PathfindingCell target = flowfield[targetY][targetX];

        float currentCellCost = 0f;
        flowfield[targetY][targetX].setCost(currentCellCost);

        // If there is weird behaviour of the flowfield maybe a centering issue, find a way to centre it to the player
        LinkedList<PathfindingCell> queue = new LinkedList<>();
        HashSet<PathfindingCell> visited = new HashSet<>();
        queue.offer(target);

        float MAX_COST = 999f;
        while (!queue.isEmpty()){

            currentCellCost++;
            PathfindingCell current = queue.pollFirst();
            visited.add(current);
            if (current.getCost() == MAX_COST) continue; //skip cells set to max cost

            for (PathfindingCell neighbour : getNeighbours(current)){

                if (visited.contains(neighbour)) continue; // skip visited cells

                queue.offer(neighbour);

                //adding cost to squares diagonal from the target to increase chance of diagonal movement
                // if flowfield generates weird movement, tweak or remove this (im not quite sure if I got the maths right)
                float angle = neighbour.getFlowDirection().angleDeg(target.getFlowDirection());
                // angle steps in multiples of half pi
                if ( Math.abs((float) (angle - (Math.PI / 2) * Math.ceil(angle / (Math.PI / 2)))) > Math.PI / 12) {
                    neighbour.setCost(currentCellCost + 1);
                } else {
                    neighbour.setCost(currentCellCost);
                }

            }
        }

        // Calculate flow directions for each cell of flowfield
        for (int y = 0; y < flowfield.length ; y++) {
            for (int x = 0; x < flowfield[y].length; x++) {
                PathfindingCell currentFlowfieldCell = flowfield[y][x];
                if ( currentFlowfieldCell == target ) continue;
                float cheapestVal = MAX_COST;

                // instantiating cheapest neighbour as self, if there is somehow no cheapest neighbour
                PathfindingCell cheapestNeighbour = currentFlowfieldCell;
                for (PathfindingCell neighbour : getNeighbours(currentFlowfieldCell)){
                    if (neighbour.getCost() < cheapestVal) {
                        cheapestVal = neighbour.getCost();
                        cheapestNeighbour = neighbour;
                    }
                }

                // Calculate unit vector to nearest flowfield
                Vector2i vectorToCheapest = cheapestNeighbour.getCoordinates().sub(currentFlowfieldCell.getCoordinates());
                Vector2 directionToCheapest = getDirectionToCheapest(vectorToCheapest);
                flowfield[y][x].setFlowDirection(directionToCheapest);
            }
        }
    }

    private Vector2 getDirectionToCheapest(Vector2i vectorToCheapest) {
        double magnitudeVTC = Math.sqrt(Math.pow(vectorToCheapest.x, 2) + Math.pow(vectorToCheapest.y, 2));
        float vtcX;
        float vtcY;

        if (magnitudeVTC != 0){
            vtcX = (float) (vectorToCheapest.x / magnitudeVTC);
            vtcY = (float) (vectorToCheapest.y / magnitudeVTC);
        } else {
            vtcX = 0;
            vtcY = 0;
        }

        return new Vector2(vtcX, vtcY);
    }

    /**
     * @param position a Vector2 position of an object in gamespace
     * @return A PathfindingCell in the flowfield that provides the flow direction for objects at the input position.
     */
    private PathfindingCell getCellAt(Vector2 position, Vector2 playerPosition){
//        int targetX = (int) Math.floor(position.x / screenResolution);
//        int targetY = (int) Math.floor(position.y / screenResolution);
        int targetX = (int) (playerPosition.x + Math.floor(position.x / screenResolution));
        int targetY = (int) (playerPosition.y + Math.floor(position.y / screenResolution));

        if (0 < targetX  && targetX < this.gridWidth && 0 < targetY && targetY < this.gridHeight){

            return this.flowfield[targetY][targetX];
        } else {
            return new PathfindingCell(-1, -1);
        }

    }

    /**
     * @param gridPosition a Vector2i position of an object in the flowfield grid.
     * @return The PathfindingCell located at the input position.
     */
    private PathfindingCell getCellAt(Vector2i gridPosition){
        return this.flowfield[gridPosition.y][gridPosition.x];
    }

    /**
     * @param cell A PathfindingCell
     * @return An ArrayList of neighbouring cells in the PathfindingGrid
     */
    private ArrayList<PathfindingCell> getNeighbours(PathfindingCell cell){
        ArrayList<PathfindingCell> neighbours = new ArrayList<>();

        //adds an offset to cells position to get position of neighbour
        final Vector2i[] OFFSETS = {
            new Vector2i(1, 0), new Vector2i(0, 1),
            new Vector2i(-1, 0), new Vector2i(0, -1),
            new Vector2i(1, 1), new Vector2i(0, 0),
            new Vector2i(1, -1), new Vector2i(-1, 1)

        };

        for (Vector2i offset : OFFSETS){
            int x = cell.getCoordinates().x  + offset.x;
            int y = cell.getCoordinates().y + offset.y;

            if (x > 0 && x < gridWidth && y > 0 && y < gridHeight){
                neighbours.add(this.getCellAt(new Vector2i(x, y)));
            }
        }
        return neighbours;
    }

    /**
     * Gets the flow direction of an object on the map
     * @param position The position of the object on the map
     * @return The flow direction of the object
     */
    public Vector2 getFlow(Vector2 position){
        PathfindingCell cell = getCellAt(position, this.targetPosition);
        return cell.getFlowDirection();
    }

    public int getGridWidth(){
        return gridWidth;
    }
    public void setGridWidth(int width) {
        this.gridWidth = width;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int height) {
        this.gridHeight = height;
    }

    //function for testing remove later
    public void outputGrid() {
        for (int i = 0; i < flowfield.length; i++) {
            System.out.println();
            StringBuilder output = new StringBuilder();
            for (PathfindingCell[] cell : flowfield) {
                output.append("[").append(cell[i].getCost()).append("]");
            }
            System.out.println(output);
        }
    }


}
