package IAs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import AIs.Interpreter;
import CFG.Control;
import CFG.Factory;
import CFG.Node;
import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.ControleDeNovidade;
import EvaluateGameState.FabicaDeNovidade;
import EvaluateGameState.FabricaNov1;
import EvaluateGameState.Nov0;
import EvaluateGameState.Novidade;
import EvaluateGameState.Playout;
import EvaluateGameState.SimplePlayout;
import IAs2.AvaliaCoac;
import IAs2.Avaliador;
import LS_CFG.Empty_LS;
import LS_CFG.FactoryLS;
import LS_CFG.Node_LS;
import LS_CFG.S_LS;
import ai.coac.CoacAI;
import ai.core.AI;
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;




public class CloneCoportamental implements Search {

	Factory f;
	boolean use_cleanr;
	
	boolean cego;
	
	double T0;
	double alpha;
	double beta;
	Random r =new Random();
	Node_LS best;
	Pair<Double,Double> best_v;
	long tempo_ini;

	int limit_imitacao=360;
	FabicaDeNovidade fn;
	Pair<Novidade,Novidade>  oraculo;
	
	Avaliador ava;
	AvaliaCoac coac;
	

	public CloneCoportamental(boolean clear,Node_LS jj,double T0,double alpha,double beta,boolean cego, Pair<Novidade,Novidade>  oraculo, AvaliaCoac atual_coac,Avaliador ava,int limite_tempo) {
		// TODO Auto-generated constructor stub
		this.coac = atual_coac;
		this.f = new FactoryLS();
		this.use_cleanr = clear;
		this.limit_imitacao = limite_tempo;
		this.T0=T0;
		this.alpha = alpha;
		this.beta= beta;
		this.best = jj;
		this.ava=ava;
		this.cego = cego;
		this.fn = new FabricaNov1();
		this.oraculo=oraculo;
		this.tempo_ini = System.currentTimeMillis();

		
		UnitTypeTable utt = new UnitTypeTable();
	
		

		
		
	}
	
	
	
	public boolean if_best(Pair<Double,Double> v1 ,Pair<Double,Double>  v2) {
		if(v2.m_a>v1.m_a)return true;
	
		boolean aux = Math.abs(v2.m_a - v1.m_a) <0.1;
		if(aux && v2.m_b > v1.m_b) return true;
		return false;
	}
	
	public boolean accept(Pair<Double,Double> v1 ,Pair<Double,Double>  v2, double temperatura) {
		if(v2.m_a>v1.m_a)return true;
	
		boolean aux = Math.abs(v2.m_a - v1.m_a) <0.1;
		if(aux ) {
			//np.exp(self.beta * (next_score - current_score)/self.current_temperature)
			double aux2 = Math.exp(this.beta*(v2.m_b - v1.m_b)/temperatura);
			aux2 = Math.min(1,aux2);
			if(r.nextFloat()<aux2)return true;
		}
		return false;
	}
	
	
	public boolean if_best2(Pair<Double,Double> v1 ,Pair<Double,Double>  v2) {
		if(ava.criterioParada(v2.m_a))return true;
		if( v2.m_b > v1.m_b) return true;
		return false;
	}
	
	public boolean accept2(Pair<Double,Double> v1 ,Pair<Double,Double>  v2, double temperatura) {
		
	
			//np.exp(self.beta * (next_score - current_score)/self.current_temperature)
		double aux2 = Math.exp(this.beta*(v2.m_b - v1.m_b)/temperatura);
		aux2 = Math.min(1,aux2);
		if(r.nextFloat()<aux2)return true;
		
		return false;
	}
	
				
	
	boolean stop(Pair<Double,Double> v1 ) {
		return false;
	}
	
	
	public Node bus_imitacao(GameState gs, int max_cicle) throws Exception {
		// TODO Auto-generated method stub
		Node_LS atual =  new S_LS(new Empty_LS());
		Pair<Double,Double> v = new Pair<>(-1.0,-1.0);
		long Tini = System.currentTimeMillis();
		long paraou = System.currentTimeMillis()-Tini;
	
		int cont=0;
		while( (paraou*1.0)/1000.0 <(this.limit_imitacao*0.1)&&!ava.criterioParada(v.m_a)) {
			double T = this.T0/(1+cont*this.alpha);
			Node_LS melhor_vizinho = null ;
			Pair<Double,Double> v_vizinho = new Pair<>(-1111.0,-1111.0);
			for(int i= 0;i<5;i++) {
				
				Node_LS aux = (Node_LS) (atual.Clone(f));
				
				for(int ii=0;ii<1;ii++) {
					int n = r.nextInt(aux.countNode());
					
					int custo = r.nextInt(9)+1;
					aux.mutation(n, custo);
				}
				Pair<Double,Double> v2 = ava.Avalia(gs,max_cicle,aux,oraculo);
				//sSystem.out.println(v2.m_b+" "+aux.translate());
				if(if_best2(v_vizinho,v2)) {
					if(this.use_cleanr)aux.clear(null, f);
					melhor_vizinho = (Node_LS) aux.Clone(f);
					v_vizinho=v2;
				}
				
				
				paraou = System.currentTimeMillis()-Tini;
				if((paraou*1.0)/1000.0 >(this.limit_imitacao)*0.1|| ava.criterioParada(v_vizinho.m_a)) {
					
					break;	
				}
			}
		
			
		
			if(this.accept2(v,v_vizinho,T)) {
				atual=(Node_LS) melhor_vizinho.Clone(f);
				v = v_vizinho;
				
			}
		//	System.out.println(v_vizinho.m_b+"   t\t"+melhor_vizinho.translate());
			paraou = System.currentTimeMillis()-Tini;
			
			
			if(this.if_best2(this.best_v,v_vizinho)) {
				this.best = (Node_LS) melhor_vizinho.Clone(f);
				this.best_v = v_vizinho;
				long paraou2 = System.currentTimeMillis()-this.tempo_ini;
				System.out.println("atual2\t"+((paraou2*1.0)/1000.0)+"\t"+best_v.m_a+"\t"+best_v.m_b+"\t"+
						Control.salve(best)+"\t");
				
				
			}
			if(ava.criterioParada(best_v.m_a)) {
				this.best = (Node_LS) melhor_vizinho.Clone(f);
				this.best_v = v_vizinho;
				long paraou2 = System.currentTimeMillis()-this.tempo_ini;
				System.out.println("atual2\t"+((paraou2*1.0)/1000.0)+"\t"+best_v.m_a+"\t"+best_v.m_b+"\t"+
						Control.salve(best)+"\t");
				break;
			}
			cont++;
			
			
			
		}
		
		
		return this.best;
	}

