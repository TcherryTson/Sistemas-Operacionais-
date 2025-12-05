public class Garfos {
    private int id;
    private boolean emUso = false;

    public Garfos(int id) {
        this.id = id;
    }

    public synchronized void pegar(String nomeFilosofo) throws InterruptedException {
        while (emUso) {
            System.out.println(nomeFilosofo + " está esperando o Garfo " + id);
            wait();
        }

        emUso = true;
        System.out.println(nomeFilosofo + " pegou o Garfo " + id);
    }

    public synchronized void soltar(String nomeFilosofo) {
        emUso = false;
        System.out.println(nomeFilosofo + " soltou o Garfo " + id);
        notifyAll();
    }
}