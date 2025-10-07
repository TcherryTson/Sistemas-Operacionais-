import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao simulador de gerenciamento de memória!");
        System.out.print("Por favor, digite o tamanho da memória em blocos (ex: 32): ");
        int tamanhoMemoria = scanner.nextInt();

        List<Processo> processos = new ArrayList<>();
        processos.add(new Processo("P1", 5));
        processos.add(new Processo("P2", 4));
        processos.add(new Processo("P3", 2));
        processos.add(new Processo("P4", 5));
        processos.add(new Processo("P5", 8));
        processos.add(new Processo("P6", 3));
        processos.add(new Processo("P7", 5));
        processos.add(new Processo("P8", 8));
        processos.add(new Processo("P9", 2));
        processos.add(new Processo("P10", 6));

        System.out.println("=====================================");
        System.out.println("SIMULACAO: ALGORITMO FIRST FIT");
        System.out.println("=====================================");
        simularAlgoritmo(new GerenciadorMemoria(tamanhoMemoria), processos, 30, "firstFit");

        System.out.println("\n\n=====================================");
        System.out.println("SIMULACAO: ALGORITMO NEXT FIT");
        System.out.println("=====================================");
        simularAlgoritmo(new GerenciadorMemoria(tamanhoMemoria), processos, 30, "nextFit");

        System.out.println("\n\n=====================================");
        System.out.println("SIMULACAO: ALGORITMO BEST FIT");
        System.out.println("=====================================");
        simularAlgoritmo(new GerenciadorMemoria(tamanhoMemoria), processos, 30, "bestFit");

        System.out.println("\n\n=====================================");
        System.out.println("SIMULACAO: ALGORITMO WORST FIT");
        System.out.println("=====================================");
        simularAlgoritmo(new GerenciadorMemoria(tamanhoMemoria), processos, 30, "worstFit");

        scanner.close();
    }

    private static void simularAlgoritmo(GerenciadorMemoria gm, List<Processo> processosBase, int numOperacoes, String algoritmo) {
        Random random = new Random();

        List<Processo> processos = new ArrayList<>();
        for (Processo p : processosBase) {
            processos.add(new Processo(p.getId(), p.getTamanho()));
        }

        System.out.println("Estado inicial da memória:");
        gm.exibirMemoria();

        for (int i = 0; i < numOperacoes; i++) {
            System.out.println("--- Operacao " + (i + 1) + " ---");
            Processo processoSorteado = processos.get(random.nextInt(processos.size()));
            String idProcesso = processoSorteado.getId();

            boolean sucesso;
            if (gm.estaNaMemoria(idProcesso)) {
                System.out.println("Processo " + idProcesso + " já está na memória. Desalocando...");
                sucesso = gm.desalocar(idProcesso);
            } else {
                System.out.println("Processo " + idProcesso + " não está na memória. Alocando...");
                switch (algoritmo) {
                    case "firstFit":
                        sucesso = gm.firstFit(processoSorteado);
                        break;
                    case "nextFit":
                        sucesso = gm.nextFit(processoSorteado);
                        break;
                    case "bestFit":
                        sucesso = gm.bestFit(processoSorteado);
                        break;
                    case "worstFit":
                        sucesso = gm.worstFit(processoSorteado);
                        break;
                    default:
                        System.out.println("Algoritmo desconhecido.");
                        sucesso = false;
                        break;
                }
            }

            if(sucesso) {
                gm.exibirMemoria();
            } else {
                System.out.println("Operação falhou. Memória não alterada.");
                gm.exibirMemoria();
            }

            System.out.println("--- Fim da Operacao " + (i + 1) + " ---\n");
            gm.calcularFragmentacaoExterna();
        }
    }
}