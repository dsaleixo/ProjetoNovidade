package IAs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import AIs.Interpreter;
import CFG.Control;
import CFG.Factory;
import CFG.Node;
import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.FabicaDeNovidade;
import EvaluateGameState.FabricaNov1;
import EvaluateGameState.Nov0;
import EvaluateGameState.Novidade;
import EvaluateGameState.Playout;
import EvaluateGameState.PlayoutSimplesSelf;
import EvaluateGameState.SimplePlayout;
import LS_CFG.Empty_LS;
import LS_CFG.FactoryLS;
import LS_CFG.Node_LS;
import LS_CFG.S_LS;
import ai.abstraction.RangedRush;
import ai.coac.CoacAI;
import ai.core.AI;
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;

public class Busca2Nivel implements Search {

	int max;
	Playout playout;
	Playout playoutSS;
	AI adv;
	Factory f = new FactoryLS();
	GameState gs2;
	FabicaDeNovidade fn;
	
	Pair<Node_LS,Node_LS> best_coac;
	Pair<Double,Double> v_best_coac;
	
	List<Node_LS> js0;
	List<Node_LS> js1;
	Pair<Node_LS,Node_LS> atual;
	
	int tam = 5;
	double tempo_busca;
	
	double T0 = 1000;
	double alpha = 0.9;
	double beta =0.5;

	Random r;
	long tempo_ini;
	
	
int[] tempos = {5,10,15,20};
	
	int getTempo() {
		long paraou = System.currentTimeMillis()-tempo_ini;
		double sec = ((paraou*1.0)/1000.0);
		if(sec<2*3600) return tempos[0];
		else if(sec<6*3600)return tempos[1];
		else if(sec<14*3600)return tempos[2];
		else return tempos[3];
	}
	
	public Busca2Nivel(GameState gs, int max_cicle,int n) {
		// TODO Auto-generated constructor stub
		System.out.println("Busca2Nivel "+n);
		UnitTypeTable utt = new UnitTypeTable();
		gs2 = gs.cloneChangingUTT(utt);
		max = max_cicle;
		adv = new CoacAI(utt);
		playoutSS = new PlayoutSimplesSelf();
		playout = new SimplePlayout();
		v_best_coac = new Pair<>(-1.0,-1.0);
		best_coac = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		atual=  new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		
		js0 = new ArrayList<>();
		js1 = new ArrayList<>();
		this.tam =n;
		for(int i =0;i<tam;i++) {
			js0.add(new S_LS(new Empty_LS()));
			js1.add(new S_LS(new Empty_LS()));
		}
		
		r =new Random();
		f = new FactoryLS();
		fn = new FabricaNov1();
	}

