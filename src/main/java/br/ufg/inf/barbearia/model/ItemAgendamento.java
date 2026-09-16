package br.ufg.inf.barbearia.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "item_agendamento")
public class ItemAgendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal precoCobrado;
    @ManyToOne
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;
    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    public Long getId() { return id; }
    public BigDecimal getPrecoCobrado() { return precoCobrado; }
    public void setPrecoCobrado(BigDecimal precoCobrado) { this.precoCobrado = precoCobrado; }
    public Agendamento getAgendamento() { return agendamento; }
    public void setAgendamento(Agendamento agendamento) { this.agendamento = agendamento; }
    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }
}
