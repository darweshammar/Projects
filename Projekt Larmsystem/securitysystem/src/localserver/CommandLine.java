package localserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Scanner;

/**@author Olof Persson, Karl Andersson  @coauthor Per Blomqvist, Jens Moths**/
public class CommandLine implements Runnable {
    static int number = 0;
    Controller controller;

    public CommandLine(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        while (!controller.cameraReady) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String[] commandTakePic = {"raspistill", "-t","1","-o", "/home/pi/data/photo/cam" + number + ".jpg"};
        controller.cameraReady = false;
        ProcessBuilder processBuilder = new ProcessBuilder(commandTakePic);
        processBuilder.directory(new File(System.getProperty("user.home")));
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("\nExited with error code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        controller.pictureTaken(number);
        number++;
        controller.cameraReady = true;
    }
}