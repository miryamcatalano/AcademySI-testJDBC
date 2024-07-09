package test_Miryam_catalano;

import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		
		TestDatabase test = new TestDatabase();
		try {
			
			test.createDatabase("Libreria");
			test.useDatabase("Libreria");
			test.createTableUser("Utente", "Libreria", "id", "Nome", "Cognome");
			
			System.out.println("-------------------");
			
			System.out.println("Query n. 1 \n");
			test.getQuery1();
			System.out.println("Query n. 2 \n");
			test.getQuery2();
			System.out.println("Query n. 3 \n");
			test.getQuery3();
			System.out.println("Query n. 4 \n");
			test.getQuery4();
			System.out.println("Query n. 5 \n");
			test.getQuery5();
			System.out.println("Query n. 6 \n");
			test.getQuery6();

		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

}
