package IAs;

import java.util.Random;

import AIs.Interpreter;
import CFG.Control;
import CFG.Factory;
import CFG.Node;
import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.FabicaDeNovidade;
import EvaluateGameState.FabricaNov1;
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
import rts.units.UnitTypeTable;
import util.Pair;

public class CorrendoAtrasRabo implements Search {

	int max;
	Playout playout;
	AI adv;
	UnitTypeTable utt;
	GameState gs2;
	FabicaDeNovidade fn;
	Pair<Node_LS,Node_LS> best_coac;
	Pair<Double,Double> v_best_coac;
	Pair<Node_LS,Node_LS> alvo;
	double v_alvo;
	Random r;
	Factory f;
	long tempo_ini;
	
	
	public CorrendoAtrasRabo(GameState gs, int max_cicle) {
		// TODO Auto-generated constructor stub
		System.out.println("CorrendoAtrasRabo");
		this.utt = new UnitTypeTable();
		gs2 = gs.cloneChangingUTT(utt);
		max = max_cicle;
		adv = new CoacAI(utt);
		playout = new SimplePlayout();
		
		v_best_coac = new Pair<>(-1.0,-1.0);
		best_coac = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		alvo = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		
		v_alvo =0.5;
		r =new Random();
		f = new FactoryLS();
		this.fn = new FabricaNov1();
	}

	double AvaliaSS(int lado,Node_LS n0,Node_LS n1, boolean exibe) throws Exception{
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n0);
		AI ai1 = new Interpreter(utt,n1);
		Pair<Double,CabocoDagua2> r = playout.run(gs2, lado, max, ai, ai1, false);
	
		
		
