package TwoLevelSearch;

import EvaluateGameState.FabicaDeNovidade;
import EvaluateGameState.Novidade;
import LS_CFG.Node_LS;

public class Aleatorio implements Level1 {

	FabicaDeNovidade fn;
	
	public Aleatorio(FabicaDeNovidade fn) {
		this.fn = fn;
	}
	
	
	@Override
	public Novidade getSeed() {
		// TODO Auto-generated method stub
		return this.fn.geraAlet();
	}

	@Override
	public void update(Node_LS j, Novidade nov, double reward) {
		// TODO Auto-generated method stub

	}


	@Override
	public FabicaDeNovidade getFN() {
		// TODO Auto-generated method stub
		return fn;
	}

}
