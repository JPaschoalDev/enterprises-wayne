package com.wayne.wayneen.enterpriseswyne.model;

import com.wayne.wayneen.enterpriseswyne.DAO.LogDAO;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupRestauracao {

    private static final String USUARIO = "root";
    private static final String NOME_BANCO = "wayne_db";

    // OBS: Para Windows, você deve garantir que o caminho para mysqldump/mysql esteja nas variáveis de ambiente

    public static boolean realizarBackup(Window parentWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Backup do Banco de Dados");
        fileChooser.setInitialFileName("backup_wayne_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".sql");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo SQL", "*.sql"));

        File arquivo = fileChooser.showSaveDialog(parentWindow);
        if (arquivo != null) {
            String caminho = arquivo.getAbsolutePath();

            ProcessBuilder pb = new ProcessBuilder(
                    "cmd.exe", "/c", // para Windows
                    String.format("mysqldump -u%s -p %s > \"%s\"", USUARIO, NOME_BANCO, caminho)
            );

            try {
                Process processo = pb.start();
                int status = processo.waitFor();

                if (status == 0) {
                    LogDAO.registrar("Backup realizado com sucesso: " + caminho);
                    return true;
                } else {
                    LogDAO.registrar("Erro ao realizar backup. Código de saída: " + status);
                    return false;
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                LogDAO.registrar("Erro durante o backup: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    public static boolean restaurarBackup(Window parentWindow, String senha) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Arquivo de Backup");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo SQL", "*.sql"));

        File arquivo = fileChooser.showOpenDialog(parentWindow);
        if (arquivo != null) {
            String caminho = arquivo.getAbsolutePath();

            String comando = String.format("mysql -u%s -p%s %s < \"%s\"", USUARIO, senha, NOME_BANCO, caminho);

            try {
                Process processo = Runtime.getRuntime().exec(
                        new String[]{"cmd.exe", "/c", comando}
                );

                int status = processo.waitFor();

                if (status == 0) {
                    LogDAO.registrar("Banco restaurado com sucesso: " + caminho);
                    return true;
                } else {
                    LogDAO.registrar("Erro ao restaurar banco. Código de saída: " + status);
                    return false;
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                LogDAO.registrar("Erro na restauração: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
}
