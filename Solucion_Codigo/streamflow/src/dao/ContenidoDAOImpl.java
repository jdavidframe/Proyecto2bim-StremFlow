package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import modelo.Calidad;
import modelo.Contenido;
import modelo.Documental;
import modelo.Pelicula;
import modelo.Serie;

/**
 * @author Emily
 * SQLite de ContenidoDAO.
 */
public class ContenidoDAOImpl implements ContenidoDAO {

    private static final String URL = "jdbc:sqlite:db/streamflow.db";

    private Connection obtenerConexion() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        crearTabla(conn);
        return conn;
    }

    private void crearTabla(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS contenido ("
                + "tipo TEXT NOT NULL, "
                + "titulo TEXT NOT NULL, "
                + "genero TEXT, "
                + "calidad TEXT NOT NULL, "
                + "duracion_minutos INTEGER, "
                + "temporadas INTEGER, "
                + "director TEXT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    @Override
    public boolean guardar(Contenido contenido) {
        String sql = "INSERT INTO contenido (tipo, titulo, genero, calidad, duracion_minutos, temporadas, director) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Tipo obtenido por reflexion, sin if/else (ver nota de clase).
            pstmt.setString(1, contenido.getClass().getSimpleName().toUpperCase());
            pstmt.setString(2, contenido.getTitulo());
            pstmt.setString(3, contenido.getGenero());
            pstmt.setString(4, contenido.getCalidadDisponible().name());

            setColumnasEspecificas(pstmt, contenido);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al guardar contenido: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Contenido contenido) {
        String sql = "UPDATE contenido SET titulo = ?, genero = ?, calidad = ?, "
                + "duracion_minutos = ?, temporadas = ?, director = ? WHERE rowid = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, contenido.getTitulo());
            pstmt.setString(2, contenido.getGenero());
            pstmt.setString(3, contenido.getCalidadDisponible().name());
            setColumnasEspecificasDesdeIndice4(pstmt, contenido);
            pstmt.setLong(7, contenido.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar contenido: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(long id) {
        String sql = "DELETE FROM contenido WHERE rowid = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar contenido: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Contenido> listarTodos() {
        List<Contenido> lista = new ArrayList<>();
        String sql = "SELECT rowid, * FROM contenido";

        try (Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar contenidos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Contenido> listarPorGenero(String genero) {
        List<Contenido> lista = new ArrayList<>();
        String sql = "SELECT rowid, * FROM contenido WHERE genero = ?";

        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, genero);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar contenidos por genero: " + e.getMessage());
        }
        return lista;
    }

    private void setColumnasEspecificas(PreparedStatement pstmt, Contenido contenido) throws SQLException {
        if (contenido instanceof Pelicula pelicula) {
            pstmt.setInt(5, pelicula.getDuracionMinutos());
        } else {
            pstmt.setNull(5, Types.INTEGER);
        }

        if (contenido instanceof Serie serie) {
            pstmt.setInt(6, serie.getTemporadas());
        } else {
            pstmt.setNull(6, Types.INTEGER);
        }

        if (contenido instanceof Documental documental) {
            pstmt.setString(7, documental.getDirector());
        } else {
            pstmt.setNull(7, Types.VARCHAR);
        }
    }

    private void setColumnasEspecificasDesdeIndice4(PreparedStatement pstmt, Contenido contenido) throws SQLException {
        if (contenido instanceof Pelicula pelicula) {
            pstmt.setInt(4, pelicula.getDuracionMinutos());
        } else {
            pstmt.setNull(4, Types.INTEGER);
        }

        if (contenido instanceof Serie serie) {
            pstmt.setInt(5, serie.getTemporadas());
        } else {
            pstmt.setNull(5, Types.INTEGER);
        }

        if (contenido instanceof Documental documental) {
            pstmt.setString(6, documental.getDirector());
        } else {
            pstmt.setNull(6, Types.VARCHAR);
        }
    }

    private Contenido mapear(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        String titulo = rs.getString("titulo");
        String genero = rs.getString("genero");
        Calidad calidad = Calidad.valueOf(rs.getString("calidad"));

        Contenido contenido = switch (tipo) {
            case "PELICULA" -> new Pelicula(titulo, genero, calidad, rs.getInt("duracion_minutos"));
            case "SERIE" -> new Serie(titulo, genero, calidad, rs.getInt("temporadas"));
            case "DOCUMENTAL" -> new Documental(titulo, genero, calidad, rs.getString("director"));
            default -> throw new IllegalStateException("Tipo de contenido desconocido: " + tipo);
        };

        contenido.setId(rs.getLong("rowid"));
        return contenido;
    }
}
