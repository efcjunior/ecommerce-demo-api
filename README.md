# ✅ Atendimento ao Caso Técnico – E-commerce Backend

## 🔐 Autenticação
**Pedido:**
- Implementar autenticação JWT.
- Criar perfis de usuário:
    - **Admin:** CRUD de produtos e relatórios.
    - **User:** criação e pagamento de pedidos, listagem de produtos.

**Entregue:**
- ✅ JWT funcional com geração e validação de token.
- ✅ Perfis `ADMIN` e `USER` com controle via `@PreAuthorize`.
- ✅ Rotas `/auth/**` públicas.
- ✅ O usuário da API deve se **registrar via** [`/auth/register`](http://localhost:8080/auth/register) **e obter o token JWT através de** [`/auth/login`](http://localhost:8080/auth/login).

---

## 🧱 Produtos
**Pedido:**
- CRUD completo (id, nome, descrição, preço, categoria, estoque, datas).
- Busca via **Elasticsearch** com:
    - Tolerância a erros de digitação (fuzzy search).
    - Filtros por **categoria** e **faixa de preço**.
- Apenas produtos com estoque disponível.
- Sincronização MySQL ↔ Elasticsearch em criação, atualização e exclusão.

**Entregue:**
- ✅ CRUD completo implementado.
- ✅ Busca com `fuzziness("AUTO")` e filtros aplicados.
- ✅ Filtro de estoque ativo.
- ✅ Indexação automática no Elasticsearch ao criar, atualizar e excluir.

---

## 💳 Pedidos
**Pedido:**
- Criar pedido com múltiplos produtos.
- Status inicial `PENDING`; se faltar estoque, `CANCELED`.
- Calcular total com base no preço atual.
- Após pagamento, publicar evento `order.paid` no **Kafka** e atualizar estoque.

**Entregue:**
- ✅ Criação de pedido com cálculo de total e validação de estoque.
- ✅ Status `PENDING` e `CANCELED` corretamente aplicados.
- ✅ Pagamento publica evento no Kafka.
- ✅ Consumer atualiza estoque no MySQL.

---

## 📊 Relatórios
**Pedido:**
1. Top 5 usuários com maior valor em pedidos pagos.
2. Ticket médio por usuário, com filtro de data.
3. Faturamento total do mês atual.

**Entregue:**
- ✅ Todos os relatórios implementados.
- ✅ Filtros por data funcionais.
- ✅ Retornos corretos via use cases e DTOs.

---

## 🗃 Entidades e Persistência
**Pedido:**
- Todas as entidades persistidas em **MySQL**.
- Produtos indexados também no **Elasticsearch**.

**Entregue:**
- ✅ Entidades persistidas via Spring Data JPA.
- ✅ Produtos indexados e sincronizados com o ES.
- ✅ Transações garantem consistência entre as camadas.

---

## ⚙️ Regras de Negócio
**Pedido:**
- Validação de estoque no momento da criação do pedido.
- Atualização do estoque após pagamento.
- Evento `order.paid` no Kafka.
- Autenticação e autorização corretas por perfil.

**Entregue:**
- ✅ Todas as regras aplicadas conforme especificação.
- ✅ Producer e consumer Kafka operando corretamente.
- ✅ Perfis e permissões validadas.

---

## 🧩 Arquitetura
**Pedido:**
- Aplicar **Clean Architecture**, conforme o fluxo:
    - Controller → Use Case → Gateway → Infrastructure

**Entregue:**
- ✅ Estrutura fiel ao documento *Fluxo de Interação.pdf*.
- ✅ Camadas bem isoladas e gateways independentes.

---

## ⚠️ Apontamentos de Melhoria
1. Personalizar exceções na camada **Use Case** e tratá-las externamente, exibindo mensagens amigáveis e **HTTP status** corretos.
2. Adicionar **logs estruturados** e **métricas de performance** (ES, Kafka, relatórios).
3. Melhorar rotina **batch de reindexação** — hoje reindexa toda a tabela; ajustar para processar apenas produtos alterados.
4. Implementar **retry** ou fila para falhas de sincronização entre MySQL e Elasticsearch.
5. Substituir controle de duplicidade Kafka (**HashSet**) por **Redis** ou persistência.
6. Padronizar exceções (`BusinessException`, `NotFoundException`) e centralizar em `@ControllerAdvice`.
7. Adicionar **testes de integração** com containers (MySQL, Kafka, Elasticsearch).
8. Centralizar **roles** em enum `Role { ADMIN, USER }`.
9. Registrar logs detalhados no fluxo de **pedidos e relatórios**.
10. Incluir mensagens de erro descritivas em **cancelamentos** e **relatórios**.

---

# 🚀 Ambiente de Desenvolvimento

## 🧩 Serviços Disponíveis (Docker Compose)

O projeto inclui um ambiente completo de desenvolvimento configurado via **Docker Compose**.  
Todos os serviços se conectam na mesma rede `ecommerce-net`.

| Serviço               | Descrição                           | URL de Acesso / Porta                                                                      |
|-----------------------|-------------------------------------|--------------------------------------------------------------------------------------------|
| **MySQL**             | Banco de dados principal            | `localhost:3306`                                                                           |
| **Adminer**           | Interface web para o MySQL          | [http://localhost:8081](http://localhost:8081)                                             |
| **Elasticsearch**     | Engine de busca                     | [http://localhost:9200](http://localhost:9200)                                             |
| **Elasticsearch Head**| UI para inspeção do índice ES       | [http://localhost:9100](http://localhost:9100)                                             |
| **Zookeeper**         | Coordenação do Kafka                | `localhost:2181`                                                                           |
| **Kafka**             | Broker de mensageria                | `localhost:9093`                                                                           |
| **Kafka UI**          | Interface de gerenciamento do Kafka | [http://localhost:8085](http://localhost:8085)                                             |
| **Swagger UI**     | Swagger                             | [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) |

---

## 🐳 Comandos Docker

Subir os containers (modo background):

```bash
docker compose -f docker-compose.dev.yml up -d

```
Encerrar e remover os containers:

```bash
docker compose -f docker-compose.dev.yml up -d

```
## 🧠 Executar a Aplicação

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Ou definir o perfil de ambiente antes de iniciar:

```bash
export SPRING_PROFILES_ACTIVE=dev
```

## 🗄️ Banco de Dados

- O banco de dados e suas tabelas são **criadas automaticamente no startup** através do **Flyway**, garantindo versionamento, migrações controladas e consistência entre ambientes.

## 🧰 Tecnologias Utilizadas
| Tecnologia         | Versão / Observação                    |
| ------------------ | -------------------------------------- |
| **Java**           | 25 (JDK 25)                            |
| **Spring Boot**    | 3.5.x (Web, Security, Data JPA, Kafka) |
| **MySQL**          | 8.0                                    |
| **Elasticsearch**  | 8.14.3                                 |
| **Kafka**          | 3.8.0 + Zookeeper 3.8.4                |
| **Docker Compose** | 3.8                                    |
| **Adminer**        | latest                                 |
| **Kafka UI**       | latest                                 |



