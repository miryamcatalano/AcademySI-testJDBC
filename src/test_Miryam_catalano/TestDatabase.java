package test_Miryam_catalano;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

public class TestDatabase {
	
	private Connection con;
	
	protected Connection startConnection(String database) throws SQLException {
		
	if(con == null) {
				
				MysqlDataSource dataSource = new MysqlDataSource();
				dataSource.setServerName("127.0.0.1");
				dataSource.setPortNumber(3306);
				dataSource.setUser("root");
				dataSource.setPassword("admin");
				dataSource.setDatabaseName(database);
				
				con = dataSource.getConnection();
			}
			
			return con;
		
	}
	
	protected void createDatabase(String db_name) throws SQLException{
		
		String create = "CREATE DATABASE IF NOT EXISTS " + db_name;
				
		PreparedStatement ps = startConnection(null).prepareStatement(create);
		
		ps.executeUpdate();
		
		System.out.println("Database " + db_name + " creato con successo");
		
		ps.close();
	}
	
	protected void useDatabase(String db) throws SQLException {
		
		String use = "Use " + db;
		
		PreparedStatement ps = startConnection("Libreria").prepareStatement(use);

		ps.executeUpdate();

		System.out.println("Stai usando il database " + db);
		
		ps.close();
	}
	
	protected void createTableUser(String tab, String db, String id, String col1, String col2) throws SQLException {
		
		
		String crtUser = "CREATE TABLE IF NOT EXISTS " + tab 
						+ " (" + id + " INT PRIMARY KEY, "
						+ col1 + " VARCHAR(100) NOT NULL, "
						+ col2 + " VARCHAR(100) NOT NULL)";
		
		PreparedStatement ps = startConnection("Libreria").prepareStatement(crtUser);
		
		ps.executeUpdate();
		
		System.out.println("Tabella " + tab + " creata con successo");
		
		ps.close();
		
	}
	
	protected void getQuery1() throws SQLException {
		
		String sql = "SELECT l.titolo, p.data_inizio \r\n"
				+ "FROM libro l \r\n"
				+ "JOIN prestito p ON l.id = p.id_L\r\n"
				+ "JOIN utente u ON p.id_U = u.id\r\n"
				+ "WHERE u.cognome = \"Vallieri\"\r\n"
				+ "ORDER BY p.data_inizio";
		
		PreparedStatement ps = startConnection("Libreria").prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			
			System.out.println("Libro " + rs.getString(1) + " prestato in data prestito: " + rs.getDate(2));
		}
		
		System.out.println("-------------------------");
		rs.close();
		ps.close();
		
	}
	
	protected void getQuery2() throws SQLException {
		
		String sql = "SELECT u.nome, u.cognome, COUNT(p.id_L) AS numero_libri_letti\r\n"
				+ "FROM prestito p\r\n"
				+ "JOIN utente u ON p.id_U = u.id\r\n"
				+ "GROUP BY u.nome, u.cognome\r\n"
				+ "ORDER BY numero_libri_letti DESC\r\n"
				+ "LIMIT 3";
		
		PreparedStatement ps = startConnection("Libreria").prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			
			System.out.println("L'utente " + rs.getString(1) + " " + rs.getString(2) + " ha letto " + rs.getInt(3) + " libri");
		}
		
		System.out.println("-------------------------");
		rs.close();
		ps.close();
		
	}

	protected void getQuery3() throws SQLException {
	
		String sql = "SELECT u.nome, u.cognome, l.titolo, p.data_inizio\r\n"
				+ "FROM prestito p\r\n"
				+ "JOIN utente u ON p.id_U = u.id\r\n"
				+ "JOIN libro l ON p.id_L = l.id\r\n"
				+ "WHERE p.data_fine IS NULL";
		
		PreparedStatement ps = startConnection("Libreria").prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			
			System.out.println("L'utente " + rs.getString(1) + " " + rs.getString(2) + " deve ancora restituire il libro " + rs.getString(3) + " prestato in data " + rs.getString(4));
		}
		
		System.out.println("-------------------------");
		rs.close();
		ps.close();
		
	}

	protected void getQuery4() throws SQLException {
		
		String sql = "SELECT l.titolo, p.data_inizio, p.data_fine\r\n"
				+ "FROM prestito p\r\n"
				+ "JOIN utente u ON p.id_U = u.id\r\n"
				+ "JOIN libro l ON p.id_L = l.id\r\n"
				+ "WHERE u.nome = \"Marzia\" && u.cognome = \"Esposito\"";
		
		PreparedStatement ps = startConnection("Libreria").prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		System.out.println("L'utente Marzia Esposito ha letto i seguenti libri: ");
		
		while(rs.next()) {
			
			System.out.println(rs.getString(1) + " dalla data" + rs.getDate(2) + " alla data " + rs.getDate(3));
		}
		
		System.out.println("-------------------------");
		rs.close();
		ps.close();
		
	}
	
	protected void getQuery5() throws SQLException {
		
		String sql = "SELECT l.titolo, COUNT(*) AS numero_prestiti\r\n"
				+ "FROM prestito AS p \r\n"
				+ "JOIN libro AS l ON p.id_L = l.id\r\n"
				+ "GROUP BY p.id_L\r\n"
				+ "ORDER BY numero_prestiti DESC";
		
		PreparedStatement ps = startConnection("Libreria").prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			
			System.out.println("Il libro " + rs.getString(1) + " è stato prestato " + rs.getInt(2) + " volte ");
		}
		
		System.out.println("-------------------------");
		rs.close();
		ps.close();
		
	}
	
	protected void getQuery6() throws SQLException {
		
		String sql = "SELECT l.titolo, u.nome, u.cognome\r\n"
				+ "FROM libro l \r\n"
				+ "JOIN prestito p ON l.id = p.id_L\r\n"
				+ "JOIN utente u ON p.id_U = u.id\r\n"
				+ "WHERE DATEDIFF(p.data_fine, p.data_inizio) > 15";
		
		PreparedStatement ps = startConnection("Libreria").prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		System.out.println("I libri prestati per più di 15 giorni sono: ");
		
		while(rs.next()) {
			
			System.out.println("Titolo: " + rs.getString(1) + ", utente: " + rs.getString(2) + " " + rs.getString(3));
		}
		
		System.out.println("-------------------------");
		rs.close();
		ps.close();
		
	}

	
}



