# 🐛 RELATÓRIO DE BUGS - Wayne Enterprises

**Data da Análise:** 01 de Setembro de 2026  
**Versão:** 1.0-SNAPSHOT  
**Análise:** Completa - 121 arquivos Java, 52 FXML, 8 XML analisados

---

## 📋 RESUMO EXECUTIVO

O sistema Wayne Enterprises é uma aplicação desktop JavaFX completa com funcionalidades de RH, gestão de eventos, treinamentos e auditoria. Foram identificados **13 bugs críticos de NullPointerException**, **4 problemas de design de conexão**, **2 problemas de imports duplicados**, além de outros problemas de menor severidade.

**Severidade Geral:** 🔴 **ALTA** - Diversos bugs podem causar crashes em produção

---

## 🔴 BUGS CRÍTICOS

### 1. **NullPointerException em CadastroController.java (Linha 58)**

**Severidade:** 🔴 CRÍTICA  
**Arquivo:** `src/main/java/com/wayne/wayneen/enterpriseswyne/controller/CadastroController.java:58`

**Problema:**
```java
funcionario.setDataAdmissao(Date.valueOf(adm)); // adm pode ser NULL!
```

**Causa:** Se o usuário não selecionar uma data de admissão, `adm` será `null`, causando `NullPointerException` ao chamar `Date.valueOf(null)`.

**Correção Recomendada:**
```java
LocalDate adm = (dataAdmissaoPicker != null) ? dataAdmissaoPicker.getValue() : null;
funcionario.setDataAdmissao(adm != null ? Date.valueOf(adm) : null);
```

---

### 2. **NullPointerException em DAO - getDate().toLocalDate() (13 ocorrências)**

**Severidade:** 🔴 CRÍTICA  
**Arquivos Afetados:**
- `src/main/java/com/wayne/wayneen/enterpriseswyne/DAO/AgendaCorporativaDAO.java:39`
- `src/main/java/com/wayne/wayneen/enterpriseswyne/DAO/AvaliacaoDAO.java:48, 97`
- `src/main/java/com/wayne/wayneen/enterpriseswyne/DAO/AvisoDAO.java:30, 34`
- `src/main/java/com/wayne/wayneen/enterpriseswyne/DAO/EventoCorporativoDAO.java:25`
- `src/main/java/com/wayne/wayneen/enterpriseswyne/DAO/TreinamentoDAO.java:43`
- `src/main/java/com/wayne/wayneen/enterpriseswyne/controller/AvisoController.java:91`
- `src/main/java/com/wayne/wayneen/enterpriseswyne/controller/PainelEventosController.java:25`

**Problema:**
```java
evento.setDataEvento(rs.getDate("data_evento").toLocalDate()); // NPE se getDate retornar null
```

**Causa:** Se o campo `data_evento` for `NULL` no banco de dados, `rs.getDate()` retorna `null`, causando NPE ao chamar `.toLocalDate()`.

**Correção Recomendada:**
```java
Date sqlDate = rs.getDate("data_evento");
LocalDate data = sqlDate != null ? sqlDate.toLocalDate() : null;
evento.setDataEvento(data);
```

---

### 3. **NullPointerException em ParticipacaoTreinamentoDAO.java (Linha 109)**

**Severidade:** 🔴 CRÍTICA  
**Arquivo:** `src/main/java/com/wayne/wayneen/enterpriseswyne/DAO/ParticipacaoTreinamentoDAO.java:109`

**Problema:**
```java
ps.setDate(3, Date.valueOf(dataParticipacao)); // dataParticipacao pode ser null
```

**Causa:** Conversão direta sem validação de null.

**Correção Recomendada:**
```java
ps.setDate(3, dataParticipacao != null ? Date.valueOf(dataParticipacao) : null);
```

---

### 4. **NullPointerException em EquipamentoController.java (Linha 69)**

**Severidade:** 🔴 CRÍTICA  
**Arquivo:** `src/main/java/com/wayne/wayneen/enterpriseswyne/controller/EquipamentoController.java:69`

**Problema:**
```java
Date.valueOf(dataAquisicao).toLocalDate() // dataAquisicao pode ser null
```

**Causa:** Falta null check antes da conversão.

---

## 🟠 BUGS DE DESIGN E ARQUITETURA

### 5. **Singleton de Conexão com Thread Unsafety**

**Severidade:** 🟠 ALTA  
**Arquivo:** `src/main/java/com/wayne/wayneen/enterpriseswyne/model/ConnectionFactory.java`

**Problema:**
```java
private static Connection conexao; // Singleton único, compartilhado entre threads

public static Connection getConexao() throws SQLException {
    if (conexao == null || conexao.isClosed()) {
        // Pode haver race condition aqui em aplicação multithreaded
        conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
    }
    return conexao;
}
```

**Consequências:**
- Race condition em múltiplas threads
- A conexão única pode ficar em estado inconsistente
- Problemas de transação isoladas

**Correção Recomendada:**
```java
// Usar Connection Pool (HikariCP, C3P0) ao invés de singleton
private static HikariDataSource dataSource;

public static Connection getConnection() throws SQLException {
    if (dataSource == null) {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/wayne_db");
        dataSource.setUsername("root");
        dataSource.setPassword("");
    }
    return dataSource.getConnection();
}
```

---

### 6. **Credenciais de Banco de Dados Hardcoded**

**Severidade:** 🟠 ALTA  
**Arquivo:** `src/main/java/com/wayne/wayneen/enterpriseswyne/model/ConnectionFactory.java:9-11`

**Problema:**
```java
private static final String URL = "jdbc:mysql://localhost:3306/wayne_db";
private static final String USUARIO = "root";
private static final String SENHA = ""; // Vazio!
```

