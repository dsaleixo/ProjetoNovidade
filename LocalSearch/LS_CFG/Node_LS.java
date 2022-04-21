package LS_CFG;

import java.util.List;

import CFG.Node;

public interface Node_LS extends Node {
	void sample(int budget);
	void countNode(List<Node_LS> list);
	void mutation(int node_atual,int budget,boolean descreve);
	
}
