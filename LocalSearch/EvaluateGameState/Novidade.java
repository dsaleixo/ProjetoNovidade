package EvaluateGameState;

public interface Novidade {
	int compareTo(Novidade n);
    public int hashCode();
    Novidade Clone();
    void mutacao();
    double semelhaca(Novidade n);
}
