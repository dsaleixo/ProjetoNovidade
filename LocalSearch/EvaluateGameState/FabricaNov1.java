package EvaluateGameState;

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

}
