package dd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

public class Main {

	private Connection con = null;
	private static Statement st = null;
	private static ResultSet rs = null;

	private static String url = "jdbc:mysql://localhost:3306/matchcoverage?autoReconnect=true&useSSL=false";
	private static String user = "root";
	private static String password = "root";
	private static String query = "select Time from eventdataset";

	public static void main(String[] args) {

		ResultSet rs = getAllTheData();
		createOutput(rs);

	}

	private static void createOutput(ResultSet rs2) {

		try {

			GenerateOutput go1 = new GenerateOutput();
			boolean ver = true;

			StringBuilder output = new StringBuilder();
			output.append("From-To,Total Post\n");

			while (rs2.next()) {
				String onlyTime = rs2.getString("Time").substring(8, 19);
				if (ver) {
					ver = false;
					go1 = new GenerateOutput(onlyTime);
				} else {

					GenerateOutput go2 = new GenerateOutput(onlyTime);
					int timeDif = calculateTimeDif(go1, go2);

					if (timeDif < GenerateOutput.checkTime) {
						go1.addCount();
					} else {
						output.append(go1.output());
						go1.makeNext();
					}
				}
			}

			if (go1.count !=0) {
				output.append(go1.output());
			}

			
			PrintWriter pw = null;
			
			try {
				pw = new PrintWriter(new File("NewData.csv"));
				pw.write(output.toString());
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("done!");

		} catch (

		SQLException e) {
			e.printStackTrace();
		}

	}

	private static int calculateTimeDif(GenerateOutput go1, GenerateOutput go2) {
		// TODO Auto-generated method stub

		int checkTime = GenerateOutput.checkTime;
		if (go1.date1 != go2.date1) {
			return checkTime + 1;
		}

		if (go1.hour1 != go2.hour1) {
			return checkTime + 1;
		}

		return Math.abs(go1.min1 - go2.min1);
	}

	private static ResultSet getAllTheData() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			return st.executeQuery(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
