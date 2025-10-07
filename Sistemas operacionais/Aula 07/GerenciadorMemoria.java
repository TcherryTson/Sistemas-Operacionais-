import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class GerenciadorMemoria {
    private int[] memoria;
    private int tamanhoMemoria;
    private int ultimoAlocadoIndex;
    private List<Processo> processosAlocados;

    public GerenciadorMemoria(int tamanhoMemoria) {
        this.tamanhoMemoria = tamanhoMemoria;
        this.memoria = new int[tamanhoMemoria];
        Arrays.fill(this.memoria, 0);
        this.ultimoAlocadoIndex = 0;
        this.processosAlocados = new ArrayList<>();
    }

    public void exibirMemoria() {
        System.out.println("Mapa de Bits da Memória:");
        for (int i = 0; i < memoria.length; i++) {
            System.out.print(memoria[i] + " ");
        }
        System.out.println("\n");
    }

    public boolean desalocar(String idProcesso) {
        Processo processoParaRemover = null;
        for (Processo p : processosAlocados) {
            if (p.getId().equals(idProcesso)) {
                processoParaRemover = p;
                break;
            }
        }

        if (processoParaRemover != null) {
            int inicio = processoParaRemover.getEnderecoInicial();
            int tamanho = processoParaRemover.getTamanho();
            for (int i = 0; i < tamanho; i++) {
                memoria[inicio + i] = 0;
            }
            processosAlocados.remove(processoParaRemover);
            System.out.println("SUCESSO: Processo " + idProcesso + " foi desalocado.");
            return true;
        } else {
            System.out.println("ERRO: Processo " + idProcesso + " não encontrado na memória para desalocação.");
            return false;
        }
    }

    private void alocarNoIndice(Processo processo, int indice) {
        for (int i = 0; i < processo.getTamanho(); i++) {
            memoria[indice + i] = 1;
        }
        processo.setEnderecoInicial(indice);
        processosAlocados.add(processo);
    }

    public void calcularFragmentacaoExterna() {
        int blocosLivres = 0;
        for (int bit : memoria) {
            if (bit == 0) {
                blocosLivres++;
            }
        }

        System.out.println("Estatísticas de Fragmentação:");
        System.out.println("Total de blocos livres: " + blocosLivres);

        int maiorEspacoLivre = 0;
        int espacoAtual = 0;
        for (int bit : memoria) {
            if (bit == 0) {
                espacoAtual++;
            } else {
                if (espacoAtual > maiorEspacoLivre) {
                    maiorEspacoLivre = espacoAtual;
                }
                espacoAtual = 0;
            }
        }
        if (espacoAtual > maiorEspacoLivre) {
            maiorEspacoLivre = espacoAtual;
        }

        System.out.println("Maior bloco contíguo livre: " + maiorEspacoLivre + " blocos.");
    }

    public boolean estaNaMemoria(String idProcesso) {
        for (Processo p : processosAlocados) {
            if (p.getId().equals(idProcesso)) {
                return true;
            }
        }
        return false;
    }

    public boolean firstFit(Processo processo) {
        System.out.println("Tentando alocar Processo " + processo.getId() + " (" + processo.getTamanho() + " blocos) usando First Fit.");
        int espacoLivreContiguo = 0;
        int indiceInicio = -1;

        for (int i = 0; i < tamanhoMemoria; i++) {
            if (memoria[i] == 0) {
                espacoLivreContiguo++;
                if (indiceInicio == -1) {
                    indiceInicio = i;
                }
            } else {
                espacoLivreContiguo = 0;
                indiceInicio = -1;
            }

            if (espacoLivreContiguo >= processo.getTamanho()) {
                alocarNoIndice(processo, indiceInicio);
                System.out.println("SUCESSO: Processo " + processo.getId() + " alocado no bloco " + indiceInicio + ".");
                return true;
            }
        }
        System.out.println("ERRO: Não foi possível alocar o Processo " + processo.getId() + " - espaço insuficiente.");
        return false;
    }

    public boolean nextFit(Processo processo) {
        System.out.println("Tentando alocar Processo " + processo.getId() + " (" + processo.getTamanho() + " blocos) usando Next Fit.");
        int espacoLivreContiguo = 0;
        int indiceInicio = -1;
        int inicioBusca = ultimoAlocadoIndex;

        for (int i = inicioBusca; i < tamanhoMemoria; i++) {
            if (memoria[i] == 0) {
                espacoLivreContiguo++;
                if (indiceInicio == -1) {
                    indiceInicio = i;
                }
            } else {
                espacoLivreContiguo = 0;
                indiceInicio = -1;
            }
            if (espacoLivreContiguo >= processo.getTamanho()) {
                alocarNoIndice(processo, indiceInicio);
                ultimoAlocadoIndex = indiceInicio;
                System.out.println("SUCESSO: Processo " + processo.getId() + " alocado no bloco " + indiceInicio + ".");
                return true;
            }
        }

        espacoLivreContiguo = 0;
        indiceInicio = -1;
        for (int i = 0; i < inicioBusca; i++) {
            if (memoria[i] == 0) {
                espacoLivreContiguo++;
                if (indiceInicio == -1) {
                    indiceInicio = i;
                }
            } else {
                espacoLivreContiguo = 0;
                indiceInicio = -1;
            }
            if (espacoLivreContiguo >= processo.getTamanho()) {
                alocarNoIndice(processo, indiceInicio);
                ultimoAlocadoIndex = indiceInicio;
                System.out.println("SUCESSO: Processo " + processo.getId() + " alocado no bloco " + indiceInicio + ".");
                return true;
            }
        }

        System.out.println("ERRO: Não foi possível alocar o Processo " + processo.getId() + " - espaço insuficiente.");
        return false;
    }

    public boolean bestFit(Processo processo) {
        System.out.println("Tentando alocar Processo " + processo.getId() + " (" + processo.getTamanho() + " blocos) usando Best Fit.");
        int melhorIndice = -1;
        int menorEspacoLivre = Integer.MAX_VALUE;
        int espacoLivreContiguo = 0;
        int indiceAtual = -1;

        for (int i = 0; i < tamanhoMemoria; i++) {
            if (memoria[i] == 0) {
                espacoLivreContiguo++;
                if (indiceAtual == -1) {
                    indiceAtual = i;
                }
            } else {
                if (espacoLivreContiguo >= processo.getTamanho() && espacoLivreContiguo < menorEspacoLivre) {
                    menorEspacoLivre = espacoLivreContiguo;
                    melhorIndice = indiceAtual;
                }
                espacoLivreContiguo = 0;
                indiceAtual = -1;
            }
        }
        if (espacoLivreContiguo >= processo.getTamanho() && espacoLivreContiguo < menorEspacoLivre) {
            melhorIndice = indiceAtual;
        }

        if (melhorIndice != -1) {
            alocarNoIndice(processo, melhorIndice);
            System.out.println("SUCESSO: Processo " + processo.getId() + " alocado no bloco " + melhorIndice + ".");
            return true;
        } else {
            System.out.println("ERRO: Não foi possível alocar o Processo " + processo.getId() + " - espaço insuficiente.");
            return false;
        }
    }

    public boolean worstFit(Processo processo) {
        System.out.println("Tentando alocar Processo " + processo.getId() + " (" + processo.getTamanho() + " blocos) usando Worst Fit.");
        int piorIndice = -1;
        int maiorEspacoLivre = -1;
        int espacoLivreContiguo = 0;
        int indiceAtual = -1;

        for (int i = 0; i < tamanhoMemoria; i++) {
            if (memoria[i] == 0) {
                espacoLivreContiguo++;
                if (indiceAtual == -1) {
                    indiceAtual = i;
                }
            } else {
                if (espacoLivreContiguo >= processo.getTamanho() && espacoLivreContiguo > maiorEspacoLivre) {
                    maiorEspacoLivre = espacoLivreContiguo;
                    piorIndice = indiceAtual;
                }
                espacoLivreContiguo = 0;
                indiceAtual = -1;
            }
        }
        if (espacoLivreContiguo >= processo.getTamanho() && espacoLivreContiguo > maiorEspacoLivre) {
            piorIndice = indiceAtual;
        }

        if (piorIndice != -1) {
            alocarNoIndice(processo, piorIndice);
            System.out.println("SUCESSO: Processo " + processo.getId() + " alocado no bloco " + piorIndice + ".");
            return true;
        } else {
            System.out.println("ERRO: Não foi possível alocar o Processo " + processo.getId() + " - espaço insuficiente.");
            return false;
        }
    }
}