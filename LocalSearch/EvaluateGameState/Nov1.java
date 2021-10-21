package EvaluateGameState;

import java.util.Random;

public class Nov1 implements Novidade {
	int v[]= {0,0,0,0,0,0,0};
	
	public Nov1() {
		// TODO Auto-generated constructor stub
		
		for(int i=0;i<7;i++) {
			v[i]=0;
		}
	}

	public Nov1(CabocoDagua2 cd) {
		// TODO Auto-generated constructor stub
		
		v[0]=cd.getWorker();
		v[1]=cd.getLight();
		v[2]=cd.getRanged();
		v[3]=cd.getHeavy();
		v[4]=cd.getBase();
		v[5]=cd.getBarrack();
		v[6]=cd.getSaved_resource();
		
	}
	
	public String toString() {
	
	
		return this.v[0]+" "+this.v[1]+" "+this.v[2]+" "+this.v[3]+" "+this.v[4]+" "+this.v[5]+" "+this.v[6];
	}
	
	 @Override
	    public int hashCode() {
	        int code =0;
	        code+= this.v[0];
	        code+= 20*this.v[1];
	        code+= 400*this.v[2];
	        code+= 8000*this.v[3];
	        code+= 160000*this.v[4];
	        code+= 3200000*this.v[5];
	        code+= 64000000*this.v[6];
	       return code;
	    }

	 @Override
	    public boolean equals(Object obj) {
		 Nov1 aux =  (Nov1)obj;
		 
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
		Nov1 nov1 = new Nov1();
		for(int i=0;i<7;i++) {
			nov1.v[i]=this.v[i];
		}
		return nov1;
	}

	@Override
	public void mutacao() {
		// TODO Auto-generated method stub
		Random r =new Random();
		
		int n = r.nextInt(5)+1;
		
		for(int i=0;i<n;i++) {
			int c = r.nextInt(7);
			int valor = Math.max(1, (this.v[c]+1)/2);
			int sinal;
			if(r.nextInt(4)<3)sinal=1;
			else sinal=-1;
			v[c]+=sinal*valor;
			if(v[c]<0)v[c]=0;
			if(v[c]>19)v[c]=19;
			if(r.nextFloat()<0.05) {
				if(r.nextInt(2)==0)v[c]=0;
				else v[c]=19;
			}
		}
		
	}

	@Override
	public double semelhaca(Novidade n) {
		// TODO Auto-generated method stub
		
		
		Nov1 aux =  (Nov1)n;
		
		double cont=0;
		for(int i=0;i<7;i++) {
			cont+=1-(1.0*Math.abs( this.v[i]-aux.v[i])) /Math.max(Math.max(this.v[i],aux.v[i]),1);
		}
		
	
		return cont/7;
	}

	@Override
	public int compareTo(Novidade n) {
		// TODO Auto-generated method stub
		return 0;
	}

}
