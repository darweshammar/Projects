package localserver;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**@author Olof Persson, Karl Andersson  @coauthor Per Blomqvist**/
class PlaySound implements Runnable {

    private String filepath;

    public PlaySound(String filepath){
        this.filepath = filepath;
    }


    public void run() {
        AudioInputStream audioIn;
        try {
            audioIn = AudioSystem.getAudioInputStream(new File(filepath));
            Clip clip;
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength()/1000);
            clip.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException  e1) {
            e1.printStackTrace();
        }
    }
}

