package simulador;

public class Principal {
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		
		Simulador lowerbound = new Simulador("lowerbound", 100, 100, 1000, 2000, 64);
		System.out.println(lowerbound.toString());
		
		Simulador eomlee = new Simulador("eom-lee", 100, 100, 1000, 2000, 64);
		System.out.println(eomlee.toString());
		
		Simulador algQ = new Simulador("q", 100, 100, 1000, 2000, 64);
		System.out.println(algQ.toString());
		
		System.out.println(System.currentTimeMillis());
	}
	
}
