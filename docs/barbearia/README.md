# Sistema de Agendamento de Barbearia — Artefatos de Modelagem e Persistência (ORM)

Domínio: uma barbearia agenda horários de clientes com barbeiros para a realização de um ou mais serviços (corte, barba, etc).

## 1. Diagrama de Classes (PlantUML)

- Fonte: [class-diagram.puml](class-diagram.puml)
- Imagem: [class-diagram.png](class-diagram.png)

Relações modeladas:
- **1:N** — `Barbearia` possui vários `Barbeiro`.
- **1:1** — `Cliente` possui um único `Endereco`.
- **N:M** — `Agendamento` inclui vários `Servico` (e um `Servico` pode estar em vários `Agendamento`), resolvida através da classe associativa `ItemAgendamento`, que também guarda o preço cobrado no momento do agendamento.

## 2. Diagrama Entidade-Relacionamento (mapeamento ORM)

- Fonte: [er-diagram.puml](er-diagram.puml)
- Imagem: [er-diagram.png](er-diagram.png)

Mostra as tabelas geradas a partir das entidades JPA (`barbearia`, `barbeiro`, `cliente`, `endereco`, `servico`, `agendamento`, `item_agendamento`), com chaves primárias/estrangeiras e a tabela associativa `item_agendamento` que resolve o N:M.

## 3. Camada de Persistência (JPA/Hibernate)

Código-fonte em [`src/main/java/br/ufg/inf/barbearia`](../../src/main/java/br/ufg/inf/barbearia):

- `model/` — entidades JPA (`Barbearia`, `Barbeiro`, `Cliente`, `Endereco`, `Servico`, `Agendamento`, `ItemAgendamento`, `StatusAgendamento`).
- `util/BarbeariaJPAUtil.java` — gerenciamento de `EntityManagerFactory`/`EntityManager` e execução de transações, no mesmo padrão usado em `br.ufg.inf.tutorial.util.JPAUtil`.
- `Main.java` — demonstração populando o banco (H2 em memória) com dados de exemplo.

A persistence-unit `barbearia-jpa` (e a de teste `barbearia-jpa-test`) está registrada em [`src/main/resources/META-INF/persistence.xml`](../../src/main/resources/META-INF/persistence.xml), usando Hibernate como provider e H2 em memória.

Testes JUnit (validam as três relações — 1:N, 1:1 e N:M) em
[`src/test/java/br/ufg/inf/barbearia/BarbeariaPersistenceTest.java`](../../src/test/java/br/ufg/inf/barbearia/BarbeariaPersistenceTest.java):

```bash
mvn test -Dtest=BarbeariaPersistenceTest
```

## 4. Notebook Jupyter (testes interativos)

[`notebooks/barbearia_tests.ipynb`](../../notebooks/barbearia_tests.ipynb) — usa o kernel **IJava**, no mesmo formato do notebook existente ([`notebooks/tests.ipynb`](../../notebooks/tests.ipynb)).

Antes de rodar:

```bash
mvn compile
```

O notebook então carrega as dependências via `%maven`, adiciona `../target/classes` ao classpath (incluindo o `persistence.xml`) e executa, célula a célula:
1. Criação de uma `Barbearia` com dois `Barbeiro` (1:N) e verificação da coleção.
2. Criação de um `Cliente` com seu `Endereco` (1:1) e consulta via JPQL.
3. Criação de `Agendamento`s com múltiplos `Servico`s reaproveitados entre agendamentos (N:M via `ItemAgendamento`), validando a relação nos dois sentidos.
