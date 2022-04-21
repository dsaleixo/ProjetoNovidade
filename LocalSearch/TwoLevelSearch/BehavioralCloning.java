package TwoLevelSearch;

import java.util.Random;

import CFG.Control;
import CFG.Factory;
import CFG.Node;
import EvaluateGameState.FabicaDeNovidade;
import EvaluateGameState.Novidade;
import EvaluateGameState.Playout;
import EvaluateGameState.SimplePlayout;
import IAs.CloneCoportamental;
import IAs.Search;
import IAs2.AvaliaCoac;
import IAs2.Avaliador;
import LS_CFG.Empty_LS;
import LS_CFG.FactoryLS;
import LS_CFG.Node_LS;
import LS_CFG.S_LS;
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;

public class BehavioralCloning implements Level2 {


	
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
	
	
	
	
	
	public BehavioralCloning(AvaliaCoac coac, int tempo,double T0, double alpha, double beta) {
		// TODO Auto-generated constructor stub
		
		System.out.println("Busca CCB");
		this.coac = coac;
		this.tempo_limite=tempo;
		
		this.T0_inicial = T0;
		this.alpha_inicial= alpha;
		this.beta_inicial = beta;
		f = new FactoryLS();
		r =new Random();
		
	}
	
	
	@Override
	public Node_LS run(GameState gs, int max, Node_LS j,Novidade nov, Avaliador ava,Level1 l1) throws Exception {
		// TODO Auto-generated method stub
		this.resert();
		
		System.out.println("busca "+nov);
		Search se = new CloneCoportamental(l1,false,j,this.T0,this.alpha,this.beta,false,nov,this.coac,ava,this.tempo_limite);
		Node_LS n=(Node_LS) se.run(gs, max, 0);
		return n;
	}
	
	

	
	public void resert() {
		// TODO Auto-generated method stub
		this.T0 = this.T0_inicial;
		this.alpha = this.alpha_inicial;
		this.beta = this.beta_inicial;
	}
	
	

}
