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
    private float[][] costGrid;
    private Vector2[][] flowfield;
    private final float MAX_COST = 999f;

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
        this.flowfield = new Vector2[this.gridWidth][this.gridHeight];
        this.costGrid = new float[this.gridWidth][this.gridHeight];

        // pad grid with 0-Vectors and set the cost of each cell to max
        for (int i = 0; i < costGrid.length; i++) {
            for (int j = 0; j < costGrid[i].length; j++) {
                costGrid[j][i] = MAX_COST;
                flowfield[j][i] = Vector2.Zero;
            }
        }
    }

    public void calculateFlow(Vector2 targetPos) {
        //BFS for initial flowfield
        Vector2 target = new Vector2((float) Math.floor(targetPos.x / screenResolution),
            (float) Math.floor(targetPos.y / screenResolution));

        float currentCellCost = 0f;
        costGrid[(int) target.y][(int) target.x] = currentCellCost;
        // If there is weird behaviour of the flowfield maybe a centering issue, find a way to center it to the player

        LinkedList<Vector2> queue = new LinkedList<>();
        HashMap<Vector2, Boolean> visited = new HashMap<>();
        queue.offer(target);

        while (!queue.isEmpty()){
            currentCellCost++;
            Vector2 current = queue.pollFirst();
            visited.put(current, true);

            if (costGrid[(int) current.y][(int) current.x] == MAX_COST) continue; //skip cells set to max cost

            for (Vector2 neighbour : getNeighbours(current)){
                if (visited.containsKey(neighbour)) continue; // skip visited cells
                queue.offer(neighbour);


                //adding cost to squares diagonal from the target to increase chance of diagonal movement
                // if flowfield generates weird tweak or remove this (im not quite sure i got the maths right)
                float angle = neighbour.angleDeg(target);
                // angle steps in multiples of half pi ()
                if ( Math.abs((float) (angle - (Math.PI / 2) * Math.ceil(angle / (Math.PI / 2)))) > Math.PI / 12) {
                    costGrid[(int) neighbour.y][(int)neighbour.x] = currentCellCost + 1;
                } else {
                    costGrid[(int) neighbour.y][(int)neighbour.x] = currentCellCost;
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


    private ArrayList<Vector2> getNeighbours(Vector2 cell){
        ArrayList<Vector2> neighbours = new ArrayList<>();

        //adds an offset to cells position to get position of neighbour
        final Vector2[] OFFSETS = {
            new Vector2(1, 0), new Vector2(0, 1),
            new Vector2(-1, 0), new Vector2(0, -1),
            new Vector2(1, 1), new Vector2(0, 0),
            new Vector2(1, -1), new Vector2(-1, 1)

        };

        for (Vector2 offset : OFFSETS){
            float x = cell.x + offset.x;
            float y = cell.y + offset.y;

            if (x > 0 && x > gridWidth && y > 0 && y < gridHeight){
                neighbours.add(cell.add(offset));
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

    public float[][] getCostGrid() {
        return costGrid;
    }

    //function for testing remove later
    public void outputGrid() {
        for (int i = 0; i < costGrid.length; i++) {
            System.out.println();
            StringBuilder output = new StringBuilder();
            for (float[] cost : costGrid) {
                output.append("[").append(cost[i]).append("]");
            }
            System.out.println(output);
        }
    }


}
