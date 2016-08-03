package Colaborate;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.text.NumberFormat;
import java.util.Locale;

import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;


public class Draw extends ClientAccessor 
{
	
	private long time = System.currentTimeMillis();
	private static final int price = GeItem.price(245);
	
    public Draw(ClientContext ctx) 
    {
		super(ctx);
	}

    public void drawMouse(Graphics g, String status, int wines) {

        final Graphics2D g2 = (Graphics2D) g;
        Point p = ctx.input.getLocation();

        g2.setColor(Color.white);
        g2.drawOval(p.x - 10, p.y - 10, 20, 20);
        g2.drawLine(p.x + 5, p.y + 5, p.x - 5, p.y - 5);
        g2.drawLine(p.x + 5, p.y - 5, p.x - 5, p.y + 5);
        
        AlphaComposite composite = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.85f );
        
        //border
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.red);
        g2.drawRect(15, 15, 200, 140);
        
        //Background
        g2.setStroke(new BasicStroke(1));
        g2.setComposite(composite);
        g2.setColor(Color.black);
        g2.fillRect(17, 16, 198, 138);
        
        //Title
        g2.setColor(Color.white);
        g2.drawString("Wine Grabber 2016", 60, 35);
        g2.drawLine(30, 45, 200, 45);
            
        //Text Updates
        g2.drawString("Status: " + status, 35, 70);
        g2.drawString("Profit: " + NumberFormat.getNumberInstance(Locale.US).format(price*wines) , 35, 90);
        g2.drawString("Wines Grabbed: " + NumberFormat.getNumberInstance(Locale.US).format(wines), 35, 110);
        g2.drawString("Average/Hour: " +  NumberFormat.getNumberInstance(Locale.US).format((3600000/(System.currentTimeMillis() - time))*(price*wines)),35 , 130);
    }
    

}