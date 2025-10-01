import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class AlgoritmosEscalonamento {

    /**
     * ALGORITMO 1: Priority Scheduling (Preemptive)
     * Simula o escalonamento onde o processo de maior prioridade (menor número)
     * sempre assume a CPU, interrompendo (preemptando) o processo atual se necessário.
     */
    public static String prioritySchedulingPreemptive(List<Processo> processos) {
        List<Processo> processosCopia = processos.stream().map(Processo::new).collect(Collectors.toList());
        List<Processo> concluidos = new ArrayList<>();
        List<String> ordemExecucao = new ArrayList<>();

        int tempoAtual = 0;
        Processo processoAtual = null;

        while (concluidos.size() < processosCopia.size()) {
            final int tempoFinal = tempoAtual;
            List<Processo> prontos = processosCopia.stream()
                    .filter(p -> p.tempoChegada <= tempoFinal && p.tempoRestante > 0)
                    .sorted(Comparator.comparingInt(p -> p.prioridade))
                    .collect(Collectors.toList());

            if (!prontos.isEmpty()) {
                Processo processoMaisPrioritario = prontos.get(0);
                if (processoAtual != processoMaisPrioritario) {
                    processoAtual = processoMaisPrioritario;
                }
                processoAtual.tempoRestante--;
                ordemExecucao.add("T" + tempoAtual + ": " + processoAtual);
                if (processoAtual.tempoRestante == 0) {
                    processoAtual.tempoConclusao = tempoAtual + 1;
                    concluidos.add(processoAtual);
                }
            } else {
                ordemExecucao.add("T" + tempoAtual + ": CPU Ociosa");
            }
            tempoAtual++;
        }

        return formatarResultados("Priority Scheduling (Preemptive)", processosCopia, ordemExecucao);
    }

    /**
     * ALGORITMO 2: Priority Scheduling - Multiple Queues
     * Organiza os processos em 3 filas de prioridade. A CPU sempre executa processos
     * da fila de maior prioridade primeiro. Dentro de cada fila, usamos FCFS (First-Come, First-Served).
     */
    public static String prioritySchedulingMultipleQueues(List<Processo> processos) {
        List<Processo> processosCopia = processos.stream().map(Processo::new).collect(Collectors.toList());
        List<String> ordemExecucao = new ArrayList<>();
        Queue<Processo> filaAlta = new LinkedList<>();
        Queue<Processo> filaMedia = new LinkedList<>();
        Queue<Processo> filaBaixa = new LinkedList<>();

        int tempoAtual = 0;
        int processosConcluidos = 0;

        while (processosConcluidos < processosCopia.size()) {
            final int tempoFinal = tempoAtual;
            processosCopia.stream()
                    .filter(p -> p.tempoChegada == tempoFinal)
                    .forEach(p -> {
                        if (p.prioridade <= 3) filaAlta.add(p);
                        else if (p.prioridade <= 6) filaMedia.add(p);
                        else filaBaixa.add(p);
                    });

            Processo processoAtual = null;
            if (!filaAlta.isEmpty()) {
                processoAtual = filaAlta.peek();
            }
            else if (!filaMedia.isEmpty()) {
                processoAtual = filaMedia.peek();
            }
            else if (!filaBaixa.isEmpty()) {
                processoAtual = filaBaixa.peek();
            }

            if (processoAtual != null) {
                processoAtual.tempoRestante--;
                ordemExecucao.add("T" + tempoAtual + ": " + processoAtual);
                if (processoAtual.tempoRestante == 0) {
                    processoAtual.tempoConclusao = tempoAtual + 1;
                    processosConcluidos++;
                    if (processoAtual.prioridade <= 3) filaAlta.poll();
                    else if (processoAtual.prioridade <= 6) filaMedia.poll();
                    else filaBaixa.poll();
                }
            } else {
                ordemExecucao.add("T" + tempoAtual + ": CPU Ociosa");
            }
            tempoAtual++;
        }

        return formatarResultados("Priority Scheduling - Multiple Queues", processosCopia, ordemExecucao);
    }

    private static String formatarResultados(String nomeAlgoritmo, List<Processo> processosConcluidos, List<String> ordemExecucao) {
        StringBuilder sb = new StringBuilder();
        sb.append("ALGORITMO: ").append(nomeAlgoritmo).append("\n\n");
        sb.append("ORDEM DE EXECUCAO:\n");

        String execucaoAnterior = "";
        int inicio = 0;
        for (int i = 0; i < ordemExecucao.size(); i++) {
            String atual = ordemExecucao.get(i).split(": ")[1];
            if (!atual.equals(execucaoAnterior) && i > 0) {
                sb.append(inicio).append("-").append(i).append(": ").append(execucaoAnterior).append("\n");
                inicio = i;
            }
            execucaoAnterior = atual;
        }
        sb.append(inicio).append("-").append(ordemExecucao.size()).append(": ").append(execucaoAnterior).append("\n\n");

        double somaTempoEspera = 0;
        double somaTempoRetorno = 0;

        sb.append("METRICAS INDIVIDUAIS:\n");
        for (Processo p : processosConcluidos) {
            p.tempoRetorno = p.tempoConclusao - p.tempoChegada;
            p.tempoEspera = p.tempoRetorno - p.tempoExecucao;
            somaTempoEspera += p.tempoEspera;
            somaTempoRetorno += p.tempoRetorno;
            sb.append(String.format("Processo P%d: Tempo de Espera = %d, Tempo de Retorno = %d\n", p.id, p.tempoEspera, p.tempoRetorno));
        }

        double mediaEspera = somaTempoEspera / processosConcluidos.size();
        double mediaRetorno = somaTempoRetorno / processosConcluidos.size();

        sb.append("\nMETRICAS GERAIS:\n");
        sb.append(String.format("Tempo Médio de Espera: %.2f\n", mediaEspera));
        sb.append(String.format("Tempo Médio de Retorno: %.2f\n", mediaRetorno));

        return sb.toString();
    }
}
