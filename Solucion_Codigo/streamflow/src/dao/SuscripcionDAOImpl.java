package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Calidad;
import modelo.Suscripcion;

/**
 * @author Emily
 */
public class SuscripcionDAOImpl implements SuscripcionDAO {

    private static final String URL = "jdbc:sqlite:db/streamflow.db";

    private Connection obtenerConexion() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        crearTabla(conn);
        return conn;
    }

    private void crearTabla(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS suscripcion ("
                + "usuario_email TEXT NOT NULL, "
                + "calidad TEXT NOT NULL, "
                + "costo_mensual REAL, "
                + "fecha_inicio TEXT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    @Override
    public boolean guardar(Suscripcion suscripcion) {
        String sql = "INSERT INTO suscripcion (usuario_email, calidad, costo_mensual, fecha_inicio) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, suscripcion.getUsuarioEmail());
            pstmt.setString(2, suscripcion.getCalidad().name());
            pstmt.setDouble(3, suscripcion.getCostoMensual());
            pstmt.setString(4, suscripcion.getFechaInicio());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al guardar suscripcion: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Suscripcion> listarPorUsuario(String usuarioEmail) {
        List<Suscripcion> lista = new ArrayList<>();
        String sql = "SELECT rowid, * FROM suscripcion WHERE usuario_email = ?";

        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuarioEmail);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar suscripciones del usuario: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Suscripcion> listarTodas() {
        List<Suscripcion> lista = new ArrayList<>();
        String sql = "SELECT rowid, * FROM suscripcion";

        try (Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar suscripciones: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Suscripcion buscarUltimaPorUsuario(String usuarioEmail) {
        String sql = "SELECT rowid, * FROM suscripcion WHERE usuario_email = ? ORDER BY rowid DESC LIMIT 1";

        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuarioEmail);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar la suscripcion vigente: " + e.getMessage());
        }
        return null;
    }

    private Suscripcion mapear(ResultSet rs) throws SQLException {
        Suscripcion suscripcion = new Suscripcion(
                rs.getString("usuario_email"),
                Calidad.valueOf(rs.getString("calidad")),
                rs.getDouble("costo_mensual"),
                rs.getString("fecha_inicio")
        );
        suscripcion.setId(rs.getLong("rowid"));
        return suscripcion;
    }
}

