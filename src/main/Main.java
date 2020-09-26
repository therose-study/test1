package main;

import base.Playlist;
import base.Song;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import playMusic.Player;
import playlistPane.PlaylistListPane;
import playlistPane.TopListPane;
import spider.WYY;
import ShowSongList.SongListPane;
import titlePane.TitlePane;
import userPane.UserPane;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private WYY wyy;
    private Stage playerStage,userStage,mainStage,titleStage,bgStage ,maskStage;
    private UserPane userPane;
    private Player player;
    private SongListPane songListPane;
    private PlaylistListPane playlistListPane;
    private Scene scene1,scene2,scene3;
    private TitlePane titlePane;
    private TopListPane topListPane;

    private double x,y;
    private double tx,ty,mx,my;
    private boolean pressing = false;

    @Override
    public void start(Stage primaryStage) throws Exception {

        x = 500;

        y = 200;

        // load the spider application

        wyy = new WYY();

        Playlist[] playlists = wyy.getTopList();


        loadUserPane();

        userPane.setSpider(wyy);

        loadPlayer();

        songListPane = new SongListPane(player,userPane.listPane,wyy,titleStage);

        userPane.setSongListPane(songListPane);

        topListPane = new TopListPane(wyy);

        playlistListPane = new PlaylistListPane(songListPane,wyy,scene2,mainStage);

        loadMainPane();

        loadTitle();

//        loadBackground();

        mainStage.initOwner(titleStage);

        playerStage.initOwner(titleStage);

        userStage.initOwner(titleStage);

//        maskStage.initOwner(titleStage);
//        bgStage.initOwner(titleStage);


        titleStage.show();


//        bgStage.show();
//        maskStage.show();


        mainStage.show();

        playerStage.show();

        userStage.show();

        userPane.setPP(mainStage,scene1,scene2,scene3,topListPane);

        topListPane.setPP(mainStage,scene2,songListPane);

        titlePane.setPP(mainStage,scene2);

        titlePane.setOnClose(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    userPane.exit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    wyy.exit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                titleStage.close();
            }
        });
    }

    public static void main(String[] args){
        launch(args);
    }

    private void loadTitle(){
        titleStage = new Stage();
        titlePane = new TitlePane(wyy,songListPane,titleStage);
        titleStage.initStyle(StageStyle.TRANSPARENT);
        titleStage.setScene(new Scene(titlePane,1000,50));
        titleStage.setX(x);
        titleStage.setY(y);

        titlePane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!pressing) {
                    tx = titleStage.getX();
                    ty = titleStage.getY();
                    mx = event.getScreenX();
                    my = event.getScreenY();
                    pressing = true;
                }
            }
        });
        titlePane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pressing = false;
            }
        });
        titlePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double nx = event.getScreenX()+tx-mx;
                double ny = event.getScreenY()+ty-my;
                titleStage.setX(nx);
                titleStage.setY(ny);
                mainStage.setX(nx+200);
                mainStage.setY(ny+50);
                playerStage.setX(nx);
                playerStage.setY(ny+650);
                userStage.setX(nx);
                userStage.setY(ny+50);


            }
        });
    }

    private void loadPlayer() throws IOException {
        playerStage = new Stage();
        URL location = getClass().getResource("../playMusic/playMusic.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();
        player =  fxmlLoader.getController();
        player.primaryStage = playerStage;
        player.root = root;
        player.wyy = wyy;
        player.userPane = userPane;
        player.init();
        playerStage.initStyle(StageStyle.TRANSPARENT);
        playerStage.setX(x);
        playerStage.setY(y+650);
    }

    private void loadUserPane() throws IOException {
        userStage = new Stage();
        userPane = new UserPane();
        Scene scene = new Scene(userPane,200,600);
        scene.setFill(null);
        userPane.setStyle("-fx-background-color: #ffffff ;");
        userStage.initStyle(StageStyle.TRANSPARENT);
        userStage.setScene(scene);
        userStage.setX(x);
        userStage.setY(y+50);
    }

    private void loadMainPane() throws IOException {
        mainStage = new Stage();
        scene1 = new Scene(playlistListPane,800,600);
        scene1.setFill(Color.WHITE);
        playlistListPane.setStyle("-fx-background-color: #ffffff ;");
        scene2 = new Scene(songListPane,800,600);
        scene3 = new Scene(topListPane,800,600);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.setScene(scene1);
        mainStage.setX(x+200);
        mainStage.setY(y+50);
        playlistListPane.setPP(scene2,mainStage);
    }

    private void play(Song song) throws IOException {
        player.addMusic(song);
    }

}
