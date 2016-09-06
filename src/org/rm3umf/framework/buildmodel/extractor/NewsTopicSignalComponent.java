package org.rm3umf.framework.buildmodel.extractor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.News;
import org.rm3umf.domain.NewsEntity;
import org.rm3umf.domain.NewsTopic;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.PseudoFragment;
import org.rm3umf.domain.SignalComponent;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

public class NewsTopicSignalComponent implements StrategyExtraction {

	private static Logger logger = Logger.getLogger(NewsEntitySignalComponent.class);
	private String type = "NewsTopic";
	private String regex = "(http|https):(.)[^\\s]*";


	@Override
	public List<SignalComponent> extract(PseudoFragment pseudo) throws PersistenceException, ExtractorException {
		List<Message> listanews = pseudo.getMessages();
		List<SignalComponent> result = new LinkedList<SignalComponent>();
//		Map<Integer,SignalComponent> map2tf=new HashMap<Integer, SignalComponent>();
		SignalComponent sc = null;
		NewsTopic e = null;
		for(Message m: listanews) {
			//in realtà id message è l'id della news
			List<NewsTopic> listaen = AAFacadePersistence.getInstance().newstopicRetrieveByNewsId(m.getIdMessage(), this.type);
			for(NewsTopic ne: listaen) {
//				String id_en= ne.getName();
//				sc=map2tf.get(id_en);
//				int occurence;
//				if(sc==null){
					sc=new SignalComponent();
					sc.setEnrichment(ne); //setto la news
					sc.setPeriod(pseudo.getPeriod());   //setto il periodo
					sc.setOccorence(ne.getNumberInstance());//
					sc.setType(this.type);
					//setto l'utente
					sc.setUser(pseudo.getUser());
					result.add(sc);
//					map2tf.put(id_en,sc);
//				}
				//incrementa occurence
//				occurence=sc.getOccorence()+1;
//				sc.setOccorence(occurence);
//				//memorizzo il massimo
//				if(max<occurence)
//					max=occurence;
			}
		}
		return result;
	}

	public String getCriterion() {
		return this.type;
	}

	@Override
	public void exploreResource(boolean exploreResource) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Enrichment> filter(int tHRESHOLD) throws PersistenceException {
		return AAFacadePersistence.getInstance().newsTopicfilter(tHRESHOLD);
	}

	@Override
	public void delete(Enrichment enr) throws PersistenceException {
		AAFacadePersistence.getInstance().newsTopicDelete(enr);

	}

	@Override
	public List<Enrichment> retrieveAllEnrichment() throws PersistenceException {
		return AAFacadePersistence.getInstance().newsTopicRetrieveAll();

	}

	@Override
	public Map<String, Enrichment> retrieveEnrichmentUserAndPeriod(User user, int periodid) throws PersistenceException {
		return AAFacadePersistence.getInstance().newsTopicRetrieve4UserAndPeriod(user, periodid);
	}

	@Override
	public List<Message> doRetrieveByUserIdAndDate(User user, Period period)
			throws PersistenceException {
		return AAFacadePersistence.getInstance().newsTopicdoRetrieveByUserIdAndDate(user, period);
	}

}
