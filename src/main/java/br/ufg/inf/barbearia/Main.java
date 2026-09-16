package br.ufg.inf.barbearia;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufg.inf.barbearia.model.Agendamento;
import br.ufg.inf.barbearia.model.Barbearia;
import br.ufg.inf.barbearia.model.Barbeiro;
import br.ufg.inf.barbearia.model.Cliente;
import br.ufg.inf.barbearia.model.Endereco;
import br.ufg.inf.barbearia.model.Servico;
import br.ufg.inf.barbearia.model.StatusAgendamento;
import br.ufg.inf.barbearia.util.BarbeariaJPAUtil;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            popularDados();
            LOGGER.info("Sistema de agendamento de barbearia executado com sucesso");
        } finally {
            BarbeariaJPAUtil.closeEntityManagerFactory();
        }
    }

    private static void popularDados() {
        BarbeariaJPAUtil.executeInTransaction(entityManager -> {
            Barbearia barbearia = new Barbearia();
            barbearia.setNome("Barbearia Estilo & Navalha");
            barbearia.setTelefone("(62) 3333-4444");
            barbearia.setEndereco("Av. Central, 100 - Goiania/GO");
            entityManager.persist(barbearia);

            Barbeiro barbeiro = new Barbeiro();
            barbeiro.setNome("Carlos Souza");
            barbeiro.setEspecialidade("Cortes classicos e barba");
            barbeiro.setDataContratacao(LocalDate.of(2022, 3, 1));
            barbeiro.setBarbearia(barbearia);
            entityManager.persist(barbeiro);

            Cliente cliente = new Cliente();
            cliente.setNome("Joao Pereira");
            cliente.setTelefone("(62) 99999-1111");
            cliente.setEmail("joao.pereira@example.com");
            entityManager.persist(cliente);

            Endereco endereco = new Endereco();
            endereco.setRua("Rua das Flores");
            endereco.setNumero("123");
            endereco.setBairro("Setor Central");
            endereco.setCidade("Goiania");
            endereco.setCep("74000-000");
            endereco.setCliente(cliente);
            entityManager.persist(endereco);

            Servico corte = new Servico();
            corte.setNome("Corte de cabelo");
            corte.setDuracaoMinutos(30);
            corte.setPreco(new BigDecimal("40.00"));
            entityManager.persist(corte);

            Servico barba = new Servico();
            barba.setNome("Barba");
            barba.setDuracaoMinutos(20);
            barba.setPreco(new BigDecimal("25.00"));
            entityManager.persist(barba);

            Agendamento agendamento = new Agendamento();
            agendamento.setDataHora(LocalDateTime.now().plusDays(1));
            agendamento.setStatus(StatusAgendamento.AGENDADO);
            agendamento.setCliente(cliente);
            agendamento.setBarbeiro(barbeiro);
            agendamento.adicionarServico(corte);
            agendamento.adicionarServico(barba);
            entityManager.persist(agendamento);

            LOGGER.info("Agendamento criado: {} para {} com {} servico(s)",
                    agendamento.getDataHora(), cliente.getNome(), agendamento.getItens().size());
        });
    }
}
