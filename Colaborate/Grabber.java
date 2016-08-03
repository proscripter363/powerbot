package Colaborate;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.*;

public class Grabber extends ClientAccessor
{
	private static final int wineID = 245;
	private boolean Ready; // Ready to grab
	private int winesInInv;
	private int winesGrabbed;
	
    public Grabber(ClientContext ctx) 
    {
		super(ctx);
		ctx.backpack.select();
		winesInInv = ctx.backpack.id(wineID).count();
		winesGrabbed = 0;
		Ready = false;
	}
    
    public void grab() {
		

		if(ctx.players.local().animation() == -1) //If idle
		{
		  //Get Wine
		  GroundItem wine = ctx.groundItems.select().id(wineID).nearest().poll();
		  
		  //Ready Up
          setUp();
		  
		  //Hover
		  Tile temp = new Tile(ctx.players.local().tile().x(), ctx.players.local().tile().y()-1);
		  
		  
		  //If the mouse is not on the precise point then move it
		  if((ctx.input.getLocation().x != temp.matrix(ctx).centerPoint().x || ctx.input.getLocation().y != temp.matrix(ctx).centerPoint().y))
		  {
			  ctx.input.move(temp.matrix(ctx).centerPoint().x,temp.matrix(ctx).centerPoint().y);
		  }	
		  
		  //Grab
		  if (wine.inViewport() && wine != null && Ready)
		  {	  
			  if(ctx.client().isSpellSelected())
			  {
				  ctx.input.click(true);
				  exitToLobby();
			  }
		  
			  Ready = false;
			  
		  }
		  
		  //Count wines
		  ctx.backpack.select();	
		  if(winesInInv < ctx.backpack.id(wineID).count())
		  {
			  winesInInv++;
			  winesGrabbed++;
		  }

		}
	}
    
    private void setUp()
    {
    	Component out = ctx.widgets.widget(1477).component(75).component(1);
    	Component exit = ctx.widgets.widget(26).component(14);
    	
    	if(!exit.visible())
    		out.click();
    	
    	if(!ctx.client().isSpellSelected())
    		ctx.input.send("2");
		 
    	Ready = true;
    }
    
    private void exitToLobby()
    {
		  //Exit to Lobby to Avoid Damage
		  Component exit = ctx.widgets.widget(26).component(14);
		  		  
		  exit.click();
		  Condition.sleep(Random.nextInt(3000, 3600));
  	
    }
	
	
	public int getWinesGrabbed()
	{
		return winesGrabbed;
	}
	
    public void resetWinesInInv()
    {
       winesInInv = 0;
    }
    

}
