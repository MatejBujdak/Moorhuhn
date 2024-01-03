package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Zasobnik {
	
	private Rectangle2D viewport;
	ImageView pc;
	Group root;
	private double img_sirka;
	private double img_vyska;
	private int obrazok = 0;
	private boolean vystrelil = false;
	
	public Zasobnik(int pos_x, int map_width, int map_height, Group root) {
		Image img = new Image("images/ammo-sprite.png");
		pc = new ImageView(img);
		
		img_sirka = img.getWidth();
		img_vyska = (img.getHeight() / 21);
		
		pc.setLayoutX(map_width - pos_x - img_sirka);
		pc.setLayoutY(map_height - 130);

		viewport = new Rectangle2D(0, obrazok * img_vyska, img_sirka, img_vyska);
		pc.setViewport(viewport);
		this.root = root;
		root.getChildren().add(pc);
	}
	
	public void set_obrazok() {
		this.obrazok++;
		if(obrazok < 3) {
			pc.setLayoutY(pc.getLayoutY() - 10);
		}else if(obrazok < 6){
			pc.setLayoutY(pc.getLayoutY() - 5);
		}else if(obrazok < 9){
			pc.setLayoutY(pc.getLayoutY() + 5);
		}else {
			pc.setLayoutY(pc.getLayoutY() + 10);
		}
		viewport = new Rectangle2D(0, obrazok * img_vyska, img_sirka, img_vyska);
		pc.setViewport(viewport);
	}
	
	public void set_vystrel() {
		this.vystrelil = true;
	}
	
	
	public boolean vystrelil() {
		return vystrelil;
	}
	

	public ImageView obrazok() {
		return pc;
	}
}
