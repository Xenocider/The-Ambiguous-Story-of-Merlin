package workexpIT.merlin.tiles;

        import workexpIT.merlin.entities.Entity;

public class Wall extends Tile {

    public Wall() {
        super(12,false);
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        //Cannot move past ever (Wall)
        if (instance == 11) {
            return  true;
        }
        return false;
    }
}
