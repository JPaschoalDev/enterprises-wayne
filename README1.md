# 🏢 Wayne Enterprises - Sistema de Gestão Corporativo

**Versão:** 1.0-SNAPSHOT  
**Desenvolvido em:** JavaFX 21 + MySQL 8.4 + Maven  
**Banco de Dados:** MySQL 8.0+  
**Java:** 23 (Preview Features Enabled)

---

## 📑 Índice

1. [Visão Geral](#visão-geral)
2. [Arquitetura do Sistema](#arquitetura-do-sistema)
3. [Módulos Principais](#módulos-principais)
4. [Regras de Negócio](#regras-de-negócio)
5. [Funcionalidades](#funcionalidades)
6. [Estrutura de Banco de Dados](#estrutura-de-banco-de-dados)
7. [Guia de Instalação](#guia-de-instalação)
8. [Guia de Uso](#guia-de-uso)
9. [Fluxo de Autenticação](#fluxo-de-autenticação)
10. [API de DAOs](#api-de-daos)

---

## 🎯 Visão Geral

**Wayne Enterprises** é um sistema desktop completo de gerenciamento corporativo desenvolvido em **JavaFX** com interface gráfica intuitiva. O sistema foi criado para consolidar todas as operações de Recursos Humanos, gestão de eventos, treinamentos, auditoria e comunicação interna em uma única plataforma.

### Principais Características:
- ✅ Autenticação segura com controle de sessão
- ✅ Gestão completa de funcionários (cadastro, atualização, exclusão)
- ✅ Avaliações de desempenho e feedback
- ✅ Gestão de férias e licenças
- ✅ Eventos corporativos e agenda
- ✅ Programa de treinamentos com certificação
- ✅ Recrutamento e processos seletivos
- ✅ Sistema de chat corporativo
- ✅ Notificações centralizadas
- ✅ Auditoria completa com logs
- ✅ Exportação de relatórios (PDF, Excel, CSV)
- ✅ Backup e restauração de dados
- ✅ KPIs e dashboards analíticos

---

## 🏗️ Arquitetura do Sistema

### Stack Tecnológico

```
┌─────────────────────────────────────────────┐
│         JavaFX 21 (GUI Desktop)              │
│   Controllers + FXML (52 telas + CSS)       │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│         Camada de Negócio (Models)           │
│  Funcionario, Usuarios, Evento, etc.        │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│      Camada de Acesso a Dados (DAO)          │
│    26 DAOs para operações CRUD              │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│    MySQL 8.0+ (JDBC com Conexão Singleton)   │
│     Database: wayne_db (25+ tabelas)        │
└──────────────────────────────────────────────┘
```

### Padrões de Design Utilizados

1. **DAO Pattern** - Abstração de acesso a dados
2. **Singleton Pattern** - ConnectionFactory para gerenciar conexões
3. **MVC/MVVM Pattern** - Controllers + FXML
4. **Session Manager Pattern** - Controle de sessão de usuário
5. **Factory Pattern** - FXMLLoader para instanciação de telas

---

## 📦 Módulos Principais

### 1. **Módulo de Gestão de Recursos Humanos**

#### Funcionalidades:
- Cadastro completo de funcionários (nome, CPF, cargo, departamento, email, datas)
- Upload de documentos (currículo, contrato, foto)
- Atualização de dados de funcionários
- Exclusão com confirmação
- Listagem com filtros
- Cálculo de tempo médio de empresa
- Distribuição por cargo

#### Controllers:
- `CadastroController` - Inserção de novos funcionários
- `AtualizarController` - Edição de dados
- `ExclusaoController` - Exclusão com confirmação
- `ListagemController` - Visualização e filtros

#### Models:
- `Funcionario` - Entidade principal

#### DAOs:
- `FuncionarioDAO` - Operações CRUD
- `FuncionarioDAOMethods` - Métodos específicos

---

### 2. **Módulo de Avaliações e Desempenho**

#### Funcionalidades:
- Criação de avaliações periódicas
- Avaliação 360 graus
- Pontuação de desempenho
- Histórico de avaliações
- Gráficos de progresso

#### Controllers:
- `AvaliacaoController` - Gestão de avaliações
- `ListarAvaliacoesController` - Visualização histórica
- `PainelAvaliacaoController` - Dashboard de avaliações

#### Models:
- `Avaliacao` - Entidade de avaliação

#### DAOs:
- `AvaliacaoDAO` - Operações CRUD

---

### 3. **Módulo de Férias e Licenças**

#### Funcionalidades:
- Cadastro de períodos de férias
- Solicitação de licenças
- Acompanhamento de saldos
- Aprovação/rejeição
- Relatórios de ausência

#### Controllers:
- `CadastroFeriasController` - Novo cadastro
- `ListarFeriasController` - Visualização

#### Models:
- `Ferias` - Entidade de férias

#### DAOs:
- `FeriasDAO` - Operações CRUD

---

### 4. **Módulo de Eventos Corporativos**

#### Funcionalidades:
- Criação de eventos (reuniões, treinamentos, confraternizações)
- Calendário visual
- Agenda corporativa
- RSVP e confirmação de presença
- Notificações automáticas

#### Controllers:
- `AdicionarEventosController` - Novo evento
- `EditarEventoController` - Edição
- `AgendaCorporativaController` - Visualização
- `CalendarioController` - Calendário visual
- `PainelEventosController` - Dashboard

#### Models:
- `Evento` - Evento genérico
- `EventoCorporativo` - Evento corporativo
- `EventoCalendario` - Evento em calendário
- `AgendaCorporativa` - Agenda

#### DAOs:
- `EventoDAO`, `EventoCorporativoDAO`, `AgendaCorporativaDAO`, `EventoCalendarioDAO`

---

### 5. **Módulo de Treinamentos**

#### Funcionalidades:
- Catálogo de cursos e treinamentos
- Inscrição de funcionários
- Acompanhamento de progresso
- Certificação
- Avaliação de efetividade

#### Controllers:
- `TreinamentoController` - Gestão de treinamentos
- `ParticipacaoTreinamentoController` - Participação

#### Models:
- `Treinamento` - Curso/treinamento
- `ParticipacaoTreinamento` - Inscrição

#### DAOs:
- `TreinamentoDAO`
- `ParticipacaoTreinamentoDAO`

---

### 6. **Módulo de Recrutamento**

#### Funcionalidades:
- Gestão de vagas abertas
- Recebimento de currículos
- Análise de candidatos
- Agendamento de entrevistas
- Processo seletivo completo

#### Controllers:
- `RecrutamentoController` - Gestão geral
- `ProcessoSeletivoController` - Processo
- `CurriculoFormController` - Upload de currículo
- `CurriculoListController` - Listagem de currículos

#### Models:
- `Candidato` - Candidato
- `Curriculo` - Dados de currículo
- `ProcessoSeletivo` - Processo seletivo

#### DAOs:
- `CandidatoDAO`, `CurriculoDAO`, `ProcessoSeletivoDAO`

---

### 7. **Módulo de Comunicação**

#### Funcionalidades:
- Chat corporativo em tempo real
- Notificações do sistema
- Avisos e comunicados
- Broadcast de mensagens

#### Controllers:
- `ChatController` - Sistema de chat
- `NotificacaoController` - Notificações
- `AvisoController` - Avisos gerais
- `EnviarNotificacaoController` - Envio de notificações

#### Models:
- `ChatMessage` - Mensagem de chat
- `Conversation` - Conversa
- `Notificacao` - Notificação
- `NotificacaoTipoEntity` - Tipo de notificação
- `Aviso` - Aviso corporativo

#### DAOs:
- `ChatDAO`, `NotificacaoDAO`, `NotificacaoTipoDAO`, `AvisoDAO`

---

### 8. **Módulo de Auditoria e Segurança**

#### Funcionalidades:
- Log completo de todas as operações
- Rastreamento de quem fez o quê e quando
- Histórico de alterações
- Relatórios de auditoria
- Backup e restauração

#### Controllers:
- `AuditoriaController` - Gestão de auditoria
- `LogAuditoriaController` - Visualização de logs
- `RestauracaoController` - Backup/Restauração

#### Models:
- `LogAcao` - Ação registrada
- `LogAuditoria` - Auditoria
- `AuditoriaService` - Serviço de auditoria
- `BackupRestauracao` - Backup

#### DAOs:
- `LogAuditoria`, `AuditoriaDAO`, `LogDAO`

---

### 9. **Módulo de Administração**

#### Funcionalidades:
- Gestão de usuários do sistema
- Controle de permissões
- Cadastro de cargos e salários
- Benefícios
- Plano de carreira

#### Controllers:
- `CargoController` - Gestão de cargos
- `BeneficioController` - Gestão de benefícios
- `PlanoCargosController` - Plano de carreira

#### Models:
- `Cargo` - Cargo
- `Beneficio` - Benefício
- `Usuarios` - Usuário do sistema

#### DAOs:
- `CargoDAO`, `BeneficioDAO`, `UsuariosDAO`, `UsuariosDAOJdbc`

---

### 10. **Módulo de Relatórios e Inteligência**

#### Funcionalidades:
- KPIs de RH
- Gráficos de desempenho
- Relatórios de turnover
- Análise de clima organizacional
- Exportação em múltiplos formatos

#### Controllers:
- `KpiController` - KPIs
- `RelatorioGraficoController` - Gráficos
- `DashboardController` - Dashboard geral
- `ExportacaoController` - Exportação

#### Models:
- `ExportadorPDF` - Exportador PDF
- `ExportadorExcel` - Exportador Excel
- `CSVExportUtil`, `PdfExportUtil`, `ExcelExportUtil` - Utilitários
- `RelatorioUtil` - Utilitários de relatórios

---

## 📋 Regras de Negócio

### 1. **Autenticação e Autorização**

```
Regra 1: Todo usuário deve estar autenticado para acessar o sistema
    - Login: usuário + senha (verificado contra tabela usuarios)
    - Sessão: mantida em SessionManager
    - Logout: encerra sessão e retorna ao login

Regra 2: Um usuário só pode acessar dados de sua própria conta
    - Exceção: Administradores têm acesso total
    - Rastreamento: Todos as ações registram o usuário
```

### 2. **Gestão de Funcionários**

```
Regra 3: Funcionário deve ter CPF único no sistema
    - Validação: CPF é verificado antes de inserir
    - Formato: 999.999.999-99

Regra 4: Data de admissão não pode ser anterior à data de nascimento
    - Validação: getDataAdmissao() > getDataNascimento()
    - Erro: Sistema rejeita operação

Regra 5: Email deve ser válido
    - Formato: padrão RFC 5321
    - Verificação: ValidacaoUtil.validarEmail()
```

### 3. **Férias e Licenças**

```
Regra 6: Funcionário pode ter no máximo 30 dias de férias por ano
    - Acúmulo: Saldo carrega para próximo ano até limite
    - Limite máximo acumulado: 45 dias

Regra 7: Solicitação de férias deve ter aprovação
    - Estados: PENDENTE -> APROVADO/REJEITADO
    - Responsável aprovação: Gerente direto ou RH
```

### 4. **Eventos Corporativos**

```
Regra 8: Evento não pode ser criado com data no passado
    - Validação: data_evento >= data_atual

Regra 9: RSVP deve ser confirmado até 24h antes do evento
    - Limite: DataEvento - 1 dia
    - Exceção: Admin pode confirmar após
```

### 5. **Treinamentos**

```
Regra 10: Certificado só é emitido após 100% de presença
    - Cálculo: presencas / total_sessoes >= 100%
    
Regra 11: Funcionário não pode se inscrever em treinamento já iniciado
    - Validação: data_inicio >= data_atual
```

### 6. **Recrutamento**

```
Regra 12: Processo seletivo passa por 4 fases obrigatórias
    - Fase 1: Análise de currículo (CV filtering)
    - Fase 2: Entrevista técnica
    - Fase 3: Entrevista comportamental
    - Fase 4: Proposta de contrato

Regra 13: Candidato não pode participar de 2 entrevistas simultâneas
    - Validação: Não marcar mesmo candidato em horário conflitante
```

### 7. **Auditoria e Segurança**

```
Regra 14: Toda operação de escrita (INSERT/UPDATE/DELETE) deve ser registrada
    - Log: Quem fez, o quê, quando, em qual tabela
    - Retenção: Mínimo 2 anos de histórico

Regra 15: Não é permitida exclusão de registros auditados
    - Operação permitida: Soft delete (marcar como inativo)
    - Dados históricos: Mantidos para rastreamento
```

### 8. **Permissões e Papéis**

```
Regra 16: Usuário em papel "Funcionário" tem acesso limitado
    - Pode: Ver seus próprios dados, solicitar férias, visualizar eventos
    - Não pode: Ver dados de outros, deletar registros, gerar relatórios gerenciais

Regra 17: Usuário em papel "Gerente" tem acesso ampliado
    - Pode: Gerenciar equipe, aprovar férias, avaliar desempenho
    - Não pode: Deletar usuários, acessar financeiro

Regra 18: Usuário em papel "Admin" tem acesso total
    - Pode: Qualquer operação
```

---

## 🚀 Funcionalidades

### Dashboard Principal
- Resumo de funcionários ativos
- Próximos eventos
- Treinamentos em progresso
- Notificações não lidas
- KPIs principais

### Gestão de RH
- ✅ CRUD de funcionários completo
- ✅ Upload de documentos
- ✅ Fotos de perfil
- ✅ Histórico de alterações
- ✅ Cálculos de tenure

### Avaliações
- ✅ Avaliação de desempenho
- ✅ Feedback 360 graus
- ✅ Histórico de avaliações
- ✅ Gráficos de progresso
- ✅ Comparação por período

### Eventos e Agenda
- ✅ Criação de eventos
- ✅ Calendário visual
- ✅ RSVP de participantes
- ✅ Notificações automáticas
- ✅ Lembretes por email

### Treinamentos
- ✅ Catálogo de cursos
- ✅ Inscrição de funcionários
- ✅ Acompanhamento de progresso
- ✅ Certificação automática
- ✅ Relatórios de efetividade

### Recrutamento
- ✅ Gestão de vagas
- ✅ Recebimento de CVs
- ✅ Análise de candidatos
- ✅ Agendamento de entrevistas
- ✅ Processo seletivo estruturado

### Comunicação
- ✅ Chat corporativo
- ✅ Notificações centralizadas
- ✅ Avisos gerenciais
- ✅ Broadcast de mensagens

### Relatórios
- ✅ Exportação PDF
- ✅ Exportação Excel
- ✅ Exportação CSV
- ✅ Gráficos dinâmicos
- ✅ KPIs customizados

### Segurança
- ✅ Auditoria completa
- ✅ Logs de operações
- ✅ Backup automatizado
- ✅ Restauração de dados
- ✅ Histórico de acesso

---

## 🗄️ Estrutura de Banco de Dados

### Tabelas Principais

```sql
-- Usuários e Autenticação
usuarios (id, usuario, senha, nome_completo, email, online, last_seen)

-- Recursos Humanos
funcionarios (id, nome_completo, cpf, cargo, departamento, email, 
              data_admissao, data_nascimento, caminho_curriculo, 
              caminho_contrato, caminho_foto)

cargos (id, nome_cargo, descricao, salario_base)

beneficios (id, nome_beneficio, descricao, valor_mensal)

-- Férias e Licenças
ferias (id, funcionario_id, data_inicio, data_fim, dias_solicitados, 
        status, data_solicitacao)

-- Eventos
eventos (id, titulo, descricao, data_evento, local, organizador_id)
agenda_corporativa (id, titulo, data_evento, tipo, descricao)
evento_corporativo (id, nome_evento, descricao, data_evento, local)
evento_calendario (id, titulo, data, status)

-- Avaliações
avaliacoes (id, funcionario_id, avaliador_id, data_avaliacao, 
            pontuacao, feedback, periodo)

-- Treinamentos
treinamentos (id, nome_treinamento, descricao, data_inicio, 
              data_fim, instrutor, local)
participacao_treinamento (id, treinamento_id, funcionario_id, 
                          data_inscricao, status)

-- Recrutamento
candidatos (id, nome, email, telefone, data_candidatura)
curriculos (id, candidato_id, arquivo_path, status_analise)
processo_seletivo (id, vaga_id, candidato_id, fase, data_fase, resultado)

-- Comunicação
chat_messages (id, remetente_id, destinatario_id, mensagem, timestamp)
conversations (id, usuario1_id, usuario2_id, ultima_mensagem)
notificacoes (id, usuario_id, mensagem, tipo, data_envio, lida)
avisos (id, titulo, descricao, data, tipo)

-- Auditoria
log_auditoria (id, usuario_id, tabela, operacao, dados_antigos, 
               dados_novos, timestamp)
log_acao (id, usuario_id, acao, data_hora)

-- Sistema
empresa_info (id, nome_empresa, cnpj, endereco, telefone)

-- Equipamentos e Estoque
equipamentos (id, nome, descricao, valor_aquisicao, data_aquisicao)
estoque_item (id, equipamento_id, quantidade_disponivel, local_armazenamento)

-- Documentos
documentos (id, funcionario_id, tipo, arquivo_path, data_upload)

-- Chamados
chamados (id, usuario_id, assunto, descricao, status, data_criacao)
```

### Relacionamentos

```
funcionarios 1 ──────── N ferias
funcionarios 1 ──────── N avaliacoes
funcionarios 1 ──────── N participacao_treinamento
usuarios 1 ──────── N log_auditoria
usuarios 1 ──────── N chat_messages
candidatos 1 ──────── N curriculos
```

---

## 📥 Guia de Instalação

### Pré-requisitos

- **Java 23+** (com suporte a Preview Features)
- **Maven 3.8+**
- **MySQL 8.0+**
- **JavaFX 21.0.2**

### Passos de Instalação

#### 1. **Clonar/Extrair o Projeto**
```bash
unzip enterprises-wayne.zip
cd enterprises-wayne
```

#### 2. **Criar Banco de Dados MySQL**
```sql
CREATE DATABASE wayne_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE wayne_db;

-- Executar script SQL de criação das tabelas
-- (arquivo: schema.sql - Se disponível)
```

#### 3. **Configurar Credenciais**
Editar `src/main/java/com/wayne/wayneen/enterpriseswyne/model/ConnectionFactory.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/wayne_db";
private static final String USUARIO = "root";
private static final String SENHA = "sua_senha_aqui";
```

#### 4. **Compilar com Maven**
```bash
mvn clean install
```

#### 5. **Executar a Aplicação**
```bash
mvn javafx:run
```

Ou diretamente com Java:
```bash
mvn clean javafx:run
```

### Primeira Execução

1. Tela de login aparecerá
2. Use credenciais padrão (verificar no banco) ou criar novo usuário
3. Clique em "Entrar" para acessar o dashboard

---

## 💻 Guia de Uso

### Tela Principal (Dashboard)

A tela principal exibe:
- **Resumo de Funcionários:** Total, ativos, inativos
- **Próximos Eventos:** Eventos nos próximos 30 dias
- **Notificações:** Mensagens não lidas
- **Atalhos:** Acesso rápido aos módulos

### Módulo de Funcionários

#### Cadastrar Novo Funcionário
1. Menu → Gestão de RH → Cadastro de Funcionário
2. Preencher campos obrigatórios (nome, CPF, cargo, etc.)
3. Selecionar arquivo de currículo (PDF)
4. Clicar em "Salvar"

#### Atualizar Funcionário
1. Menu → Gestão de RH → Atualizar Funcionário
2. Selecionar funcionário na lista
3. Editar campos desejados
4. Clicar em "Atualizar"

#### Excluir Funcionário
1. Menu → Gestão de RH → Excluir Funcionário
2. Selecionar funcionário
3. Confirmar exclusão
4. Sistema registra exclusão em auditoria

### Módulo de Avaliações

1. Menu → Avaliações → Nova Avaliação
2. Selecionar funcionário e avaliador
3. Preencher formulário de avaliação (1-10)
4. Adicionar feedback qualitativo
5. Salvar avaliação

### Módulo de Eventos

1. Menu → Eventos → Novo Evento
2. Preencher título, descrição, data e hora
3. Convidar participantes
4. Salvar evento
5. Sistema envia notificações aos convidados

### Geração de Relatórios

1. Menu → Relatórios → Selecionar tipo (PDF/Excel/CSV)
2. Escolher período
3. Filtrar por departamento (opcional)
4. Clicar em "Gerar"
5. Arquivo é baixado automaticamente

---

## 🔐 Fluxo de Autenticação

```
┌─────────────────────────────────────────────┐
│         Tela de Login (login.fxml)           │
│  [usuário] [senha] [Entrar]                  │
└────────────────────┬────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────┐
│    LoginController.realizarLogin()           │
│  - Validar campos (não vazios)               │
│  - Preparar SQL (PreparedStatement)          │
│  - Executar: SELECT * FROM usuarios WHERE   │
│             usuario=? AND senha=?           │
└────────────────────┬────────────────────────┘
                     │
          ┌──────────┴──────────┐
          ▼                     ▼
      [Falha]             [Sucesso]
         │                   │
         │                   ▼
    Mostrar         ┌──────────────────────┐
    Alerta de      │ Criar objeto Usuarios │
    Erro           │ (id, nome, email)    │
         │         └──────────┬───────────┘
         │                    │
         │                    ▼
         │         ┌──────────────────────────┐
         │         │SessionManager.setUsuario │
         │         │Logado(u)                 │
         │         │(Guarda na memória)       │
         │         └──────────┬───────────────┘
         │                    │
         │                    ▼
         │         ┌──────────────────────────┐
         │         │LogDAO.registrar(u,       │
         │         │"[Login] Autenticado...")│
         │         │(Grava em auditoria)     │
         │         └──────────┬───────────────┘
         │                    │
         │                    ▼
         │         ┌──────────────────────────┐
         │         │  Abrir painel_geral.fxml │
         │         │  (Tela Principal)        │
         │         └──────────────────────────┘
         │
         └────────────► Retorna ao Login
```

### Sessão Ativa

Enquanto usuário está logado:
- `SessionManager.getUsuarioLogado()` retorna objeto do usuário
- Todas as operações verificam `SessionManager.isUsuarioLogado()`
- Logout chama `SessionManager.encerrarSessao()` (retorna null)

---

## 🔌 API de DAOs

### Padrão CRUD Utilizado

Cada DAO segue padrão de 4 operações:

```java
public class XxxDAO {
    // CREATE
    public static void salvar(Xxx obj)
    
    // READ
    public static List<Xxx> listarTodos()
    public static Xxx buscarPorId(int id)
    public static List<Xxx> buscarPorFiltro(String filtro)
    
    // UPDATE
    public static void atualizar(Xxx obj)
    
    // DELETE
    public static boolean excluir(int id)
}
```

### Exemplo: FuncionarioDAO

```java
// Salvar novo funcionário
Funcionario f = new Funcionario();
f.setNomeCompleto("João Silva");
f.setCpf("123.456.789-10");
f.setCargo("Desenvolvedor");
f.setDepartamento("TI");
FuncionarioDAO.salvar(f);

// Listar todos
List<Funcionario> lista = FuncionarioDAO.listarTodos();

// Buscar por ID
Funcionario f = FuncionarioDAO.buscarPorId(1);

// Atualizar
f.setCargo("Gerente");
FuncionarioDAO.atualizar(f);

// Deletar
FuncionarioDAO.excluir(1);

// Métodos especializados
double tempoMedio = FuncionarioDAO.calcularTempoMedioEmpresa();
Map<String, Integer> cargos = FuncionarioDAO.contarCargos();
```

### Exemplo: NotificacaoDAO

```java
// Criar notificação
Notificacao not = new Notificacao();
not.setUsuarioId(1);
not.setMensagem("Nova avaliação recebida");
not.setTipo("AVALIACAO");
NotificacaoDAO.salvar(not);

// Listar notificações não lidas
List<Notificacao> naoLidas = 
    NotificacaoDAO.buscarNaoLidas(usuarioId);

// Marcar como lida
NotificacaoDAO.marcarComoLida(notificacaoId);
```

### Tratamento de Exceções

Todos os DAOs tratam `SQLException`:

```java
try (Connection conn = ConnectionFactory.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {
    
    // Executar operação
    
} catch (SQLException e) {
    // Tratamento de erro
    e.printStackTrace();
    // Lançar ou retornar valor padrão
}
```

---

## 📊 Exportação de Dados

### Formatos Suportados

| Formato | Classe | Extensão |
|---------|--------|----------|
| PDF | `ExportadorPDF` / `PdfExportUtil` | .pdf |
| Excel | `ExportadorExcel` / `ExcelExportUtil` | .xlsx |
| CSV | `CSVExportUtil` | .csv |

### Exemplo de Uso

```java
// Exportar funcionários para PDF
List<Funcionario> lista = FuncionarioDAO.listarTodos();
ExportadorPDF.exportarFuncionarios(lista, "relatorio.pdf");

// Exportar para Excel
ExportadorExcel.exportarFuncionarios(lista, "funcionarios.xlsx");

// Exportar para CSV
CSVExportUtil.exportarFuncionarios(lista, "funcionarios.csv");
```

---

## 🎨 UI/UX

### Componentes Utilizados

- **JavaFX Controls:** Button, TextField, DatePicker, TableView, ComboBox, TextArea
- **ControlsFX:** Validação, diálogos customizados
- **BootstrapFX:** Temas CSS baseados em Bootstrap

### CSS

- **app.css:** Estilos globais
- **estilo.css:** Estilos específicos por tela

### Paleta de Cores

- Primária: Azul Wayne (#1E3A5F)
- Secundária: Cinza (#4A5568)
- Sucesso: Verde (#48BB78)
- Erro: Vermelho (#F56565)
- Aviso: Amarelo (#ED8936)

---

## 📝 Logging e Auditoria

### Sistema de Logs

Toda operação de negócio é registrada através de `LogDAO`:

```java
LogDAO.registrar(usuario, "[Login] Autenticado com sucesso");
LogDAO.registrar(usuario, "[RH] Funcionário cadastrado: João Silva");
LogDAO.registrar(usuario, "[Avaliacao] Nova avaliação criada");
```

### Estrutura de Auditoria

```sql
-- Log de cada operação
log_auditoria:
  - usuario_id: Quem fez
  - tabela: Qual tabela
  - operacao: INSERT/UPDATE/DELETE
  - dados_antigos: Antes
  - dados_novos: Depois
  - timestamp: Quando
```

---

## 🚨 Tratamento de Erros

### Padrão Utilizado

```java
try {
    // Operação
} catch (SQLException e) {
    e.printStackTrace(); // Log
    mostrarAlerta("Erro", "Mensagem amigável", Alert.AlertType.ERROR);
}
```

### Alertas de UI

```java
private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
    Alert alert = new Alert(tipo);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(msg);
    alert.showAndWait();
}
```

---

## 🔗 Dependências Maven

```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.2</version>
</dependency>
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.4.0</version>
</dependency>
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itextpdf</artifactId>
    <version>5.5.13.3</version>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.4</version>
</dependency>
<dependency>
    <groupId>com.sun.mail</groupId>
    <artifactId>jakarta.mail</artifactId>
    <version>2.0.1</version>
</dependency>
```

---

## 🎯 Próximos Passos

1. **Testes Unitários:** Implementar JUnit 5 para DAOs
2. **CI/CD:** Integração com Jenkins/GitHub Actions
3. **API REST:** Expor funcionalidades como REST API
4. **Mobile:** Versão mobile do sistema
5. **Notificações Push:** Push notifications via Firebase
6. **Sincronização:** Suporte para offline-first

---

## 📞 Suporte

- **Documentação:** Ver arquivos .md no projeto
- **Bugs:** Relatar através de issue tracker
- **Features:** Sugerir melhorias

---

## 📄 Licença

Propriedário - Wayne Enterprises (2026)

---

**Versão 1.0 - Setembro 2026**  
*Sistema Corporativo Integrado Wayne Enterprises*