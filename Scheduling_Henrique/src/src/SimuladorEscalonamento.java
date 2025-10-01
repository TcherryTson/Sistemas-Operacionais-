import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimuladorEscalonamento {

    public static void main(String[] args) {

        List<Processo> processos = new ArrayList<>();
        processos.add(new Processo(1, 0, 8, 3));
        processos.add(new Processo(2, 1, 4, 1));
        processos.add(new Processo(3, 2, 9, 4));
        processos.add(new Processo(4, 3, 5, 2));
        processos.add(new Processo(5, 4, 2, 5));

        String resultadoPrioridade = AlgoritmosEscalonamento.prioritySchedulingPreemptive(processos);
        String resultadoMultiplasFilas = AlgoritmosEscalonamento.prioritySchedulingMultipleQueues(processos);

        String nomeArquivo = "resultados_escalonamento.txt";
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            System.out.println("Executando simulação e gravando resultados em '" + nomeArquivo + "'...");

            writer.write(resultadoPrioridade);
            writer.write("\n--------------------------------------------------\n\n");
            writer.write(resultadoMultiplasFilas);

            System.out.println("Resultados gravados com sucesso!");

        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao gravar o arquivo: " + e.getMessage());
        }

        System.out.println("\n--- RESULTADOS DA SIMULACAO ---\n");
        System.out.println(resultadoPrioridade);
        System.out.println("--------------------------------------------------\n");
        System.out.println(resultadoMultiplasFilas);
    }
}