		return r.m_a;
		
	}

	Novidade AvaliaNovidade(int lado,Node_LS n0,Node_LS n1) throws Exception{
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n0);
		AI ai1 = new Interpreter(utt,n1);
		Pair<Double,CabocoDagua2> r = playout.run(gs2, lado, max, ai, ai1, false);
		if(r.m_b!=null) {
			return fn.gerar(r.m_b);

		}
		return fn.gerar();
		
	}
	
	
	public void testa(int lado,Node_LS n) {
		
	}
	
	public void atualiza(Node_LS n) throws Exception {
		double r =AvaliaSS(0,alvo.m_a,alvo.m_b,true);
		
		
		boolean a0=false;
		boolean a1=false;
		
		
		double rr = AvaliaSS(0,n,alvo.m_b,true);
		if(rr>=r) {
			a0=true;
		}
	
		rr  = AvaliaSS(1,n,alvo.m_a,true);
		if(rr>=1-r) {
			a1=true;
		}
		
		if(a0)alvo.m_a= (Node_LS) n.Clone(f);
		if(a1)alvo.m_b= (Node_LS) n.Clone(f);
		long paraou = System.currentTimeMillis()-tempo_ini;
		if(a0||a1) {
			System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+
					Control.salve((Node) alvo.m_a)+"\t"+Control.salve((Node) alvo.m_b));
		}
	}
	
	
	@Override
	public Node run(GameState gs, int max_cicle, int lado) throws Exception {
		// TODO Auto-generated method stub
		this.tempo_ini = System.currentTimeMillis();
		Pair<Node_LS,Node_LS> local = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		
	
		inicializa(gs,max_cicle);
		long paraou = System.currentTimeMillis()-tempo_ini;
		System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+
				Control.salve((Node) alvo.m_a)+"\t"+Control.salve((Node) alvo.m_b));
		
		
		atualiza(alvo.m_a);
		atualiza(alvo.m_b);
		while(true) {
		//	atualiza(alvo.m_a);
		//	atualiza(alvo.m_b);
			
			
			Novidade nov0 = AvaliaNovidade(0,alvo.m_a,alvo.m_b);
			Novidade nov1 = AvaliaNovidade(1,alvo.m_b,alvo.m_a);
			
			System.out.println("nov0 "+nov0);
			System.out.println("nov1 "+nov1);
			nov0.mutacao();
			nov1.mutacao();
			System.out.println("nov0 "+nov0);
			System.out.println("nov1 "+nov1);
			
			
			
			
			
			double r =AvaliaSS(0,alvo.m_a,alvo.m_b,false);
			
			AvaliaCoac(alvo.m_a,0);
			AvaliaCoac(alvo.m_a,1);
			AvaliaCoac(alvo.m_b,0);
			AvaliaCoac(alvo.m_b,1);
			
		
			if(r<=0.9) {
				System.out.println("busca0 "+nov0+"   "+alvo.m_b.translate());
				AI adv  = new Interpreter(utt,alvo.m_b);
				Search se = new CloneCoportamental(false,adv,playout,1000,0.9,0.5,false,nov0,this.v_best_coac,this.tempo_ini);
				Node_LS n=(Node_LS) se.run(gs, max_cicle, 0);
				this.atualiza(n);
			}
				
			if(1-r<=0.9) {
				System.out.println("busca1 "+nov1+alvo.m_a.translate());
				AI adv  = new Interpreter(utt,alvo.m_a);
				Search se = new CloneCoportamental(false,adv,playout,1000,0.9,0.5,false,nov1,this.v_best_coac,this.tempo_ini);
				
				Node_LS n=(Node_LS) se.run(gs, max_cicle, 1);
				this.atualiza(n);
			}
			
		}
		
		
	
	}

	
	public void  AvaliaCoac( Node_LS n0, int lado) throws Exception {
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n0);
		Pair<Double,CabocoDagua2> r = playout.run(gs2, lado, max, ai, adv, false);
		double res = r.m_a;
		if(lado==0) {
			if(this.v_best_coac.m_a<res) {
				this.v_best_coac.m_a=res;
				this.best_coac.m_a=(Node_LS) n0.Clone(f);
				long paraou = System.currentTimeMillis()-tempo_ini;
				double pontuacao = (v_best_coac.m_a+ v_best_coac.m_b)/2;
				System.out.println("atual\t"+((paraou*1.0)/1000.0)+"\t"+pontuacao+"\t"+
						Control.salve((Node) best_coac.m_a)+"\t"+Control.salve((Node) best_coac.m_b));
			}
		}else if(lado ==1 ) {
			if(this.v_best_coac.m_b<res) {
				this.v_best_coac.m_b=res;
				this.best_coac.m_b=(Node_LS) n0.Clone(f);
				long paraou = System.currentTimeMillis()-tempo_ini;
				double pontuacao = (v_best_coac.m_a+ v_best_coac.m_b)/2;
				System.out.println("atual\t"+((paraou*1.0)/1000.0)+"\t"+pontuacao+"\t"+
						Control.salve((Node) best_coac.m_a)+"\t"+Control.salve((Node) best_coac.m_b));
			}
		}
		
	}
	
	private void inicializa(GameState gs, int max_cicle) throws Exception {
		// TODO Auto-generated method stub
		long paraou = System.currentTimeMillis()-this.tempo_ini;
		while((paraou*1.0)/1000.0 <1000) {
			double r0= AvaliaSS(0,alvo.m_a,alvo.m_b,false);
			double r1= 1-r0;
			
			Node_LS aux0 = (Node_LS) alvo.m_a.Clone(f);
			int n0 = r.nextInt(aux0.countNode());
			int custo = r.nextInt(7)+3;
			aux0.mutation(n0, custo);
			
			
			Node_LS aux1 = (Node_LS) alvo.m_b.Clone(f);
			int n1 = r.nextInt(aux1.countNode());
			custo = r.nextInt(7)+3;
			aux1.mutation(n1, custo);
			
			AvaliaCoac(aux0,0);
			AvaliaCoac(aux1,1);
			
			
		
			boolean a0=false;
		
			boolean a1=false;
			
			if(r0<=0.9) {
				double rr = AvaliaSS(0,aux0,alvo.m_b,false);
				if(rr>r0) {
					a0=true;
					
				}
			}
				
			if(r1<=0.9) {
				Double rr  = AvaliaSS(1,aux1,alvo.m_a,false);
				if(rr>r1) {
					a1=true;
					
				}
			}
			
		
			if(a0)alvo.m_a= (Node_LS) aux0.Clone(f);
			if(a1)alvo.m_b= (Node_LS) aux1.Clone(f);
			
			paraou = System.currentTimeMillis()-this.tempo_ini;
		}
	}

}
