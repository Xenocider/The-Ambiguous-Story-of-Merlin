package workexpIT.merlin.data;

import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.entities.Rock;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ict11 on 2016-04-25.
 */
public class EventReader {

    public static String map = null;
    public static String entityClass;

    public enum EventTriggerType {LocationTrigger};
    public enum CommandType {addEntity,runFunctionOfEntity}

    public static CommandType commandType;

    public static String entityDialog;
    public static String function;
    public static List<String> argumentsType = new ArrayList<String>();
    public static List<String> arguments = new ArrayList<String>();

    public static void runLocationEvent(int x, int y) {

    }

    public static void loadEventData(String mapid) {
        map = mapid;
        Output.write("Loading Event Data...");
        FileReader FReader = null;
        try {
            FReader = new FileReader("resources/worlddata/default/" + mapid + "/events.txt");
            BufferedReader BReader = new BufferedReader(FReader);

            String line = null;
            int insideLevel = 0;

            while ((line = BReader.readLine()) != null) {
                if (line.contains("locationTrigger") && insideLevel == 0) {
                    int x;
                    int y;
                    Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
                    m.find();
                    x = Integer.parseInt(m.group(1));
                    m.find();
                    y = Integer.parseInt(m.group(1));
                    WorldData.tiles[x][y].locationTrigger = true;
                    Output.write("Location trigger added");
                }
                if (line.contains("{")) {
                    insideLevel = insideLevel + 1;
                }
                if (line.contains("}")) {
                    insideLevel = insideLevel - 1;
                    if (insideLevel < 0) {
                        Output.error("There are more } than { and so I cannot read the data file correctly...");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readEventData(EventTriggerType trigger,int[] loc) {
        if (trigger == EventTriggerType.LocationTrigger) {
            Output.write("Loading Event Data...");
            FileReader FReader = null;
            try {
                FReader = new FileReader("resources/worlddata/default/" + map + "/events.txt");
                BufferedReader BReader = new BufferedReader(FReader);

                String line = null;
                int insideLevel = 0;

                boolean shouldContinue = false;
                boolean finished = false;

                while ((line = BReader.readLine()) != null && !finished) {
                    Output.write("reading line: " + line);
                    if (line.contains("locationTrigger") && insideLevel == 0) {
                        int x;
                        int y;
                        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
                        m.find();
                        x = Integer.parseInt(m.group(1));
                        m.find();
                        y = Integer.parseInt(m.group(1));
                        if (x == loc[0] && y == loc[1]) {
                            shouldContinue = true;
                        }
                    }
                    if (line.contains("{")) {
                        insideLevel = insideLevel + 1;
                    }
                    if (line.contains("}")) {
                        insideLevel = insideLevel - 1;
                        if (insideLevel < 0) {
                            Output.error("There are more } than { and so I cannot read the data file correctly...");
                        }
                        if (insideLevel == 0) {
                            shouldContinue = false;
                            finished = true;
                        }
                    }
                    if (shouldContinue) {
                        if (line.contains("addEntity")) {
                            commandType = CommandType.addEntity;
                        }
                        if (line.contains("runFunctionOfEntity")) {
                            Output.write("true");
                            commandType = CommandType.runFunctionOfEntity;
                        }
                        if (commandType == CommandType.addEntity) {
                            if (insideLevel == 1) {
                                Entity entity;
                                int state;
                                int level;
                                int x;
                                int y;
                                String dialog;
                                boolean talk;
                                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
                                m.find();
                                Class<?> clazz = Class.forName("workexpIT.merlin.entities." + m.group(1));
                                m.find();
                                state = Integer.parseInt(m.group(1));
                                m.find();
                                level = Integer.parseInt(m.group(1));
                                m.find();
                                x = Integer.parseInt(m.group(1));
                                m.find();
                                y = Integer.parseInt(m.group(1));
                                m.find();
                                dialog = m.group(1);
                                m.find();
                                if (m.group(1) == "true") {
                                    talk = true;
                                } else {
                                    talk = false;
                                }
                                Constructor<?> ctor = clazz.getConstructor(int.class, int.class, int.class, int.class, String.class, boolean.class);
                                entity = (Entity) ctor.newInstance(x, y, state, level, dialog, talk);
                                WorldData.entities.add(entity);
                                commandType = null;
                            }
                        }
                        if (commandType == CommandType.runFunctionOfEntity) {
                            if (insideLevel == 1) {
                                Output.write("getting level 1 args");
                                argumentsType.clear();
                                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
                                m.find();
                                entityClass = m.group(1);
                                m.find();
                                entityDialog = m.group(1);
                                m.find();
                                function = m.group(1);
                                boolean go = true;
                                while (m.find()) {
                                    Output.write("loop");
                                    argumentsType.add(m.group(1));
                                }
                            }
                            if (insideLevel == 2 && !line.contains("{")) {
                                arguments.clear();
                                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
                                boolean go = true;
                                while (m.find()) {
                                    arguments.add(m.group(1));
                                }
                                runFunctionOfEntity(entityClass,entityDialog,function,argumentsType,arguments);
                                commandType = null;
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            Output.error("Cannot load this kind of event trigger with the data provided");
        }
        WorldData.tiles[loc[0]][loc[1]].locationTrigger = false;
    }

    private static void runFunctionOfEntity(String entityClassName, String entityDialog, String function, List<String> argumentsType, List<String> arguments) throws ClassNotFoundException {
        Output.write("finding entity");
        Entity entity = WorldData.findEntity(entityDialog);
        Output.write("found entity");
        Class<?> clazz = Class.forName("workexpIT.merlin.entities." + entityClassName);
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < argumentsType.size(); i++) {
            if (argumentsType.get(i).contains("int")) {
                list.add(Integer.parseInt(arguments.get(i)));
            }
            else if (argumentsType.get(i).contains("boolean")) {
                if (arguments.get(i).contains("true")) {
                    list.add(true);
                }
                else {
                    list.add(false);
                }
            }
            else if (argumentsType.get(i).contains("string")) {
                list.add(arguments.get(i));
            }
        }
        Object[] args = list.toArray();
        Class[] classes = new Class[args.length];
        for (int i = 0; i < args.length; i ++) {
            classes[i] = args[i].getClass();
            if (classes[i] == Integer.class) {
                classes[i] = int.class;
            }
        }
        try {
            Output.write("getting the method: " + function);
            Output.write(clazz.getSimpleName());
            Method method = clazz.getMethod(function,classes);
            Output.write("got function, now invocking it with " + args.length + " arguments");
            Output.write(entity.getName() + "  " + args[0]);
            method.invoke(entity,args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
