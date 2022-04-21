package IAs2;

import EvaluateGameState.Novidade;
import LS_CFG.Node_LS;
import TwoLevelSearch.Level1;
import TwoLevelSearch.Level2;
import rts.GameState;
import util.Pair;

public interface Avaliador {
	Pair<Double,Double> Avalia(GameState gs,int max,Node_LS n,Novidade oraculo,Level1 l1) throws Exception;
	double Avalia(GameState gs,int max,Node_LS n) throws Exception;
	void update(GameState gs,int max,Node_LS n) throws Exception;
	Node_LS getIndividuo();
	Node_LS getBest();
	boolean criterioParada(double d);
}
