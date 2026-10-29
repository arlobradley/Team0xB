package io.team0xB;

import com.badlogic.gdx.math.Vector2;

import java.util.Objects;

public class PathfindingCell {
    private Vector2 flowDirection;
    private Vector2i coordinates;
    private float cost;
    private final float MAX_COST = 999f;

    public PathfindingCell(Vector2 flowDirection, float cost) {
        this.flowDirection = flowDirection;
        this.cost = cost;
    }

    /**
     * A cell stored in a pathfindingGrid.
     * @param cellRow integer y coordinate
     * @param cellColumn integer x coordinate
     */
    public PathfindingCell(int cellRow, int cellColumn) {
        this.flowDirection = Vector2.Zero;
        this.cost = MAX_COST;
        this.coordinates = new Vector2i(cellRow, cellColumn);
    }

    // If two pathfinding cells share the same coordinates they should be treated as equal
    // their flow direction does not matter
    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof PathfindingCell ){
            return this.getCoordinates().equals(((PathfindingCell) obj).getCoordinates());
        } else {
            return false;
        }
    }


    @Override
    public int hashCode(){
        return 31 * this.getCoordinates().hashCode();
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Vector2 getFlowDirection() {
        return flowDirection;
    }

    public void setFlowDirection(Vector2 flowDirection) {
        this.flowDirection = flowDirection;
    }

    public Vector2i getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Vector2i coordinates) {
        this.coordinates = coordinates;
    }
}
