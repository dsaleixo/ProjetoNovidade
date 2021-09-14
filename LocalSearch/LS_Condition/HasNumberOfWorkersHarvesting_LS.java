package LS_Condition;

import java.util.List;
import java.util.Random;

import CFG.N;
import CFG_Condition.HasNumberOfWorkersHarvesting;
import LS_CFG.Node_LS;

public class HasNumberOfWorkersHarvesting_LS extends HasNumberOfWorkersHarvesting implements Node_LS {

	public HasNumberOfWorkersHarvesting_LS() {
		// TODO Auto-generated constructor stub
	}

	public HasNumberOfWorkersHarvesting_LS(N n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub
		
		N n = new N();

		Random gerador = new Random();
	
		List<String> l2 = n.Rules();
		int g = gerador.nextInt(l2.size());
		n.setN(l2.get(g));
		this.setN(n);
	}

	@Override
	public int countNode() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	@Override
	public void mutation(int node_atual, int budget) {
		// TODO Auto-generated method stub
		if(node_atual==0)this.sample(budget);
	}
}