package io.team0xB;

import java.util.Vector;

/**
 * 2D integer vector. Created to avoid repeated casting of LibGDX's float Vector2 Class.
 */
public class Vector2i {
    public final static Vector2i ZERO = new Vector2i(0, 0);
    public final static Vector2i ONE = new Vector2i(1, 1);

    public int x;
    public int y;


    /**
     * @param x x coordinate of the vector.
     * @param y y coordinate of the vector.
     */
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Vector2i.class) {
            return ((Vector2i) obj).x == this.x && ((Vector2i) obj).y == this.y;
        }
        return false;
    }

    public Vector2i add(Vector2i other){
        return new Vector2i(this.x + other.x, this.y + other.y);
    }

    public Vector2i sub(Vector2i other){
        return new Vector2i(this.x - other.x, this.y - other.y);
    }
}
