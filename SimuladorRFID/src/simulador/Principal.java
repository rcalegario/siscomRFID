package simulador;

public class Principal {

	public static void main(String[] args) {

		long t0 = System.currentTimeMillis();
		System.out.println(t0);

		Plot plot = new Plot();

		Simulador lowerbound = new Simulador("lowerbound", 100, 100, 1000, 2000, 64);
		System.out.println(lowerbound.toString());

		System.out.println("tempo lowerbound: " + (System.currentTimeMillis()-t0));

		Simulador eomlee = new Simulador("eom-lee", 100, 100, 1000, 2000, 64);
		System.out.println(eomlee.toString());
		System.out.println("tempo eomlee: " + (System.currentTimeMillis()-t0));

		Simulador algQ = new Simulador("q", 100, 100, 1000, 2000, 64);
		System.out.println(algQ.toString());
		System.out.println("tempo q: " + (System.currentTimeMillis()-t0));

		Simulador[] simuladores = {lowerbound, eomlee};
		plot.grafico(simuladores,"slots");
		plot.grafico(simuladores,"colisao");
		plot.grafico(simuladores,"vazio");

		Simulador[] simuladoreQ = {algQ};
		plot.grafico(simuladoreQ,"slotsq");
		
		Simulador[] simuladorTempo = {lowerbound, eomlee, algQ};
		plot.grafico(simuladorTempo,"tempo");
		
		System.out.println("tempo final: " + (System.currentTimeMillis()-t0));

	}

}
