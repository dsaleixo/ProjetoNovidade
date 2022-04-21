package Tests;

import java.util.Random;

import CS.CS;
import DO.DO;
import EvaluateGameState.FabricaNov0;
import FCS.FCS;
import IAs2.Algoritmo1;
import IAs2.AvaliaCoac;
import IAs2.Avaliador;
import IAs2.AvaliadorPadrao;
import IAs2.B2N;
import IAs2.B2N2;
import IAs2.CCBasica;
import IAs2.CCBasicaMutacao;
import IAs2.HC;
import IAs2.SA;

import IAs2.Search2;
import TwoLevelSearch.Aleatorio;
import TwoLevelSearch.BehavioralCloning;
import TwoLevelSearch.TwoLevelsearch;
import UFC.UFC;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.UnitTypeTable;

public class Teste2 {
	static int max=6000;
	public Teste2() {
		// TODO Auto-generated constructor stub
		
	}

	public static String getMap(String s) {
		if(s.equals("0")) return "maps/8x8/basesWorkers8x8A.xml";
		if(s.equals("1")) return "maps/NoWhereToRun9x8.xml";
		if(s.equals("2")) return "maps/16x16/basesWorkers16x16A.xml";
		if(s.equals("3")) return "maps/24x24/basesWorkers24x24A.xml";
		if(s.equals("4")) return "maps/DoubleGame24x24.xml";
		if(s.equals("5")) return "maps/32x32/basesWorkers32x32A.xml";
	    if(s.equals("6")) { max =15000;;return "maps/BroodWar/(4)BloodBath.scmB.xml";}
	   return null;
	}
    
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		UnitTypeTable utt = new UnitTypeTable();
		String path_map = getMap(args[0]);
		PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
		GameState gs2 = new GameState(pgs, utt);
		

		System.out.println(path_map+" novo mapas teste budget"
				+ "");
		AvaliaCoac coac = new AvaliaCoac();
		Algoritmo1 se =null;
		/*
		if(args[1].equals("0"))se= new Algoritmo1(new SA(coac,1500,1000,0.9,15),1);
		if(args[1].equals("1"))se= new Algoritmo1(new SA(coac,1500,1000,0.9,15),3);
		if(args[1].equals("2"))se= new Algoritmo1(new SA(coac,1500,1000,0.9,15),1000);
		if(args[1].equals("3"))se= new Algoritmo1(new SA2(coac,1500,1000,0.9,15),1);
		if(args[1].equals("4"))se= new Algoritmo1(new SA2(coac,1500,1000,0.9,15),3);
		if(args[1].equals("5"))se= new Algoritmo1(new SA2(coac,1500,1000,0.9,15),1000);
		if(args[1].equals("6"))se= new Algoritmo1(new HC(coac,1500),1);
		if(args[1].equals("7"))se= new Algoritmo1(new HC(coac,1500),3);
		if(args[1].equals("8"))se= new Algoritmo1(new HC(coac,1500),1000);
		*/
		if(args[1].equals("0"))se= new Algoritmo1(new SA(coac,2000,2000,0.9,0.5),new AvaliadorPadrao(1));
		if(args[1].equals("1"))se= new Algoritmo1(new SA(coac,2000,2000,0.9,0.5),new AvaliadorPadrao(1000));
		if(args[1].equals("2")) se= new Algoritmo1(new SA(coac,2000,2000,0.9,0.5),new UFC());
		if(args[1].equals("3")) se= new Algoritmo1(new SA(coac,2000,2000,0.9,0.5),new CS());	
		if(args[1].equals("4")) se= new Algoritmo1(new SA(coac,2000,2000,0.9,0.5),new DO());
		if(args[1].equals("5")) se= new Algoritmo1(new SA(coac,2000,2000,0.9,0.5),new CS());	
		
		
		
		se.run(gs2, max);
	}

}
