package com.wayne.wayneen.enterpriseswyne.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/wayne_db";
    private static final String USUARIO = "root";
    private static final String SENHA = ""; // Insira a senha, se houver

    private static Connection conexao;
    private static final Logger logger = Logger.getLogger(ConnectionFactory.class.getName());

    // Método Singleton: retorna sempre a mesma conexão aberta
    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                logger.info("✅ Conexão com o banco estabelecida com sucesso.");
            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, "❌ Driver JDBC não encontrado!", e);
                throw new SQLException("Driver JDBC não encontrado.", e);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "❌ Erro ao conectar com o banco de dados!", e);
                throw e;
            }
        }
        return conexao;
    }

    // Método opcional para compatibilidade com bibliotecas externas
    public static Connection getConnection() throws SQLException {
        return getConexao();
    }

    // Fecha a conexão manualmente, se necessário
    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
                logger.info("🔒 Conexão com o banco fechada.");
            } catch (SQLException e) {
                logger.log(Level.WARNING, "⚠️ Erro ao fechar conexão.", e);
            }
        }
    }
    public static Connection conectar() throws SQLException {
        return getConexao();
    }

}
