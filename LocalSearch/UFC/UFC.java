package UFC;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import AIs.Interpreter;
import CFG.Control;
import CFG.Factory;
import CFG.Node;
import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.FabricaNov1;
import EvaluateGameState.Novidade;
import EvaluateGameState.SimplePlayout;
import IAs2.Avaliador;
import LS_CFG.Empty_LS;
import LS_CFG.FactoryLS;
import LS_CFG.Node_LS;
import LS_CFG.S_LS;
import TwoLevelSearch.Level1;
import ai.core.AI;
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;

public class UFC implements Avaliador {

	int budget;
	List<Node_LS> individuos;
	List<List<Double>> score;
	List<AI> adv_atual;
	SimplePlayout playout;
	Factory f;
	long tempo_ini;
	Set<String> interessente;
	FabricaNov1 fn = new FabricaNov1();
	
	public UFC() {
		// TODO Auto-generated constructor stub
		System.out.println("UFC");
		UnitTypeTable utt = new UnitTypeTable();
		tempo_ini=System.currentTimeMillis();
		this.playout = new SimplePlayout();
		this.f = new FactoryLS();
		this.individuos = new ArrayList();
		this.individuos.add(new S_LS(new Empty_LS()));
		System.out.println("Camp\t"+0.0+"\t"+0+"\t"+
				Control.salve((Node)this.individuos.get(0)) );
		
		this.adv_atual = new ArrayList();
		this.adv_atual.add(new Interpreter(utt,this.individuos.get(0)));
		this.score = new ArrayList();
		score.add( new ArrayList());
		score.get(0).add(0.5);
		this.budget=0;
		this.interessente = new HashSet<>();
	}

	@Override
	public Pair<Double, Double> Avalia(GameState gs, int max, Node_LS n, Novidade oraculo, Level1 l1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double Avalia(GameState gs, int max, Node_LS n) throws Exception {
		// TODO Auto-generated method stub
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n);
		double r=0;
		
		for(int i =0;i<this.adv_atual.size();i++) {
		
			double r0 = playout.run(gs, utt,0, max, ai, adv_atual.get(i), false).m_a;
			double r1 = playout.run(gs,utt, 1, max, ai, adv_atual.get(i), false).m_a;	
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
	public void update(GameState gs, int max, Node_LS n) throws Exception {
		// TODO Auto-generated method stub
		UnitTypeTable utt = new UnitTypeTable();
		Node_LS camp= (Node_LS) n.Clone(f);
		AI ai = new Interpreter(utt,camp);
		List<Double> aux = new ArrayList();;
		for(int i=0;i< this.individuos.size();i++) {
			AI ai2 = new Interpreter(utt,this.individuos.get(i));
			Pair<Double,CabocoDagua2> aux0 = playout.run(gs,utt, 0, max, ai, ai2, false);
			Pair<Double,CabocoDagua2> aux1 = playout.run(gs,utt, 1, max, ai, ai2, false);
			double r = (aux0.m_a+ aux1.m_a)/2;
			this.score.get(i).add(1-r);
			aux.add(r);
		}
		camp.clear(null, f);
		this.individuos.add((Node_LS) camp);
		aux.add(0.5);
		this.score.add(aux);
		
		
		long paraou = System.currentTimeMillis()-this.tempo_ini;
		System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+this.budget+"\t"+
				Control.salve((Node)this.getBest()) );
		this.atualizaAdv();

	}

	@Override
	public Node_LS getIndividuo() {
		// TODO Auto-generated method stub
		return this.getBest();
	}

	@Override
	public Node_LS getBest() {
		// TODO Auto-generated method stub
		int index = this.getIdBest();
		System.out.println("Melhor "+ index);
		return (Node_LS) this.individuos.get(index).Clone(f);
	
	}

	public void atualizaAdv(){
		this.adv_atual.clear();
		UnitTypeTable utt = new UnitTypeTable();
		
		if(this.individuos.size()==1) {
			this.adv_atual.add(new Interpreter(utt,this.individuos.get(0)));
			System.out.println("Selecionado: 0");
	
		}
		
		int mel = this.getIdBest();
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		 System.out.println("Melhor: "+mel);
		 this.adv_atual.add(new Interpreter(utt,this.individuos.get(mel)));
        System.out.print("Selecionado: ");
        for (int i = 0; i < score.size(); i++) {
         
            if (this.score.get(mel).get(i)<0.5) {
            	this.adv_atual.add(new Interpreter(utt,this.individuos.get(i)));
                System.out.print(i+" ");
            }
            
        }
        System.out.println();
        System.out.println("Tabela: ");
        for (int i = 0; i < score.size(); i++)   System.out.print("\t"+i);
        System.out.println();
        for (int i = 0; i < score.size(); i++) {
        	System.out.print(i);
        	 for (int j = 0; j < score.size(); j++) {
        		 System.out.print("\t"+score.get(i).get(j));
        	 }
        	 System.out.println();
         }
            
		
	}
	
	public int getIdBest() {
		// TODO Auto-generated method stub
		double melhor=-1111111;
		int index = -1;
		for(int i=0;i<this.individuos.size();i++) {
			double cont=0;
			for(int j=0;j<this.individuos.size();j++) {
				cont+=score.get(i).get(j);
			}
			if(cont>=melhor) {
				melhor=cont;
				index =i;
			}
		}
		
		return index;
	}
	
	@Override
	public boolean criterioParada(double d) {
		// TODO Auto-generated method stub
		return d>this.adv_atual.size()-0.1;
	}

}
