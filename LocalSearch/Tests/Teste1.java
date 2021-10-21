package Tests;

import EvaluateGameState.Playout;
import EvaluateGameState.SimplePlayout;
import IAs.Busca2Nivel;
import IAs.FictionsPlayTeste;
import IAs.SABasic;
import IAs.Search;
import IAs.SelfPlayNovidade;
import IAs.SelfPlayTeste;
import ai.coac.CoacAI;
import ai.core.AI;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.UnitTypeTable;

public class Teste1 {
	static int max=6000;
	public Teste1() {
		// TODO Auto-generated constructor stub
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
		UnitTypeTable utt = new UnitTypeTable();
		String path_map = getMap(args[0]);
		PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
		GameState gs2 = new GameState(pgs, utt);
		
		int test = 64000000*20;
		System.out.println(path_map+" "+test);
		
		
		
		Search search =null;
		
		if(args[1].equals("0"))search = new SelfPlayTeste(gs2,max);
		if(args[1].equals("1"))search = new SelfPlayNovidade(gs2,max);
		if(args[1].equals("2"))search = new Busca2Nivel(gs2,max);
		
		
		 search.run(gs2, max,0);
		
		
	}

}
