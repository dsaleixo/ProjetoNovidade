package IAs2;

import java.util.List;

import LS_CFG.Node_LS;
import rts.GameState;

public interface Search2 {
	Node_LS run(GameState gs,int max,Node_LS j,Avaliador ava)throws Exception;
	void resert();
}
