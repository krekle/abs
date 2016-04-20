package no.krekle.abs.abs.driver;

import java.util.Arrays;

/**
 * Created by krekle on 30/03/16.
 */
public class LogParser {

    private static String[] ids = new String[]{"2C1", "B4", "Distance"};
    private static String[] labels = new String[]{"Throttle", "Velocity", "Distance"};

    public static DriveInstance parseObdLine(String obdLine, DriveInstance driveInstance) {
        obdLine.replace("[", "");
        String[] obdList = obdLine.split(" ");
        if(obdList.length != 10) {
            System.out.println("Not parsable");
            System.out.println(obdList.length + "");
            System.out.println(Arrays.toString(obdList));
            return driveInstance;
        }

        // Get id
        String id = getId(obdList[0]);

        // Speed
        if(id.equals("B4")){
            // Parse correct value
            int vel = parseSpeed(obdList);

            // Apply the speed to the driveInstance
            driveInstance.setSpeed(vel);
            return driveInstance;

        } else if (id.equals("1C4")) {
            int rpm = parseRPM(obdList);

            // Apply rpm
            driveInstance.setRpm(rpm);
            return driveInstance;
        } else if (id.equals("2C1")) {
            int throt = parseThrottle(obdList);

            // Apply throttle
            driveInstance.setThrottle(throt);
        } else if(id.equals("224")) {
            // Brake in %
            int brake = (parseBrake(obdList));

            // Apply brake
            driveInstance.setBrake(brake);
        }

        return driveInstance;


    }

    /**
     * Converts odb format speed to int in km/h
     *
     * @param speedList number 7 and 8 in the odb data combines to create km/h
     * @return the speed in km/h rounded
     */
    private static int parseSpeed(String[] speedList){
        String a = speedList[7];
        String b = speedList[8];

        if(b.length() < 2){
            b = "0" + b;
        }

        String combined = a + b;

        //Divide to m/s
        int vel = Integer.parseInt(combined, 16) / 360;

        return vel;
    }

    private static int parseRPM(String [] rmpList) {
        String a = rmpList[2];
        String b = rmpList[3];

        if(b.length() < 2){
            b = "0" + b;
        }

        String combined = a + b;

        int rpm = Integer.parseInt(combined, 16);
        return rpm;
    }

    private static int parseThrottle(String[] pedalList) {
        String pedal  = pedalList[8];

        int throt = Integer.parseInt(pedal, 16);
        return throt;
    }

    private static int parseBrake(String[] brakeList) {
        String a = brakeList[6];
        String b = brakeList[7];

        if(b.length() < 2){
            b = "0" + b;
        }

        String combined = a + b;

        int brake = Integer.parseInt(combined, 16);
        return brake;
    }

    private static String getId(String _id) {
        return _id.replace("#", "");
    }



}

