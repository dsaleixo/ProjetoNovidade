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

public class CCBasicaMutacao implements Search2 {

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
	
	
	public CCBasicaMutacao(AvaliaCoac coac, int tempo,double T0, double alpha, double beta,FabicaDeNovidade fn) {
		// TODO Auto-generated constructor stub
		System.out.println("Busca CCBM");
		this.coac = coac;
		this.tempo_limite=tempo;
		this.playout = new SimplePlayout();
		this.T0_inicial = T0;
		this.alpha_inicial= alpha;
		this.beta_inicial = beta;
		f = new FactoryLS();
		r =new Random();
		this.fn =fn;
	}

	Pair<Novidade,Novidade> gerasemente(GameState gs, int max,Node_LS j) throws Exception {
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,j.Clone(f));
		AI ai2 = new Interpreter(utt,j.Clone(f));
		Pair<Double,CabocoDagua2> r0 = playout.run(gs, 0, max, ai, ai2, false);
		Pair<Double,CabocoDagua2> r1 = playout.run(gs, 1, max, ai, ai2, false);
		Novidade nov0 = fn.gerar(r0.m_b);
		Novidade nov1 = fn.gerar(r1.m_b);

		return new Pair<>(nov0,nov1);
	}
	
	@Override
	public Node_LS run(GameState gs, int max, Node_LS j, Avaliador ava) throws Exception {
		// TODO Auto-generated method stub
		this.resert();
		UnitTypeTable utt = new UnitTypeTable();
		Pair<Novidade,Novidade>  nov = gerasemente(gs,max,j);
		System.out.println("atual3 "+nov.m_a+" x "+nov.m_b+" "+j.translate());
		nov.m_a.mutacao();
		nov.m_b.mutacao();
	
		System.out.println("atual3 "+nov.m_a+" x "+nov.m_b);
		Search se = new CloneCoportamental(false,new S_LS(new Empty_LS()),this.T0,this.alpha,this.beta,false,nov,this.coac, ava,this.tempo_limite);
		Node_LS n=(Node_LS) se.run(gs, max, 0);
		
		
		return n;
	}

	@Override
	public void resert() {
		// TODO Auto-generated method stub
		this.T0 = this.T0_inicial;
		this.alpha = this.alpha_inicial;
		this.beta = this.beta_inicial;

	}

}
