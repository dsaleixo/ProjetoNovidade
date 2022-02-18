package IAs2;

import java.util.List;
import java.util.Random;

import AIs.Interpreter;
import CFG.Factory;
import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.FabicaDeNovidade;
import EvaluateGameState.FabricaNov1;
import EvaluateGameState.Novidade;
import EvaluateGameState.Playout;
import EvaluateGameState.SimplePlayout;
import IAs.CloneCoportamental;
import IAs.Search;
import LS_CFG.Empty_LS;
import LS_CFG.FactoryLS;
import LS_CFG.Node_LS;
import LS_CFG.S_LS;
import ai.core.AI;
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;

public class B2N2 implements Search2 {

	Playout playout;
	FabicaDeNovidade fn;
	AvaliaCoac coac;
	
	int tempo_limite;
	double T0_inicial;
	double alpha_inicial;
	double beta_inicial;
	double T0;
	double alpha;
	double beta;

	Factory f;
	Random r;
	
	public B2N2(AvaliaCoac coac, int tempo,double T0, double alpha, double beta,FabicaDeNovidade fn) {
		// TODO Auto-generated constructor stub
		System.out.println("Busca B2N2");
		this.coac = coac;
		this.tempo_limite=tempo;
		this.playout = new SimplePlayout();
		this.T0_inicial = T0;
		this.alpha_inicial= alpha;
		this.beta_inicial = beta;
		f = new FactoryLS();
		r =new Random();
		this.fn = fn;
	}

	Novidade gerasemente(GameState gs, int max, int lado,Node_LS j, Node_LS adv) throws Exception {
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,j);
		AI ai2 = new Interpreter(utt,adv);
		Pair<Double,CabocoDagua2> r = playout.run(gs, lado, max, ai, ai2, false);
		Novidade nov = fn.gerar(r.m_b);
		return nov;
	}
	
	
	Pair<Novidade,Novidade> gerasemente(GameState gs, int max,Node_LS j) throws Exception {
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,j.Clone(f));
		AI ai2 = new Interpreter(utt,j.Clone(f));
		Pair<Double,CabocoDagua2> r0 = playout.run(gs, 0, max, ai, ai2, false);
		Pair<Double,CabocoDagua2> r1 = playout.run(gs, 1, max, ai, ai2, false);
		Novidade nov0 = fn.gerar();
		Novidade nov1 = fn.gerar();
		if(r0.m_b!=null) nov0 = fn.gerar(r0.m_b);
		if(r1.m_b!=null) nov1 = fn.gerar(r1.m_b);
		return new Pair<>(nov0,nov1);
	}
	
	

	
	@Override
	public Node_LS run(GameState gs, int max, Node_LS j,Avaliador ava) throws Exception {
		
		// TODO Auto-generated method stub
		Node_LS best=j;
		Pair<Novidade,Novidade> local = gerasemente(gs,max,j);
		int cont=0;
		double v = ava.Avalia(gs, max, j);
		double v_local=v;
		long Tini = System.currentTimeMillis();
		long tempo = System.currentTimeMillis()-Tini;
	
	
		
		while((tempo*1.0)/1000.0 <tempo_limite && !ava.criterioParada(v)) {
			
			double T = this.T0/(1+cont*this.alpha);
			Pair<Novidade,Novidade> melhor_vizinho = null ;
			Node_LS meslhor_script=null;
			double v_vizinho = -1111;
			 local = gerasemente(gs,max,j);
			 System.out.println("Busca "+local +" Valor"+v);
			for(int i= 0;i<3;i++) {
				
				Pair<Novidade,Novidade> aux = new Pair(local.m_a.Clone(),local.m_b.Clone());
				for(int ii=0;ii<3;ii++) {
					aux.m_a.mutacao();
					aux.m_b.mutacao();
				}
				System.out.println("Busca2 "+aux );
				Search se = new CloneCoportamental(false,(Node_LS) new S_LS(new Empty_LS()),this.T0,this.alpha,this.beta,false,aux,this.coac,ava,2000);
				Node_LS n = (Node_LS) se.run(gs, max, 0);
				double v2 = ava.Avalia(gs, max, n);
				
				if(v_vizinho<v2) {
					melhor_vizinho = new Pair(aux.m_a.Clone(),aux.m_b.Clone());;
					v_vizinho=v2;	
					meslhor_script=(Node_LS) n.Clone(f);
					
				}
				
				if((tempo*1.0)/1000.0 >tempo_limite || ava.criterioParada(v_vizinho)) {
					;
					break;
				}
			}
				
			
			//System.out.println(v_vizinho.m_b+"   t2\t"+melhor_vizinho.translate());
			tempo = System.currentTimeMillis()-Tini;
		
		
			if(v<v_vizinho) {
				System.out.println("aceite "+ v_local+"<"+v_vizinho);
				j=  (Node_LS) meslhor_script.Clone(f);
				best = (Node_LS) meslhor_script.Clone(f);
				v= v_vizinho;		
			}
		
			cont++;
		}
		return best;
	}
	@Override
	public void resert() {
		// TODO Auto-generated method stub
		this.T0 = this.T0_inicial;
		this.alpha = this.alpha_inicial;
		this.beta = this.beta_inicial;
	}

}
