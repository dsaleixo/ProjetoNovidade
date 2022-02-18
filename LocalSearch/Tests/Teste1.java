package Tests;

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
import IAs2.AvaliadorPadrao;
import IAs2.B2N;
import IAs2.B2N2;
import IAs2.CCBasica;
import IAs2.CCBasicaMutacao;
import IAs2.SA;

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
		

		System.out.println(path_map+" novo teste6");
		
		
		
		System.out.println(path_map+" novo teste bonito2");
		AvaliaCoac coac = new AvaliaCoac();
		Algoritmo1 se =null;
		FabicaDeNovidade fn = (FabicaDeNovidade) new FabricaNov0();
		if(args[1].equals("0"))se= new Algoritmo1(new SA(coac,3500,1000,0.9,15),new AvaliadorPadrao(1));
		if(args[1].equals("1"))se= new Algoritmo1(new SA(coac,3500,1000,0.9,15),new AvaliadorPadrao(1000));
		if(args[1].equals("2"))se= new Algoritmo1(new CCBasica(coac,1500,1000,0.9,15, fn),new AvaliadorPadrao(1));
		if(args[1].equals("3"))se= new Algoritmo1(new CCBasica(coac,1500,1000,0.9,15,fn),new AvaliadorPadrao(1000));
		if(args[1].equals("4"))se= new Algoritmo1(new CCBasicaMutacao(coac,1500,1000,0.9,15,fn),new AvaliadorPadrao(1));
		if(args[1].equals("5"))se= new Algoritmo1(new CCBasicaMutacao(coac,1500,1000,0.9,15,fn),new AvaliadorPadrao(1000));
		if(args[1].equals("6"))se= new Algoritmo1(new B2N(coac,1500,1000,0.9,15,fn),new AvaliadorPadrao(1));
		if(args[1].equals("7"))se= new Algoritmo1(new B2N(coac,1500,1000,0.9,15,fn),new AvaliadorPadrao(1000));
		if(args[1].equals("8"))se= new Algoritmo1(new B2N2(coac,1500,1000,0.9,15,fn),new AvaliadorPadrao(1));
		if(args[1].equals("9"))se= new Algoritmo1(new B2N2(coac,1500,1000,0.9,15,fn),new AvaliadorPadrao(1000));
		
		se.run(gs2, max);
		
		
	}

}
