import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Escalonador {
    public void executarFCFS(List<Processo> processos) {
        System.out.println("--- FCFS (First-Come, First-Served) ---");
        
        Collections.sort(processos);

        int tempoAtual = 0;
        List<Processo> ordemExecucao = new ArrayList<>();

        for (Processo p : processos) {
            if (tempoAtual < p.tempoChegada) {
                tempoAtual = p.tempoChegada;
            }
        
            p.tempoEspera = tempoAtual - p.tempoChegada;
            p.tempoConclusao = tempoAtual + p.tempoExecucao;
            p.tempoRetorno = p.tempoConclusao - p.tempoChegada;
            tempoAtual = p.tempoConclusao;
            ordemExecucao.add(p);
        }

        imprimirResultados(ordemExecucao);
    } 
    
    public void executarSJF(List<Processo> processos) {
        System.out.println("--- SJF (Shortest Job First) - Nao-Preemptivo ---");

        List<Processo> processosRestantes = new ArrayList<>(processos);
        List<Processo> ordemExecucao = new ArrayList<>();
        int tempoAtual = 0;

        while (!processosRestantes.isEmpty()) {
            List<Processo> filaDeProntos = new ArrayList<>();

            for (Processo p : processosRestantes) {
                if (p.tempoChegada <= tempoAtual) {
                    filaDeProntos.add(p);
                }
            }

            if (filaDeProntos.isEmpty()) {
                tempoAtual++;
                continue;
            }

            filaDeProntos.sort(Comparator.comparingInt(p -> p.tempoExecucao));

            Processo processoAtual = filaDeProntos.get(0);

            processoAtual.tempoEspera = tempoAtual - processoAtual.tempoChegada;
            processoAtual.tempoConclusao = tempoAtual + processoAtual.tempoExecucao;
            processoAtual.tempoRetorno = processoAtual.tempoConclusao - processoAtual.tempoChegada;
            
            tempoAtual = processoAtual.tempoConclusao;

            ordemExecucao.add(processoAtual);
            processosRestantes.remove(processoAtual);
        }

        imprimirResultados(ordemExecucao);
    }

    private void imprimirResultados(List<Processo> processosExecutados) {
        int totalEspera = 0;
        int totalRetorno = 0;

        System.out.print("Ordem de Execucao: ");
        for (int i = 0; i < processosExecutados.size(); i++) {
            System.out.print(processosExecutados.get(i) + (i == processosExecutados.size() - 1 ? "" : " -> "));
        }

        System.out.println("\n");
        System.out.printf("%-10s | %-15s | %-15s\n", "Processo", "Tempo de Espera", "Tempo de Retorno");
        System.out.println("-------------------------------------------------");

        for (Processo p : processosExecutados) {
            System.out.printf("%-10s | %-15d | %-15d\n", p, p.tempoEspera, p.tempoRetorno);
            totalEspera += p.tempoEspera;
            totalRetorno += p.tempoRetorno;
        }

        System.out.println("-------------------------------------------------");
        System.out.printf("Tempo Medio de Espera: %.2f\n", (double) totalEspera / processosExecutados.size());
        System.out.printf("Tempo Medio de Retorno: %.2f\n", (double) totalRetorno / processosExecutados.size());
        System.out.println("-------\n");
    }
}