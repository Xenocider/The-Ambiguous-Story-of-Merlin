package workexpIT.merlin.tiles;

        import workexpIT.merlin.data.WorldData;
        import workexpIT.merlin.entities.Entity;
        import workexpIT.merlin.entities.Rock;
        import workexpIT.merlin.graphics.JavaDrawer;

public class Hole extends Tile {

    public Hole() {
        super(2,false);
    }

    @Override
    public boolean movingOnToTileExtra(Entity e) {
        //Cannot move past ever (Wall)
        if (instance == 0) {
            if (e.getName() == "rock") {
                WorldData.entities.remove(e);
                setInstance(1);
                JavaDrawer.redrawMap(getLocation());
                return true;
            } else {
                return false;
            }
        }
        else {
            return  true;
        }
    }
}
