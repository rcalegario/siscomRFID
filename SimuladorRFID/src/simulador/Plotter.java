package simulador;

import java.io.FileWriter;
import java.io.IOException;

public class Plotter {

	private String[] estimadores;

	public Plotter(String[] estimadores) {
		this.estimadores = estimadores;
	}

	public void graficoTipo(String tipo){
		String xLabel = "set xlabel \"Número de etiquetas\"\n";
		String corpo_arquivo = xLabel + getYLabel(tipo) + getSetup(tipo) + getPlots(estimadores, tipos);

		this.escrever_arquivo(getFileName(tipo)+".plt", corpo_arquivo);
	}

	public void plotarGraficos() {
		graficoTipo("slots");
		graficoTipo("slotsq");
		graficoTipo("colisao");
		graficoTipo("vazio");
		graficoTipo("tempo");
	}

	public void getSetup(String tipo) {
		String setup = "set key vertical top left\n";
		setup += "set grid\n";
		setup += "set pointsize 2\n";
		setup += "set terminal png\n";
		setup += "set output './results/" + tipo + ".png'\n";
		setup += "set monochrome\n";
		if(tipo.equalsIgnoreCase("vazio"))
			setup += "set yrange [0:1100]\n";
		else if(tipo.equalsIgnoreCase("tempo")) {
			setup += "set logscale y\n";
		}
		return setup;
	}

	public String getFileName(String tipo) {
		switch(tipo) {
			case("slots"):
				return nome_arquivo = "script_grafico_total_slots";
			case("slotsq"):
				return nome_arquivo = "script_q_grafico_total_slots";
			case("colisao"):
				return nome_arquivo = "script_grafico_total_colisao";
			case("vazio"):
				return nome_arquivo = "script_grafico_total_vazio";
			case("tempo"):
				return nome_arquivo = "script_grafico_tempo_medio";
			default:
			 	return "";
		}
	}

	public String getYLabel(String tipo) {
		switch(tipo) {
			case("slots"):
				return "set ylabel \"Número total de slots\"\n";
			case("slotsq"):
				return "set ylabel \"Número total de slots\"\n";
			case("colisao"):
				return "set ylabel \"Número total de slots em colisão\"\n";
			case("vazio"):
				return "set ylabel \"Número total de slots vazio\"\n";
			case("tempo"):
				return "set ylabel \"Tempo de Simulação\"\n";
			default:
			 	return "";
		}
	}

	public String getPlots(String tipo) {
		String plots = "plot ";
		for (int i = 0; i < Simulator.length; i++) {
			plots += "\"" + nomes[i] + "\" u 1:";
			switch(tipo){
			case("slots"):
				plots += "2 ";
				break;
			case("colisao"):
				plots += "3 ";
				break;
			case("vazio"):
				plots += "4 ";
				break;
			case("tempo"):
				plots += "5 ";
				break;
			}
			if(i == Simulator.length - 1) {
				plots += "t \'" + Simulator[i].estimador + "\' w linespoints pt " + (i+1) + "\n";
			} else {
				plots += "t \'" + Simulator[i].estimador + "\' w linespoints pt " + (i+1) + ",";
			}
		}
		return plots;
	}

	public String getSlotsString() {
			String xLabel = "set xlabel \"Número de etiquetas\"\n";
			String yLabel = "set ylabel \"Número total de slots\"\n";
			String setup = getSetup();

	}

}
