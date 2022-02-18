package IAs2;

import java.util.ArrayList;
import java.util.List;

import AIs.Interpreter;
import CFG.Control;
import CFG.Factory;
import CFG.Node;
import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.FabricaNov0;
import EvaluateGameState.FabricaNov1;
import EvaluateGameState.Novidade;
import EvaluateGameState.SimplePlayout;
import LS_CFG.Empty_LS;
import LS_CFG.FactoryLS;
import LS_CFG.Node_LS;
import LS_CFG.S_LS;
import ai.core.AI;
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;

public class AvaliadorPadrao implements Avaliador {
	int budget;
	List<Node_LS> js;
	int n;
	SimplePlayout playout;
	Factory f;
	long tempo_ini=System.currentTimeMillis();
	double best;
	FabricaNov0 fn = new FabricaNov0();
	public AvaliadorPadrao(int n) {
		// TODO Auto-generated constructor stub
		System.out.println("N = "+n);
		this.playout = new SimplePlayout();
		this.f = new FactoryLS();
		this.n = n;
		this.js = new ArrayList();
		this.best =0.5;
		this.js.add(new S_LS(new Empty_LS()));
		System.out.println("Camp\t0.0"+"\t"+0+"\t"+
				Control.salve((Node)js.get(0)) );
		this.budget=0;
		
	}

	@Override
	public Pair<Double,Double> Avalia(GameState gs, int max, Node_LS j, Pair<Novidade,Novidade> oraculo) throws Exception {
		// TODO Auto-generated method stub
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,j);
		Pair<Double,Double> r = new Pair(0.0,0.0);
		Pair<Novidade,Novidade> nov=new Pair(fn.gerar(),fn.gerar());;
		for(Node_LS adv :this.js) {
			AI ai2 = new Interpreter(utt,adv);
			Pair<Double,CabocoDagua2> aux0 = playout.run(gs, 0, max, ai, ai2, false);
			Pair<Double,CabocoDagua2> aux1 = playout.run(gs, 1, max, ai, ai2, false);
			
			r.m_a+=aux0.m_a + aux1.m_a;
			nov.m_a = fn.gerar(aux0.m_b);
			nov.m_b = fn.gerar(aux1.m_b);
			
		
		}
		
		double copia = oraculo.m_a.semelhaca(nov.m_a)+oraculo.m_a.semelhaca(nov.m_b);
		
		return new Pair(r.m_a/2,copia/2);
	}

	
	@Override
	public double Avalia(GameState gs, int max, Node_LS j) throws Exception {
		// TODO Auto-generated method stub
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,j);
		double r=0;
	
		for(Node_LS adv :this.js) {
			AI ai2 = new Interpreter(utt,adv);
			double r0= playout.run(gs, 0, max, ai, ai2, false).m_a;
			double r1= playout.run(gs, 1, max, ai, ai2, false).m_a;
			if(r0+r1>=0) {
				this.budget+=1;
			}
			r+=r0+r1;
			long paraou = System.currentTimeMillis()-this.tempo_ini;
			if(this.budget%1000==0) {
				System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+this.budget+"\t"+
						Control.salve((Node)this.getBest()) );
			}
			
			
		
		}
		
		return r/2;
	}
	
	@Override
	public void update(GameState gs, int max, Node_LS j) throws Exception {
		// TODO Auto-generated method stub
		long paraou = System.currentTimeMillis()-this.tempo_ini;
		
		Node_LS camp= (Node_LS) j.Clone(f);
		double r =Avalia(gs,max,camp);
		System.out.println("resultado "+((paraou*1.0)/1000.0)+"     "+r+ ">" + this.best+" individuos = "+js.size() );
		if(r> this.best) {
			if(js.size()>=this.n) js.remove(0);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" );
			System.out.println("atulizou "+r+ ">" + this.best+" individuos = "+js.size() );
			
			camp.clear(null, f);
			js.add((Node_LS) camp);
			
			this.best= Avalia(gs,max,camp);
			
		}
		System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+this.budget+"\t"+
				Control.salve((Node)js.get(js.size()-1)) );
	}

	@Override
	public Node_LS getIndividuo() {
		// TODO Auto-generated method stub
		return (Node_LS) js.get(js.size()-1).Clone(f);
	}

	@Override
	public boolean criterioParada(double d) {
		// TODO Auto-generated method stub
		return d>this.js.size()-0.1;
	}

	@Override
	public Node_LS getBest() {
		// TODO Auto-generated method stub
		return (Node_LS) js.get(js.size()-1).Clone(f);
	}

}
