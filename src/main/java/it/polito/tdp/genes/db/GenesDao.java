package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Arco;
import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<String> getAllLocalizations(){
		String sql = "SELECT DISTINCT localization "
				+ "FROM classification "
				+ "ORDER BY localization ASC";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("localization"));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<Arco> getArchi(){
		String sql = "SELECT c1.Localization AS loc1,  c2.Localization AS loc2, TYPE "
				+ "FROM interactions, classification AS c1, classification AS c2 "
				+ "WHERE interactions.GeneID1 = c1.GeneID AND interactions.GeneID2 = c2.GeneID AND c1.Localization != c2.Localization "
				+ "GROUP BY loc1, loc2, type "
				+ "ORDER BY loc1, loc2, type";
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Arco a = new Arco(res.getString("loc1"),res.getString("loc2"));
				if (!result.contains(a)) {
					a.getTipi().add(res.getString("TYPE"));
					result.add(a);
				}
				else {
					int index = result.indexOf(a);
					if (!result.get(index).getTipi().contains(res.getString("TYPE")))
						result.get(index).getTipi().add(res.getString("TYPE"));
				}
				
			}
			
			for (Arco a : result)
				a.setPeso();
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}


	
}
