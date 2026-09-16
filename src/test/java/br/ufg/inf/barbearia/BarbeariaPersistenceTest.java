package br.ufg.inf.barbearia;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ufg.inf.barbearia.model.Agendamento;
import br.ufg.inf.barbearia.model.Barbearia;
import br.ufg.inf.barbearia.model.Barbeiro;
import br.ufg.inf.barbearia.model.Cliente;
import br.ufg.inf.barbearia.model.Endereco;
import br.ufg.inf.barbearia.model.Servico;
import br.ufg.inf.barbearia.model.StatusAgendamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

class BarbeariaPersistenceTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    static void setUpClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("barbearia-jpa-test");
    }

    @AfterAll
    static void tearDownClass() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    void tearDown() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

    @Test
    void deveCriarBarbeariaComBarbeiroOneToMany() {
        Barbearia barbearia = new Barbearia();
        barbearia.setNome("Barbearia Teste");
        barbearia.setTelefone("62999990000");
        barbearia.setEndereco("Rua Teste, 1");

        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setNome("Barbeiro Teste");
        barbeiro.setEspecialidade("Corte");
        barbeiro.setDataContratacao(LocalDate.now());
        barbeiro.setBarbearia(barbearia);
        barbearia.getBarbeiros().add(barbeiro);

        entityManager.getTransaction().begin();
        entityManager.persist(barbearia);
        entityManager.persist(barbeiro);
        entityManager.getTransaction().commit();
        entityManager.clear();

        Barbearia encontrada = entityManager.find(Barbearia.class, barbearia.getId());
        assertThat(encontrada.getBarbeiros()).hasSize(1);
        assertThat(encontrada.getBarbeiros().get(0).getNome()).isEqualTo("Barbeiro Teste");
    }

    @Test
    void deveCriarClienteComEnderecoOneToOne() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setTelefone("62988880000");
        cliente.setEmail("cliente@teste.com");

        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");
        endereco.setNumero("10");
        endereco.setBairro("Centro");
        endereco.setCidade("Goiania");
        endereco.setCep("74000-000");
        endereco.setCliente(cliente);

        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.persist(endereco);
        entityManager.getTransaction().commit();
        entityManager.clear();

        Endereco encontrado = entityManager.find(Endereco.class, endereco.getId());
        assertThat(encontrado.getCliente().getNome()).isEqualTo("Cliente Teste");
    }

    @Test
    void deveCriarAgendamentoComVariosServicosManyToMany() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente ManyToMany");
        Barbearia barbearia = new Barbearia();
        barbearia.setNome("Barbearia ManyToMany");
        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setNome("Barbeiro ManyToMany");
        barbeiro.setBarbearia(barbearia);

        Servico corte = new Servico();
        corte.setNome("Corte");
        corte.setDuracaoMinutos(30);
        corte.setPreco(new BigDecimal("40.00"));

        Servico barba = new Servico();
        barba.setNome("Barba");
        barba.setDuracaoMinutos(20);
        barba.setPreco(new BigDecimal("25.00"));

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(LocalDateTime.now());
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setCliente(cliente);
        agendamento.setBarbeiro(barbeiro);
        agendamento.adicionarServico(corte);
        agendamento.adicionarServico(barba);

        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.persist(barbearia);
        entityManager.persist(barbeiro);
        entityManager.persist(corte);
        entityManager.persist(barba);
        entityManager.persist(agendamento);
        entityManager.getTransaction().commit();
        entityManager.clear();

        Agendamento encontrado = entityManager.find(Agendamento.class, agendamento.getId());
        assertThat(encontrado.getItens()).hasSize(2);
        assertThat(encontrado.getItens())
                .extracting(item -> item.getServico().getNome())
                .containsExactlyInAnyOrder("Corte", "Barba");
    }
}
