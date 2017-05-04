package simulador;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Simulador {

	// Entradas
	public String estimador;
	private int qtd_inicial_etiquetas;
	private int incremeto;
	private int max_etiquetas;
	private int repeticao;
	private int quadro_inicial;

	// Saidas
	public int[] qtd_etiquetas;
	public int[] total_slots;
	public int[] total_slots_vazio;
	public int[] total_slots_colisao;
	public double[] tempo_simulacao;
	public String nome_arquivo;

	public Simulador(String e, int qie, int i, int me, int r, int qi){
		this.estimador = e;
		this.qtd_inicial_etiquetas = qie;
		this.incremeto = i;
		this.max_etiquetas = me;
		this.repeticao = r;
		this.quadro_inicial = qi;

		int tan = (this.max_etiquetas/this.incremeto)+1;
		this.qtd_etiquetas = new int[tan];
		for (int j = 0; j < qtd_etiquetas.length; j++) {
			if(j == 0) qtd_etiquetas[j] = 1;
			else qtd_etiquetas[j] = this.qtd_inicial_etiquetas + this.incremeto * (j-1);
		}
		this.total_slots = new int[tan];
		this.total_slots_vazio = new int[tan];
		this.total_slots_colisao = new int[tan];
		this.tempo_simulacao = new double[tan];

		this.simular();

		this.nome_arquivo = this.gerar_arquivo();
	}

	private int lowerbound(int colisoes){
		return colisoes * 2;
	}

	private int eomlee(double colisoes, double sucessos, double tamanho_quadro){
		double limiar =  0.001;
		double gama_atual = 0;
		double gama_anterior = 2;

		do{

			double betaK = tamanho_quadro/(gama_anterior*colisoes + sucessos);

			double e = Math.exp(-(1/betaK));
			double a = 1 - e;
			double eb = (1 + (1/betaK))*e;
			double b = betaK * (1 - eb);
			gama_atual = a/b;

			gama_anterior = gama_atual;

		}while(Math.abs(gama_anterior - gama_atual) >= limiar);

		return (int) (gama_atual * colisoes);

	}

	private void simulador(int posicao){

		Random ran = new Random();
		int total_slots = 0;
		int quadro = this.quadro_inicial;
		int qtd_etiquetas = this.qtd_etiquetas[posicao];

		int colisao = 0, sucesso = 0, vazio = 0;

		do{
			colisao = 0; sucesso = 0; vazio = 0;

			total_slots += quadro;

			int[] slots_abertos = new int[quadro];
			for (int i = 0; i < qtd_etiquetas; i++) {
				int n = ran.nextInt(quadro); //slot em que a etiqueta irá responder
				slots_abertos[n]++;
			}

			for (int i = 0; i < slots_abertos.length; i++) {
				if(slots_abertos[i] > 1) {
					colisao++;
				} else if(slots_abertos[i] == 1){
					sucesso++;
				} else {
					vazio++;
				}
			}

			// escolha do estimador
			if(colisao > 0){
				switch(this.estimador){
				case("lowerbound"):
					quadro = lowerbound(colisao);
					break;
				case("eom-lee"):
					quadro = eomlee(colisao,sucesso,quadro);
					break;
				}
			}

			qtd_etiquetas -= sucesso;

			this.total_slots_vazio[posicao] += vazio;
			this.total_slots_colisao[posicao] += colisao;

			//System.out.println("Colisões:\t" + estado[0] + "\tAcertos:\t" + estado[1] + "\tVazio:\t" + estado[2]);
		}while(colisao != 0);

		this.total_slots[posicao] += total_slots;
	}

	private void simuladorQ(int posicao){
		Random ran = new Random();

		int total_slots_usados = 0, slot_obs;
		double qfp = 4.0, cq = 0.3, q;
		int qtd_etiquetas = this.qtd_etiquetas[posicao];

		do{
			total_slots_usados++;

			q = Math.round(qfp);
			int quadro = (int) Math.pow(2.0, q);

			slot_obs = 0;
			for (int i = 0; i < qtd_etiquetas; i++) {
				if(ran.nextInt(quadro) == 0)
					slot_obs++;
			}

			if(slot_obs == 0){
				qfp = Math.max(0,  qfp-cq);
				this.total_slots_vazio[posicao]++;
			}else if(slot_obs > 1){
				qfp = Math.min(15, qfp+cq);
				this.total_slots_colisao[posicao]++;
			}else
				qtd_etiquetas--;

			//System.out.println("slots usado " + total_slots_usados + "\netiquetas obs: " + slot_obs + "\nqfp "+qfp);

		}while(qtd_etiquetas > 0);

		this.total_slots[posicao] += total_slots_usados;
	}

	private static class ThreadSimuladora implements Runnable {
		private int i;
		private Simulador sim;

		public ThreadSimuladora(int i, Simulador sim) {
			this.i = i;
			this.sim = sim;
		}

		public void run() {
			for (int j = 0; j < sim.repeticao; j++) {
				double inicio = System.currentTimeMillis();

				if(!sim.estimador.equalsIgnoreCase("q"))
					sim.simulador(i);
				else
					sim.simuladorQ(i);

				double fim = System.currentTimeMillis();
				sim.tempo_simulacao[i] += fim-inicio;
			}
		}
	}

	private void simular(){
		Thread[] threads = new Thread[this.qtd_etiquetas.length];

		for (int i = 0; i < this.qtd_etiquetas.length; i++) {
			Thread t = new Thread(new ThreadSimuladora(i, this));
			threads[i] = t;
			threads[i].start();
		}

		for (int i = 0; i < this.qtd_etiquetas.length; i++) {
			try {
				threads[i].join();
			} catch(Exception e) {
				e.printStackTrace();
			}

			this.total_slots[i] = this.total_slots[i]/this.repeticao;
			this.total_slots_vazio[i] = this.total_slots_vazio[i]/this.repeticao;
			this.total_slots_colisao[i] = this.total_slots_colisao[i]/this.repeticao;
			this.tempo_simulacao[i] = this.tempo_simulacao[i]/this.repeticao;
		}

	}

	public String gerar_arquivo(){

		String nome_arquivo = estimador + "_" + qtd_inicial_etiquetas + "_" + incremeto + "_"+ max_etiquetas + "_"+ repeticao + "_"+ quadro_inicial + ".txt";

		try {
			FileWriter writer = new FileWriter(nome_arquivo);

			for (int i = 0; i < qtd_etiquetas.length; i++) {
				if(i == 0)
					writer.write(qtd_etiquetas[i] + " " + total_slots[i] + " " + total_slots_colisao[i] + " " + total_slots_vazio[i] + " " + tempo_simulacao[i] + "\n");
				else
					writer.append(qtd_etiquetas[i] + " " + total_slots[i] + " " + total_slots_colisao[i] + " " + total_slots_vazio[i] + " " + tempo_simulacao[i] + "\n");
			}

			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return System.getProperty("user.dir") + "/" + nome_arquivo;
	}

	public String toString(){
		String retorno =  "ESTIMADOR: " + this.estimador;

		retorno += "\nQTD ETIQUETAS\t";
		for (int i = 0; i < qtd_etiquetas.length; i++) {
			retorno += this.qtd_etiquetas[i] + "\t";
		}

		retorno += "\nTOTAL SLOTS\t";
		for (int i = 0; i < qtd_etiquetas.length; i++) {
			retorno += this.total_slots[i] + "\t";
		}

		retorno += "\nSLOTS VAZIO\t";
		for (int i = 0; i < qtd_etiquetas.length; i++) {
			retorno += this.total_slots_vazio[i] + "\t";
		}

		retorno += "\nSLOTS COLISAO\t";
		for (int i = 0; i < qtd_etiquetas.length; i++) {
			retorno += this.total_slots_colisao[i] + "\t";
		}

		retorno += "\nTEMPO SIMULACAO\t";
		for (int i = 0; i < qtd_etiquetas.length; i++) {
			retorno += this.tempo_simulacao[i] + "\t";
		}

		return retorno + "\n";
	}

}
