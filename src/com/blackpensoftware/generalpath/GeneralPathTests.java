package com.blackpensoftware.generalpath;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;
import java.util.Random;

public class GeneralPathTests extends Applet{

	public void init(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screen_width = (int)(screenSize.getWidth());
		int screen_height = (int)(screenSize.getHeight());
		
		this.setSize(new Dimension(screen_width, screen_height));
	}//End of init method
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		Random ran = new Random();
		
		int Base_x = 100, Base_y = 100;
		
		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_NON_ZERO, 2);

		polyline.moveTo(Base_x, Base_y);
		polyline.lineTo(Base_x + 100, Base_y + 150);
		polyline.curveTo(Base_x + 200,Base_y + 150, Base_x + 150, Base_y + 75, Base_x + 175, Base_y + 100);
		
		polyline.closePath();
		g2.setColor(new Color(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
		g2.fill(polyline);

	}// End of paint method
}//End of class
