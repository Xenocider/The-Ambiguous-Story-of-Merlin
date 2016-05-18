package workexpIT.merlin.tiles;

        import workexpIT.merlin.entities.Entity;

public class House extends Tile {

    public House() {
        super(4,false);
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        if (instance == 0 || instance == 2) {
            return true;
        }
        //Cannot move past ever (Wall)
        return false;
    }
}
