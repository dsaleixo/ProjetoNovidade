package IAs;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import AIs.Interpreter;
import CFG.Control;
import CFG.Factory;
import CFG.Node;
import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.Nov0;
import EvaluateGameState.Novidade;
import EvaluateGameState.Playout;
import EvaluateGameState.PlayoutSimplesSelf;
import EvaluateGameState.SimplePlayout;
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

public class SelfPlayTeste implements Search {
	int max;
	Playout playout;
	AI adv;
	GameState gs2;
	
	Pair<Node_LS,Node_LS> best;
	Pair<Double,Double> v_best;
	Pair<Node_LS,Node_LS> alvo;
	double v_alvo;
	Random r;
	Factory f;
	long tempo_ini;
	
	
	public SelfPlayTeste(GameState gs, int max_cicle) {
		// TODO Auto-generated constructor stub
		UnitTypeTable utt = new UnitTypeTable();
		gs2 = gs.cloneChangingUTT(utt);
		max = max_cicle;
		adv = new CoacAI(utt);
		playout = new PlayoutSimplesSelf();
		
		v_best = new Pair<>(-1.0,-1.0);
		best = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		alvo = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		
		v_alvo =0.5;
		r =new Random();
		f = new FactoryLS();
	}

	public void  AvaliaCoac( Node_LS n0, int lado) throws Exception {
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n0);
		Pair<Double,CabocoDagua2> r = playout.run(gs2, lado, max, ai, adv, false);
		double res = r.m_a;
		if(lado==0) {
			if(this.v_best.m_a<res) {
				this.v_best.m_a=res;
				this.best.m_a=(Node_LS) n0.Clone(f);
				long paraou = System.currentTimeMillis()-tempo_ini;
				double pontuacao = (v_best.m_a+ v_best.m_b)/2;
				System.out.println("atual\t"+((paraou*1.0)/1000.0)+"\t"+pontuacao+"\t"+
						Control.salve((Node) best.m_a)+"\t"+Control.salve((Node) best.m_b));
			}
		}else if(lado ==1 ) {
			if(this.v_best.m_b<res) {
				this.v_best.m_b=res;
				this.best.m_b=(Node_LS) n0.Clone(f);
				long paraou = System.currentTimeMillis()-tempo_ini;
				double pontuacao = (v_best.m_a+ v_best.m_b)/2;
				System.out.println("atual\t"+((paraou*1.0)/1000.0)+"\t"+pontuacao+"\t"+
						Control.salve((Node) best.m_a)+"\t"+Control.salve((Node) best.m_b));
			}
		}
		
	}
	
	double AvaliaSS(int lado,Node_LS n0,Node_LS n1) throws Exception{
			UnitTypeTable utt = new UnitTypeTable();
			AI ai = new Interpreter(utt,n0);
			AI ai1 = new Interpreter(utt,n1);
			Pair<Double,CabocoDagua2> r = playout.run(gs2, lado, max, ai, ai1, false);
		
			
			
			return r.m_a;
			
		}
	
	@Override
	public Node run(GameState gs, int max_cicle, int lado) throws Exception {
		// TODO Auto-generated method stub
		this.tempo_ini = System.currentTimeMillis();
		Pair<Node_LS,Node_LS> local = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		
	
		while(true) {
			Node_LS aux0 = (Node_LS) local.m_a.Clone(f);
			int n0 = r.nextInt(aux0.countNode());
			int custo = r.nextInt(7)+3;
			aux0.mutation(n0, custo);
			
			
			Node_LS aux1 = (Node_LS) local.m_b.Clone(f);
			int n1 = r.nextInt(aux1.countNode());
			custo = r.nextInt(7)+3;
			aux1.mutation(n1, custo);
			double r0= this.v_alvo;
			double r1= 1-this.v_alvo;
			
			AvaliaCoac(aux0,0);
			AvaliaCoac(aux1,1);
			
			
			boolean b0=false;
			boolean a0=false;
			boolean b1=false;
			boolean a1=false;
			
			if(r0<=0.9) {
				double rr = AvaliaSS(0,aux0,alvo.m_b);
				if(rr>r0) {
					a0=true;
					b0=true;
				}
			}
				
			if(r1<=0.9) {
				Double rr  = AvaliaSS(1,aux1,alvo.m_a);
				if(rr>r1) {
					a1=true;
					b1=true;
				}
			}
			
			
			if(b0)local.m_a= (Node_LS) aux0.Clone(f);
			if(b1)local.m_b= (Node_LS) aux1.Clone(f);
			if(a0)alvo.m_a= (Node_LS) aux0.Clone(f);
			if(a1)alvo.m_b= (Node_LS) aux1.Clone(f);
			
			
			if(a1||a0) {
				double rr  = AvaliaSS(0,alvo.m_a,alvo.m_b);
				this.v_alvo = rr;
				long paraou = System.currentTimeMillis()-tempo_ini;
				System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+
						Control.salve((Node) alvo.m_a)+"\t"+Control.salve((Node) alvo.m_b));
			}
			
		}
		
	

	}

}
