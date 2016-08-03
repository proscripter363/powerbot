package Colaborate;

import java.awt.Graphics;
import org.powerbot.script.*;
import org.powerbot.script.rt6.Bank.Amount;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

@Script.Manifest(name = "Wine Grabber 2016", description = "Grabs Wines Like A Boss.")

public class winegrabber2016 extends PollingScript<ClientContext> implements PaintListener {

	private static final int wineID = 245;
	private static final int lawID = 563;
	private Grabber grabClass;
	private LogIn logInClass;
	private Draw draw;
	private boolean forceBank;
	private int favNum;

	private enum STATE 
	{
		BANKING, GRABBING, TELEPORTING, TRAVELING, REPOSITIONING, LOBBY, ADJUSTING, IDLE
	}

	@Override
	public void start() 
	{
		//Class Set Up
		grabClass = new Grabber(ctx);
		logInClass = new LogIn(ctx);
		draw = new Draw(ctx);
		
		//Vars
		forceBank = false;
		favNum = 143;
		ctx.properties.put("login.disable", "true"); 
	}
	

	private STATE state() 
	{
		//If I am in the lobby
		if(!ctx.players.local().valid())
			return STATE.LOBBY;
		
		//Vars
		Tile playerLoc = ctx.players.local().tile();
		
		//Keep A Consistent Camera Angle
		if(!new Tile(2952,3474).matrix(ctx).inViewport() && !yawInBounds())
			return STATE.ADJUSTING;	
		
		//If I can see tile and im not on it or if camera angle is not right
		if(((playerLoc.x() != 2952 && playerLoc.y() != 3474 && new Tile(2952,3474).matrix(ctx).inViewport())) || !yawInBounds())
			return STATE.REPOSITIONING;

        if((checkFull() && playerLoc.y() < 3474) || forceBank)
        	return STATE.BANKING;
		
        //if im in the church and my inv is full or I have no runes left then teleport me
		if (checkFull() && playerLoc.y() >= 3471)
			return STATE.TELEPORTING; 
		
		//if im below the church travel there
		if(!checkFull() && (playerLoc.y() <= 3471 || playerLoc.x() >= 2954 ))
			return STATE.TRAVELING;

		//If im in position
		if (!checkFull() && (playerLoc.x() == 2952 && playerLoc.y() == 3474))
			return STATE.GRABBING;
		
		return STATE.IDLE;

	}
	
	private boolean yawInBounds()
	{
	  int yaw = ctx.camera.yaw();
	  int pitch = ctx.camera.pitch();
	  
	  //If in range
	  if( (pitch >= 83 && pitch <= 85) && ( (yaw > 350 && yaw <= 360) || (yaw >= 0 && yaw < 10) ))
	  {
		 return true; 
	  }
	  
	  return false;
	}
	
	private void move()
	{
		//Moves to certain tile
		
		Tile playerLoc = ctx.players.local().tile();
		
		if(playerLoc.x() != 2952 || playerLoc.y() != 3474)
		{
		  Tile x = new Tile(2952,3474);	
		  ctx.camera.turnTo(x.matrix(ctx));
		  x.matrix(ctx).interact("Walk Here");
		}
		
		Condition.sleep(Random.nextInt(800, 1500));
		
		moveCamera();
	}
	
	private void moveCamera()
	{
		if(!yawInBounds())
		{
			ctx.camera.pitch(Random.nextInt(82, 86));
			ctx.camera.angle(Random.nextInt(0, 3));
		}		
	}
	
	private void travel()
	{

		if(ctx.players.local().tile().distanceTo(ctx.movement.destination()) <= 10)
		{
			ctx.movement.step(new Tile(Random.nextInt(2948, 2953),Random.nextInt(3474, 3477),0));
		}
		else if(ctx.players.local().idle())
		{
			ctx.movement.step(new Tile(Random.nextInt(2948, 2953),Random.nextInt(3474, 3477),0));
		}

	}
    
	private void bank()
	{
        
		if(!ctx.bank.opened())
		{
		  if(ctx.bank.inViewport())
		  {
			  ctx.objects.select().id(11758).nearest().poll().interact("Bank");
		  }
		  else
		  {
			//Traverse to bank
			if(ctx.players.local().tile().distanceTo(ctx.movement.destination()) <= 10)
			{
				ctx.movement.step(ctx.bank.nearest());
			}
			else if(ctx.players.local().idle())
			{
				ctx.movement.step(ctx.bank.nearest());
			}
		  }
		}
		else
		{
		   forceBank = false;
		   ctx.bank.deposit(wineID, Amount.ALL);
		   grabClass.resetWinesInInv();
		   
		   //Refresh Backpack
		   ctx.backpack.select();
		   
		   //If no more law runes...stop the bot
		   if(ctx.backpack.id(563).count() == 0 && !ctx.bank.withdraw(563, 100))
		   {
			   System.out.println("Out Of Runes");
			   stop();
		   }
		   else if(ctx.backpack.id(wineID).count() == 0)
           {
        	   ctx.bank.close();
           }
           
           
		}
	}

	private void teleport() 
	{
		if(ctx.players.local().idle())
		{
			if(!ctx.widgets.widget(1092).component(16).visible())
			{
				ctx.input.send("1");
			}
			
			ctx.widgets.widget(1092).component(16).click();
			Condition.sleep(Random.nextInt(3000, 3500));
		}

	}
	
	private void hopWorld()
	{
		//Components
		Component worldButton = ctx.widgets.widget(906).component(favNum);
		
		Condition.sleep(Random.nextInt(6000, 7000));
		ctx.input.move(worldButton.centerPoint());
		worldButton.click();
		
		if(favNum == 161)
		{
			favNum = 143;
		}
		else
		{
			favNum += 9;
		}
	}
	
	private boolean checkFull()
	{
		ctx.backpack.select();
		
		int backpackNum = ctx.backpack.count();
		
		if(!ctx.backpack.id(lawID).poll().valid() || backpackNum == 28)
			return true;
		
		return false;
	}
	
	private void checkFighting()
	{
		if(ctx.players.local().inCombat())
		{
			while(ctx.players.local().tile().y() >= 3471)
			{
				ctx.input.send("3");
				Condition.sleep(Random.nextInt(1000, 1500));
			}
			
			forceBank = true;
		}
	}
	
	
	@Override
	public void poll() {
		
		//Safe
		checkFighting();
		
		//Keep Going
		logInClass.checkLogOut();;

		final STATE state = state();

		System.out.println(state);
		
		switch (state) { 
		case LOBBY:
			hopWorld();
			break;
		case ADJUSTING:
			moveCamera();
			break;
		case REPOSITIONING:
		    move();
		    break;
		case TRAVELING:
			travel();
			break;
		case BANKING:
			bank();
			break;
		case TELEPORTING:
			teleport();
			break;
		case IDLE:
			move();
			break;
		case GRABBING:
			grabClass.grab();
			break;
		default:
			break;

		}
	}
	
	@Override
	public void repaint(Graphics g) {
		
		//Draw The Mouse
		try
		{
			draw.drawMouse(g, state().toString(), grabClass.getWinesGrabbed());
		}
		catch( Exception e)
		{
			
		}
		
	}


}