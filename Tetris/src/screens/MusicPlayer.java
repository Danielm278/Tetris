package screens;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;

public class MusicPlayer {
    private Clip clip;
    private static MusicPlayer instance;

    private MusicPlayer() {
        // Private constructor to prevent instantiation
    }

    public static MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    // Method to play music with an option to loop
    public void playMusic(String filePath, boolean loop) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop(); // Stop any existing clip before starting a new one
            }
            File musicFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the background music
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    
    public void playSoundOnce(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
 }


    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}

