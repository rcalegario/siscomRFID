package simulador;

public static class FileHandling {

  public static void writeContent(String nome_arquivo, String corpo_arquivo) {
    try {
      FileWriter writer = new FileWriter(nome_arquivo);
      writer.write(corpo_arquivo);
      writer.flush();
      writer.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
