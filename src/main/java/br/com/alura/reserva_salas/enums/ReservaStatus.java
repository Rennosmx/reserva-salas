package br.com.alura.reserva_salas.enums;

public enum ReservaStatus {
    ATIVA("Ativa"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");

    private final String descricao;

    ReservaStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
