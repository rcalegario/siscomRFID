package simulador;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Simulador {

	public static int lowerbound(int colisoes){
		return colisoes * 2;
	}
	
	public static int eomlee(double colisoes, double sucessos, double tamanho_quadro){
		double limiar =  0.001;
		double gama_atual = 0;
		double gama_anterior = 2;
		boolean parar = false;
		
		while(!parar){
			
			double betaK = tamanho_quadro/(gama_anterior*colisoes + sucessos);
			
			double e = Math.exp(-(1/betaK));
			double a = 1 - e;
			double eb = (1 + (1/betaK))*e;
			double b = betaK * (1 - eb);
			gama_atual = a/b;
			
			if(Math.abs(gama_anterior - gama_atual) < limiar) parar = true;
			gama_anterior = gama_atual;
		}
		
		return (int) (gama_atual * colisoes);
		
	}
	
	public static int simulacao(int quadro_inicail, int qtd_etiquetas, String estimador){
		int slots = 0;
		boolean colisao = true;
		Random ran = new Random();
		int tamanho_quadro = quadro_inicail;	
		
		while(colisao){
			int[] slots_abertos = new int[tamanho_quadro];
			slots += tamanho_quadro;
			for (int i = 0; i < qtd_etiquetas; i++) {
				int j = ran.nextInt(tamanho_quadro);
				slots_abertos[j]++;
			}
			
			int[] estado = new int[3];// 0-colisao, 1-sucessos, 2-vazio
			for (int i = 0; i < slots_abertos.length; i++) {
				if(slots_abertos[i] > 1) estado[0]++;
				else if(slots_abertos[i] == 1) estado[1]++;
				else estado[2]++;
			}
			
			if(estado[0] > 0){
				switch(estimador){
				case("lowerbound"):
					tamanho_quadro = lowerbound(estado[0]);
				break;
				case("eom-lee"):
					tamanho_quadro = eomlee(estado[0],estado[1],tamanho_quadro);
				break;
				}				
			}
			
			qtd_etiquetas -= estado[1];
			if(estado[0] == 0) colisao = false;
			
			//System.out.println("Colisões:\t" + estado[0] + "\tAcertos:\t" + estado[1] + "\tVazio:\t" + estado[2]);
		}
		
		return slots;
	}
	
	public static int simuladorQ(double cq, int qtd_etiquetas){
		Random ran = new Random();
		int total_slots_usados = 0, slot_obs;
		double qfp = 4.0, q;
		
		do{
			total_slots_usados++;
			
			q = Math.round(qfp);
			int quadro = (int) Math.pow(2.0, q);
			
			slot_obs = 0;
			for (int i = 0; i < qtd_etiquetas; i++) {
				if(ran.nextInt(quadro) == 0) 
					slot_obs++;
			}
			
			if(slot_obs == 0) 
				qfp = Math.max(0,  qfp-cq);
			else if(slot_obs > 1) 
				qfp = Math.min(15, qfp+cq);
			else
				qtd_etiquetas--;
			
			//System.out.println("slots usado " + total_slots_usados + "\netiquetas obs: " + slot_obs + "\nqfp "+qfp);
			
		}while(qtd_etiquetas > 0);
		
		return total_slots_usados;
	}
	
	public static int[] qtd_slots(int total_etiquetas, int incre_etiqueta, int interacao, int slots_iniciais, String estimador, double cp){
		int k = total_etiquetas/incre_etiqueta;
		int[] qtd_totals_slots = new int[k+1];
			
		for (int i = 0; i < qtd_totals_slots.length; i++) {
			
			int qtd_etiquetas = 100*i;
			if(i == 0) qtd_etiquetas = 1; //valor para 1 etiqueta
			System.out.println(qtd_etiquetas);
			
			for (int j = 0; j < interacao; j++) {
				if(cp == 0) qtd_totals_slots[i] += simulacao(slots_iniciais, qtd_etiquetas, estimador);
				else qtd_totals_slots[i] += simuladorQ(cp,qtd_etiquetas);
					
			}				
			
			qtd_totals_slots[i] = qtd_totals_slots[i]/interacao;
		}
		
		return qtd_totals_slots;
	}
	
	public static void main(String[] args) {

		ArrayList<int[]> resultado = new ArrayList<>();
		
		Date date = new Date();
		
		resultado.add(qtd_slots(1000,100,2000,64,"lowerbound",0.0));
		System.out.println(date.toString());
		resultado.add(qtd_slots(1000,100,2000,64,"eom-lee",0.0));
		System.out.println(date.toString());
		resultado.add(qtd_slots(1000,100,2000,0,"q",0.3));
		System.out.println(date.toString());
		
		for (int[] is : resultado) {
			for (int i = 0; i < is.length; i++) {
				System.out.print(is[i] + " ");
			}
			System.out.println();
		}
	}
	
}
