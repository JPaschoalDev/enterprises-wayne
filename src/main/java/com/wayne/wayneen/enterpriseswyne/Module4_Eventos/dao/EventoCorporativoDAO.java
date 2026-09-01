package com.wayne.wayneen.enterpriseswyne.Module4_Eventos.dao;


import com.wayne.wayneen.enterpriseswyne.Module10_Core.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.Module4_Eventos.model.EventoCorporativo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoCorporativoDAO {

    public static List<EventoCorporativo> listarTodos() {
        List<EventoCorporativo> eventos = new ArrayList<>();
        String sql = "SELECT * FROM eventos_corporativos ORDER BY data_evento ASC";

        try (Connection conn = ConnectionFactory.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EventoCorporativo evento = new EventoCorporativo();
                evento.setId(rs.getInt("id"));
                evento.setTitulo(rs.getString("titulo"));
                evento.setDescricao(rs.getString("descricao"));
                Date sqlDate = rs.getDate("data_evento");
                evento.setData(sqlDate != null ? sqlDate.toLocalDate() : null);
                evento.setTipo(rs.getString("tipo_evento"));
                evento.setLocal(rs.getString("local"));
                evento.setResponsavel(rs.getString("responsavel"));
                eventos.add(evento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventos;
    }
}

