package TwoLevelSearch;

import EvaluateGameState.FabicaDeNovidade;
import EvaluateGameState.Novidade;
import LS_CFG.Node_LS;
import util.Pair;

public interface Level1 {

	Pair<Novidade,Node_LS> getSeed();
	void update(Node_LS j, Novidade nov, double reward );
	FabicaDeNovidade getFN();
	void imprimir();
}
