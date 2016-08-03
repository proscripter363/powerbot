package Colaborate;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

public class LogIn extends ClientAccessor {
	
	private final Component annoyingMSG = ctx.widgets.widget(906).component(605);
	private final Component logOutScreen = ctx.widgets.widget(596).component(70);
	private final Component annoyingMSG2 = ctx.widgets.widget(596).component(148);
	private final Component randomCase = ctx.widgets.widget(1420).component(373);
	private final Component confirm = ctx.widgets.widget(1420).component(487);
	private final Component passInput = ctx.widgets.widget(596).component(50);
	private final Component logInButton =ctx.widgets.widget(596).component(70);
	private Gui gui;
	private String pass = "";
	
    public LogIn(ClientContext ctx) 
    {
		super(ctx);
		gui = new Gui();
	}
    
    
    public void checkLogOut()
    {
    	if( !gui.getPass().equals("") && pass.equals("") )
    	{
    		pass = gui.getPass();
    	}
    	else
    	{
    		return;
    	}
    		
		
		if(randomCase.visible())
		{
			randomCase.click();
			Condition.sleep(Random.nextInt(800, 1500));
			confirm.click();
			Condition.sleep(Random.nextInt(800, 1500));
		}
		
		if(logOutScreen.visible())
		{
			passInput.click();
			clearBox();
			ctx.input.send(pass);
			logInButton.click();
		}
		
		if(annoyingMSG.visible())
		{
		   annoyingMSG.click();
		   Condition.sleep(Random.nextInt(180000, 200000));
		}
		
		if(annoyingMSG2.visible())
		{
		   annoyingMSG2.click();
		   Condition.sleep(Random.nextInt(180000, 200000));
		   logInButton.click();
		}   	
    }
    
	private void clearBox()
	{
		for(int i = 0; i < 20; i++)
		{
			ctx.input.send("{VK_BACK_SPACE down}");
			Condition.sleep(50);
			ctx.input.send("{VK_BACK_SPACE up}");
		}
	}
}
