package animals.mapElements;

import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTHWEST,
    WEST,
    SOUTHWEST,
    SOUTH,
    SOUTHEAST,
    EAST,
    NORTHEAST;

    public MapDirection next() {
        switch (this) {
            case NORTH: return NORTHEAST;
            case NORTHWEST: return NORTH;
            case SOUTH: return SOUTHWEST;
            case SOUTHWEST: return WEST;
            case WEST: return NORTHWEST;
            case SOUTHEAST: return SOUTH;
            case EAST: return SOUTHEAST;
            case NORTHEAST: return EAST;
        }
        return null;
    }

    public MapDirection previous(){
        switch (this) {
            case NORTH: return NORTHWEST;
            case NORTHWEST: return WEST;
            case SOUTH: return SOUTHEAST;
            case SOUTHWEST: return SOUTH;
            case WEST: return SOUTHWEST;
            case SOUTHEAST: return EAST;
            case EAST: return NORTHEAST;
            case NORTHEAST: return NORTH;
        }
        return null;
    }

    public Vector2d toUnitVector(){
        switch (this) {
            case NORTH: return new Vector2d(0,1);
            case NORTHWEST: return new Vector2d(-1,1);
            case SOUTH: return new Vector2d(0,-1);
            case SOUTHWEST: return new Vector2d(-1,-1);
            case WEST: return new Vector2d(-1,0);
            case SOUTHEAST: return new Vector2d(1,-1);
            case EAST: return new Vector2d(1,0);
            case NORTHEAST: return new Vector2d(1,1);
        }
        return null;
    }

    public static MapDirection getOrientation(){
        Random r = new Random();
        int index = r.nextInt(8);
        return MapDirection.values()[index];
    }
}
