package simulador;

public static class Estimation {

  public static int lowerbound(int colisoes){
		return colisoes * 2;
	}

	public static int eomlee(double colisoes, double sucessos, double tamanho_quadro){
		double limiar =  0.001;
		double gama_atual = 0;
		double gama_anterior = 2;

		boolean parar = false;

		do{
      
			double betaK = tamanho_quadro/(gama_anterior*colisoes + sucessos);

			double e = Math.exp(-(1/betaK));
			double a = 1 - e;
			double eb = (1 + (1/betaK))*e;
			double b = betaK * (1 - eb);
			gama_atual = a/b;

			if(Math.abs(gama_anterior - gama_atual) < limiar) parar = true;
			gama_anterior = gama_atual;

		}while(!parar);

		return (int) Math.ceil(gama_atual * colisoes);
	}
}
