package io.team0xB;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class PathfindingGrid {
    // creates a grid for flow field pathfinding

    private int gridWidth;
    private int gridHeight;
    private float screenResolution;
//    private float[][] costGrid;
//    private Vector2[][] flowfield;
    private final float MAX_COST = 999f;
    private PathfindingCell[][] flowfield;

    /**
     * A flowfield pathfinding grid
     * Start of each square is top left and grid (0,0) is also top left
     * @param screenWidth float width of GameScreen
     * @param screenHeight float height of GameScreen
     * @param screenResolution float size of individual vector squares (how detailed pathfinding will be.
     *                         Smaller values will result in higher pathfinding detail)
     */
    public PathfindingGrid(float screenWidth, float screenHeight, float screenResolution) {

        this.gridWidth = (int) Math.ceil(screenWidth / screenResolution);
        this.gridHeight = (int) Math.ceil(screenHeight / screenResolution);
        this.flowfield = new PathfindingCell[this.gridWidth][this.gridHeight];

        // pad grid with 0-Vectors and set the cost of each cell to max
        for (int i = 0; i < flowfield.length; i++) {
            for (int j = 0; j < flowfield[i].length; j++) {
                flowfield[j][i] = new PathfindingCell(j, i);
            }
        }
    }

    /**
     * @param targetPos The coordinates of the pathfinding target (should always be the player.)
     */
    public void calculateFlow(Vector2 targetPos) {
        //BFS for initial flowfield
        int targetX = (int) Math.floor(targetPos.x / screenResolution);
        int targetY = (int) Math.floor(targetPos.y / screenResolution);
        PathfindingCell target = flowfield[targetY][targetX];


        float currentCellCost = 0f;
        flowfield[targetY][targetX].setCost(currentCellCost);

        // If there is weird behaviour of the flowfield mayb                if (visited.contains(neighbour) ) continue; // skip visited cellse a centering issue, find a way to centre it to the player
        LinkedList<PathfindingCell> queue = new LinkedList<>();
        ArrayList<PathfindingCell> visited = new ArrayList<>();
        queue.offer(target);

        while (!queue.isEmpty()){
            currentCellCost++;
            PathfindingCell current = queue.pollFirst();
            visited.add(current);

            if (current.getCost() == MAX_COST) continue; //skip cells set to max cost

            for (PathfindingCell neighbour : getNeighbours(current)){
                if (visited.contains(neighbour)) continue; // skip visited cells
                queue.offer(neighbour);


                //adding cost to squares diagonal from the target to increase chance of diagonal movement
                // if flowfield generates weird movement, tweak or remove this (im not quite sure i got the maths right)
                float angle = neighbour.getFlowDirection().angleDeg(target.getFlowDirection());
                // angle steps in multiples of half pi ()
                if ( Math.abs((float) (angle - (Math.PI / 2) * Math.ceil(angle / (Math.PI / 2)))) > Math.PI / 12) {
                    neighbour.setCost(currentCellCost + 1);
                } else {
                    neighbour.setCost(currentCellCost);
                }

            }
        }
        {

        }
        // Calculate flow directions for each cell of flowfield
        for (int i = 0; i < flowfield.length ; i++) {
            for (int j = 0; j < flowfield[i].length; j++) {
                Vector2 currentFlowfieldCell = flowfield[j][i];
                if ( currentFlowfieldCell == target ) continue;
                float cheapestVal = MAX_COST;

                // instantiating cheapest neighbour as self, if there is somehow no cheapest neighbour
                Vector2 cheapestNeighbour = currentFlowfieldCell;
                for (Vector2 neighbour : getNeighbours(currentFlowfieldCell)){
                    float currentNeighbourCost = costGrid[(int)neighbour.y][(int)neighbour.x];
                    if (currentNeighbourCost < cheapestVal) {
                        cheapestVal = currentNeighbourCost;
                        cheapestNeighbour = neighbour;
                    }
                }
                flowfield[j][i] = cheapestNeighbour.sub(currentFlowfieldCell); // flowfield direction
            }
        }
    }

    private PathfindingCell getCellAt(Vector2 position){
        int targetX = (int) Math.floor(position.x / screenResolution);
        int targetY = (int) Math.floor(position.y / screenResolution);
        return this.flowfield[targetY][targetX];
    }

    private PathfindingCell getCellAt(Vector2i gridPosition){
        return this.flowfield[gridPosition.y][gridPosition.x];
    }

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

            if (x > 0 && x > gridWidth && y > 0 && y < gridHeight){
                neighbours.add(this.getCellAt(new Vector2i(x, y)));
            }
        }
        return neighbours;
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
