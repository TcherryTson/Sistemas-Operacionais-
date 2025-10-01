public class Processo {
    int id;
    int tempoChegada;
    int tempoExecucao;
    int prioridade;

    int tempoRestante;
    int tempoConclusao;
    int tempoRetorno;
    int tempoEspera;

    public Processo(int id, int tempoChegada, int tempoExecucao, int prioridade) {
        this.id = id;
        this.tempoChegada = tempoChegada;
        this.tempoExecucao = tempoExecucao;
        this.prioridade = prioridade;
        this.tempoRestante = tempoExecucao;
    }
    public Processo(Processo outro) {
        this.id = outro.id;
        this.tempoChegada = outro.tempoChegada;
        this.tempoExecucao = outro.tempoExecucao;
        this.prioridade = outro.prioridade;
        this.tempoRestante = outro.tempoExecucao;
    }
    @Override
    public String toString() {
        return "P" + id;
    }
}
