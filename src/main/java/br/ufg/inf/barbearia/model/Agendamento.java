package br.ufg.inf.barbearia.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "agendamento")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataHora;
    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Barbeiro barbeiro;
    @OneToMany(mappedBy = "agendamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ItemAgendamento> itens = new ArrayList<>();

    public Long getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Barbeiro getBarbeiro() { return barbeiro; }
    public void setBarbeiro(Barbeiro barbeiro) { this.barbeiro = barbeiro; }
    public List<ItemAgendamento> getItens() { return itens; }

    public void adicionarServico(Servico servico) {
        ItemAgendamento item = new ItemAgendamento();
        item.setAgendamento(this);
        item.setServico(servico);
        item.setPrecoCobrado(servico.getPreco());
        this.itens.add(item);
    }
}
