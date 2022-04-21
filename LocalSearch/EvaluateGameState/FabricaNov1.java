package EvaluateGameState;

import java.util.Random;

public class FabricaNov1 implements FabicaDeNovidade {

	public FabricaNov1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Novidade gerar(CabocoDagua2 cd) {
		// TODO Auto-generated method stub
		return new Nov1(cd);
	}

	@Override
	public Novidade gerar() {
		// TODO Auto-generated method stub
		return new Nov1();
	}

	@Override
	public Novidade geraAlet() {
		// TODO Auto-generated method stub
		Random r =new Random();
		
		return new Nov0(r.nextInt(10),
				r.nextInt(10),
				r.nextInt(10),
				r.nextInt(10),
				r.nextInt(10),
				r.nextInt(10),
				r.nextInt(10));
	}

	@Override
	public Novidade gerar(String novS) {
		// TODO Auto-generated method stub
		String dados[] = novS.split(" ");
		int w= Integer.parseInt(dados[0].substring(0, dados[0].length()-1));
		int l=Integer.parseInt(dados[1].substring(0, dados[1].length()-1));;
		int r=Integer.parseInt(dados[2].substring(0, dados[2].length()-1));;
		int h=Integer.parseInt(dados[3].substring(0, dados[3].length()-1));;
		int ba=Integer.parseInt(dados[4].substring(0, dados[4].length()-2));;
		int br=Integer.parseInt(dados[5].substring(0, dados[5].length()-2));;
		int re=Integer.parseInt(dados[6].substring(0, dados[6].length()-2));;;
		return this.gerar( new CabocoDagua2(w,l,r,h,ba,br,re));
		
		
	}

}
