package application;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.util.Duration;
import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;




public class Game{
	
	private ImageView background;
	private ImageView background2;
	private ImageView background3;
	private ImageView background4;
	private boolean end = false;
	private int mnozstvo_sliepok = 20;  //mensie cislo, viacej sliepok
	static boolean nabity_zasobnik;
	Text counterText;
	Text time;
	Text reload;
	private Timer t;
	private int sec = 30;
	private int min = 1;
    static int score = 0;
    private int pocet_nabojov = 5; //pocet nabojov
    private int aktualny_naboj;
	private int w;
	private int h;
	Group root;
	private ArrayList<Sliepka> sliepky = new ArrayList<>();
	private ArrayList<Zasobnik> zasobnik = new ArrayList<>();
	    
	public Game(int w, int h, Group root, Scene scene) {
		this.root = root;
		
		// strela
		scene.setOnKeyPressed(event -> {
		    if (event.getCode() == KeyCode.R) {
		        if(score >= 5) score -= 5;
		        else score = 0;
		        for (int i = 0; i < zasobnik.size(); i++) {
		            root.getChildren().remove(zasobnik.get(i).obrazok());
		        }
		        
		        zasobnik.clear();
		        nabi();
		    }
		});
		
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
		    if (!end) { 
		    	
		    	for(int n = 0; n < zasobnik.size(); n++) {
					if(n == aktualny_naboj) {
						Zasobnik a = zasobnik.get(n);
						
						aktualny_naboj--; 
						a.set_vystrel();
						if(aktualny_naboj == -1) {
							nabity_zasobnik = false;
							reload.setText("PRESS R TO RELOAD");
						}
					}
				}
		    	
		    	// shoot sound
		    	if(nabity_zasobnik) {
		        try {
		            URL resource = Game.class.getResource("/Sounds/shotgun.wav");
		            String filePath = resource.getPath();
		            File file = new File(filePath);

		            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
		            Clip clip = AudioSystem.getClip();
		            clip.open(audioInputStream);
		            clip.start();

		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    	}
		    }
		});

		 
		 //nastavenie pozadia
	    Image bg = new Image("images/clouds.png" ,w,h,false,false);
	    Image bg2 = new Image("images/mountains.png",w,0,false,false);
	    Image bg3 = new Image("images/castle.png",w,0,false,false);
	    Image bg4 = new Image("images/foreground.png",w,0,false,false);
	    background = new ImageView(bg);
	    background2 = new ImageView(bg2);
	    background3 = new ImageView(bg3);
	    background4 = new ImageView(bg4);
	    
	    background2.setLayoutY(h - bg2.getHeight());
	    background3.setLayoutY(h - bg3.getHeight());
	    background4.setLayoutY(h - bg4.getHeight());

	    root.getChildren().addAll(background, background2);
	    root.getChildren().addAll( background3, background4);
	    this.w = w;
	    this.h = h;
	    