	private List<Novidade> geraVizinhos(Novidade m_a) {
		// TODO Auto-generated method stub
		List<Novidade> novs = new ArrayList<>();
		System.out.println("Novos vizinhos");
		for(int i=0;i<this.tam;i++) {
			Novidade aux = m_a.Clone();
			aux.mutacao();
			novs.add(aux);
			System.out.println("\t "+ aux.toString());
		}
		//novs.add(m_a.Clone());
		//System.out.println("\t "+ m_a.toString());
		return novs;
	}
	
	
	
	
	
	
	
	
	public Pair<Double,Novidade>  AvaliaCoac( Node_LS n0, int lado) throws Exception {
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n0);
		Pair<Double,CabocoDagua2> r = playout.run(gs2, lado, max, ai, adv, false);
		double res = r.m_a;
		Novidade nov = fn.gerar(r.m_b);
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
		return new Pair<>(res,nov);
		
		
	}
	
	
	private Pair<Novidade, Novidade> gerasemente(Pair<Node_LS, Node_LS> vencedores) throws Exception {
		// TODO Auto-generated method stub
		
		Pair<Double,Novidade> j0=AvaliaCoac(vencedores.m_a,0);
		Pair<Double,Novidade> j1=AvaliaCoac(vencedores.m_b,1);
		return new Pair<>(j0.m_b,j1.m_b);
		
		
	}

	
	
	public double AvaliaSS(int lado,Node_LS n0,Node_LS n1) throws Exception{
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n0);
		AI ai1 = new Interpreter(utt,n1);
		Pair<Double,CabocoDagua2> r = playoutSS.run(gs2, lado, max, ai, ai1, false);
		return  r.m_a;
		
	}
	
	private double partidas(Node_LS node_LS, List<Node_LS> advs, int lado) throws Exception {
		// TODO Auto-generated method stub
		double r=0;
		for(int i=0;i<advs.size();i++) {
			r+= AvaliaSS(lado,node_LS,advs.get(i));
		}
		
		return r;
	}

	public void atualizaTempo() {
		if(this.tempo_busca<20)this.tempo_busca+=0.5;
	}
	
	public Pair<Double,Double> AvaliaFic(Node_LS j0,Node_LS j1) throws Exception{
		Pair<Double,Double> r = new Pair<>(0.0,0.0);
		
		for(Node_LS j : js1) {
			r.m_a+=AvaliaSS(0,j0,j);
		}
		
		for(Node_LS j : js0) {
			r.m_b+=AvaliaSS(1,j1,j);
		}
		
		return r;
	}
	
	 void add(Node_LS j, List<Node_LS> lado,List<Node_LS> ini,int campo) throws Exception {
			// TODO Auto-generated method stub
			if(lado.size()>=this.tam)lado.remove(0);
			
			Node_LS camp= (Node_LS) j.Clone(f);
			for(Node_LS jj : ini) {
				AvaliaSS(campo,camp,jj);
			}
			camp.clear(null, f);
			lado.add((Node_LS) camp);
	}
	
	@Override
	public Node run(GameState gs, int max_cicle, int lado) throws Exception {
		
		// TODO Auto-generated method stub
		Pair<Novidade,Novidade> sementes = new Pair<>(fn.gerar(),fn.gerar() );
		int cont=0;
		this.tempo_ini=System.currentTimeMillis();
		while(true) {
			
			
			
			atual.m_a = (Node_LS) js0.get(js0.size()-1).Clone(f);
			atual.m_b = (Node_LS) js1.get(js1.size()-1).Clone(f);
			sementes = gerasemente(atual);
			System.out.println();
			System.out.println("nov0  "+ sementes.m_a);
			System.out.println("nov1  "+ sementes.m_b);
			System.out.println("J0"+ atual.m_a.translate());
			System.out.println("j1"+ atual.m_b.translate());
			Pair<Double,Double> parcial = AvaliaFic(atual.m_a,atual.m_b);
			
			Novidade nov0 = sementes.m_a.Clone();
			nov0.mutacao();
			System.out.println("nov0 busca  "+nov0);
			CloneCoportamental CC0 = new CloneCoportamental(false,js1,playout,this.T0,this.alpha,this.beta,false,nov0,this.v_best_coac,this.tempo_ini,10);
			Node_LS j0 =null;
			if(parcial.m_a<this.tam-0.1)	j0 =(Node_LS) CC0.run(gs, max_cicle, 0);
			else j0 = atual.m_a;
			
		
			Novidade nov1 = sementes.m_b.Clone();
			nov1.mutacao();
			System.out.println("nov1 busca  "+nov1);
			CloneCoportamental CC1 = new CloneCoportamental(false,js0,playout,this.T0,this.alpha,this.beta,false,nov1,this.v_best_coac,this.tempo_ini, 10);
			Node_LS j1 =null;
			if(parcial.m_b<this.tam-0.1)	j1 =(Node_LS) CC1.run(gs, max_cicle, 1);
			else j1 = atual.m_b;
					
			Pair<Double,Double> resposta = AvaliaFic(j0,j1);
			Pair<Double,Double> resposta1 = AvaliaFic(j1,j0);
			
			
			System.out.println("Resultado0 "+parcial.m_a+" "+resposta.m_a+" "+resposta1.m_a);
			System.out.println("Resultado1 "+parcial.m_b+" "+resposta.m_b+" "+resposta1.m_b);
			if((parcial.m_a<this.tam-0.1) ) {
				if(parcial.m_a<resposta.m_a) {
					this.add(j0, js0,js1,0);
					long paraou = System.currentTimeMillis()-tempo_ini;
					Node_LS j00 = this.js0.get(this.js0.size()-1);
					Node_LS j11 = this.js1.get(this.js1.size()-1);
					System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+
							Control.salve((Node)j00) +"\t"+Control.salve((Node) j11));
				
					
				}else if(parcial.m_a<resposta1.m_a ) {
					this.add(j1, js0,js1,0);
					long paraou = System.currentTimeMillis()-tempo_ini;
					Node_LS j00 = this.js0.get(this.js0.size()-1);
					Node_LS j11 = this.js1.get(this.js1.size()-1);
					System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+
							Control.salve((Node)j00) +"\t"+Control.salve((Node) j11));
					
				}
			}
			if((parcial.m_b<this.tam-0.1)) {
				if(parcial.m_b<resposta.m_b) {
					this.add(j1, js1,js0,1);
					long paraou = System.currentTimeMillis()-tempo_ini;
					Node_LS j00 = this.js0.get(this.js0.size()-1);
					Node_LS j11 = this.js1.get(this.js1.size()-1);
					System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+
							Control.salve((Node)j00) +"\t"+Control.salve((Node) j11));
					
					
				}else if(parcial.m_b<resposta1.m_b) {
					this.add(j0, js1,js0,1);
					long paraou = System.currentTimeMillis()-tempo_ini;
					Node_LS j00 = this.js0.get(this.js0.size()-1);
					Node_LS j11 = this.js1.get(this.js1.size()-1);
					System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+
							Control.salve((Node)j00) +"\t"+Control.salve((Node) j11));
					
				}
			}
			
		
			cont++;
		}
		
	}

	private boolean acceptSA(Double m_a, Double m_a2, int t) {
		// TODO Auto-generated method stub
		double aux2 = Math.exp(this.beta*(m_a -  m_a2)/t);
		aux2 = Math.min(1,aux2);
		if(r.nextFloat()<aux2)return true;
		
		return false;
	}

	

	

	

	

}
