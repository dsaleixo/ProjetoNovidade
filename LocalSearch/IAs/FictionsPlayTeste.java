package IAs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import AIs.Interpreter;
import CFG.Control;
import CFG.Factory;
import CFG.Node;
import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.Playout;
import EvaluateGameState.PlayoutSimplesSelf;
import LS_CFG.Empty_LS;
import LS_CFG.FactoryLS;
import LS_CFG.Node_LS;
import LS_CFG.S_LS;
import ai.coac.CoacAI;
import ai.core.AI;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.UnitTypeTable;
import util.Pair;

public class FictionsPlayTeste implements Search {

	int max;
	Playout playout;
	AI adv;
	Factory f = new FactoryLS();
	GameState gs2;
	
	public FictionsPlayTeste(GameState gs, int max_cicle) {
		// TODO Auto-generated constructor stub
		
		UnitTypeTable utt = new UnitTypeTable();
		gs2 = gs.cloneChangingUTT(utt);
		max = max_cicle;
		adv = new CoacAI(utt);
		playout = new PlayoutSimplesSelf();
		f = new FactoryLS();
	}


	
	
	 double Avalia(GameState gs, int max_cicle,int lado,Node_LS n0,Node_LS n1) throws Exception{
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n0);
		AI ai1 = new Interpreter(utt,n1);
		Pair<Double,CabocoDagua2> r = playout.run(gs, lado, max_cicle, ai, ai1, false);
	

		return r.m_a;
		
	}
	
	 double run(GameState gs, int max_cicle,int lado,Node_LS n0,List<Node_LS> n1) throws Exception{
		
		double r =0;
		for(int i= 0;i<n1.size();i++) {
			r+=Avalia(gs,max_cicle,lado,n0,n1.get(i));
		}
		return r;
		
	}
	
	
	 double AvaliaCoac(GameState gs, int max_cicle,int lado,Node_LS n0,AI ai1) throws Exception{
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n0);
		Pair<Double,CabocoDagua2> r = playout.run(gs, lado, max_cicle, ai, ai1, false);
		return r.m_a;
		
	}
	
	
	 void add(Node_LS j, List<Node_LS> lado, int n) {
		// TODO Auto-generated method stub
		if(lado.size()>=n)lado.remove(0);
		lado.add((Node_LS) j);
	}
	
	
	 

	@Override
	public Node run(GameState gs, int max_cicle, int lado) throws Exception {
		// TODO Auto-generated method stub
		Pair<Node_LS,Node_LS> best = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		System.out.println("Ficticious");
		double c0=-1;
		double c1 =-1;
		playout = new PlayoutSimplesSelf();
		Factory f = new FactoryLS();
		Random r =new Random();
		
		
		
		int tam_n=5;
		List<Node_LS> lado0 = new ArrayList<>();
		List<Node_LS> lado1 = new ArrayList<>();
		lado0.add(new S_LS(new Empty_LS()));
		lado1.add(new S_LS(new Empty_LS()));
		
		double v0=0.5;
		double v1=0.5;
		long tempo_ini = System.currentTimeMillis();
		while(true) {
			
			Node_LS j0 = (Node_LS) lado0.get(lado0.size()-1).Clone(f);
			int n0 = r.nextInt(j0.countNode());
			int custo = r.nextInt(7)+3;
			j0.mutation(n0, custo);
			
			Node_LS j1 = (Node_LS) lado1.get(lado1.size()-1).Clone(f);
			int n1 = r.nextInt(j1.countNode());
			custo = r.nextInt(7)+3;
			j1.mutation(n1, custo);
			
			boolean b0=false;
			boolean b1=false;
			
			
			double r0 = run(gs2,max,0,j0,lado1);
			double r1 = run(gs2,max,1,j1,lado0);
		
			double rc0 = AvaliaCoac(gs2,max,0,j0,adv);
			double rc1 = AvaliaCoac(gs2,max,1,j1,adv);
		
			
			if(rc0>c0) {
				c0 = rc0;
				long paraou = System.currentTimeMillis()-tempo_ini;
				double pontuacao = (c0+ c1)/2;
				best.m_a = (Node_LS) j0.Clone(f);
				System.out.println("atual\t"+((paraou*1.0)/1000.0)+"\t"+pontuacao+"\t"+
						Control.salve((Node) best.m_a)+"\t"+Control.salve((Node) best.m_b));
			}
			
			
			if(rc1>c1) {
				c1 = rc1;
				long paraou = System.currentTimeMillis()-tempo_ini;
				double pontuacao = (c0+ c1)/2;
				best.m_b = (Node_LS) j1.Clone(f);
				System.out.println("atual\t"+((paraou*1.0)/1000.0)+"\t"+pontuacao+"\t"+
						Control.salve((Node) best.m_a)+"\t"+Control.salve((Node) best.m_b));
			}
			
			if(r0>v0) {
		
				b0 = true;
				add(j0,lado0,tam_n);
			}
			if(r1>v1) {
				
				b1 = true;
				add(j1,lado1,tam_n);
			}
			
			if(b0||b1) {
				
				 j0 = lado0.get(lado0.size()-1);
				 j1 = lado1.get(lado1.size()-1);
				 
				v0 = run(gs2,max,0,j0,lado1);
				v1 = run(gs2,max,1,j1,lado0);
				long paraou = System.currentTimeMillis()-tempo_ini;
				System.out.println("atualizou\t"+v0+"\t"+v1+"\t"+
						Control.salve((Node) j0)+"\t"+Control.salve((Node) j1));
				System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+
						Control.salve((Node)j0) +"\t"+Control.salve((Node) j1));
			}
			
		}
	}

	

}
