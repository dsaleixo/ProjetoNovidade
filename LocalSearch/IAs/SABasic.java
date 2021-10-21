package IAs;

import java.util.Random;

import AIs.Interpreter;
import CFG.Control;
import CFG.Factory;
import CFG.Node;

import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.ControleDeNovidade;
import EvaluateGameState.Nov0;
import EvaluateGameState.Novidade;
import EvaluateGameState.Playout;
import EvaluateGameState.SimplePlayout;
import LS_CFG.Empty_LS;
import LS_CFG.FactoryLS;
import LS_CFG.Node_LS;
import LS_CFG.S_LS;
import ai.core.AI;
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;

public class SABasic implements Search {

	Factory f;
	boolean use_cleanr;
	AI adv;
	boolean cego;
	Playout playout;
	double T0;
	double alpha;
	double beta;
	Random r =new Random();
	Node_LS best;
	double best_v;
	long tempo_ini;
	ControleDeNovidade CN;
	
	public SABasic() {
		// TODO Auto-generated constructor stub
		f = new FactoryLS();
		use_cleanr = true;
		Node_LS n =new S_LS(new Empty_LS());
		UnitTypeTable utt = new UnitTypeTable();
		this.adv = new Interpreter(utt,n);
		this.playout = new SimplePlayout();
		this.T0 = 2000;
		this.alpha=0.9;
		this.beta = 1;
		this.best = new S_LS(new Empty_LS());
		this.best_v= 0;
	}

	public SABasic(boolean clear,AI adv,Playout playout,double T0,double alpha,double beta,boolean cego) {
		// TODO Auto-generated constructor stub
		
		this.f = new FactoryLS();
		this.use_cleanr = clear;
		this.adv = adv;
		this.playout= playout;
		this.T0=T0;
		this.alpha = alpha;
		this.beta= beta;
		this.best = new S_LS(new Empty_LS());
		this.best_v = 0;
		this.cego = cego;
		if(!cego) {
			CN = new ControleDeNovidade();
			CN.add(new Nov0(0,0,0,0,0,0,0), this.best);
		}
		
	}
	
	public boolean if_best(double v1 ,double v2) {
		if(v2>v1)return true;
	
		return false;
	}
	
	public boolean accept(double v1 ,double v2, double temperatura) {
		
	

		
			//np.exp(self.beta * (next_score - current_score)/self.current_temperature)
			double aux2 = Math.exp(this.beta*(v2 - v1)/temperatura);
			aux2 = Math.min(1,aux2);
			if(r.nextFloat()<aux2)return true;
		
		return false;
	}
	
	
	public boolean if_best2(Pair<Double,Double> v1 ,Pair<Double,Double>  v2) {

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
	
	double Avalia(GameState gs, int max_cicle,int lado,Node_LS n) throws Exception{
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n);
		Pair<Double,CabocoDagua2> r = this.playout.run(gs, lado, max_cicle, ai, adv, false);
		if(r.m_b==null)return 0;

		if(!this.cego) {
			Novidade nov = new Nov0(r.m_b);
			CN.add(nov, n);
		}
		return r.m_a;
		
	}
	
	boolean stop(Pair<Double,Double> v1 ) {
		return false;
	}
	
	
	public Node bus_Novidade(GameState gs, int max_cicle,int lado) throws Exception {
		// TODO Auto-generated method stub
		
		return CN.pop();
	}

	public Node bus_adv(GameState gs, int max_cicle,int lado, Node aux2) throws Exception {
		// TODO Auto-generated method stub
		Node_LS atual =  (Node_LS) aux2.Clone(f);
		double v = this.best_v;
		long Tini = System.currentTimeMillis();
		long paraou = System.currentTimeMillis()-Tini;
	
		int cont=0;
		while( (paraou*1.0)/1000.0 <2000) {
			double T = this.T0/(1+cont*this.alpha);
			Node_LS melhor_vizinho = null ;
			double v_vizinho = -1;
			for(int i= 0;i<20;i++) {
				
				Node_LS aux = (Node_LS) (atual.Clone(f));
				for(int ii=0;ii<2;ii++) {
					int n = r.nextInt(aux.countNode());
					int custo = r.nextInt(7)+3;
					aux.mutation(n, custo);
				}
				double v2 = this.Avalia(gs, max_cicle,lado,aux);
					//System.out.println(v2.m_b+" "+aux.translate());
		
				
				if(if_best(v_vizinho,v2)) {
						if(this.use_cleanr)aux.clear(null, f);
						melhor_vizinho = (Node_LS) aux.Clone(f);
						v_vizinho=v2;	
				}
				paraou = System.currentTimeMillis()-Tini;
				if((paraou*1.0)/1000.0 >2000)break;
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
				System.out.println("atual\t"+((paraou2*1.0)/1000.0)+"\t"+best_v+"\t"+
							Control.salve(best)+"\t");
			}
			
			cont++;
			
			
			
		}
		
		
		return this.best;
	}
	
	@Override
	public Node run(GameState gs, int max_cicle,int lado) throws Exception {
		// TODO Auto-generated method stub
		
		this.tempo_ini = System.currentTimeMillis();
		
		long paraou = System.currentTimeMillis()-this.tempo_ini;
		while(true) {
			Node n=null;
			if(this.cego) {
				n = new S_LS(new Empty_LS());
			}else {
				
				n=	this.bus_Novidade(gs, max_cicle,lado);
			}
	

			this.bus_adv(gs, max_cicle,lado,n);
		}
		
		
		
		
	}

	
}