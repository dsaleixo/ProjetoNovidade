package IAs2;

import java.util.List;
import java.util.Random;

import AIs.Interpreter;
import CFG.Factory;
import EvaluateGameState.Playout;
import EvaluateGameState.SimplePlayout;
import LS_CFG.FactoryLS;
import LS_CFG.Node_LS;
import ai.core.AI;
import rts.GameState;
import rts.units.UnitTypeTable;

public class HC implements Search2 {


	AvaliaCoac coac;
	int tempo_limite;
	
	Factory f;
	Random r;
	
	public HC(AvaliaCoac coac, int tempo) {
		// TODO Auto-generated constructor stub
		System.out.println("Busca HC");
		this.coac = coac;
	
		this.tempo_limite=tempo;
		f = new FactoryLS();
		r =new Random();
	}

	
	
	@Override
	public Node_LS run(GameState gs, int max, Node_LS best,Avaliador ava) throws Exception {
		// TODO Auto-generated method stub
		this.resert();
		double v = ava.Avalia(gs, max, best);
		long Tini = System.currentTimeMillis();
		long tempo = System.currentTimeMillis()-Tini;
	
	
		
		while( (tempo*1.0)/1000.0 <tempo_limite && ava.criterioParada(v)) {
		
			Node_LS melhor_vizinho = null ;
			double v_vizinho = -111111;
			for(int i= 0;i<3;i++) {
				
				Node_LS aux = (Node_LS) (best.Clone(f));
				for(int ii=0;ii<1;ii++) {
					int n = r.nextInt(aux.countNode());
					int custo = r.nextInt(7)+3;
					aux.mutation(n, custo);
				}
				double v2 = ava.Avalia(gs, max, aux);
				//System.out.println(v2.m_b+" "+aux.translate());
				this.coac.Avalia(gs, max, aux);
			
				if(v_vizinho<v2) {
					melhor_vizinho = (Node_LS) aux.Clone(f);
					v_vizinho=v2;	
				}
				
				tempo = System.currentTimeMillis()-Tini;
				if((tempo*1.0)/1000.0 >tempo_limite || ava.criterioParada(v_vizinho))break;
		
			}
			
		//System.out.println(v_vizinho.m_b+"   t2\t"+melhor_vizinho.translate());
		tempo = System.currentTimeMillis()-Tini;
		
		
		if(v<v_vizinho) {
			best = (Node_LS) melhor_vizinho.Clone(f);
			v= v_vizinho;		
		
		}
		
		
			
		}
		return best;
	}

	@Override
	public void resert() {
		// TODO Auto-generated method stub

	}

}
