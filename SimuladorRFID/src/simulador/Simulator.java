package simulador;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Simulator {

	// Saidas
	public Simulation simulation;
	public String nome_arquivo;

	public Simulator(){
		this(new Simulation());
	}

	public Simulator() {
		this.estimador = e;
		this.slotCount = new int[tan];
		this.emptySlotCount = new int[tan];
		this.collisionCount = new int[tan];
		this.tempo_simulacao = new double[tan];

		this.nome_arquivo = this.gerar_arquivo();
	}

	public void simular(){
		Thread[] threads = Thread[this.tagCount.length];

		for (int i = 0; i < this.tagCount.length; i++) {
			Thread t = new Thread(new SimulatorThread(i, this));
			threads[i] = t;
			threads[i].start();
		}

		for (int i = 0; i < this.tagCount.length; i++) {
			try {
				threads[i].join();
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.slotCount[i] = this.slotCount[i]/this.repeticao;
			this.emptySlotCount[i] = this.emptySlotCount[i]/this.repeticao;
			this.collisionCount[i] = this.collisionCount[i]/this.repeticao;
			this.tempo_simulacao[i] = this.tempo_simulacao[i]/this.repeticao;
		}
	}

	private static class SimulatorThread implements Runnable {
		private int i;
		private Simulator sim;

		public SimulatorThread(int i, Simulator sim) {
			this.i = i;
			this.sim = sim;
		}

		public void run() {
			System.out.println(sim.tagCount[i]);

			for (int j = 0; j < 1; j++) {
				double inicio = System.currentTimeMillis();

				if(sim.estimador.equalsIgnoreCase("q")) {
					sim.simulator(i);
				} else {
					sim.simulatorQ(i);
				}

				System.out.println("Total slots: " + sim.slotCount[i]);
				System.out.println("Total slots vazio: " + sim.emptySlotCount[i]);
				System.out.println("Total slots colisao: " + sim.collisionCount[i]);

				double fim = System.currentTimeMillis();
				sim.tempo_simulacao[i] += fim-inicio;
			}
		}
	}

	public String gerar_arquivo(){
		String nome_arquivo = estimador + ".txt";
		String corpo_arquivo = "";

		int j = 1;
		if(this.estimador.equals("q")) j = 0;

		for (int i = j; i < tagCount.length; i++) {
			if(i == 0)
				corpo_arquivo.append(tagCount[i] + " " + slotCount[i] + " " + collisionCount[i] + " " + emptySlotCount[i] + " " + tempo_simulacao[i] + "\n");
			else
				corpo_arquivo.append(tagCount[i] + " " + slotCount[i] + " " + collisionCount[i] + " " + emptySlotCount[i] + " " + tempo_simulacao[i] + "\n");
		}

		FileHandling.writeContent(nome_arquivo, corpo_arquivo);

		return nome_arquivo;
	}

}
