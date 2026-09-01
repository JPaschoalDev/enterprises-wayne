# Relatório de Mudanças — Wayne Enterprises

**Data:** 01 de Setembro de 2026  
**Versão:** 1.0-SNAPSHOT  
**Status:** ✅ BUILD SUCCESS — todos os bugs críticos corrigidos

---

## 1. Compatibilidade de Versão Java (`pom.xml`)

**Problema:** O projeto declarava `<java.version>23</java.version>`, mas o JDK instalado na máquina é o OpenJDK 25. A flag `--enable-preview` exige que a versão alvo seja exatamente a versão do compilador, causando o erro:

```
invalid source release 23 with --enable-preview
(preview language features are only supported for release 25)
```

**Correção:**
```xml
<!-- Antes -->
<java.version>23</java.version>

<!-- Depois -->
<java.version>25</java.version>
```

---

## 2. NullPointerException em Datas — 13 ocorrências

**Causa raiz:** Quando um campo de data é `NULL` no banco de dados, `ResultSet.getDate()` retorna `null`. Chamar `.toLocalDate()` diretamente sobre esse valor causa `NullPointerException` em runtime, derrubando a aplicação.

**Padrão de correção aplicado em todos os casos:**

```java
// Antes (quebra se o banco retornar NULL)
evento.setData(rs.getDate("data_evento").toLocalDate());

// Depois (seguro)
Date sqlDate = rs.getDate("data_evento");
evento.setData(sqlDate != null ? sqlDate.toLocalDate() : null);
```

### Arquivos e locais corrigidos

| Arquivo | Método | Campo do banco |
|---|---|---|
| `DAO/AgendaCorporativaDAO.java` | `listarTodos()` | `data_evento` |
| `DAO/AvaliacaoDAO.java` | `filtrarAvaliacoes()` | `data_avaliacao` |
| `DAO/AvaliacaoDAO.java` | `listarTodas()` | `data_avaliacao` |
| `DAO/AvisoDAO.java` | `listarTodos()` | `data` |
| `DAO/EventoCorporativoDAO.java` | `listarTodos()` | `data_evento` |
| `DAO/TreinamentoDAO.java` | `listar()` | `data` |
| `controller/AvisoController.java` | `carregarAvisos()` | `data` |
| `controller/PainelEventosController.java` | `listarTodos()` | `data_evento` |

### Correção adicional em `CadastroController.java`

```java
// Antes — NPE se o usuário não preencher a data de admissão
LocalDate adm = (dataAdmissaoPicker != null) ? dataAdmissaoPicker.getValue() : null;
funcionario.setDataAdmissao(Date.valueOf(adm)); // quebra se adm == null

// Depois
funcionario.setDataAdmissao(adm != null ? Date.valueOf(adm) : null);
```

### Correção em `ParticipacaoTreinamentoDAO.java`

```java
// Antes — NPE se dataParticipacao for null
ps.setDate(3, Date.valueOf(dataParticipacao));

// Depois
ps.setDate(3, dataParticipacao != null ? Date.valueOf(dataParticipacao) : null);
```

---

## 3. Resource Leak — `FuncionarioDAO.java`

**Problema:** Em dois métodos (`buscarPorId` e `buscarPorNome`), o `ResultSet` era obtido fora do bloco `try-with-resources`. Se a leitura lançasse uma exceção ou o método retornasse no meio, o `ResultSet` ficava aberto indefinidamente, vazando recursos do banco de dados.

**Correção — `buscarPorId()`:**
```java
// Antes
ResultSet rs = stmt.executeQuery();
if (rs.next()) {
    // ...
    return f; // ResultSet não fechado!
}

// Depois
try (ResultSet rs = stmt.executeQuery()) {
    if (rs.next()) {
        // ...
        return f; // ResultSet fechado automaticamente ao sair do try
    }
}
```

A mesma correção foi aplicada no método `buscarPorNome()`.

---

## 4. Conversão Redundante — `EquipamentoController.java`

