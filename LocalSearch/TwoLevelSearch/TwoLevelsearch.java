package TwoLevelSearch;

import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.Nov1;
import EvaluateGameState.Novidade;
import IAs2.Avaliador;
import IAs2.Search2;
import LS_CFG.Node_LS;
import rts.GameState;
import util.Pair;

public class TwoLevelsearch  {

	Avaliador ava;
	Level1 l1;
	Level2 l2;

	
	public TwoLevelsearch(Level1 l1, Level2 l2,Avaliador ava) {
		// TODO Auto-generated constructor stub
		this.ava=ava;
		this.l1 = l1;
		this.l2 = l2;
	
	}
	
	
	
	
	public void run(GameState gs,int max) throws Exception {
		
		while(true) {
			
			Pair<Novidade, Node_LS> seed = this.l1.getSeed();
			System.out.println("xxxxxxxxxxxxxxx");
			l1.imprimir();
			
			System.out.println("Selecionado: "+seed.m_a);
			System.out.println("Script inicial "+seed.m_b.translate());
			
			Node_LS  c0 = this.l2.run(gs, max, seed.m_b,seed.m_a, this.ava,l1);
			
			
			
			ava.update(gs, max, c0);
			
		}
		
		
	}

	
}
