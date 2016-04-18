package workexpIT.merlin.tiles;

        import workexpIT.merlin.data.ImageReader;
        import workexpIT.merlin.entities.Entity;

public class Bush extends Tile {

    public Bush() {
        super(1,false);
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        //Cannot move past ever (Wall)
        return false;
    }
}
