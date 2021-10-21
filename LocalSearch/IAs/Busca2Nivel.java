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
	
	Pair<Node_LS,Node_LS> best;
	Pair<Double,Double> v_best;
	Pair<Node_LS,Node_LS> alvo;
	int tam = 5;
	double tempo_busca;
	
	double T0 = 1000;
	double alpha = 0.9;
	double beta =0.5;

	Random r;
	long tempo_ini;
	
	public Busca2Nivel(GameState gs, int max_cicle) {
		// TODO Auto-generated constructor stub
		System.out.println("Busca2Nivel");
		UnitTypeTable utt = new UnitTypeTable();
		gs2 = gs.cloneChangingUTT(utt);
		max = max_cicle;
		adv = new CoacAI(utt);
		playoutSS = new PlayoutSimplesSelf();
		playout = new SimplePlayout();
		v_best = new Pair<>(-1.0,-1.0);
		best = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		alvo = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		
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
	
	
	public Node_LS bus_adv( int lado, Node aux2,Novidade nov) throws Exception {
		// TODO Auto-generated method stub
		Node_LS atual =  (Node_LS) aux2.Clone(f);
		
		Pair<Double,Double> v = new Pair<>(0.0,0.0);
		if(lado==0) {
			v.m_a=this.v_best.m_a + 0.1*AvaliaSS(0,atual,this.alvo.m_b);
		
		}
		if(lado==1) {
			v.m_a=this.v_best.m_b + 0.1*AvaliaSS(1,atual,this.alvo.m_a);

		}
		long Tini = System.currentTimeMillis();
		long paraou = System.currentTimeMillis()-Tini;
	
		int cont=0;
		while( (paraou*1.0)/1000.0 <60) {
			double T = this.T0/(1+cont*this.alpha);
			Node_LS melhor_vizinho = null ;
			Pair<Double,Double> v_vizinho = new Pair<>(-1.0,-1.0);
			for(int i= 0;i<20;i++) {
				
				Node_LS aux = (Node_LS) (atual.Clone(f));
				for(int ii=0;ii<1;ii++) {
					int n = r.nextInt(aux.countNode());
					int custo = r.nextInt(9)+1;
					aux.mutation(n, custo);
				}
				Pair<Double,Double> v2 = this.Avalia(this.gs2, this.max,lado,aux,nov);
					//System.out.println(v2.m_b+" "+aux.translate());
		
				
				if(if_best(v_vizinho,v2)) {
						
						melhor_vizinho = (Node_LS) aux.Clone(f);
						v_vizinho=v2;	
				}
				paraou = System.currentTimeMillis()-Tini;
				if((paraou*1.0)/1000.0 >60)break;
			}
		
			if(this.if_best(v,v_vizinho)) {
				
				long paraou2 = System.currentTimeMillis()-this.tempo_ini;
				System.out.println("atual2\t"+((paraou2*1.0)/1000.0)+"\t"+v_vizinho.m_a+"\t"+v_vizinho.m_b+"\t"+
							Control.salve(melhor_vizinho)+"\t");
			}

				if(accept(v,v_vizinho,T)) {
					atual=(Node_LS) melhor_vizinho.Clone(f);
					v = v_vizinho;
					
				}
			//System.out.println(v_vizinho.m_b+"   t2\t"+melhor_vizinho.translate());
			
			
			
			cont++;
			
			
			
		}
		
		
		return atual;
	}
	
	
	private Pair<Double, Double> Avalia(GameState gs, int max_cicle, int lado, Node_LS aux,Novidade nov) throws Exception {
		// TODO Auto-generated method stub
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,aux);
		Pair<Double,Double> r = new Pair<>(0.0,0.0);
		double coac = this.playout.run(gs,lado, max_cicle, ai, adv, false).m_a;
		
		AI  av=null;
		if(lado==0)av=new Interpreter(utt,this.alvo.m_b);
		else  av=new Interpreter(utt,this.alvo.m_a);
		
		
		Pair<Double,CabocoDagua2> res =this.playout.run(gs,lado, max_cicle, ai, av, false) ;
		if(res.m_b!=null) {
			Novidade nova = fn.gerar(res.m_b);
			
			r.m_b = nova.semelhaca(nov);
	
		}else {
			r.m_b = -1.0;
		}
		r.m_a = res.m_a;
		
		
		if(lado==0) {
			if(this.v_best.m_a<coac) {
				this.v_best.m_a=coac;
				this.best.m_a=(Node_LS) aux.Clone(f);
				long paraou = System.currentTimeMillis()-tempo_ini;
				double pontuacao = (v_best.m_a+ v_best.m_b)/2;
				System.out.println("atual\t"+((paraou*1.0)/1000.0)+"\t"+pontuacao+"\t"+
						Control.salve((Node) best.m_a)+"\t"+Control.salve((Node) best.m_b));
			}
		}else if(lado ==1 ) {
			if(this.v_best.m_b<coac) {
				this.v_best.m_b=coac;
				this.best.m_b=(Node_LS) aux.Clone(f);
				long paraou = System.currentTimeMillis()-tempo_ini;
				double pontuacao = (v_best.m_a+ v_best.m_b)/2;
				System.out.println("atual\t"+((paraou*1.0)/1000.0)+"\t"+pontuacao+"\t"+
						Control.salve((Node) best.m_a)+"\t"+Control.salve((Node) best.m_b));
			}
		}
		
	
		r.m_a=res.m_a*0.1+coac;
		
		return r;
		
	}

	private List<Node_LS> buscaComportamental(List<Novidade> novs0, int lado,Node_LS seed) throws Exception {
		// TODO Auto-generated method stub
		List<Node_LS>  ls = new ArrayList<>();
		for(int i =0;i<novs0.size();i++) {
			System.out.println(lado+" "+i+" ==> "+novs0.get(i));
			ls.add(this.bus_adv(lado, seed, novs0.get(i)));
		}
		return ls;
		
	}
	
	public Pair<Double,Novidade>  AvaliaCoac( Node_LS n0, int lado) throws Exception {
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n0);
		Pair<Double,CabocoDagua2> r = playout.run(gs2, lado, max, ai, adv, false);
		double res = r.m_a;
		Novidade nov = fn.gerar(r.m_b);
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
		return new Pair<>(res,nov);
		
		
	}
	
	
	private Pair<Novidade, Novidade> gerasemente(Pair<Node_LS, Node_LS> vencedores) throws Exception {
		// TODO Auto-generated method stub
		
		//Pair<Double,Novidade> j0=AvaliaCoac(vencedores.m_a,0);
		//Pair<Double,Novidade> j1=AvaliaCoac(vencedores.m_b,1);
		//return new Pair<>(j0.m_b,j1.m_b);
		
		
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,vencedores.m_a);
		AI ai1 = new Interpreter(utt,vencedores.m_b);
		Pair<Double,CabocoDagua2> r0 = playout.run(gs2, 0, max, ai, ai1, false);
		Pair<Double,CabocoDagua2> r1 = playout.run(gs2, 1, max, ai1, ai, false);
		Novidade nov0 = fn.gerar();
		if(nov0!=null)nov0 = fn.gerar(r0.m_b);
		Novidade nov1 = fn.gerar();
		if(nov1!=null)nov1 = fn.gerar(r1.m_b);
		return new Pair<>(nov0,nov1);
	}

	private Pair<Node_LS, Node_LS> selecao(List<Node_LS> js0, List<Node_LS> js1) throws Exception {
		// TODO Auto-generated method stub
		
		
		List<Node_LS>  jogadores = new ArrayList<>(); 
		for(int i=0;i<js0.size();i++)jogadores.add(js0.get(i));
		for(int i=0;i<js1.size();i++)jogadores.add(js1.get(i));
		
		double melhor0 =-1;
		int j0=-1;
		for(int i=0;i<jogadores.size();i++) {
			double r = partidas(jogadores.get(i),jogadores,0);
			if(melhor0<r) {
				melhor0 = r;
				j0=i;
			}
		}
		
		
		double melhor1 =-1;
		int j1=-1;
		for(int i=0;i<js1.size();i++) {
			double r = partidas(jogadores.get(i),jogadores,1);
			if(melhor1<r) {
				melhor1 = r;
				j1=i;
			}
		}
		System.out.println(j0+" "+j1);
		return new Pair<>(jogadores.get(j0),jogadores.get(j1));
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
	
	@Override
	public Node run(GameState gs, int max_cicle, int lado) throws Exception {
		// TODO Auto-generated method stub
		
		Pair<Novidade,Novidade> semente = new Pair<>(fn.gerar(),fn.gerar());
		Pair<Node_LS,Node_LS> vencedores = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		
		this.tempo_ini=System.currentTimeMillis();
		while(true) {
			System.out.println();
		System.out.println("semente0 "+semente.m_a + vencedores.m_a.translate());
		System.out.println("semente1 "+semente.m_b+ vencedores.m_b.translate())  ;
		System.out.println("alvo0 "+ alvo.m_a.translate());
		System.out.println("alvo1 "+ alvo.m_b.translate())  ;
		List<Novidade> novs0 = geraVizinhos(semente.m_a);
		List<Novidade> novs1 = geraVizinhos(semente.m_b);
		
		
		List<Node_LS> js0 = buscaComportamental(novs0,0,vencedores.m_a);
		List<Node_LS> js1 = buscaComportamental(novs1,1,vencedores.m_b);
		
		 System.out.println("Campeao ");
		 vencedores = selecao(js0,js1); 
		 System.out.println(vencedores.m_a.translate()+"           "+vencedores.m_a.translate());
		 this.selfPlay(vencedores);
		 
		 semente = gerasemente(vencedores);
		}
		
		
	}

	private void selfPlay(Pair<Node_LS, Node_LS> vencedores) throws Exception {
		// TODO Auto-generated method stub
		
		double ant = AvaliaSS(0,alvo.m_a,alvo.m_b);
		
		double r0=ant;
		double r1=1-ant;
		
		double r = AvaliaSS(0,vencedores.m_a,alvo.m_b);
		 
		if(r>=r0) {
			long paraou = System.currentTimeMillis()-tempo_ini;
			this.alvo.m_a = vencedores.m_a;
			System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+
					Control.salve((Node) alvo.m_a)+"\t"+Control.salve((Node) alvo.m_b));
		}
		
		
		r += AvaliaSS(1,vencedores.m_b,alvo.m_a);
		if(r>=r1) {
			long paraou = System.currentTimeMillis()-tempo_ini;
			this.alvo.m_b = vencedores.m_b;
			System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+
					Control.salve((Node) alvo.m_a)+"\t"+Control.salve((Node) alvo.m_b));
		}
	}

	

	

	

}
