package org.rm3umf.framework.buildmodel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.rm3umf.domain.Period;




/**
 *Questa classe dato un numero N di giorni e una dataMax e una dataMin suddive il periodo 
 *(dataMin,dataMax) in intervalli di N giorni.
 * 
 * @author Giulz
 *
 */

public class FactoryPeriod {

	private static final Logger logger = Logger.getLogger(FactoryPeriod.class);

	/**
	 * @param numberOfDay
	 */

	public FactoryPeriod(){

	}

	/**
	 * Data una dataStart e dataEnd suddifivde il periodo che va da startDate-endDate in sottoperiodi
	 * di lunghezza numberOfDay
	 * @param startDate
	 * @param endDate 
	 * @param numberOfDay - numero di giorni per periodo
	 * @return listPeriodo - lista di periodi
	 *  
	 * @throws BuildModelException
	 */
	public List<Period> createPeriods(String startDate,String endDate,int numberOfDay) throws BuildModelException {
		logger.info("start FilterPeriod");
		List<Period> listaPeriodi=null;
		try{
			//suddivido l'intervallo di tempo in periodi
			listaPeriodi=getPeriodDay(startDate,endDate,numberOfDay);
			//Suddivido l'intervallo di tempo in periodi di un mese
//			listaPeriodi=getPeriodMonth(startDate,endDate);

			//memorizzo i periodi trovati all'interno del DB
			for(Period period:listaPeriodi){
				logger.info("creato periodp : "+period);
			}
		}catch(ParseException e){
			logger.error("errore la suddivisione in periodi");
			throw new BuildModelException(e.getMessage());
		}
		logger.info("end FilterPeriod");
		return listaPeriodi;


	}
	/**
	 * Suddivide il periodo (startDate-endDate) in sottoperiodi n di giorni dayPerPeriod. Se il periodo non è estatamente di
	 * n*dayPerPeriod i gioni che avanzano verranno ignarati. 
	 * @param startDate
	 * @param endDate
	 * @param dayForPeriod
	 * @return periodi - i sotto periodi in cui è stato diviso il periodo (startDate-endDate) 
	 * @throws ParseException
	 */
	private static List<Period> getPeriodDay(String startDate,String endDate,int dayForPeriod) throws ParseException{

		//la lista che conterr� i periodi
		List<Period> listaPeriodi=new LinkedList<Period>();


		//startDate = "2012-02-26";
		//endDate = "2012-03-13";

		//setto il formato con cui vengono salvati
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//creo un oggetto data
		Date dateMin=dateFormat.parse(startDate);
		Date dateMax=dateFormat.parse(endDate);		

		//creo un oggetto calendatio necessatio per manipolare le date
		//min
		Calendar calMin = Calendar.getInstance();
		calMin.setTime(dateMin);
		//max
		Calendar calMax = Calendar.getInstance();
		//setto il calendario al tempo minimo
		calMax.setTime(dateMax);

		Calendar calCorr=Calendar.getInstance();
		calCorr.setTime(dateMin);
		String dataInizioPeriodo=dateFormat.format(calMin.getTime());
		dataInizioPeriodo = dataInizioPeriodo.concat(" 00:00:00");
		calCorr.add(Calendar.DATE, dayForPeriod-1);

		//aggiorno data fine periodo
		String dataFinePeriodo=dateFormat.format(calCorr.getTime());
		dataFinePeriodo = dataFinePeriodo.concat(" 23:59:59");
		int idPeriodo=0;

		//divido l'intervallo globale di tempo in tanti sottoperiodi
		while(calMax.after(calCorr)){

			//registra periodo precedente
			Period periodCorrente=new Period(idPeriodo,dataInizioPeriodo, dataFinePeriodo);
			listaPeriodi.add(periodCorrente);

			//vai alla prossima settimana
			calCorr.add(Calendar.DATE, 1);
			dataInizioPeriodo=dateFormat.format(calCorr.getTime());
			dataInizioPeriodo = dataInizioPeriodo.concat(" 00:00:00");
			calCorr.add(Calendar.DATE, dayForPeriod-1);
			dataFinePeriodo=dateFormat.format(calCorr.getTime());
			dataFinePeriodo = dataFinePeriodo.concat(" 23:59:59");
			//aggiorno l'id del periodo
			idPeriodo++;	
		}
		dataFinePeriodo=dateFormat.format(calMax.getTime());
		dataFinePeriodo = dataFinePeriodo.concat(" 23:59:59");
		//setto la data dell'ultimo periodo con la maxdate
		listaPeriodi.get(listaPeriodi.size()-1).setDataFinePeriodo(dataFinePeriodo);

		return listaPeriodi;

	}


