package EvaluateGameState;

import java.util.Random;

public class Nov0 implements Novidade {

	Boolean v[]= {false,false,false,false,false,false,false};
	/* Ordem
	  worker
	light
	ranged
	heavy
	base
	barrack
	saved_resource
	*/
	public Nov0(CabocoDagua2 cd) {
		// TODO Auto-generated constructor stub
		
		if(cd.getWorker()>0)v[0]=true;
		if(cd.getLight()>0)v[1]=true;
		if(cd.getRanged()>0)v[2]=true;
		if(cd.getHeavy()>0)v[3]=true;
		if(cd.getBase()>0)v[4]=true;
		if(cd.getBarrack()>0)v[5]=true;
		if(cd.getSaved_resource()>0)v[6]=true;
		
	}
	
	public Nov0(int w, int l, int r,int h,int ba,int br,int re) {
		// TODO Auto-generated constructor stub
		
		if(w>0)v[0]=true;
		if(l>0)v[1]=true;
		if(r>0)v[2]=true;
		if(h>0)v[3]=true;
		if(ba>0)v[4]=true;
		if(br>0)v[5]=true;
		if(re>0)v[6]=true;
		
	}

	@Override
	public int compareTo(Novidade n) {
		// TODO Auto-generated method stub
		Nov0 aux = (Nov0)n;
	
		if(this.v[0]!=aux.v[0] && v[0])return -1;
		else if(this.v[0]!=aux.v[0] && aux.v[0])return 1;
		
		if(this.v[1]!=aux.v[1] && v[1])return -1;
		else if(this.v[1]!=aux.v[1] && aux.v[1])return 1;
		
		if(this.v[2]!=aux.v[2] && v[2])return -1;
		else if(this.v[2]!=aux.v[2] && aux.v[2])return 1;
		
		if(this.v[3]!=aux.v[3] && v[3])return -1;
		else if(this.v[3]!=aux.v[3] && aux.v[3])return 1;
		
		if(this.v[4]!=aux.v[4] && v[4])return -1;
		else if(this.v[4]!=aux.v[4] && aux.v[4])return 1;
		
		if(this.v[5]!=aux.v[5] && v[5])return -1;
		else if(this.v[5]!=aux.v[5] && aux.v[5])return 1;
		
		if(this.v[6]!=aux.v[6] && v[6])return -1;
		else if(this.v[6]!=aux.v[6] && aux.v[6])return 1;

		return 0;
	}
	
	public String toString() {
		int w=0;
		int l=0;
		int r=0;
		int h=0;
		int ba=0;
		int br=0;
		int re=0;
		
		if(v[0])w=1;
		if(v[1])l=1;
		if(v[2])r=1;
		if(v[3])h=1;
		if(v[4])ba=1;
		if(v[5])br=1;
		if(v[6])re=1;
		return w+""+l+""+r+""+h+""+ba+""+br+""+re;
	}
	
	 @Override
	    public int hashCode() {
	        int code =0;
	        if(v[0])code+=1;
	        if(v[1])code+=2;
	        if(v[2])code+=4;
	        if(v[3])code+=8;
	        if(v[4])code+=16;
	        if(v[5])code+=32;
	        if(v[6])code+=64;
	    
	       return code;
	    }

	 @Override
	    public boolean equals(Object obj) {
		 Nov0 aux =  (Nov0)obj;
		 
		 if(this.v[0]!=aux.v[0] )return false;
		 if(this.v[1]!=aux.v[1] )return false;
		if(this.v[2]!=aux.v[2] )return false;
		if(this.v[3]!=aux.v[3] )return false;
		if(this.v[4]!=aux.v[4] )return false;
		if(this.v[5]!=aux.v[5]) return false;
		if(this.v[6]!=aux.v[6]) return false;
	
		return true;
	    }

	@Override
	public Novidade Clone() {
		// TODO Auto-generated method stub
		
		Nov0 clone = new Nov0(0,0,0,0,0,0,0);
		for(int i =0;i<7;i++) clone.v[i]= this.v[i];
		return clone;
	}

	@Override
	public void mutacao() {
		// TODO Auto-generated method stub
		Random r =new Random();
		for(int i=0;i<2;i++) {
			int n = r.nextInt(7);
			int b = r.nextInt(4);
			if(b<3)this.v[n]= ! this.v[n];
		
		}
		
	}

	@Override
	public double semelhaca(Novidade n) {
		// TODO Auto-generated method stub
		Nov0 aux = (Nov0)n;
		double cont = 0;
		for(int i =0;i<7;i++) {
			if(this.v[i]==true&&aux.v[i]==true)cont+=1;
			if(this.v[i]==false&&aux.v[i]==false)cont+=1;
		}
		
		
		return cont/7;
	}

	
}
