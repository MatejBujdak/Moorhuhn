package application;

import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color; 

public class Sliepka {

	public ImageView picture; 
	public int rychlost_letu_male = 8;
	public int rychlost_letu_stredne = 5;
	public int rychlost_letu_velke = 3;
	public int sliepka_velkost;
	private int obrazok = 0; 

	public double pic_sirka;
	private double pic_vyska;
	public boolean alive = true;
	private Image pc;
	public int reverse = (int) (Math.random() * 2);
	private int random_spawn;            
	private Rectangle2D viewport;
	
	public Sliepka(int w, int h, Group root) {
		Image img = new Image("images/bird-sprite.png");
		sliepka_velkost = (int) (Math.random() * 3); 
		double img_sirka = img.getWidth();
		double img_vyska = img.getHeight();
		this.random_spawn = (h/8 * 5);	//rozsah nahodneho objavenia sliepky
		
		///////////////////////////////
		
		if(reverse == 1) { // sliepka z prava do lava
			
		if(sliepka_velkost == 1) {
			pc = new Image("images/bird-sprite.png", (img_sirka /3), (img_vyska/3), false, false);
		}else if(sliepka_velkost == 2) {
		    pc = new Image("images/bird-sprite.png", (img_sirka /2), (img_vyska/2), false, false);
		}else {
			pc = new Image("images/bird-sprite.png", img_sirka, img_vyska, false, false);
		}
		
		picture = new ImageView(pc);
		picture.setLayoutX(w);
	    
		}else {    // sliepka z lava do prava
			
			if(sliepka_velkost == 1) {
				pc = reverseImageHorizontally(new Image("images/bird-sprite.png", (img_sirka /3), (img_vyska/3), false, false));
			}else if(sliepka_velkost == 2) {
			    pc = reverseImageHorizontally(new Image("images/bird-sprite.png", (img_sirka /2), (img_vyska/2), false, false));
			}else {
				pc = reverseImageHorizontally(new Image("images/bird-sprite.png", img_sirka, img_vyska, false, false));
			}
			
			
			picture = new ImageView(pc);
			picture.setLayoutX(-img_sirka/2);
		}
			double pic_sirka = (pc.getWidth() / 2);
		    double pic_vyska = (pc.getHeight() / 13);
		
		////////////////////////////////
		    
		    
	    this.pic_sirka = pic_sirka;
	    this.pic_vyska = pic_vyska;
	
	    picture.setLayoutY(Math.random() * random_spawn);

	    picture.setOnMouseClicked(e -> klik());
	    
	    root.getChildren().add(picture);
	}
	
	// animacia letu sliepky
	public void animacia() {
		if(alive) {
			obrazok++;
			if(obrazok > 12) obrazok = 0;
		}else {
			obrazok++;
			if(obrazok > 7) obrazok = 7;
		}

	}
	
	 public void pripravitZmenuViewportu() {
		 if(reverse == 1) {
			if(alive) {
				viewport = new Rectangle2D(0, obrazok * pic_vyska, pic_sirka, pic_vyska);
			}else {
				viewport = new Rectangle2D(pic_sirka, obrazok * pic_vyska, pic_sirka, pic_vyska);	
			}
		 }else {
			 if(alive) {
				 viewport = new Rectangle2D(pic_sirka, obrazok * pic_vyska, pic_sirka, pic_vyska);
			 }else {
				 viewport = new Rectangle2D(0, obrazok * pic_vyska, pic_sirka, pic_vyska);
			 }
		 }
	    }
	 
	 public Rectangle2D getViewport() {
	        return viewport;
	    }
	 
	 public ImageView obrazok() {
		 return picture;
	 }
	 
	 // otocenie obrazku sliepky
	 public Image reverseImageHorizontally(Image originalImage) {
		    int width = (int) originalImage.getWidth();
		    int height = (int) originalImage.getHeight();
		    
		    WritableImage reversedImage = new WritableImage(width, height);
		    
		    PixelReader pixelReader = originalImage.getPixelReader();
		    PixelWriter pixelWriter = reversedImage.getPixelWriter();
		    
		    for (int x = 0; x < width; x++) {
		        for (int y = 0; y < height; y++) {
		            Color color = pixelReader.getColor(width - x - 1, y);
		            pixelWriter.setColor(x, y, color);
		        }
		    }
		    
		    return reversedImage;
		}
	 
	 //akcia po kliknuti na sliepku
		public void klik() {  
			if (alive && Game.nabity_zasobnik) {
	            alive = false;
	            obrazok = 0;
	            
	            if(sliepka_velkost == 1) {
	            	Game.score += 10;
	            }else if(sliepka_velkost == 2) {
	            	Game.score += 5;
	            }else {
	            	Game.score += 2;
	            }
	            
	            // kill sound
	            try {
	                URL resource = Game.class.getResource("/Sounds/kill.wav");
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
	
}
