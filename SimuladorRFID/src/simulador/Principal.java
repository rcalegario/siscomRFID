package simulador;

public class Principal {
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		
		Plot plot = new Plot();
		
		Simulador lowerbound = new Simulador("lowerbound", 100, 100, 1000, 2000, 64);
		System.out.println(lowerbound.toString());
		
		Simulador eomlee = new Simulador("eom-lee", 100, 100, 1000, 2000, 64);
		System.out.println(eomlee.toString());
		
		Simulador[] simuladores = {lowerbound, eomlee};
		plot.grafico(simuladores,"slots");
		plot.grafico(simuladores,"colisao");
		plot.grafico(simuladores,"vazio");
		
//		Simulador algQ = new Simulador("q", 100, 100, 1000, 2000, 64);
//		System.out.println(algQ.toString());
		
		System.out.println(System.currentTimeMillis());
	}
	
}