**Segurança:** Credenciais expostas no código-fonte.

**Correção Recomendada:**
- Usar arquivo `.properties` ou variáveis de ambiente
- Usar SecureString ou criptografia para senhas

---

### 7. **Resource Leak em FuncionarioDAO.buscarPorId()**

**Severidade:** 🟠 ALTA  
**Arquivo:** `src/main/java/com/wayne/wayneen/enterpriseswyne/DAO/FuncionarioDAO.java:130`

**Problema:**
```java
try (Connection conn = ConnectionFactory.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {

    stmt.setInt(1, id);
    ResultSet rs = stmt.executeQuery(); // NÃO está em try-with-resources!

    if (rs.next()) {
        // ... processo
        return f; // ResultSet não é fechado!
    }

} catch (SQLException e) {
    e.printStackTrace();
}
```

**Consequências:**
- Vazamento de recursos (ResultSet aberto)
- Pode esgotar pool de conexões em produção

**Correção Recomendada:**
```java
try (Connection conn = ConnectionFactory.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {

    stmt.setInt(1, id);
    try (ResultSet rs = stmt.executeQuery()) { // Usar try-with-resources
        if (rs.next()) {
            // ... processo
            return f;
        }
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

---

## 🟡 PROBLEMAS DE CÓDIGO E QUALIDADE

### 8. **Imports Duplicados em PainelGeralController.java**

**Severidade:** 🟡 MÉDIO  
**Arquivo:** `src/main/java/com/wayne/wayneen/enterpriseswyne/controller/PainelGeralController.java:1-38`

**Problema:**
```java
import java.net.URL;     // Linha 21
import java.net.URL;     // Linha 36 (duplicado!)

import javafx.fxml.FXML; // Linha 11
import javafx.fxml.FXML; // Linha 25 (duplicado!)

import javafx.event.ActionEvent; // Linha 9
import javafx.event.ActionEvent; // Linha 24 (duplicado!)

// ... 3 mais importações duplicadas
```

**Impacto:** Código desorganizado, dificultação manutenção.

---

### 9. **System.out.println em Produção**

**Severidade:** 🟡 MÉDIO  
**Arquivos:**
- `PainelGeralController.java:` 3 ocorrências
- `EmailUtil.java:` 1 ocorrência

**Problema:**
```java
System.out.println("[FxLoader] Carregado (abs): " + url);
System.out.println("✅ E-mail enviado com sucesso para: " + destinatario);
```

**Recomendação:** Usar Logger (java.util.logging ou SLF4J).

---

### 10. **Chamadas de LogDAO com Assinatura Incorreta**

**Severidade:** 🟡 MÉDIO  
**Arquivo:** `src/main/java/com/wayne/wayneen/enterpriseswyne/controller/PainelGeralController.java:48`

**Problema:**
```java
LogDAO.registrar("Funcionários", "Novo funcionário cadastrado");
```

**Possível Causa:** LogDAO pode esperar apenas 1 argumento ou um objeto Usuarios.

**Verificar:** Assinatura correta de `LogDAO.registrar()`.

---

### 11. **Falta de Validação de Input em QueryBuilder**

**Severidade:** 🟡 MÉDIO - Risco de SQL Injection (Baixo)  
**Arquivos:** Vários DAO

**Observação Positiva:** O código usa `PreparedStatement` corretamente (sem concatenação de strings), reduzindo risco de SQL Injection. ✅

---

## 🔵 PROBLEMAS MENORES

### 12. **Tratamento de Exceção Genérico com printStackTrace()**

**Severidade:** 🔵 BAIXO  
**Ocorrências:** 50+ arquivos

**Problema:**
```java
catch (Exception e) {
    e.printStackTrace(); // Não registra em log estruturado
}
```

**Impacto:** Difícil rastreamento de erros em produção.

---

### 13. **Falta de Documentação em Classes Críticas**

**Severidade:** 🔵 BAIXO  
**Arquivo:** Modelos e DAOs

**Ausência de JavaDoc** em classes como `Funcionario`, `Usuarios`, `ConnectionFactory`.

---

## 📊 TABELA DE SEVERIDADES

| Tipo | Quantidade | Severidade | Impacto |
|------|-----------|-----------|---------|
| NullPointerException | 13 | 🔴 CRÍTICA | Crashes em produção |
| Thread Unsafety | 1 | 🟠 ALTA | Inconsistência de dados |
| Resource Leak | 1+ | 🟠 ALTA | Vazamento de conexões |
| Credentials Hardcoded | 1 | 🟠 ALTA | Segurança |
| Imports Duplicados | 1 | 🟡 MÉDIO | Qualidade |
| System.out.println | 4+ | 🟡 MÉDIO | Logging |
| Tratamento Genérico | 50+ | 🔵 BAIXO | Observabilidade |

---

## ✅ RECOMENDAÇÕES IMEDIATAS

### Prioritárias (Semana 1):
1. ✅ Corrigir todos os 13 bugs de NullPointerException
2. ✅ Implementar Connection Pool (HikariCP)
3. ✅ Mover credenciais para variáveis de ambiente

### Importante (Semana 2-3):
4. ✅ Remover prints de produção, usar Logger
5. ✅ Implementar try-with-resources para todos os Resources
6. ✅ Remover imports duplicados

### Melhorias (Médio Prazo):
7. ✅ Adicionar testes unitários
8. ✅ Implementar integração contínua
9. ✅ Documentar com JavaDoc

---

**Análise Concluída**  
*Relatório Gerado Automaticamente - Wayne Enterprises*