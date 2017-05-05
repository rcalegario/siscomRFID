package simulador;

public class Simulation {

  // Input
	public int[] tagCount;
	public int[] slotCount;
	public int[] emptySlotCount;
	public int[] collisionCount;
	public double[] tempo_simulacao;

  public Simulation() {
		Scanner scanner = new Scanner(new File("tall.txt"));
		int [] tall = new int [100];
		int i = 0;
		while(scanner.hasNextInt()){
		   tall[i++] = scanner.nextInt();
		}

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
  }


  public String toString(){
		String retorno =  "ESTIMADOR: " + this.estimador;

		retorno += "\nQTD ETIQUETAS\t";
		for (int i = 0; i < tagCount.length; i++) {
			retorno += this.tagCount[i] + "\t";
		}

		retorno += "\nTOTAL SLOTS\t";
		for (int i = 0; i < tagCount.length; i++) {
			retorno += this.slotCount[i] + "\t";
		}

		retorno += "\nSLOTS VAZIO\t";
		for (int i = 0; i < tagCount.length; i++) {
			retorno += this.emptySlotCount[i] + "\t";
		}

		retorno += "\nSLOTS COLISAO\t";
		for (int i = 0; i < tagCount.length; i++) {
			retorno += this.collisionCount[i] + "\t";
		}

		retorno += "\nTEMPO SIMULACAO\t";
		for (int i = 0; i < tagCount.length; i++) {
			retorno += this.tempo_simulacao[i] + "\t";
		}

		return retorno + "\n";
	}

  private void simulator(int posicao){

		Random ran = new Random();
		int slotCount = 0;
		int tan_quadro = this.quadro_inicial;
		int tagCount = this.tagCount[posicao];

		int colisao = 0, sucesso = 0, vazio = 0;

		do{
			colisao = 0; sucesso = 0; vazio = 0;
			System.out.println("quantidade de slots: " + slotCount);
			slotCount += tan_quadro;

			int[] quadro = new int[tan_quadro];
			for (int i = 0; i < tagCount; i++) {
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
					tan_quadro = Estimator.lowerbound(colisao);
					break;
				case("eom-lee"):
					tan_quadro = Estimator.eomlee(colisao,sucesso,tan_quadro);
					break;
				}
			}

			tagCount -= sucesso;

			this.emptySlotCount[posicao] += vazio;
			this.collisionCount[posicao] += colisao;

		}while(tagCount > 0);

		this.slotCount[posicao] += slotCount;
	}

	private void simulatorQ(int posicao){
		Random ran = new Random();

		int slotCount_usados = 0, slot_obs;
		double qfp = 4.0, cq = 0.3, q;
		int tagCount = this.tagCount[posicao];

		do{
			slotCount_usados++;

			q = Math.round(qfp);
			int quadro = (int) Math.pow(2.0, q);

			slot_obs = 0;
			for (int i = 0; i < tagCount; i++) {
				if(ran.nextInt(quadro) == 0)
					slot_obs++;
			}

			if(slot_obs == 0){
				qfp = Math.max(0,  qfp-cq);
				this.emptySlotCount[posicao]++;
			}else if(slot_obs > 1){
				qfp = Math.min(15, qfp+cq);
				this.collisionCount[posicao]++;
			}else
				tagCount--;

			//System.out.println("slots usado " + slotCount_usados + "\netiquetas obs: " + slot_obs + "\nqfp "+qfp);

		}while(tagCount > 0);

		this.slotCount[posicao] += slotCount_usados;
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

}
