package FCS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import AIs.Interpreter;
import CFG.Control;
import CFG.Factory;
import CFG.Node;
import EvaluateGameState.CabocoDagua2;
import EvaluateGameState.Novidade;
import EvaluateGameState.SimplePlayout;
import IAs2.Avaliador;
import LS_CFG.Empty_LS;
import LS_CFG.FactoryLS;
import LS_CFG.Node_LS;
import LS_CFG.S_LS;
import TwoLevelSearch.Level1;
import ai.core.AI;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;

	

public class FCS implements Avaliador {

	
	int budget =0;
	List<Node_LS> individuos;
	private ArrayList< ArrayList<Integer>> payoff;
	List<AI> adv_atual;
	SimplePlayout playout;
	Factory f;
	long tempo_ini;
	
	
	public FCS() {
		// TODO Auto-generated constructor stub
		System.out.println("FCS");
		UnitTypeTable utt = new UnitTypeTable();
		tempo_ini=System.currentTimeMillis();
		this.playout = new SimplePlayout();
		this.f = new FactoryLS();
		this.individuos = new ArrayList();
		this.individuos.add(new S_LS(new Empty_LS()));
		System.out.println("Camp\t"+0.0+"\t"+0+"\t"+
				Control.salve((Node)this.individuos.get(0)) );
	
		this.adv_atual = new ArrayList();
		this.adv_atual.add(new Interpreter(utt,this.individuos.get(0)));
	
		
		payoff = new ArrayList<>();
		payoff.add(new ArrayList<>());
	}

	
	@Override
	public double Avalia(GameState gs, int max, Node_LS n) throws Exception {
		// TODO Auto-generated method stub
		UnitTypeTable utt = new UnitTypeTable();
		AI ai = new Interpreter(utt,n);
		double r=0;
		
		for(int i =0;i<this.adv_atual.size();i++) {
		
			double r0 = playout.run(gs,utt, 0, max, ai, adv_atual.get(i), false).m_a;
			double r1 = playout.run(gs,utt, 1, max, ai, adv_atual.get(i), false).m_a;	
			if(r0+r1>=0) {
				this.budget+=1;
			}
			r+=r0+r1;
			
			if(r0+r1>1)this.payoff.get(i).add(1);
			else this.payoff.get(i).add(0);
			
			long paraou = System.currentTimeMillis()-this.tempo_ini;
			if(this.budget%1000==0) {
				System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+this.budget+"\t"+
						Control.salve((Node)this.getBest()) );
			}
			
		}
		
