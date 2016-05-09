package workexpIT.merlin.tiles;

        import workexpIT.merlin.entities.Entity;

public class Table extends Tile {

    public Table() {
        super(9,false);
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        //Cannot move past ever (Wall)
        return false;
    }
}