	    //timer
	  	t = new Timer(this);
	  	t.start();

	    
	    //music
	    try {
	    	URL resource = Game.class.getResource("/Music/music.wav");

	    	String filePath = resource.getPath();
	    	File file = new File(filePath);

	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    
	    //SCORE
	    counterText = new Text("" + score);
	    counterText.setLayoutX(w - 150);
	    counterText.setLayoutY(100);
	    
	    //RELOAD TEXT
	    reload = new Text("");
	    reload.setLayoutX(w - 380);
	    reload.setLayoutY(h - 80);
	    
	    //CAS DO KONCA HRY
	    time = new Text(min + ":" + sec);
	    time.setFill(Color.RED);
	    time.setLayoutX(50);
	    time.setLayoutY(100);
	    time.setFont(Font.font("Arial", FontWeight.BOLD, 70));


	    counterText.setFill(Color.BLACK);
	    counterText.setFont(Font.font("Arial", FontWeight.BOLD, 70));

	    
	    reload.setFill(Color.BLACK);
	    reload.setFont(Font.font("Arial", FontWeight.BOLD, 30));

	    root.getChildren().addAll(time, counterText, reload);
	    Timeline secondTimer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	sec--;
	        	if(sec < 0) {
	        		sec = 59;
	        		min--;
	        	}
	        	if(min == 0 && sec == 0) {
	        		koniec();
	        	}	
	        	String fsec = String.format("%02d", sec);
	        	time.setText(min + ":" + fsec);
	        }
	    }));
	    secondTimer.setCycleCount(Timeline.INDEFINITE);
	    secondTimer.play();

	    nabi();
	}

	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void update(double deltaTime) {
		
		//naboje

		for(int nb = 0; nb < zasobnik.size(); nb++) {
			Zasobnik zas = zasobnik.get(nb);
			
			if(zas.vystrelil()) {
				zas.set_obrazok();
			}
		}
		
		
		
		//sliepky
		int spawn = (int) (Math.random() * deltaTime);
		counterText.setText("" + score);
		
		if(spawn < deltaTime/ mnozstvo_sliepok) {
			Sliepka sliepka = new Sliepka(w, h, root);
		    sliepky.add(sliepka);
		}
	
		for (int i = 0; i < sliepky.size(); i++) {
		    Sliepka s = sliepky.get(i);
	        s.animacia();
	        
	        double pos_x = s.picture.getLayoutX();
	        s.rychlost_letu_male += 0.6;
	        s.rychlost_letu_stredne += 0.4;
	        s.rychlost_letu_velke += 0.25;
	        
			if(s.reverse == 1) {  // sliepka z prava do lava
				if((pos_x + s.pic_sirka) < 0) {  //preletela
					sliepky.remove(i);
		            i--;
		            continue;
				}else if(!s.alive){  // odstrani zostrelenu sliepku ked pri padani prekroci scenu
					double pos_y = s.picture.getLayoutY();
					if((pos_y+ 25) > h) {
						root.getChildren().remove(sliepky.get(i).obrazok());
						sliepky.remove(i);
			            i--;
			            continue;
					}else { //rychlost padania sliepky
						s.picture.setLayoutY(s.picture.getLayoutY() + 25);	
					}
				}else {  
					if(s.sliepka_velkost == 1) {
						s.picture.setLayoutX(pos_x -= s.rychlost_letu_male);
					}else if(s.sliepka_velkost == 2) {
						s.picture.setLayoutX(pos_x -= s.rychlost_letu_stredne);
					}else {
						s.picture.setLayoutX(pos_x -= s.rychlost_letu_velke);
					}
				}
			}else {    // sliepka z lava do prava
				if(pos_x > w) {  
					sliepky.remove(i);
		            i--;
		            continue;
				}else if(!s.alive){  // odstrani zostrelenu sliepku ked pri padani prekroci scenu
					double pos_y = s.picture.getLayoutY();
					if((pos_y+ 25) > h) {
						root.getChildren().remove(sliepky.get(i).obrazok());
						sliepky.remove(i);
			            i--;
			            continue;
					}else {     //rychlost padania sliepky
							s.picture.setLayoutY(s.picture.getLayoutY() + 25);	
					}
				}else {
					if(s.sliepka_velkost == 1) {
						s.picture.setLayoutX(pos_x += s.rychlost_letu_male);
					}else if(s.sliepka_velkost == 2) {
						s.picture.setLayoutX(pos_x += s.rychlost_letu_stredne);
					}else {
						s.picture.setLayoutX(pos_x += s.rychlost_letu_velke);
					}
				}
			}
	        
	        s.pripravitZmenuViewportu();
	    }

	    for (Sliepka s : sliepky) {
	        s.picture.setViewport(s.getViewport());
	    }
	}

	//funkcia ktora sa zavola ked hra skonci
	public void koniec() {
		end = true;
	    for (int i = 0; i < sliepky.size(); i++) {
	        sliepky.remove(i);
	    }
	    t.stop();
	    root.getChildren().clear();

	    Text gameOverText = new Text("Game Over! Your score: " + score);
	    gameOverText.setFill(Color.RED);
	    gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 70));
	    gameOverText.setStyle("-fx-font-style: italic;");
	    
	    Button button = new Button("Ukončiť hru");
	    button.setOnAction(e -> {
	        System.exit(0);
	    });

	    gameOverText.setFill(Color.RED);
	    gameOverText.setTextOrigin(VPos.CENTER);
	    gameOverText.setBoundsType(TextBoundsType.VISUAL);
	    
	    Bounds textBounds = gameOverText.getBoundsInLocal();
	    double textWidth = textBounds.getWidth();
	    double textHeight = textBounds.getHeight();
	    
	    gameOverText.setLayoutX((w - textWidth) / 2); 
	    gameOverText.setLayoutY((h - textHeight) / 2); 

	    Rectangle blackBackground = new Rectangle(w, h);
	    blackBackground.setFill(Color.BLACK);

	    root.getChildren().addAll(blackBackground, gameOverText, button);
	} 
	
	//nabitie zasobniku
	public void nabi() {
		//pump
	    try {
	    	URL resource = Game.class.getResource("/Sounds/pump.wav");

	    	String filePath = resource.getPath();
	    	File file = new File(filePath);

	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    reload.setText("");
		for(int i = 0; i < pocet_nabojov; i++) {
			Zasobnik naboj = new Zasobnik((i * 70),w, h, root);
			zasobnik.add(naboj);
			aktualny_naboj = pocet_nabojov-1;
			nabity_zasobnik = true;
		}
	}
}
