package main;


import java.sql.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Connection con;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			// Create the text input fields
	         TextField textField1 = new TextField();
	         TextField textField2 = new TextField();
	         TextField numberField = new TextField();

	        // Create the text output field
	        TextArea outputArea = new TextArea();
	        outputArea.setEditable(false);

	        // Create a GridPane layout to arrange the components
	        GridPane gridPane = new GridPane();
	        gridPane.setPadding(new Insets(10));
	        gridPane.setHgap(10);
	        gridPane.setVgap(10);

	        // Add labels and input fields to the GridPane
	        gridPane.add(new Label("Vorname:"), 0, 0);
	        gridPane.add(textField1, 1, 0);
	        gridPane.add(new Label("Nachname:"), 0, 1);
	        gridPane.add(textField2, 1, 1);
	        gridPane.add(new Label("Note:"), 0, 2);
	        gridPane.add(numberField, 1, 2);

	        // Add the output field to the GridPane
	        gridPane.add(new Label(), 0, 3);
	        gridPane.add(outputArea, 1, 3);

	        // Create a button to perform some action
	        Button applyButton = new Button("Add student");
	        Button showAll= new Button("All students");
	       
	        applyButton.setOnAction(event->{
	        	String vorname=textField1.getText();
	        	String nachname=textField2.getText();
				Integer note=Integer.valueOf(numberField.getText());
				
				//INSERT student stored procedure
				//insert trigger
				try {
					CallableStatement callProcedure=con.prepareCall("{call dbo.InsertStudent(?, ?, ?)}");
					callProcedure.setString(0, vorname);
					callProcedure.setString(1, nachname);
					callProcedure.setInt(2, note);
					
					callProcedure.executeUpdate();
					
					Statement callAverage=con.createStatement();
					ResultSet rs=callAverage.executeQuery("SELECT Average FROM NoteAverage");
					
					outputArea.clear();
					outputArea.setText("Durchschnitt: "+rs.toString());
					
					rs.close();
					callProcedure.close();
					callAverage.close();
					
				}catch(Exception e) {
					e.printStackTrace();
				}
	        });
	        
	        showAll.setOnAction(event->{
	        	//cursor
	        	try {
	        		// Create the CallableStatement for calling the stored procedure
	                CallableStatement callCursor = con.prepareCall("{CALL GetStudents}");

	                // Execute the stored procedure
	                callCursor.execute();

	                // Retrieve the output from the stored procedure using the getMoreResults and getResultSet methods
	                int updateCount = -1;
	                while (true) {
	                    if (callCursor.getMoreResults()) {
	                        ResultSet rs = callCursor.getResultSet();

	                        while (rs.next()) {
	                            String vorname = rs.getString(1);
	                            String nachname = rs.getString(2);
	                            int note = rs.getInt(3);

	                            // Append the student information to the outputArea
	                            outputArea.appendText("Vorname: " + vorname + "\n");
	                            outputArea.appendText("Nachname: " + nachname + "\n");
	                            outputArea.appendText("Note: " + note + "\n");
	                            outputArea.appendText("\n");
	                        }

	                        rs.close();
	                    } else {
	                        updateCount = callCursor.getUpdateCount();
	                        if (updateCount == -1) {
	                            break;
	                        }
	                    }
	                }

	                // Close the CallableStatement
	                callCursor.close();
	                
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        });

	        // Add the button to the GridPane
	        gridPane.add(applyButton, 0, 4);
	        gridPane.add(showAll, 1, 4);
	        
	        ColumnConstraints col1 = new ColumnConstraints();
	        col1.setPercentWidth(50);
	        ColumnConstraints col2 = new ColumnConstraints();
	        col2.setPercentWidth(50);
	        gridPane.getColumnConstraints().addAll(col1, col2);

	        // Create the scene and set it on the primary stage
	        Scene scene = new Scene(gridPane, 400, 300);
	        primaryStage.setScene(scene);
	        primaryStage.setTitle("DB2Lab-Noten");
	        primaryStage.show();
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		String dbHost="itnt0005";
		String dbPort="1433";
		String dbName="Spieler2_LAB";
		String dbUser="wkb4";
		String dbPass="wkb4";
		
		String connectionUrl="jdbc:sqlserver://"+dbHost+":"+dbPort+";"+"databaseName="+dbName+";"+"user="+dbUser+";"+"password="+dbPass+";";
		
		con=null;
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
