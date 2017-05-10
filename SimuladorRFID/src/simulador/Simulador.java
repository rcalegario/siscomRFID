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
	public int[] total_interacao;
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
		this.total_interacao = new int[tan];

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

	private int chen(int E, int S, int C, int posicao){
		int L = E + S + C;
		int n =  S + 2*C;
		double next = 0;
		double previous = -1;
		
		//System.out.println(E +  " " + S +  " " + C );
		
		while(previous < next){
			double pe = Math.pow((1 - (1/L)), n);
			double ps = (n/L) * Math.pow((1 - (1/L)),n-1);
			double pc = 1 - pe - ps;
			previous = next;
			double a = (fac(L)/fac(E)*fac(S)*fac(C));
			next = a*Math.pow(pe, E)*Math.pow(ps, S)*Math.pow(pc, C);
			n++;
			this.total_interacao[posicao]++;
		}
		
		return n - 2;
	}
	
	private int vahedi(int E, int S, int C){
		int L = E + S + C;
		int n =  S + 2*C;
		double next = 0;
		double previous = -1;
		
		//System.out.println(E +  " " + S +  " " + C );
		
		while (previous < next) {
			double p1 = Math.pow((1 - (E/L)), n);
			double x = (fac(n)/(fac(S)*fac(n-S)));
			double y = Math.pow((L-E-S), (n-S)) / Math.pow((L-E), n);
			double p2 = x*y*fac(S);
			double p3 = 0;

			for(int k = 0; k < C; k++) {
				for(int v = 0; v < C - k; v++) {
					double a = Math.pow(-1, k+v);
					double b = fac(C)/(fac(k)*fac(C-k));
					double c = fac(C-k)/(fac(v)*fac(C-k-v));
					double d = fac(n-S)/fac(n-S-k);
					double e = Math.pow((C-k-v), (n-S-k))/Math.pow(C, (n-S));
					p3 = p3 + a * b * c * d * e;
				}
			}

			previous = next;
			next = (fac(L)/fac(E)*fac(S)*fac(C))*p1*p2*p3;
			n = n + 1;
		}
		
		return n - 2;
	}
	
	private double fac(int num) {
		double fac = 1;
		for(int i = 1; i <= num; i++) {
			fac *= i;
		}
		return fac;
	}
	
	private void simulador(int posicao){

		Random ran = new Random();
		int total_slots = 0;
		int tan_quadro = this.quadro_inicial;
		int qtd_etiquetas = this.qtd_etiquetas[posicao];

		int colisao = 0, sucesso = 0, vazio = 0;

		do{
			colisao = 0; sucesso = 0; vazio = 0;

			total_slots += tan_quadro;

			int[] quadro = new int[tan_quadro];
			for (int i = 0; i < qtd_etiquetas; i++) {
				int n = ran.nextInt(tan_quadro); //slot em que a etiqueta irá responder
				quadro[n]++;
			}

			for (int i = 0; i < quadro.length; i++) {
				if(quadro[i] > 1) {
					colisao++;
				} else if(quadro[i] == 1){
					sucesso++;
				} else {
					vazio++;
				}
			}

			// escolha do estimador
			if(colisao > 0){
				switch(this.estimador){
				case("lowerbound"):
					tan_quadro = lowerbound(colisao);
				break;
				case("eom-lee"):
					tan_quadro = eomlee(colisao,sucesso,tan_quadro);
				break;
				case("chen"):
					tan_quadro = chen(vazio,sucesso,colisao,posicao) - sucesso;
				break;
				case("vahedi"):
					tan_quadro = vahedi(vazio,sucesso,colisao) - sucesso;
				break;
				}
			}

			qtd_etiquetas -= sucesso;

			this.total_slots_vazio[posicao] += vazio;
			this.total_slots_colisao[posicao] += colisao;

		}while(qtd_etiquetas > 0);

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

	private void simular(){

		for (int i = 0; i < this.qtd_etiquetas.length; i++) {
			
			System.out.println(qtd_etiquetas[i]);
			
			for (int j = 0; j < this.repeticao; j++) {
				double inicio = System.currentTimeMillis();

				if(!this.estimador.equalsIgnoreCase("q")) 
					simulador(i);
				else
					simuladorQ(i);

				double fim = System.currentTimeMillis();
				this.tempo_simulacao[i] += fim-inicio;
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

			int j = 1;
			if(this.estimador.equals("q")) j = 0;

			for (int i = j; i < qtd_etiquetas.length; i++) {
				if(i == 0){
					writer.write(qtd_etiquetas[i] + " " + total_slots[i] + " " + total_slots_colisao[i] + " " + total_slots_vazio[i] + " " + tempo_simulacao[i]);
					if(this.estimador.equals("chen")) writer.append(" " + total_interacao[i] + "\n");
					else  writer.append("\n");
				}else{
					writer.append(qtd_etiquetas[i] + " " + total_slots[i] + " " + total_slots_colisao[i] + " " + total_slots_vazio[i] + " " + tempo_simulacao[i]);
					if(this.estimador.equals("chen")) writer.append(" " + total_interacao[i] + "\n");
					else  writer.append("\n");
			
				}
			}

			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return nome_arquivo;
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
		
		retorno += "\nTOTAL INTERACAO\t";
		for (int i = 0; i < qtd_etiquetas.length; i++) {
			retorno += this.total_interacao[i] + "\t";
		}

		return retorno;
	}

}
