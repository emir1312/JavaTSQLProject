package main;

import java.sql.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*public void connection() {
		String dbHost="itnt0005";
		String dbPort="1433";
		String dbName="Spieler2_LAB";
		String dbUser="wkb4";
		String dbPass="wkb4";
		
		String connectionUrl="jdbc:sqlserver://"+dbHost+":"+dbPort+";"+"databaseName="+dbName+";"+"user="+dbUser+";"+"password="+dbPass+";";
		
		Connection con=null;
		PreparedStatement p_stmt=null;
		ResultSet rs=null;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con=DriverManager.getConnection(connectionUrl);
			
			String SQL="SELECT * FROM spieler";
			p_stmt=con.prepareStatement(SQL);
			rs=p_stmt.executeQuery();
			
			while(rs.next()) {
				System.out.println(rs.getString("vorname")+" "+rs.getString("name"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally{
			if(rs!=null) try {rs.close();} catch(Exception e) {}
			if(p_stmt!=null) try {p_stmt.close();} catch(Exception e) {}
			if(con!=null) try {con.close();} catch(Exception e) {}
		}
	}*/
	public static void main(String[] args) {
		
		String dbHost="itnt0005";
		String dbPort="1433";
		String dbName="Spieler2_LAB";
		String dbUser="wkb4";
		String dbPass="wkb4";
		
		String connectionUrl="jdbc:sqlserver://"+dbHost+":"+dbPort+";"+"databaseName="+dbName+";"+"user="+dbUser+";"+"password="+dbPass+";";
		
		Connection con=null;
		PreparedStatement p_stmt=null;
		ResultSet rs=null;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con=DriverManager.getConnection(connectionUrl);
			
			String SQL="SELECT * FROM spieler";
			p_stmt=con.prepareStatement(SQL);
			rs=p_stmt.executeQuery();
			
			while(rs.next()) {
				System.out.println(rs.getString("vorname")+" "+rs.getString("name"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally{
			if(rs!=null) try {rs.close();} catch(Exception e) {}
			if(p_stmt!=null) try {p_stmt.close();} catch(Exception e) {}
			if(con!=null) try {con.close();} catch(Exception e) {}
		}
		
		launch(args);
	}
}
