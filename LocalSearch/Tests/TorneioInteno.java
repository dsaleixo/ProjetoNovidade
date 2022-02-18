package Tests;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;

import AIs.Interpreter;
import CFG.Control;
import LS_CFG.FactoryLS;
import MentalSeal.MentalSeal;
import Standard.StrategyTactics;
import ai.RandomBiasedAI;
import ai.Rojo;
import ai.UMSBot;
import ai.mayari;
import ai.UTS_Imass_2019.UTS_Imass;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.POWorkerRush;
import ai.asymmetric.PGS.LightPGSSCriptChoice;
import ai.asymmetric.SSS.LightSSSmRTSScriptChoice;
import ai.coac.CoacAI;
import ai.competition.GRojoA3N.GuidedRojoA3N;
import ai.competition.dropletGNS.Droplet;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.core.AI;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.puppet.PuppetSearchMCTS;
import gui.PhysicalGameStatePanel;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;
import util.Pair;

public class TorneioInteno {

	static int max=6000;
	static UnitTypeTable utt ;
	static List<Pair<String,String>> camps3;
	
	static AI getAI(int ia,int lado) throws Exception {
		if(ia<5) {
			if(lado==0)return new Interpreter(utt, Control.load(camps3.get(ia).m_a,new FactoryLS()));
			if(lado==1)return new Interpreter(utt, Control.load(camps3.get(ia).m_a,new FactoryLS()));
		}
		if(ia==5) return new RandomBiasedAI();
		if(ia==6)return new NaiveMCTS(100, -1, 100,10,0.3f, 1.0f, 0.0f, 1.0f, 0.4f, 1.0f, new RandomBiasedAI(), new SimpleSqrtEvaluationFunction3(),false);
		if(ia==7) return new POWorkerRush(utt);
		if(ia==8) return new RandomBiasedAI();//GuidedRojoA3N(utt);
		if(ia==9) return new POLightRush(utt);
		if(ia==10) return new Rojo(utt);
		if(ia==11) return new UMSBot(utt);
		if(ia==12) return new CoacAI(utt);
		if(ia==13) return new mayari(utt);
		if(ia==14)return new Droplet(utt);
		if(ia==15)return new PuppetSearchMCTS(utt);
		if(ia==16)return new StrategyTactics(utt);
		if(ia==17)return new LightPGSSCriptChoice(utt, decodeScripts(utt, "0;1;2;3;"), 100, "PGSR_LIGHT");
		if(ia==18)return new LightSSSmRTSScriptChoice(utt, decodeScripts(utt, "0;1;2;3;"), 100, "SSSR_LIGHT");
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
	    	
	            pa1 = ai1.getAction(player, gs2);
	      
	    	 PlayerAction pa2 = ai2.getAction(1-player, gs2);
	         gs2.issueSafe(pa1);
	            gs2.issueSafe(pa2);
	    	
	    	
	         
	        
	            if(exibe) {
	            	w.repaint();
	            	Thread.sleep(5);
	            }
	            
	            gameover = gs2.cycle();
	           
	            
	
	    } while (!gameover && (gs2.getTime() < max_cycle));
		
	    if (gs2.winner()==player)return 1.0;
	    if (gs2.winner()==-1)return 0.5;
	    return 0;
	
	}
	
	public static String getMap(String s) {
		if(s.equals("0")) return "./maps/16x16/TwoBasesBarracks16x16.xml";
		if(s.equals("1")) return "maps/24x24/basesWorkers24x24A.xml";
		if(s.equals("2")) return "maps/32x32/basesWorkers32x32A.xml";
	    if(s.equals("3")) { max =15000;return "maps/BroodWar/(4)BloodBath.scmB.xml";}
	   return null;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String map = args[0];
		 utt = new UnitTypeTable();
		camps3 =ler(map);
		
		String path_map = getMap(map);
		PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
		GameState gs2 = new GameState(pgs, utt);
		
		
		List<Pair<String,String>> camps= ler(map);
		for(int i=0;i<5;i++) {
			double resp= Avalia(gs2,utt,map,i,camps);
			System.out.println("Rest"+"\t"+i+"\t"+resp);
		}
	}

	private static double Avalia(GameState gs2, UnitTypeTable utt, String map, int i, List<Pair<String, String>> camps) throws Exception {
		// TODO Auto-generated method stub
		Pair<String,String> j = camps.get(i);
		double pont=0;
		long tini0 = System.currentTimeMillis();
		for(int k=0;k<19;k++) {
			 AI adv = getAI(k,1);
			 double a=Jogar(gs2,utt,j.m_a,adv,0);
			 adv = getAI(k,0);
			 double b=Jogar(gs2,utt,j.m_b,adv,1);
			 long paraou0 = System.currentTimeMillis()-tini0;
			 System.out.println("\t"+k+"\t"+a+"\t"+b+"\t"+(paraou0*1.0)/1000.0);
			 pont+=a+b;
		}
		
		return pont;
	}

	private static double Jogar(GameState gs2, UnitTypeTable utt, String m_a, AI j1, int lado) throws Exception {
		// TODO Auto-generated method stub
		AI j0 = new Interpreter(utt, Control.load(m_a,new FactoryLS()));
		double cont=0;
		for(int i=0;i<5;i++) {
			cont+=partida(gs2,utt,lado,max,j0,j1,false);
		}
		return cont;
	}

	private static List<Pair<String, String>> ler(String map) throws FileNotFoundException {
		// TODO Auto-generated method stub
		List<Pair<String, String>> list = new ArrayList<>();
		
			String entrada = "out"+map+".txt";
			Scanner in = new Scanner(new FileReader("Final/"+entrada));

			while (in.hasNextLine()) {
			    String line = in.nextLine();
			    String dados[] = line.split("\t");
			    if(dados[0].equals("Camp")) {
			    	list.add(new Pair<>(dados[1],dados[2]));
			    }
			} 
			
		
		return list;
	}
	
	
	public static List<AI> decodeScripts(UnitTypeTable utt, String sScripts) {

		//decompõe a tupla
		ArrayList<Integer> iScriptsAi1 = new ArrayList<>();
		String[] itens = sScripts.split(";");

		for (String element : itens) {
			iScriptsAi1.add(Integer.decode(element));
		}

		List<AI> scriptsAI = new ArrayList<>();

		ScriptsCreator sc = new ScriptsCreator(utt, 300);
		ArrayList<BasicExpandedConfigurableScript> scriptsCompleteSet = sc.getScriptsMixReducedSet();

		iScriptsAi1.forEach((idSc) -> {
			scriptsAI.add(scriptsCompleteSet.get(idSc));
		});

		return scriptsAI;
	}
	
}


