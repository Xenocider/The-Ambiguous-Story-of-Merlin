package workexpIT.merlin.entities;

import workexpIT.merlin.Output;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.graphics.Drawer;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/**
 * Created by ict11 on 2016-02-10.
 */
public class George extends Entity {

    public static BufferedImage[] sprites = {
            ImageReader.loadImage("resources/graphics/charactersprites/george/0.png")
    };

    public George(int x, int y, int state, int level) {
        super(x, y, "george", state, level, sprites);
    }

    @Override
    public void runAI() {
        int pX = WorldData.getPlayer().getX();
        int pY = WorldData.getPlayer().getY();
        int dX = pX - x;
        int dY = pY - y;
        boolean go = true;

        Output.write("George: dX = " + dX + " dY = " + dY + " Last Direction = " + lastMove);
            //Try to move closer from farthest axis away
            if (go) {
                if (Math.abs(dY) == Math.abs(dX)) {
                    //Equal distance away on both X and Y
                    int action = (int) (Math.random());
                    //If 0 choose X
                    if (action == 0) {
                        if (dX > 0) {
                            if (lastMove != MOVE_LEFT) {
                                if (move(Entity.MOVE_RIGHT)) {
                                    go = false;
                                }
                                move(Entity.MOVE_RIGHT);
                                move(Entity.MOVE_RIGHT);
                            }
                        } else if (dX < 0) {
                            if (lastMove != MOVE_RIGHT) {
                                if (move(Entity.MOVE_LEFT)) {
                                    go = false;
                                    move(Entity.MOVE_LEFT);
                                    move(Entity.MOVE_LEFT);
                                }
                            }
                        }
                    }
                    //If 1 choose Y
                    else {
                        if (dY > 0) {
                            if (lastMove != MOVE_UP) {
                                if (move(Entity.MOVE_DOWN)) {
                                    go = false;
                                }
                                move(Entity.MOVE_DOWN);
                                move(Entity.MOVE_DOWN);
                            }
                        } else if (dY < 0) {
                            if (lastMove != MOVE_DOWN) {
                                if (move(Entity.MOVE_UP)) {
                                    go = false;
                                }
                                move(Entity.MOVE_UP);
                                move(Entity.MOVE_UP);
                            }
                        }
                    }
                    //If cannot move in that direction, try the other
                    if (go) {
                        if (action == 0) {
                            if (dY > 0) {
                                if (lastMove != MOVE_UP) {
                                    if (move(Entity.MOVE_DOWN)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_DOWN);
                                    move(Entity.MOVE_DOWN);
                                }
                            } else if (dY < 0) {
                                if (lastMove != MOVE_DOWN) {
                                    if (move(Entity.MOVE_UP)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_UP);
                                    move(Entity.MOVE_UP);
                                }
                            }
                        }
                        else {
                            if (dX > 0) {
                                if (lastMove != MOVE_LEFT) {
                                    if (move(Entity.MOVE_RIGHT)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_RIGHT);
                                    move(Entity.MOVE_RIGHT);
                                }
                            } else if (dX < 0) {
                                if (lastMove != MOVE_RIGHT) {
                                    if (move(Entity.MOVE_LEFT)) {
                                        go = false;
                                        move(Entity.MOVE_LEFT);
                                        move(Entity.MOVE_LEFT);
                                    }
                                }
                            }
                        }
                    }
                }
                if (Math.abs(dX) > Math.abs(dY)) {
                    //X value is farthest
                    if (dX > 0) {
                        if (lastMove != MOVE_LEFT) {

                            if (move(Entity.MOVE_RIGHT)) {
                                go = false;
                            }
                            move(Entity.MOVE_RIGHT);
                            move(Entity.MOVE_RIGHT);
                        }
                    } else if (dX < 0) {
                        if (lastMove != MOVE_RIGHT) {

                            if (move(Entity.MOVE_LEFT)) {
                                go = false;
                            }
                            move(Entity.MOVE_LEFT);
                            move(Entity.MOVE_LEFT);
                        }
                    }
                }
                if (Math.abs(dY) > Math.abs(dX)) {
                    //Y value is farthest
                    if (dY > 0) {
                        if (lastMove != MOVE_UP) {

                            if (move(Entity.MOVE_DOWN)) {
                                go = false;
                            }
                            move(Entity.MOVE_DOWN);
                            move(Entity.MOVE_DOWN);
                        }
                    } else if (dY < 0) {
                        if (lastMove != MOVE_DOWN) {
                            if (move(Entity.MOVE_UP)) {
                                go = false;
                            }
                            move(Entity.MOVE_UP);
                            move(Entity.MOVE_UP);
                        }
                    }
                }
                    //If no luck with moving closer from farthest axis, try other axis
                    if (go) {
                        if (Math.abs(dX) > Math.abs(dY)) {
                            //X value is farthest
                            if (dY > 0) {
                                if (lastMove != MOVE_UP) {

                                    if (move(Entity.MOVE_DOWN)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_DOWN);
                                    move(Entity.MOVE_DOWN);
                                }
                            } else if (dY < 0) {
                                if (lastMove != MOVE_DOWN) {
                                    if (move(Entity.MOVE_UP)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_UP);
                                    move(Entity.MOVE_UP);
                                }
                            }
                        }
                        if (Math.abs(dY) > Math.abs(dX)) {
                            //Y value is farthest
                            if (dX > 0) {
                                if (lastMove != MOVE_LEFT) {

                                    if (move(Entity.MOVE_RIGHT)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_RIGHT);
                                    move(Entity.MOVE_RIGHT);
                                }
                            } else if (dX < 0) {
                                if (lastMove != MOVE_RIGHT) {


                                    if (move(Entity.MOVE_LEFT)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_LEFT);
                                    move(Entity.MOVE_LEFT);
                                }
                            }
                        }
                    }

                    //If you still have no luck try moving away in the closest direction
                    if (go) {
                        if (Math.abs(dX) > Math.abs(dY)) {
                            //X value is farthest
                            if (dX > 0) {
                                if (lastMove != MOVE_RIGHT) {

                                    if (move(Entity.MOVE_LEFT)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_LEFT);
                                    move(Entity.MOVE_LEFT);
                                }
                            } else if (dX < 0) {
                                if (lastMove != MOVE_LEFT) {

                                    if (move(Entity.MOVE_RIGHT)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_RIGHT);
                                    move(Entity.MOVE_RIGHT);
                                }
                            }
                        }
                        if (Math.abs(dY) > Math.abs(dX)) {
                            //Y value is farthest
                            if (dY > 0) {
                                if (lastMove != MOVE_DOWN) {

                                    if (move(Entity.MOVE_UP)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_UP);
                                    move(Entity.MOVE_UP);
                                }
                            } else if (dY < 0) {
                                if (lastMove != MOVE_UP) {

                                    if (move(Entity.MOVE_DOWN)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_DOWN);
                                    move(Entity.MOVE_DOWN);
                                }
                            }
                        }
                    }

                    //If still no luck try the last direction
                    if (go) {
                        if (Math.abs(dX) > Math.abs(dY)) {
                            //X value is farthest
                            if (dY > 0) {
                                if (lastMove != MOVE_DOWN) {
                                    if (move(Entity.MOVE_UP)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_UP);
                                    move(Entity.MOVE_UP);
                                }
                            } else if (dY < 0) {
                                if (lastMove != MOVE_UP) {
                                    if (move(Entity.MOVE_DOWN)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_DOWN);
                                    move(Entity.MOVE_DOWN);
                                }
                            }
                        }
                        if (Math.abs(dY) > Math.abs(dX)) {
                            //Y value is farthest
                            if (dX > 0) {
                                if (lastMove != MOVE_RIGHT) {
                                    if (move(Entity.MOVE_LEFT)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_LEFT);
                                    move(Entity.MOVE_LEFT);
                                }
                            } else if (dX < 0) {
                                if (lastMove != MOVE_LEFT) {
                                    if (move(Entity.MOVE_RIGHT)) {
                                        go = false;
                                    }
                                    move(Entity.MOVE_RIGHT);
                                    move(Entity.MOVE_RIGHT);
                                }
                            }
                        }
                    }
        }
    }

}