# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

---

## Build e Execução

**JDK instalado:** `C:\Users\João Paschoal\.jdks\openjdk-25.0.2`

```powershell
# Configurar antes de qualquer comando Maven no PowerShell
$env:JAVA_HOME = "C:\Users\João Paschoal\.jdks\openjdk-25.0.2"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Compilar
.\mvnw.cmd compile

# Empacotar
.\mvnw.cmd package

# Executar via plugin JavaFX
.\mvnw.cmd javafx:run
```

**Para rodar pelo IntelliJ:** File → Project Structure → SDKs → adicionar `C:\Users\João Paschoal\.jdks\openjdk-25.0.2`, depois setar como SDK do projeto e módulo.

Ponto de entrada: `Main.java` → carrega `Home.fxml` como tela inicial.

---

## Arquitetura

Aplicação desktop JavaFX com banco de dados MySQL local (`wayne_db`). Segue o padrão **MVC manual**:

```
src/main/java/com/wayne/wayneen/enterpriseswyne/
├── controller/   ← JavaFX Controllers (lógica de tela, eventos @FXML)
├── model/        ← Entidades POJO + classes de serviço e utilidade
├── DAO/          ← Acesso ao banco via JDBC puro
└── Main.java     ← Application entry point

src/main/resources/com/wayne/wayneen/enterpriseswyne/
└── *.fxml        ← 52 arquivos FXML, um por tela
```

### Pacotes com declaração incorreta (comportamento conhecido)
Alguns arquivos em `controller/` e `model/` declaram `package com.wayne.wayneen.enterpriseswyne;` (pacote raiz) em vez do subpacote correto. Maven compila mesmo assim. FXML referencia esses controllers com o pacote raiz. **Não altere a declaração de pacote sem atualizar o `fx:controller` correspondente no FXML.**

---

## Fluxo de Navegação

```
Home.fxml (LoginController)
  └── Login bem-sucedido → painel_geral.fxml (PainelGeralController)
        └── Cada botão abre uma nova Stage com seu FXML
```

- Cada tela é aberta como um novo `Stage` independente — não há scene-switching.
- `PainelGeralController` é o hub central; todos os `abrirTela*()` usam `FXMLLoader`.

---

## Camadas e Contratos

### ConnectionFactory (`model/ConnectionFactory.java`)
Singleton de conexão JDBC. Dois métodos públicos equivalentes: `getConexao()` e `getConnection()`. DAOs usam qualquer um dos dois.

```java
// Padrão obrigatório em todo DAO — sempre try-with-resources
try (Connection conn = ConnectionFactory.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql);
     ResultSet rs = stmt.executeQuery()) { ... }
```

**Atenção:** Não é thread-safe (usa conexão singleton). Adequado apenas para uso desktop single-thread.

### SessionManager (`model/SessionManager.java`)
Singleton estático que guarda o `Usuarios` logado. Acessado por qualquer controller ou serviço.

```java
SessionManager.setUsuarioLogado(u);   // no login
SessionManager.getUsuarioLogado();    // em qualquer lugar
SessionManager.encerrarSessao();      // no logout
```

### LogDAO / AuditoriaService
Toda ação relevante deve ser registrada. Use a sobrecarga mais adequada:

```java
LogDAO.registrar("Descrição simples");                       // usuário da sessão
LogDAO.registrar(usuario, "Descrição");                      // usuário explícito
LogDAO.registrar("ModuloOrigem", "Descrição");               // dois strings
AuditoriaService.registrarCrud("Funcionario", "CADASTRO", "ID=10");
```

### Padrão de data — regra crítica
Datas do banco (`rs.getDate()`) retornam `null` quando o campo é `NULL`. **Sempre** use null-check antes de `.toLocalDate()`:

```java
Date sqlDate = rs.getDate("data_campo");
entidade.setData(sqlDate != null ? sqlDate.toLocalDate() : null);
```

Da UI para o banco, use:
```java
campo.setDataAdmissao(adm != null ? Date.valueOf(adm) : null);
```

### Validação de Funcionário
`ValidacaoUtil.validarCamposObrigatorios(Funcionario f)` — valida nome, CPF, e-mail, cargo, departamento e datas de admissão/nascimento antes de persistir.

---

## FXML ↔ Controller

O link é feito pelo atributo `fx:controller` no FXML. Exemplo:

```xml
<!-- painel_geral.fxml -->
fx:controller="com.wayne.wayneen.enterpriseswyne.controller.PainelGeralController"

<!-- equipamento.fxml — usa pacote raiz por declaração incorreta -->
fx:controller="com.wayne.wayneen.enterpriseswyne.EquipamentoController"
```

Para localizar o FXML de um controller: buscar o nome do arquivo `.fxml` que referencia a classe.

---

## Exportação e Relatórios

| Utilitário | Formato |
|---|---|
| `ExportadorPDF` / `PdfExportUtil` | PDF via iTextPDF 5 |
| `ExportadorExcel` / `ExcelExportUtil` | Excel via Apache POI |
| `CSVExportUtil` | CSV |
| `PDFGenerator` / `RelatorioUtil` | PDF de relatórios |

---

## Banco de Dados

- **Banco:** `wayne_db` em MySQL local (`localhost:3306`)
- **Usuário:** `root`, **Senha:** (vazia por padrão)
- Não há migrations automáticas. O schema deve existir antes de rodar.
- Tabelas principais: `funcionarios`, `usuarios`, `log_acoes`, `avisos`, `eventos_corporativos`, `treinamentos`, `equipamentos`, `avaliacoes`, `participacoes_treinamento`, `chamados`

---

## Dependências relevantes

| Dependência | Uso |
|---|---|
| JavaFX 21.0.2 | UI (controls, fxml, graphics, web) |
| MySQL Connector 8.4 | JDBC |
| iTextPDF 5.5.13 | Geração de PDF |
| Apache POI 5.2.4 | Geração de Excel |
| Jakarta Mail 2.0.1 | Envio de e-mail |
| ControlsFX 11.2.1 | Componentes UI extras |
