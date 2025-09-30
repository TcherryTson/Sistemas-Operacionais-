import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        List<Processo> processos = new ArrayList<>();
        
        processos.add(new Processo(1, 0, 5, 2));
        processos.add(new Processo(2, 2, 3, 1));
        processos.add(new Processo(3, 4, 8, 3));
        processos.add(new Processo(4, 5, 6, 2));
        processos.add(new Processo(5, 11, 8, 1));

        Escalonador escalonador = new Escalonador();

        escalonador.executarFCFS(new ArrayList<>(processos));

        escalonador.executarSJF(new ArrayList<>(processos));
    }
}