	public Node bus_adv(GameState gs, int max_cicle, Node aux2) throws Exception {
		// TODO Auto-generated method stub
		Node_LS atual =  (Node_LS) aux2.Clone(f);
		Pair<Double,Double> v = this.best_v;
		long Tini = System.currentTimeMillis();
		long paraou = System.currentTimeMillis()-Tini;
	
		int cont=0;
		while( (paraou*1.0)/1000.0 <this.limit_imitacao*1.0 && !ava.criterioParada(v.m_a)) {
			double T = this.T0/(1+cont*this.alpha);
			Node_LS melhor_vizinho = null ;
			Pair<Double,Double> v_vizinho = new Pair<>(-1.0,-1.0);
			for(int i= 0;i<5;i++) {
				
				Node_LS aux = (Node_LS) (atual.Clone(f));
				for(int ii=0;ii<1;ii++) {
					int n = r.nextInt(aux.countNode());
					int custo = r.nextInt(9)+1;
					aux.mutation(n, custo);
				}
				Pair<Double,Double> v2 = ava.Avalia(gs, max_cicle,aux,oraculo);
					//System.out.println(v2.m_b+" "+aux.translate());
		
				
				if(if_best(v_vizinho,v2)) {
						if(this.use_cleanr)aux.clear(null, f);
						melhor_vizinho = (Node_LS) aux.Clone(f);
						v_vizinho=v2;	
				}
				paraou = System.currentTimeMillis()-Tini;
				if((paraou*1.0)/1000.0 >(this.limit_imitacao)*1.0 || this.ava.criterioParada(v_vizinho.m_a) )break;
				
			}
		
			

				if(accept(v,v_vizinho,T)) {
					atual=(Node_LS) melhor_vizinho.Clone(f);
					v = v_vizinho;
					
				}
			//System.out.println(v_vizinho.m_b+"   t2\t"+melhor_vizinho.translate());
			paraou = System.currentTimeMillis()-Tini;
			
			
			if(this.if_best(this.best_v,v_vizinho)) {
				this.best = (Node_LS) melhor_vizinho.Clone(f);
				this.best_v = v_vizinho;
				long paraou2 = System.currentTimeMillis()-this.tempo_ini;
				System.out.println("atual2\t"+((paraou2*1.0)/1000.0)+"\t"+best_v.m_a+"\t"+best_v.m_b+"\t"+
							Control.salve(best)+"\t");
				
			}
			if(this.ava.criterioParada(best_v.m_a)) {
				this.best = (Node_LS) melhor_vizinho.Clone(f);
				this.best_v = v_vizinho;
				long paraou2 = System.currentTimeMillis()-this.tempo_ini;
				System.out.println("atual2\t"+((paraou2*1.0)/1000.0)+"\t"+best_v.m_a+"\t"+best_v.m_b+"\t"+
						Control.salve(best)+"\t");
				break;
			}
			cont++;
			
			
			
		}
		
		
		return this.best;
	}
	
	@Override
	public Node run(GameState gs, int max_cicle,int lado) throws Exception {
		// TODO Auto-generated method stub
		
		
		
		long paraou = System.currentTimeMillis()-this.tempo_ini;
		this.best_v = ava.Avalia(gs, max_cicle, best,oraculo);
		System.out.println("atual2\t"+0.0+"\t"+best_v.m_a+"\t"+best_v.m_b+"\t"+
				Control.salve(best)+"\t");
		Node n=null;
		if(this.cego) {
			n = new S_LS(new Empty_LS());
		}else {
			System.out.println("Imitação");
			n=	this.bus_imitacao(gs, max_cicle);
		}
		System.out.println("Ganha");
		return this.bus_adv(gs, max_cicle,n);

		
	}

	
}