	private static List<Period> getPeriodMonth(String startDate,String endDate) throws ParseException{

		//la lista che conterr� i periodi
		List<Period> listaPeriodi=new LinkedList<Period>();


		//startDate = "2012-02-26";
		//endDate = "2012-03-13";

		//setto il formato con cui vengono salvati
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//creo un oggetto data
		Date dateMin=dateFormat.parse(startDate);
		Date dateMax=dateFormat.parse(endDate);		

		//creo un oggetto calendatio necessatio per manipolare le date
		//min
		Calendar calMin = Calendar.getInstance();
		calMin.setTime(dateMin);
		//max
		Calendar calMax = Calendar.getInstance();
		//setto il calendario al tempo minimo
		calMax.setTime(dateMax);

		Calendar calCorr=Calendar.getInstance();
		calCorr.setTime(dateMin);
		String dataInizioPeriodo=dateFormat.format(calMin.getTime());
		dataInizioPeriodo = dataInizioPeriodo.concat(" 00:00:00");

		//vado all'ultimo gg del mese
		int lastDate = calCorr.getActualMaximum(Calendar.DATE);
		calCorr.set(Calendar.DATE, lastDate);
		
		//aggiorno data fine periodo
		String dataFinePeriodo=dateFormat.format(calCorr.getTime());
		dataFinePeriodo = dataFinePeriodo.concat(" 23:59:59");
		int idPeriodo=0;

		//divido l'intervallo globale di tempo in tanti sottoperiodi
		while(calMax.after(calCorr)){

			//registra periodo precedente
			Period periodCorrente=new Period(idPeriodo,dataInizioPeriodo, dataFinePeriodo);
			listaPeriodi.add(periodCorrente);

			//vai alla prossima settimana
			calCorr.add(Calendar.DATE, 1);
			dataInizioPeriodo=dateFormat.format(calCorr.getTime());
			dataInizioPeriodo = dataInizioPeriodo.concat(" 00:00:00");
			lastDate = calCorr.getActualMaximum(Calendar.DATE);
			calCorr.set(Calendar.DATE, lastDate);
			dataFinePeriodo=dateFormat.format(calCorr.getTime());
			dataFinePeriodo = dataFinePeriodo.concat(" 23:59:59");
			//aggiorno l'id del periodo
			idPeriodo++;	
		}
		dataFinePeriodo=dateFormat.format(calMax.getTime());
		dataFinePeriodo = dataFinePeriodo.concat(" 23:59:59");
		//setto la data dell'ultimo periodo con la maxdate
		Period periodCorrente=new Period(idPeriodo,dataInizioPeriodo, dataFinePeriodo);
		listaPeriodi.add(periodCorrente);
		
		return listaPeriodi;

	}


	public static void main(String[] args) throws ParseException{

		String startDate = "2012-01-5";
		String endDate = "2013-12-25";
		//Per avere i periodi lunghi quanto specifichi, adesso è 7 gg
		//		List<Period> periods=getPeriod(startDate, endDate,7);
		//Per avere i periodi di un mese
		List<Period> periods=getPeriodMonth(startDate, endDate);

		for(Period period :periods){
			System.out.println(period);
		}





	}


}
