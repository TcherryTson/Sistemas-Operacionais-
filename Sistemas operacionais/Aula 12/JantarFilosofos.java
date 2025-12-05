import java.util.concurrent.Semaphore;

public class JantarFilosofos {

    private static final int NUM_FILOSOFOS = 5;

    public static void main(String[] args) {
        System.out.println("---Início do Jantar dos Filósofos (Solução com Semáforo N-1)---");

        Garfos[] garfos = new Garfos[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Garfos(i);
        }

        Semaphore semaforoSalaJantar = new Semaphore(NUM_FILOSOFOS - 1);
        System.out.println("Regra de Sincronização: Máximo de " + (NUM_FILOSOFOS - 1) + " filósofos permitidos na sala de jantar.");

        Filosofo[] filosofos = new Filosofo[NUM_FILOSOFOS];
        String[] nomes = {"F1", "F2", "F3", "F4", "F5"};

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            filosofos[i] = new Filosofo(
                    i,
                    nomes[i],
                    garfos[i],
                    garfos[(i + 1) % NUM_FILOSOFOS],
                    semaforoSalaJantar
            );
        }

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            filosofos[i].start();
        }
    }
}