package Tests;

import java.util.Random;

import CFG.Factory;
import EvaluateGameState.FabicaDeNovidade;
import EvaluateGameState.FabricaNov0;
import EvaluateGameState.Playout;
import EvaluateGameState.SimplePlayout;
import IAs.Busca2Nivel;
import IAs.CorrendoAtrasRabo;
import IAs.CorrendoAtrasRabo2;
import IAs.FictionsPlayTeste;
import IAs.SABasic;
import IAs.Search;
import IAs.SelfPlayNovidade;
import IAs.SelfPlayTeste;
import IAs2.Algoritmo1;
import IAs2.AvaliaCoac;
import IAs2.Avaliador;
import IAs2.AvaliadorPadrao;
import IAs2.B2N;
import IAs2.B2N2;
import IAs2.CCBasica;
import IAs2.CCBasicaMutacao;
import IAs2.SA;
import TwoLevelSearch.Aleatorio;
import TwoLevelSearch.BehavioralCloning;
import TwoLevelSearch.Level1;
import TwoLevelSearch.Level2;
import TwoLevelSearch.NaiveSampling;
import TwoLevelSearch.TwoLevelsearch;
import ai.coac.CoacAI;
import ai.core.AI;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.UnitTypeTable;

public class Teste1 {
	static int max=6000;
	static  int tempo=2000;
	static int troca =24;
	public Teste1() {
		// TODO Auto-generated constructor stub
	}

	public static String getMap(String s) {
		if(s.equals("0")) return "./maps/16x16/TwoBasesBarracks16x16.xml";
		if(s.equals("1")) return "maps/24x24/basesWorkers24x24A.xml";
		if(s.equals("2")) return "maps/32x32/basesWorkers32x32A.xml";
	    if(s.equals("3")) {troca=24;tempo=5000; max =15000;;return "maps/BroodWar/(4)BloodBath.scmB.xml";}
	   return null;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		UnitTypeTable utt = new UnitTypeTable();
		String path_map = getMap(args[0]);
		PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
		GameState gs2 = new GameState(pgs, utt);
		
		

		System.out.println(path_map+" novo teste indo pro fim,final msm,sem erros, sem apredizagem");
		
		
		
		AvaliaCoac coac = new AvaliaCoac();
		TwoLevelsearch se =null;
		
		
		if(args[1].equals("0")) {
			Algoritmo1 se2 = new Algoritmo1(new SA(coac,tempo,2000,0.9,15), new AvaliadorPadrao(1,null));
			se2.run(gs2, max);
		}
				
		if(args[1].equals("1")) {
			Algoritmo1 se2 = new Algoritmo1(new SA(coac,tempo,2000,0.9,15), new AvaliadorPadrao(1000,null));
			se2.run(gs2, max);
		}
		
		if(args[1].equals("2")) {
			Level1 l1= new NaiveSampling(0.3,0.3,0.6,troca,"r1/out_1"+"_"+args[1]+"_"+args[2].charAt(1)+".txt");
			Level2 l2 = new BehavioralCloning(coac,tempo,2000,0.9,0.5);
			Avaliador ava = new AvaliadorPadrao(1,l1.getFN());	
			se = new TwoLevelsearch(l1,l2,ava);
			se.run(gs2, max);
		}
				
		if(args[1].equals("3")) {
			Level1 l1= new NaiveSampling(0.3,0.3,0.6,troca,"r1/out_1"+"_"+args[1]+"_"+args[2].charAt(1)+".txt");
			Level2 l2 = new BehavioralCloning(coac,tempo,2000,0.9,0.5);
			Avaliador ava = new AvaliadorPadrao(1000,l1.getFN());	
			se = new TwoLevelsearch(l1,l2,ava);
			se.run(gs2, max);
		}
		
		
		
		
	}

}
