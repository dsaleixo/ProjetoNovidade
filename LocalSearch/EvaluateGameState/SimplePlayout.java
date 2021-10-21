package EvaluateGameState;

import javax.swing.JFrame;

import AIs.Interpreter;
import ai.core.AI;
import gui.PhysicalGameStatePanel;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;
import util.Pair;

public class SimplePlayout implements Playout {
	

	public SimplePlayout() {
		// TODO Auto-generated constructor stub
		
	}

	
	@Override
	public Pair<Double, CabocoDagua2> run(GameState gs, int player, int max_cycle, AI ai1, AI ai2, boolean exibe) throws Exception {
		// TODO Auto-generated method stub
		CabocoDagua2 eval = new CabocoDagua2();;
		UnitTypeTable utt = new UnitTypeTable();
		ai1.reset(utt);
		ai2.reset(utt);
		GameState gs2 = gs.cloneChangingUTT(utt);
		boolean gameover = false;
		JFrame w=null;
		if(exibe) w = PhysicalGameStatePanel.newVisualizer(gs2,640,640,false,PhysicalGameStatePanel.COLORSCHEME_BLACK);
		boolean itbroke=false ;
		eval.evaluate(gs2, player);
		
        do {
        	PlayerAction pa1=null;
        	try {
                pa1 = ai1.getAction(player, gs2);
                
        	}catch(Exception e) {
        		itbroke=true;
        		break;
        	}
                PlayerAction pa2 = ai2.getAction(1-player, gs2);
                
                
                gs2.issueSafe(pa1);
                gs2.issueSafe(pa2);
             
                if(exibe) {
                	w.repaint();
                	Thread.sleep(20);
                }
                
                gameover = gs2.cycle();
                if(eval instanceof CabocoDagua2) {
                	((CabocoDagua2)eval).evaluate(pa1, player);
                }else {
                	eval.evaluate(gs2, player);
                }
                

        } while (!gameover && (gs2.getTime() <= max_cycle)); 
    
        double r=0;
        if(itbroke) return new Pair<>(-1.0,null);
        else if(gs2.winner()==player)r= 1;
		else if (gs2.winner()==-1)r= 0.5;
       
        return new Pair<>(r,eval);
        
	}

}
