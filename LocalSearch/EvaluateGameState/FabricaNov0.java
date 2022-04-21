package EvaluateGameState;

import java.util.Random;

public class FabricaNov0 implements FabicaDeNovidade {

	public FabricaNov0() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Novidade gerar(CabocoDagua2 cd) {
		// TODO Auto-generated method stub
		return new Nov0(cd);
	}

	@Override
	public Novidade gerar() {
		// TODO Auto-generated method stub
		return new Nov0(0,0,0,0,0,0,0);
	}

	@Override
	public Novidade geraAlet() {
		// TODO Auto-generated method stub
		Random r =new Random();
		
		return new Nov0(r.nextInt(2),
				r.nextInt(2),
				r.nextInt(2),
				r.nextInt(2),
				r.nextInt(2),
				r.nextInt(2),
				r.nextInt(2));
	}

}
