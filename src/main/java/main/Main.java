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
import java.text.DecimalFormat;

public class Main extends Application {
	
	private static Connection con;
	DecimalFormat formatter=new DecimalFormat("#0.0");
	
	@Override
	public void start(Stage primaryStage) {
		try {
			// Create the text input fields
	         TextField textField1 = new TextField();
	         TextField textField2 = new TextField();
	         TextField textField3 = new TextField();
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
	        gridPane.add(new Label("Studiengang:"), 0, 2);
	        gridPane.add(textField3, 1, 2);
	        gridPane.add(new Label("Note:"), 0, 3);
	        gridPane.add(numberField, 1, 3);

	        // Add the output field to the GridPane
	        gridPane.add(new Label(), 0, 4);
	        gridPane.add(outputArea, 1, 4);

	        // Create a button to perform some action
	        Button applyButton = new Button("Add");
	        Button deleteButton=new Button("Delete");
	        Button showAll= new Button("All students");
	       
	        applyButton.setOnAction(event->{
	        	String vorname=textField1.getText();
	        	String nachname=textField2.getText();
	        	String studiengang=textField3.getText();
				Double note=Double.valueOf(numberField.getText());
				
				outputArea.clear();
				
				//INSERT student stored procedure
				//insert trigger
				try {
					PreparedStatement callProcedure=con.prepareStatement("{call dbo.emmait01_InsertStudents(?, ?, ?, ?)}");
					//CallableStatement callProcedure=con.prepareCall("{call SWB_DB2_Projekt.dbo.emmait01_InsertStudents(?, ?, ?)}");
					callProcedure.setString(1, vorname);
					callProcedure.setString(2, nachname);
					callProcedure.setString(3, studiengang);
					callProcedure.setDouble(4, note);
					
					
					callProcedure.execute();
					callProcedure.close();
					
					String query="SELECT * FROM emmait01_GradeTable";
					PreparedStatement pstmt=con.prepareStatement(query);
					ResultSet rs=pstmt.executeQuery();
					
					while(rs.next()) {
						 String displayStudiengang = rs.getString("Studiengang");
						 Double displayAvgGrade=rs.getDouble("AvgNote");
						 
						 if(displayStudiengang!=null) {
							 outputArea.appendText(displayStudiengang+": "+formatter.format(displayAvgGrade)+"\n");
						 }
					}
					
					
					pstmt.close();
					rs.close();
					//rs.close();
					//callProcedure.close();
					//callAverage.close();
					
				}catch(Exception e) {
					e.printStackTrace();
				}
	        });
	        
	        deleteButton.setOnAction(event->{
	        	
	        	String vorname=textField1.getText();
	        	String nachname=textField2.getText();
	        	
	        	outputArea.clear();
	        	
	        	try {
					PreparedStatement callProcedure=con.prepareStatement("{call dbo.emmait01_DeleteStudent(?, ?)}");
					//CallableStatement callProcedure=con.prepareCall("{call SWB_DB2_Projekt.dbo.emmait01_InsertStudents(?, ?, ?)}");
					callProcedure.setString(1, vorname);
					callProcedure.setString(2, nachname);
					callProcedure.execute();
					callProcedure.close();
					
					String query="SELECT * FROM emmait01_GradeTable";
					PreparedStatement pstmt=con.prepareStatement(query);
					ResultSet rs=pstmt.executeQuery();
					
					while(rs.next()) {
						 String displayStudiengang = rs.getString("Studiengang");
						 Double displayAvgGrade=rs.getDouble("AvgNote");
						 
						 if(displayStudiengang!=null) {
							 outputArea.appendText(displayStudiengang+": "+formatter.format(displayAvgGrade)+"\n");
						 }
					}
					
	        	}catch(Exception e) {
	        		e.printStackTrace();
	        	}
	        });
	        
	        showAll.setOnAction(event->{
	        	//cursor
                outputArea.clear();
				
				//INSERT student stored procedure
				//insert trigger
				try {
					PreparedStatement callProcedure=con.prepareStatement("{call dbo.emmait01_StudentCursor}");				
					
					callProcedure.execute();
					callProcedure.close();
					
					String query="SELECT * FROM emmait01_StudentTable";
					PreparedStatement pstmt=con.prepareStatement(query);
					ResultSet rs=pstmt.executeQuery();
					
					while(rs.next()) {
						 String displayName=rs.getString("Vorname");
						 String displayNachName=rs.getString("Nachname");
						 Double displayNote=rs.getDouble("Note");
						 String displayBestanden=rs.getString("Bestanden");
						 String displayStudiengang = rs.getString("Studiengang");
						 Double displayAvgGrade=rs.getDouble("AvgNote");
						 
						 if(displayStudiengang!=null) {
							 outputArea.appendText(displayName+" "+displayNachName+", "+displayStudiengang+"  "+"Status: "+displayBestanden+", Note: "+formatter.format(displayNote)+" Durchsch. "+formatter.format(displayAvgGrade)+"\n");
						 }
					}
					
					
					pstmt.close();
					rs.close();
					//rs.close();
					//callProcedure.close();
					//callAverage.close();
					
				}catch(Exception e) {
					e.printStackTrace();
				}
	        	
	        });

	        // Add the button to the GridPane
	        gridPane.add(applyButton, 0, 5);
	        gridPane.add(deleteButton, 0, 6);
	        gridPane.add(showAll, 1, 5);
	        
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
		String dbName="SWB_DB2_Projekt";
		String dbUser="wkb4";
		String dbPass="wkb4";
		
		String connectionUrl="jdbc:sqlserver://"+dbHost+":"+dbPort+";"+"databaseName="+dbName+";"+"user="+dbUser+";"+"password="+dbPass+";"+"encrypt=true;trustServerCertificate=true";
		
		con=null;
		PreparedStatement p_stmt=null;
		ResultSet rs=null;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con=DriverManager.getConnection(connectionUrl);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		/*finally{
			if(rs!=null) try {rs.close();} catch(Exception e) {}
			if(p_stmt!=null) try {p_stmt.close();} catch(Exception e) {}
			if(con!=null) try {con.close();} catch(Exception e) {}
		}*/
		
		launch(args);
	}
}
