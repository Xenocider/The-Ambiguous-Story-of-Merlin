package workexpIT.merlin.entities;

import workexpIT.merlin.GameLoop;
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

    public George(int x, int y, int state, int level) {
        super(x, y, "george", state, level, ImageReader.loadImage("resources/graphics/charactersprites/george.png"));
        downWalkingSpritesId = new int[]{3, 4, 3, 5};
        upWalkingSpritesId = new int[]{0, 1, 0, 2};
        rightWalkingSpritesId = new int[]{9, 10, 9, 11};
        leftWalkingSpritesId = new int[]{6, 7, 6, 8};
        upSpriteId = 0;
        downSpriteId = 3;
        rightSpriteId = 9;
        leftSpriteId = 6;
        battleSprite = ImageReader.loadImage("resources/graphics/charactersprites/battle/"+getClass().getSimpleName()+".png");
        loadSprites();
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
                                if (GameLoop.pause) {go = false;}
                                move(Entity.MOVE_RIGHT);
                                move(Entity.MOVE_RIGHT);
                            }
                        } else if (dX < 0) {
                            if (lastMove != MOVE_RIGHT) {
                                if (move(Entity.MOVE_LEFT)) {
                                    go = false;
                                }
                                if (GameLoop.pause) {go = false;}

                                move(Entity.MOVE_LEFT);
                                    move(Entity.MOVE_LEFT);
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
                                if (GameLoop.pause) {go = false;}

                                move(Entity.MOVE_DOWN);
                                move(Entity.MOVE_DOWN);
                            }
                        } else if (dY < 0) {
                            if (lastMove != MOVE_DOWN) {
                                if (move(Entity.MOVE_UP)) {
                                    go = false;
                                }
                                if (GameLoop.pause) {go = false;}

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
                                    if (GameLoop.pause) {go = false;}

                                    move(Entity.MOVE_DOWN);
                                    move(Entity.MOVE_DOWN);
                                }
                            } else if (dY < 0) {
                                if (lastMove != MOVE_DOWN) {
                                    if (move(Entity.MOVE_UP)) {
                                        go = false;
                                    }
                                    if (GameLoop.pause) {go = false;}

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
                                    if (GameLoop.pause) {go = false;}

                                    move(Entity.MOVE_RIGHT);
                                    move(Entity.MOVE_RIGHT);
                                }
                            } else if (dX < 0) {
                                if (lastMove != MOVE_RIGHT) {
                                    if (move(Entity.MOVE_LEFT)) {
                                        go = false;
                                    }
                                        if (GameLoop.pause) {go = false;}
                                        move(Entity.MOVE_LEFT);
                                        move(Entity.MOVE_LEFT);
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
                            if (GameLoop.pause) {go = false;}

                            move(Entity.MOVE_RIGHT);
                            move(Entity.MOVE_RIGHT);
                        }
                    } else if (dX < 0) {
                        if (lastMove != MOVE_RIGHT) {

                            if (move(Entity.MOVE_LEFT)) {
                                go = false;
                            }
                            if (GameLoop.pause) {go = false;}

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
                            if (GameLoop.pause) {go = false;}

                            move(Entity.MOVE_DOWN);
                            move(Entity.MOVE_DOWN);
                        }
                    } else if (dY < 0) {
                        if (lastMove != MOVE_DOWN) {
                            if (move(Entity.MOVE_UP)) {
                                go = false;
                            }
                            if (GameLoop.pause) {go = false;}

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
                                    if (GameLoop.pause) {go = false;}

                                    move(Entity.MOVE_DOWN);
                                    move(Entity.MOVE_DOWN);
                                }
                            } else if (dY < 0) {
                                if (lastMove != MOVE_DOWN) {
                                    if (move(Entity.MOVE_UP)) {
                                        go = false;
                                    }
                                    if (GameLoop.pause) {go = false;}

                                    move(Entity.MOVE_UP);
                                    move(Entity.MOVE_UP);
                                }
                            }
                            else if (dY == 0) {
                                int action = (int) (Math.random());
                                if (action == 0) {
                                    if (lastMove != MOVE_UP) {

                                        if (move(Entity.MOVE_DOWN)) {
                                            go = false;
                                        }
                                        if (GameLoop.pause) {go = false;}

                                        move(Entity.MOVE_DOWN);
                                        move(Entity.MOVE_DOWN);
                                    }
                                }
                                else {
                                    if (lastMove != MOVE_DOWN) {
                                        if (move(Entity.MOVE_UP)) {
                                            go = false;
                                        }
                                        if (GameLoop.pause) {go = false;}

                                        move(Entity.MOVE_UP);
                                        move(Entity.MOVE_UP);
                                    }
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
                                    if (GameLoop.pause) {go = false;}

                                    move(Entity.MOVE_RIGHT);
                                    move(Entity.MOVE_RIGHT);
                                }
                            } else if (dX < 0) {
                                if (lastMove != MOVE_RIGHT) {


                                    if (move(Entity.MOVE_LEFT)) {
                                        go = false;
                                    }
                                    if (GameLoop.pause) {go = false;}

                                    move(Entity.MOVE_LEFT);
                                    move(Entity.MOVE_LEFT);
                                }
                            }
                            else if (dX == 0) {
                                int action = (int) (Math.random());
                                if (action == 0) {
                                    if (lastMove != MOVE_LEFT) {

                                        if (move(Entity.MOVE_RIGHT)) {
                                            go = false;
                                        }
                                        if (GameLoop.pause) {go = false;}

                                        move(Entity.MOVE_RIGHT);
                                        move(Entity.MOVE_RIGHT);
                                    }
                                }
                                else {
                                    if (lastMove != MOVE_RIGHT) {
                                        if (move(Entity.MOVE_LEFT)) {
                                            go = false;
                                        }
                                        if (GameLoop.pause) {go = false;}

                                        move(Entity.MOVE_LEFT);
                                        move(Entity.MOVE_LEFT);
                                    }
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
                                    if (GameLoop.pause) {go = false;}

                                    move(Entity.MOVE_LEFT);
                                    move(Entity.MOVE_LEFT);
                                }
                            } else if (dX < 0) {
                                if (lastMove != MOVE_LEFT) {

                                    if (move(Entity.MOVE_RIGHT)) {
                                        go = false;
                                    }
                                    if (GameLoop.pause) {go = false;}

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
                                    if (GameLoop.pause) {go = false;}

                                    move(Entity.MOVE_UP);
                                    move(Entity.MOVE_UP);
                                }
                            } else if (dY < 0) {
                                if (lastMove != MOVE_UP) {

                                    if (move(Entity.MOVE_DOWN)) {
                                        go = false;
                                    }
                                    if (GameLoop.pause) {go = false;}

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
                                    if (GameLoop.pause) {go = false;}

                                    move(Entity.MOVE_UP);
                                    move(Entity.MOVE_UP);
                                }
                            } else if (dY < 0) {
                                if (lastMove != MOVE_UP) {
                                    if (move(Entity.MOVE_DOWN)) {
                                        go = false;
                                    }
                                    if (GameLoop.pause) {go = false;}

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
                                    if (GameLoop.pause) {go = false;}

                                    move(Entity.MOVE_LEFT);
                                    move(Entity.MOVE_LEFT);
                                }
                            } else if (dX < 0) {
                                if (lastMove != MOVE_LEFT) {
                                    if (move(Entity.MOVE_RIGHT)) {
                                        go = false;
                                    }
                                    if (GameLoop.pause) {go = false;}

                                    move(Entity.MOVE_RIGHT);
                                    move(Entity.MOVE_RIGHT);
                                }
                            }
                        }
                    }
                //If still no luck.... somehow? Randomly choose between all direction not travelled previously and if still not go back the way they came
                if (go) {
                    boolean trying = true;
                    boolean up = false;
                    boolean right = false;
                    boolean down = false;
                    boolean left = false;
                    switch (lastMove) {
                        case Entity.MOVE_UP:
                            down = true;
                            break;
                        case Entity.MOVE_RIGHT:
                            left = true;
                            break;
                        case Entity.MOVE_DOWN:
                            up = true;
                            break;
                        case Entity.MOVE_LEFT:
                            right = true;
                    }
                    while (trying) {
                        int action = (int) (Math.random() * 3);
                        switch (action) {
                            case MOVE_UP:
                                if (move(MOVE_UP)) {
                                    trying = false;
                                    go = false;
                                    move(MOVE_UP);
                                    move(MOVE_UP);
                                }
                                else {
                                    up = true;
                                }
                                break;
                            case MOVE_RIGHT:
                                if (move(MOVE_RIGHT)) {
                                    trying = false;
                                    go = false;
                                    move(MOVE_RIGHT);
                                    move(MOVE_RIGHT);
                                }
                                else {
                                    right = true;
                                }
                                break;
                            case MOVE_DOWN:
                                if (move(MOVE_DOWN)) {
                                    trying = false;
                                    go = false;
                                    move(MOVE_DOWN);
                                    move(MOVE_DOWN);
                                }
                                else {
                                    down = true;
                                }
                                break;
                            case MOVE_LEFT:
                                if (move(MOVE_LEFT)) {
                                    trying = false;
                                    go = false;
                                    move(MOVE_LEFT);
                                    move(MOVE_LEFT);
                                }
                                else {
                                    left = true;
                                }
                                break;
                        }
                        if (up & right & down & left) {
                            trying = false;
                            switch (lastMove) {
                                case Entity.MOVE_UP:
                                    move(MOVE_DOWN);
                                    move(MOVE_DOWN);
                                    move(MOVE_DOWN);
                                    break;
                                case Entity.MOVE_RIGHT:
                                    move(MOVE_LEFT);
                                    move(MOVE_LEFT);
                                    move(MOVE_LEFT);
                                    break;
                                case Entity.MOVE_DOWN:
                                    move(MOVE_UP);
                                    move(MOVE_UP);
                                    move(MOVE_UP);
                                    break;
                                case Entity.MOVE_LEFT:
                                    move(MOVE_RIGHT);
                                    move(MOVE_RIGHT);
                                    move(MOVE_RIGHT);
                            }

                        }

                    }
                }
        }
    }

}