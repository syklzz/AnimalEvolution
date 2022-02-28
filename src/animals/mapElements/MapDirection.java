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
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHWEST -> NORTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case SOUTHEAST -> SOUTH;
            case EAST -> SOUTHEAST;
            case NORTHEAST -> EAST;
        };
    }

    public Vector2d toUnitVector(){
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTHWEST -> new Vector2d(-1, 1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case SOUTHEAST -> new Vector2d(1, -1);
            case EAST -> new Vector2d(1, 0);
            case NORTHEAST -> new Vector2d(1, 1);
        };
    }

    public static MapDirection getOrientation(){
        Random r = new Random();
        int index = r.nextInt(8);
        return MapDirection.values()[index];
    }
}
