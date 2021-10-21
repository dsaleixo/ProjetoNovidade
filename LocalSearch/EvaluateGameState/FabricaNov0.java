package EvaluateGameState;

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

}