**Problema:** A data de aquisição (`dataAquisicao`) era do tipo `LocalDate`, mas estava sendo convertida para `java.sql.Date` e imediatamente de volta para `LocalDate`, sem nenhum efeito útil.

```java
// Antes — conversão ida-e-volta desnecessária
new Equipamento(tipo, numeroSerie, responsavel, status,
    Date.valueOf(dataAquisicao).toLocalDate());

// Depois — usa o valor diretamente
new Equipamento(tipo, numeroSerie, responsavel, status, dataAquisicao);
```

---

## 5. Import Duplicado — `TreinamentoDAO.java`

O import da classe `Treinamento` aparecia duas vezes consecutivas, causando aviso de compilação:

```java
// Antes
import com.wayne.wayneen.enterpriseswyne.model.Treinamento;
import com.wayne.wayneen.enterpriseswyne.model.Treinamento; // duplicado

// Depois
import com.wayne.wayneen.enterpriseswyne.model.Treinamento;
```

---

## 6. Anotação `@FXML` Indevida — `FuncionarioDAO.java`

**Problema:** A anotação `@FXML` (exclusiva de controllers JavaFX) estava presente em métodos de uma classe DAO e em métodos `static` de uma inner class, o que é semanticamente incorreto e pode gerar comportamento inesperado com injeção de dependência do JavaFX.

**Correção:** Removidas todas as ocorrências de `@FXML` dos métodos `buscarPorNome()`, `contarFuncionarios()` e `calcularMediaTempoPermanencia()` em `FuncionarioDAO`.

Também foi removido o import desnecessário:
```java
// Removido de FuncionarioDAO.java
import javafx.fxml.FXML;
```

---

## 7. Imports Duplicados — `PainelGeralController.java`

O arquivo possuía um bloco de imports inserido duas vezes, resultando em mais de 10 declarações duplicadas:

```java
// Duplicatas removidas:
import javafx.event.ActionEvent;   // aparecia 2x
import javafx.fxml.FXML;           // aparecia 2x
import javafx.fxml.FXMLLoader;     // aparecia 2x
import javafx.scene.Node;          // aparecia 2x
import javafx.scene.Parent;        // aparecia 2x
import javafx.scene.Scene;         // aparecia 2x
import javafx.scene.control.Alert; // aparecia 2x
import javafx.stage.Modality;      // aparecia 2x
import javafx.stage.Stage;         // aparecia 2x
import java.net.URL;               // aparecia 2x
import java.util.Objects;          // aparecia 2x
```

O bloco de imports foi consolidado em uma única seção limpa e organizada.

---

## Resumo das Mudanças por Arquivo

| Arquivo | Tipo de correção |
|---|---|
| `pom.xml` | Versão Java 23 → 25 |
| `controller/CadastroController.java` | NPE na data de admissão |
| `controller/AvisoController.java` | NPE na data do aviso |
| `controller/PainelEventosController.java` | NPE na data do evento |
| `controller/EquipamentoController.java` | Conversão de data redundante |
| `controller/PainelGeralController.java` | Imports duplicados |
| `DAO/AgendaCorporativaDAO.java` | NPE na data do evento |
| `DAO/AvaliacaoDAO.java` | NPE + ResultSet sem try-with-resources |
| `DAO/AvisoDAO.java` | NPE duplo na data |
| `DAO/EventoCorporativoDAO.java` | NPE na data do evento |
| `DAO/FuncionarioDAO.java` | Resource leak + @FXML indevido + import duplicado |
| `DAO/ParticipacaoTreinamentoDAO.java` | NPE na data de participação |
| `DAO/TreinamentoDAO.java` | NPE na data + import duplicado |

---

## Como Compilar

```bash
# Windows (PowerShell)
$env:JAVA_HOME = "C:\Users\<usuario>\.jdks\openjdk-25.0.2"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
.\mvnw.cmd compile
```

**Resultado esperado:** `BUILD SUCCESS`
