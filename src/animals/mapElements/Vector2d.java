package animals.mapElements;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    public String toString(){ //?? public
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean follows(Vector2d other){
        return other.x <= this.x && other.y <= this.y;
    }

    public boolean precedes(Vector2d other){
        return other.x >= this.x && other.y >= this.y;
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return this.x == that.x && this.y == that.y;
    }
}
