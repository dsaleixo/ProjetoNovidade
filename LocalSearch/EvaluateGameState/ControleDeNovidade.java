package EvaluateGameState;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import java.util.Random;
import java.util.TreeSet;

import LS_CFG.Node_LS;
import LS_CFG.S_LS;
import util.Pair;

public class ControleDeNovidade {

	
	HashMap<Novidade,Node_LS> novidades;
	
	public ControleDeNovidade() {
		// TODO Auto-generated constructor stub
		
		novidades = new HashMap<>();
		
	}
	
	
	public void add(Novidade nov, Node_LS n ) {
		if(!novidades.containsKey(nov)) {
			System.out.println("add "+nov+" "+n.translate());
			novidades.put(nov, n);
		}
	}
	
	public Node_LS pop() {
		int size = novidades.size();
		Random gerador = new Random();
		int g = gerador.nextInt(size);
		
		int i =0;
		Node_LS n=null;;
		for(Novidade nov : novidades.keySet()) {
			if(g==i) {
				n=novidades.get(nov);
				novidades.remove(nov);
				System.out.println("abre "+nov+" "+n.translate());
				break;
			}
			i++;
		}
		return  n;
		
	}
	
	public boolean isEmpty() {
		return novidades.isEmpty();
	}
	
	
	public void imprimirnovidades() {
		for(Novidade nov : novidades.keySet()) {
			System.out.println(nov);
		}
		
		
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ControleDeNovidade CN = new ControleDeNovidade();
		Node_LS n= new S_LS();
		System.out.println("xxxxxxxxxxxxxxxx");
		CN.imprimirnovidades();
		CN.add(new Nov0(0,0,0,0,0,0,0),n);
		System.out.println("xxxxxxxxxxxxxxxx");
		CN.imprimirnovidades();
		CN.add(new Nov0(0,0,0,0,0,0,0),n);
		System.out.println("xxxxxxxxxxxxxxxx");
		CN.imprimirnovidades();
		CN.add(new Nov0(0,0,0,1,0,0,0),n);
		System.out.println("xxxxxxxxxxxxxxxx");
		CN.imprimirnovidades();
		CN.add(new Nov0(1,0,0,1,0,0,0),n);
		System.out.println("xxxxxxxxxxxxxxxx");
		CN.imprimirnovidades();
		CN.add(new Nov0(0,0,0,1,0,0,1),n);
		System.out.println("xxxxxxxxxxxxxxxx");
		CN.imprimirnovidades();
		CN.pop();
		System.out.println("xxxxxxxxxxxxxxxx");
		CN.imprimirnovidades();
		
	}
	
	

}
