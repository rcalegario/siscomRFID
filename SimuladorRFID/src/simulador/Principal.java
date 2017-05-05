package simulador;

public class Principal {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		runSimulations(getEstimatorList(args));
		Plot.plotarGraficos();

		println(System.currentTimeMillis() - startTime);
	}

	public Simulator[] getEstimatorList(args) {
		if(args.length > 1) {
			return args.slice(1);
		} else {
			return ["lowerbound", "eomlee", "algQ"];
		}
	}

	public void runSimulations(Simulator[] estimators) {
		for(Simulator estimator : estimators) {
			Simulator Simulator = new Simulator(estimator, 100, 100, 1000, 2000, 64);
			System.out.println(Simulator.simular());
		}
	}

/*
	public class SimulationThread implements Runnable {
		private String estimator;

		public SimulationThread(estimator) {
			this.simulator = simulators;
		}

		public void run() {
			Simulator Simulator = new Simulator(estimator, 100, 100, 1000, 2000, 64);
			System.out.println(Simulator.simular());
		}
	}
*/
}
