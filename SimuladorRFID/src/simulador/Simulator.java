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

	public String gerar_arquivo(){
		String nome_arquivo = estimador + ".txt";

		try {
			FileWriter writer = new FileWriter(nome_arquivo);

			int j = 1;
			if(this.estimador.equals("q")) j = 0;

			for (int i = j; i < tagCount.length; i++) {
				if(i == 0)
					writer.write(tagCount[i] + " " + slotCount[i] + " " + collisionCount[i] + " " + emptySlotCount[i] + " " + tempo_simulacao[i] + "\n");
				else
					writer.append(tagCount[i] + " " + slotCount[i] + " " + collisionCount[i] + " " + emptySlotCount[i] + " " + tempo_simulacao[i] + "\n");
			}

			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return nome_arquivo;
	}

}
