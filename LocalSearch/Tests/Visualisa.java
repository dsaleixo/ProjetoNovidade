package Tests;

import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

import AIs.Interpreter;
import CFG.Control;
import CFG.Node;
import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.Playout;
import EvaluateGameState.SimplePlayout;
import LS_CFG.FactoryLS;
import ai.abstraction.HeavyRush;
import ai.coac.CoacAI;
import ai.core.AI;
import ai.synthesis.dslForScriptGenerator.DslAI;
import ai.synthesis.dslForScriptGenerator.DSLCommandInterfaces.ICommand;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.IDSLCompiler;
import ai.synthesis.dslForScriptGenerator.DSLCompiler.MainDSLCompiler;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import gui.PhysicalGameStatePanel;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;
import util.Pair;

public class Visualisa {

	public Visualisa() {
		// TODO Auto-generated constructor stub
	}

public static String getMap(String s) {
		
		if(s.equals("0")) return "maps/8x8/basesWorkers8x8A.xml";
		if(s.equals("1")) return "maps/16x16/basesWorkers16x16A.xml";				
		if(s.equals("2")) return "maps/BWDistantResources32x32.xml";
		if(s.equals("3")) return "maps/BroodWar/(4)BloodBath.scmB.xml";
		if(s.equals("4")) return "maps/8x8/FourBasesWorkers8x8.xml";
		if(s.equals("5")) return "maps/16x16/TwoBasesBarracks16x16.xml";
		if(s.equals("6")) return "maps/NoWhereToRun9x8.xml";
		if(s.equals("7")) return "maps/DoubleGame24x24.xml";
		if(s.equals("8")) return "maps/24x24/basesWorkers24x24A.xml";
		if(s.equals("9")) return "maps/32x32/basesWorkers32x32A.xml";
		return null;
	}
	
public static double partida(GameState gs,UnitTypeTable utt, int player, int max_cycle, AI ai1, AI ai2, boolean exibe) throws Exception {
		
		
		
		ai1.reset(utt);
		ai2.reset(utt);
		GameState gs2 = gs.cloneChangingUTT(utt);
		boolean gameover = false;
		JFrame w=null;
		if(exibe) w = PhysicalGameStatePanel.newVisualizer(gs2,640,640,false,PhysicalGameStatePanel.COLORSCHEME_BLACK);
		boolean itbroke=false ;
		
        do {
        	PlayerAction pa1=null;
        	try {
                pa1 = ai1.getAction(player, gs2);
                
        	}catch(Exception e) {
        		itbroke=true;
        		
        		System.out.println("erro");
        		break;
        	}
        	 
                PlayerAction pa2 = ai2.getAction(1-player, gs2);
                
               
               gs2.issueSafe(pa1);
               gs2.issueSafe(pa2);
            
                if(exibe) {
                	w.repaint();
                	Thread.sleep(1);
                }
                
                gameover = gs2.cycle();
               
                

        } while (!gameover && (gs2.getTime() < max_cycle));
		
        if (gs2.winner()==player)return 1.0;
        if (gs2.winner()==-1)return 0.5;
        return 0;
    
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		UnitTypeTable utt = new UnitTypeTable();
		String path_map =getMap("8");;
		PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
		GameState gs2 = new GameState(pgs, utt);
		//build(Barrack,1,Up) harvest(2) train(Worker,4,Right) for(u) (build(Base,1,Left,u) train(Heavy,3,Down) train(Worker,6,Down) train(Ranged,8,Right) moveaway(Heavy,u) attack(Light,weakest,u) train(Worker,6,Right)) for(u) (if(HaveQtdEnemiesbyType(Worker,3)) then(moveaway(Ranged,u) attack(Worker,strongest,u)) if(!HaveQtdEnemiesbyType(Worker,1)) then(harvest(2,u)) else(train(Ranged,3,Down) moveaway(Worker,u) attack(Light,lessHealthy,u)) harvest(3,u) moveaway(Heavy,u)) if(!HaveUnitsStrongest(Ranged)) then(moveToUnit(Heavy,Enemy,farthest) train(Heavy,7,Left))
	
	
		//BuilderDSLTreeSingleton.fullPreOrderPrint((iNodeDSLTree) A);
		
		String s2 = "S;S_S;S;For_S;S;S_S;S;S_S;S;S_S;S;S_S;S;C;Train;Worker;Up;7;S;S_S;S;C;Build;Barracks;EnemyDir;3;S;C;Train;Ranged;Left;10;S;C;Idle;S;C;Harvest;7;S;C;Attack;MostHealthy;S;Empty";
		Node n = Control.load(s2,new FactoryLS());
		//Node n = ScriptsFactory.monta4();
		//System.out.print(A.);
		//AI ai1 = new Interpreter(utt,n);
		AI ai1 = new CoacAI(utt);
		AI ai2 = new CoacAI(utt);
		Playout playout = new SimplePlayout();
		for(int i =0;i<10;i++) {
			
		Pair<Double,CabocoDagua2> r = playout.run(gs2, 0, 6000, ai1, ai2, true);
	r.m_b.imprimir();
	}
		//n.clear(null, new FactoryLS());
		//System.out.println(n.translateIndentation(0));
	}
	  private static AI buildCommandsIA(UnitTypeTable utt, iDSL code) {
	        IDSLCompiler compiler = new MainDSLCompiler();
	        HashMap<Long, String> counterByFunction = new HashMap<Long, String>();
	        List<ICommand> commandsDSL = compiler.CompilerCode(code, utt);
	        AI aiscript = new DslAI(utt, commandsDSL, "P1", code, counterByFunction);
	        return aiscript;
	    }
}