package workexpIT.merlin.tiles;

        import workexpIT.merlin.entities.Entity;

public class Tree extends Tile {

    public Tree() {
        super(2);
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        //Cannot move past ever (Wall)
        return false;
    }
}
