package IAs2;

import AIs.Interpreter;
import CFG.Control;
import CFG.Factory;
import CFG.Node;
import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.Playout;
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

public class AvaliaCoac {

	AI coac;
	Playout playout ;
	Pair<Double,Double> v_best_coac;
	Pair<Node_LS,Node_LS> best_coac;
	long tempo_ini;
	Factory f;
	public AvaliaCoac() {
		// TODO Auto-generated constructor stub
		UnitTypeTable utt = new UnitTypeTable();
		coac = new CoacAI(utt);
		playout = new SimplePlayout();
		v_best_coac = new Pair<>(-1.0,-1.0);;
		tempo_ini= System.currentTimeMillis();
		best_coac = new Pair<>(new S_LS(new Empty_LS()),new S_LS(new Empty_LS()));
		f = new FactoryLS();
	}

	public void Avalia(GameState gs,int max,Node_LS n0) throws Exception {
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n0);
		 if(v_best_coac.m_a<0.9) {
			 Pair<Double,CabocoDagua2> r =playout.run(gs, 0, max, ai, coac, false);
			 if(this.v_best_coac.m_a<r.m_a) {
					this.v_best_coac.m_a=r.m_a;
					this.best_coac.m_a=(Node_LS) n0.Clone(f);
					long paraou = System.currentTimeMillis()-tempo_ini;
					double pontuacao = (v_best_coac.m_a+ v_best_coac.m_b)/2;
					System.out.println("atual\t"+((paraou*1.0)/1000.0)+"\t"+pontuacao+"\t"+
							Control.salve((Node) best_coac.m_a)+"\t"+Control.salve((Node) best_coac.m_b));
			 }
		 }
		 if(v_best_coac.m_b<0.9) {
			 Pair<Double,CabocoDagua2> r =playout.run(gs, 1, max, ai, coac, false);
			 if(this.v_best_coac.m_b<r.m_a) {
					this.v_best_coac.m_b=r.m_a;
					this.best_coac.m_a=(Node_LS) n0.Clone(f);
					long paraou = System.currentTimeMillis()-tempo_ini;
					double pontuacao = (v_best_coac.m_a+ v_best_coac.m_b)/2;
					System.out.println("atual\t"+((paraou*1.0)/1000.0)+"\t"+pontuacao+"\t"+
							Control.salve((Node) best_coac.m_a)+"\t"+Control.salve((Node) best_coac.m_b));
			 }
		 }
		
	}
	
}
