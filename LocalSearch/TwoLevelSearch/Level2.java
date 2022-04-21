package TwoLevelSearch;

import EvaluateGameState.Novidade;
import IAs2.Avaliador;
import LS_CFG.Node_LS;
import rts.GameState;

public interface Level2 {

	Node_LS run(GameState gs, int max, Node_LS j,Novidade nov, Avaliador ava,Level1 l1) throws Exception;

}
