package simulador;

import java.io.FileWriter;
import java.io.IOException;

public class Plot {

	public Plot(){

	}

	public void escrever_arquivo(String nome_arquivo, String corpo_arquivo){
		try {
			FileWriter writer = new FileWriter(nome_arquivo);

			writer.write(corpo_arquivo);

			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void grafico(Simulador[] simulador,String tipo){
		String nome_arquivo = "";

		String xLabel = "set xlabel \"Número de etiquetas\"\n";
		String yLabel = "";
		switch(tipo){
		case("slots"):
			nome_arquivo = "script_grafico_total_slots";
			yLabel = "set ylabel \"Número total de slots\"\n";
			break;
		case("colisao"):
			nome_arquivo = "script_grafico_total_colisao";
			yLabel = "set ylabel \"Número total de slots em colisão\"\n";
			break;
		case("vazio"):
			nome_arquivo = "script_grafico_total_vazio";
			yLabel = "set ylabel \"Número total de slots vázio\"\n";
			break;
		case("tempo"):
			nome_arquivo = "script_grafico_tempo_medio";
			yLabel = "set ylabel \"Tempo de Simulação\"\n";
			break;
		}

		String setup1 = "set key vertical top left\n";
		String setup2 = "set grid\n";
		String setup3 = "set pointsize 2\n";
		String setup4 = "set terminal png\n";
		String setup5 = "set output './results/" + tipo + ".png'\n";
		String setup6 = "set monochrome\n";
		String setup7 = "";

		String plots = "plot ";
		for (int i = 0; i < simulador.length; i++) {
			if(simulador[i].estimador.equals("q")) {
				if(tipo.equals("slots")) nome_arquivo = "script_q_grafico_total_slots";
				setup5 = "set output './results/" + tipo + "q.png'\n";
			}
			plots += "\"" + simulador[i].nome_arquivo + "\" u 1:";
			switch(tipo){
			case("slots"):
				plots += "2 ";
				break;
			case("colisao"):
				plots += "3 ";
				break;
			case("vazio"):
				plots += "4 ";
				setup7 = "set yrange [0:1100]\n";
				break;
			case("tempo"):
				plots += "5 ";
				setup7 = "set logscale y\n";
				break;
			}
			if(i == simulador.length - 1) {
				plots += "t \'" + simulador[i].estimador + "\' w linespoints pt " + (i+1) + "\n";
			} else {
				plots += "t \'" + simulador[i].estimador + "\' w linespoints pt " + (i+1) + ",";
			}
		}

		String corpo_arquivo = xLabel + yLabel + setup6 + setup1 + setup2 + setup3 + setup4 + setup7 + setup5 + plots;

		this.escrever_arquivo(nome_arquivo+".plt", corpo_arquivo);
	}



}
