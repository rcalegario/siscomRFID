package simulador;

public class Principal {

	public static void main(String[] args) {

		long t0 = System.currentTimeMillis();
		System.out.println(t0);

		Plot plot = new Plot();
		
//		System.out.println("Lower Bound");
//		Simulador lowerbound = new Simulador("lowerbound", 100, 100, 1000, 2000, 64);
//		System.out.println(lowerbound.toString());
//		System.out.println("tempo lowerbound: " + ((System.currentTimeMillis()-t0)) + " miliseg\n");
//
//		System.out.println("Eom Lee");
//		Simulador eomlee = new Simulador("eom-lee", 100, 100, 1000, 2000, 64);
//		System.out.println(eomlee.toString());
//		System.out.println("tempo eomlee: " + ((System.currentTimeMillis()-t0)) + " miliseg\n");
//		
//		System.out.println("Chen");
//		Simulador chen = new Simulador("chen", 100, 100, 1000, 2000, 64);
//		System.out.println(chen.toString());
//		System.out.println("tempo chen: " + ((System.currentTimeMillis()-t0)) + " miliseg\n");
		
//		System.out.println("Q");
//		Simulador algQ = new Simulador("q", 100, 100, 1000, 2000, 64);
//		System.out.println(algQ.toString());
//		System.out.println("tempo q: " + (System.currentTimeMillis()-t0) + " miliseg\n");
//		
		System.out.println("Vahedi");
		Simulador vahedi = new Simulador("vahedi", 100, 100, 1000, 2000, 64);
		System.out.println(vahedi.toString());
		System.out.println("tempo vahedi: " + (System.currentTimeMillis()-t0) + " miliseg\n");
		
//		Simulador[] simuladores = {lowerbound, eomlee, chen, vahedi};
//		plot.grafico(simuladores,"slots");
//		plot.grafico(simuladores,"colisao");
//		plot.grafico(simuladores,"vazio");
//		plot.grafico(simuladores,"tempo");
//		
//		Simulador[] simuladoreInteracao = {chen};
//		plot.grafico(simuladoreInteracao,"interacao");
//
//		Simulador[] simuladoreQ = {algQ};
//		plot.grafico(simuladoreQ,"slotsq");
//		
//		Simulador[] simuladorTempo = {lowerbound, eomlee, algQ, chen, vahedi};
//		plot.grafico(simuladorTempo,"tempo");
		
		System.out.println("tempo final: " + (System.currentTimeMillis()-t0));

	}

}
