package com.example.mp3;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController implements Initializable {
    @FXML
    Label myLabelVolume;
    @FXML
    AnchorPane myAnchorPane;
    @FXML
    Label myLabel;
    @FXML
    Button buttonNext,buttonPlay,buttonPause,buttonPreview;
    @FXML
    ProgressBar myProgressBar;
    @FXML
    Slider mySlider;
    private boolean running;
    private TimerTask task;
    private Timer timer;
    private ArrayList<File> songs;
    private File directory;
    private File[] files;
    private int songNumber;
    private int[] speed={25,50,75,100,125,150,175,200};

    private Media media;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs=new ArrayList<File>();
        directory=new File("/Users/mauriziore/Desktop/mp3/src/main/resources/song");
        files=directory.listFiles();
        if(files!=null){
            for(File file: files){
                songs.add(file);
            }
        }
        media=new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer=new MediaPlayer(media);
        playMusic();
        mySlider.valueProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(mySlider.getValue()*0.01);
                myLabelVolume.setText(Integer.toString((int)mySlider.getValue()*1));

            }
        });
    }
    public void previousMusic(){
        if(songNumber>0){
            songNumber--;
        }else{
            songNumber=songs.size()-1;
        }
        if(mediaPlayer.isAutoPlay()) {
            mediaPlayer.stop();
        }
        if(running){
            endTimer();
        }
        media=new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer=new MediaPlayer(media);
        playMusic();
    }
    public void pauseMusic(){
        running=false;
        endTimer();
        mediaPlayer.pause();
    }
    public void playMusic(){
        if(songs.get(songNumber).getName().endsWith(".mp3")){
            int endIndx=songs.get(songNumber).getName().indexOf(".mp3");
            String rimpiazzo=songs.get(songNumber).getName().replace(".mp3"," ");
            myLabel.setText(rimpiazzo);
        }
        //setto il volume iniziale come quello riportato nel valore della label
        mediaPlayer.setVolume(mySlider.getValue()*0.01);
        running=true;
        beginTimer();
        mediaPlayer.play();

    }
    public void nextMusic(){
        if(songNumber<songs.size()-1){
            songNumber++;
        }else{
            songNumber=0;
        }
        if(mediaPlayer.isAutoPlay()){
            mediaPlayer.stop();
        }
        if(running){
            endTimer();
        }
        media=new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer=new MediaPlayer(media);
        myLabel.setText(songs.get(songNumber).getName());
        playMusic();
    }
    public void beginTimer(){
        timer=new Timer();
        task=new TimerTask() {
            @Override
            public void run() {
                running=true;
                double corrente = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                //System.out.println(corrente/end);
                myProgressBar.setProgress(corrente/end);
                if(corrente/end == 1){
                    //la canzone e' terminata devo resettare la barra di progresso e devo far partire una nuova canzone
                    endTimer();
                    nextMusic();
                    //quando la canzone finisce non ne inizia una nuova, risolvi il problema
                }
            }
        };
        timer.schedule(task,1000,1000);
    }
    public void endTimer(){
        //la canzone e' il pausa o e' terminata, devo resettare la barra dei progressi
        running=false;
        timer.cancel();
    }
}