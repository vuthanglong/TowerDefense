package Model.Sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameMediaPlayer {
    private boolean muted;
    private List<MediaPlayer> mediaPlayerList = new ArrayList<>();

    public GameMediaPlayer() {
        buildList();
    }

    public void mute() {
        if(!muted) {
            for (MediaPlayer media : mediaPlayerList) {
                media.setVolume(0);
            }
            muted = true;
        }
        else {
            for (MediaPlayer media : mediaPlayerList) {
                media.setVolume(1.5);
            }
            muted = false;
        }
    }

    private void buildList()
    {
        for(int i = 0; i <= 10; i++){
            MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("src/Model/Sound/Source/Music" + i + ".mp3").toURI().toString()));
            mediaPlayer.setVolume(1.5);
            mediaPlayerList.add(mediaPlayer);
        }
    }

    public MediaPlayer getMediaPlayer(int i) {
        return mediaPlayerList.get(i);
    }
}