		return r/2;
	}

	@Override
	public void update(GameState gs, int max, Node_LS n) throws Exception {
		// TODO Auto-generated method stub
		
        
        ArrayList<Integer> total_score = calc_total_score();
        ArrayList<Integer> ops = new ArrayList<>(), t_ops = new ArrayList<>();
        int best_score = -2;
        int t_score = score_by_comparation(calc_score(produce_list(ops, this.payoff.size()-1)), total_score);
        ops.add(this.payoff.size()-1);

        while (t_score > best_score) {
            best_score = t_score;
            boolean update = false;
            for (int op= 0; op< this.payoff.size();op++) {
                if (!ops.contains(op)) {
                    int score = score_by_comparation(calc_score(produce_list(ops, op)), total_score);
                    if (score > t_score) {
                        t_score = score;
                        t_ops = (ArrayList<Integer>) ops.clone();
                        t_ops.add(op);
                        update = true;
                    }
                }
            }
            if (update) {
                ops = (ArrayList<Integer>) t_ops.clone();
            }
        }
        
        long paraou = System.currentTimeMillis()-this.tempo_ini;
		
		Node_LS camp= (Node_LS) n.Clone(f);
		double r0 =Avalia(gs,max,this.individuos.get(this.individuos.size()-1));
		double r =Avalia(gs,max,camp);
		camp.clear(null, f);
		
         
        
        List<Node_LS> novos_individuos = new ArrayList<>();
        for(int o: ops ) {
        	novos_individuos.add((Node_LS) this.individuos.get(o).Clone(f));
        }
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.print("Selecionado: ");
        for(int o: ops ) {
        	System.out.print(o+" ");
        }
        System.out.println("");
        if(r0<r) {
        System.out.println("Camp\t"+((paraou*1.0)/1000.0)+"\t"+this.budget+"\t"
        +Control.salve((Node)camp) );
        novos_individuos.add(camp);
        }
        this.individuos = novos_individuos;
        adv_atual.clear();
        
        
        UnitTypeTable utt = new UnitTypeTable();
        for(int i=0;i<this.individuos.size();i++) {
        	 this.adv_atual.add(new Interpreter(utt,this.individuos.get(i)));
        }
        
      
       this.payoff.clear();
       this.payoff = new ArrayList<>();
       for(int i=0;i<this.individuos.size();i++) {
    	   payoff.add(new ArrayList<>());
       }
        
    }
	
	
	
	
	 private int score_by_comparation(ArrayList<Integer> values, ArrayList<Integer> total_score) {
	        int count = 0;
	       
	        for (int i = 0; i < values.size(); i++) {
	            if (Objects.equals(values.get(i), total_score.get(i))) {
	                count += 1;
	            }
	        }
	        return count;
	    }
	private ArrayList<ArrayList<Integer>> produce_list(ArrayList<Integer> ops, int op_adds) {
        ArrayList<Integer> t_ops = (ArrayList) ops.clone();
        if (op_adds != -1) {
            t_ops.add(op_adds);
        }

        ArrayList<ArrayList<Integer>> tuples = new ArrayList();
        for (int o : t_ops) {
            ArrayList<Integer> elem = this.payoff.get(o);//this.payoff.get(o);
            try {

                for (int i = 0; i < elem.size(); i++) {
                    Integer get = elem.get(i);
                    try {
                        tuples.get(i).add(get);
                    } catch (Exception e) {
                        tuples.add(i, new ArrayList<>());
                        tuples.get(i).add(get);
                    }

                }
            } catch (Exception e) {
                System.err.println("Error line 352");
                
                System.err.println("List in payoff");
               
            }
        }
        return tuples;
    }
	
	
	
	private ArrayList<Integer> calc_total_score() {
		ArrayList<Integer> ops= new ArrayList<>();
		for(int i=0;i<this.individuos.size();i++) {
			ops.add(i);
		}
		
        return calc_score(produce_list(ops, -1));
    }
	
	 private ArrayList<Integer> calc_score(ArrayList<ArrayList<Integer>> tuples) {
	        ArrayList<Integer> calculate = new ArrayList<>();
	        for (int i = 0; i < (tuples.size() - 1); i++) {

	            if (Collections.frequency(tuples.get(i), 1) > Collections.frequency(tuples.get(i + 1), 1)) {
	                calculate.add(-1);
	            } else if (Collections.frequency(tuples.get(i), 1) == Collections.frequency(tuples.get(i + 1), 1)) {
	                calculate.add(0);
	            } else {
	                calculate.add(1);
	            }
	        }
	        return calculate;
	    }
	
    
	
	@Override
	public Node_LS getIndividuo() {
		// TODO Auto-generated method stub
		return (Node_LS) this.individuos.get(this.individuos.size()-1).Clone(f);
	}

	@Override
	public Node_LS getBest() {
		// TODO Auto-generated method stub
		return (Node_LS) this.individuos.get(this.individuos.size()-1).Clone(f);
	}

	@Override
	public boolean criterioParada(double d) {
		// TODO Auto-generated method stub
		return d > this.individuos.size()-0.1;
	}

	@Override
	public Pair<Double, Double> Avalia(GameState gs, int max, Node_LS n, Novidade oraculo, Level1 l1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
