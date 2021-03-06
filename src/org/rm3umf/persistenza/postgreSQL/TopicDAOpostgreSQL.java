package org.rm3umf.persistenza.postgreSQL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.NewsTopic;
import org.rm3umf.domain.TweetEntity;
import org.rm3umf.domain.TweetTopic;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.DataSource;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.TopicDAO;
import org.rm3umf.persistenza.TweetsEntityDAO;
import org.rm3umf.persistenza.TweetsTopicDAO;


public class TopicDAOpostgreSQL implements TopicDAO{

	private Logger logger = Logger.getLogger(TweetsTopicDAOpostgreSQL.class);



	@Override
	public List<Enrichment> getTopicFromTweetId(String tweetId) throws PersistenceException {
		List<Enrichment> lista = new LinkedList<Enrichment>();
		Map<String, Enrichment> mappa = new HashMap<String, Enrichment>();
		Enrichment ne = null;
		String name;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select topic, Count(*) "
					+ "from new_news_recommendation.semanticsTweetsTopic "
					+ "where tweetId=? "
					+ "Group By semanticsTweetsTopic.topic "
					+ "union all "
					+ "select semanticsNewsTopic.topic, Count(*) "
					+ "from new_news_recommendation.semanticsNewsTopic, user_news "
					+ "where id_news=newsId and id_tweet=? "
					+ "Group By semanticsNewsTopic.topic  ";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, tweetId);
			statement.setString(2, tweetId);
			result = statement.executeQuery();
			while(result.next()){
				name = result.getString(1);
				ne = mappa.get(name);
				if(ne != null) {
					int occ = ne.getNumberInstance();
					occ += result.getInt(2);
				} else {
					ne = new Enrichment();
					ne.setName(name);
					ne.setNumberInstance(result.getInt(2));
					mappa.put(name, ne);
					lista.add(ne);
				}
			}
		}catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return lista;
	}

	@Override
	public List<Enrichment> retrieveByTopic(String topic) throws PersistenceException{
		List<Enrichment> lista = new LinkedList<Enrichment>();
		Map<String, Enrichment> mappa = new HashMap<String, Enrichment>();
		Enrichment ne = null;
		String name;
		DataSource ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select topic, Count(*) "
					+ "from new_news_recommendation.semanticsTweetsTopic "
					+ "where topic=? "
					+ "union all "
					+ "select topic, Count(*) "
					+ "from new_news_recommendation.semanticsNewsTopic "
					+ "where topic=?";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, topic);
			statement.setString(2, topic);
			result = statement.executeQuery();
			while(result.next()){
				name = result.getString(1);
				ne = mappa.get(name);
				if(ne != null) {
					int occ = ne.getNumberInstance();
					occ += result.getInt(2);
				} else {
					ne = new Enrichment();
					ne.setName(name);
					ne.setNumberInstance(result.getInt(2));
					mappa.put(name, ne);
					lista.add(ne);
				}
			}
		}catch (SQLException e1) {
			throw new PersistenceException(e1.getMessage());
		}
		finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e1) {
				throw new PersistenceException(e1.getMessage());
			}
		}
		return lista;
	}

	@Override
	public List<Enrichment> retrieveAll() throws PersistenceException {
		List<Enrichment> lista = new LinkedList<Enrichment>();
		Map<String, Enrichment> mappa = new HashMap<String, Enrichment>();
		Enrichment ne = null;
		String name;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT topic, COUNT(*) "
					+ "FROM new_news_recommendation.semanticsTweetsTopic "
					+ "GROUP BY topic "
					+ "union all "
					+ "SELECT topic, COUNT(*) "
					+ "FROM new_news_recommendation.semanticsNewsTopic "
					+ "GROUP BY topic";
			statement = connection.prepareStatement(retrieve);
			result = statement.executeQuery();
			while(result.next()){
				name = result.getString(1);
				ne = mappa.get(name);
				if(ne != null) {
					int occ = ne.getNumberInstance();
					occ += result.getInt(2);
				} else {
					ne = new Enrichment();
					ne.setName(name);
					ne.setNumberInstance(result.getInt(2));
					mappa.put(name, ne);
					lista.add(ne);
				}
			}
		}catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return lista;
	}

	@Override
	public Map<String, Enrichment> retrieve4UserAndPeriod(User user, int periodid) throws PersistenceException {
		List<Enrichment> lista = new LinkedList<Enrichment>();
		Map<String, Enrichment> mappa = new HashMap<String, Enrichment>();
		Enrichment ne = null;
		String name;
		DataSource ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT topic , count(*) "
					+ "FROM new_news_recommendation.semanticsTweetsTopic, new_news_recommendation.tweets_sample "
					+ "where tweets_sample.userid=? and id=tweetId "
					+ "AND tweets_sample.creationTime > "
					+ "(Select enddate FROM new_news_recommendation.period Where id=?) "
					+ "Group BY topic "
					+ "union all "
					+ "SELECT topic , count(*) "
					+ "FROM new_news_recommendation.semanticsNewsTopic, new_news_recommendation.tweets_sample, new_news_recommendation.user_news "
					+ "where tweets_sample.userid=? and userid=id_user and id_news=newsId "
					+ "AND tweets_sample.creationTime > "
					+ "(Select enddate FROM new_news_recommendation.period Where id=?) "
					+ "Group BY topic";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user.getIduser());
			statement.setInt(2, periodid-1);
			statement.setLong(3, user.getIduser());
			statement.setInt(4, periodid-1);
			result = statement.executeQuery();
			while(result.next()){
				name = result.getString(1);
				ne = mappa.get(name);
				if(ne != null) {
					int occ = ne.getNumberInstance();
					occ += result.getInt(2);
				} else {
					ne = new Enrichment();
					ne.setName(name);
					ne.setNumberInstance(result.getInt(2));
					mappa.put(name, ne);
					lista.add(ne);
				}
			}
		}catch (SQLException e1) {
			throw new PersistenceException(e1.getMessage());
		}
		finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e1) {
				throw new PersistenceException(e1.getMessage());
			}
		}
		return mappa;
	}
	
	
	public List<Message> doRetrieveByUserIdAndDate(User user,Period period) throws PersistenceException {
		String dataInizio=period.getDataInizioPeriodo();
		String dataFine=period.getDataFinePeriodo();
		Message tweet = null;
		List<Message> listaTweet = new ArrayList<Message>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select id,content,creationTime from tweets_sample where creationTime>=? and creationTime<=? and userId=?";
			statement = connection.prepareStatement(retrieve);
			statement.setTimestamp(1, Timestamp.valueOf(dataInizio));
			statement.setTimestamp(2, Timestamp.valueOf(dataFine));
			statement.setLong(3, user.getIduser());
			result = statement.executeQuery();
			while (result.next()) {
				tweet = new Message();
				tweet.setIdMessage(result.getString(1));
				//oppure posso creare un UserProxy
				tweet.setUser(user);
				tweet.setText(result.getString(2));
				tweet.setDate(result.getDate(3).toString());
				listaTweet.add(tweet);
			}
		}
		catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return listaTweet;
	}
}



