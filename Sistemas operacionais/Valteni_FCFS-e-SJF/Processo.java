public class Processo implements Comparable<Processo> {
    int id;
    int tempoChegada;
    int tempoExecucao;
    int prioridade;

    int tempoEspera;
    int tempoRetorno;
    int tempoConclusao;

    public Processo(int id, int tempoChegada, int tempoExecucao, int prioridade) {
        this.id = id;
        this.tempoChegada = tempoChegada;
        this.tempoExecucao = tempoExecucao;
        this.prioridade = prioridade;
        
        this.tempoEspera = 0;
        this.tempoRetorno = 0;
        this.tempoConclusao = 0;
    }

    @Override
    public String toString() {
        return "P" + this.id;
    }
    
    @Override
    public int compareTo(Processo outro) {
        return Integer.compare(this.tempoChegada, outro.tempoChegada);
    }
}