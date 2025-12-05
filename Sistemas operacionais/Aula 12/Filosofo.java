import java.util.Random;
import java.util.concurrent.Semaphore;

public class Filosofo extends Thread {
    private int id;
    private String nome;
    private Garfos garfoEsquerda;
    private Garfos garfoDireita;
    private Semaphore semaforoSalaJantar;
    private Random random = new Random();

    public Filosofo(int id, String nome, Garfos garfoEsquerda, Garfos garfoDireita, Semaphore semaforoSalaJantar) {
        this.id = id;
        this.nome = nome;
        this.garfoEsquerda = garfoEsquerda;
        this.garfoDireita = garfoDireita;
        this.semaforoSalaJantar = semaforoSalaJantar;
    }

    @Override
    public void run() {
        try {
            while (true) {
                pensar();

                System.out.println(nome + " está com fome e espera para entrar na sala de jantar.");
                semaforoSalaJantar.acquire();

                comer();

                semaforoSalaJantar.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void pensar() throws InterruptedException {
        int tempoPensando = 1000 + random.nextInt(2000);
        System.out.println(nome + " está pensando por " + tempoPensando + "ms.");
        Thread.sleep(tempoPensando);
    }

    private void comer() throws InterruptedException {
        garfoEsquerda.pegar(nome);

        Thread.sleep(100);

        garfoDireita.pegar(nome);

        int tempoComendo = 500 + random.nextInt(1500);
        System.out.println( nome + " está **COMENDO** por " + tempoComendo + "ms.");
        Thread.sleep(tempoComendo);

        garfoEsquerda.soltar(nome);
        garfoDireita.soltar(nome);

        System.out.println(nome + " terminou de comer.");
    }
}