package org.rm3umf.framework.eval.valutation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rm3umf.framework.eval.ValutationFunction;
import org.rm3umf.framework.eval.Result;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

/**
 * Implementazione di S@K (Success at rank K) .
 * 
 * Hp: un utente x è rilevante per y se è verificata una delle due condizioni:
 * 	1)x è seguito da y (y appartiene a followed(x))
 * 	2)x segue y (y appartiene a follower(x)
 * 
 * NOTA: sarebbe meglio creare un'eccessione per questo tipo e lanciarla quando qualcosa va storto
 * @author giulz
 * 
 * VALORI RIFERIMENTO
 * RandomSimilarity = 0.01
 * VectorSpaceModel =0.13723916532905298(171)
 * TemporalFunction3(3)= 0.1490066225165563 | 0.14817880794701987  |0.1498344370860927 |0.155 (188)
 * CrossCorrelation(3) = 0.13741721854304637 
 * JaccardNoWeight =  0.14403973509933773 (174)
 * 
 * 

 *
 */

public class SuccessAtRankK implements ValutationFunction{
	
	private String name;
	public SuccessAtRankK(){
		this.name="TT_S@K";
	}
	
	
	public double valutate(Result training, Result test) {
		
		
		
		//mi restituisce gli utenti del dataset
		Set<Long> listUserTraining = training.getUser();
		int indice =0;
		
		double numUsers = listUserTraining.size(); 
		double success=0.0;
		double n = training.getN();
		
		//Setto il nome della funzione a seconda di n del result
		name = "TT_S@"+n;
		
		for(Long userid:listUserTraining){
			
			
			indice++;
			System.out.println("USER : ("+indice+")"+ userid);		
			
			
			//Aggiungo followers e followed alla lista dei rilevant user
			List<Long> relevantUsers = (List<Long>) test.getBestUsers(userid).keySet();
		
			//Recupero il ranking da result rispetto all'utente corrente
			List<Long> ranking = (List<Long>) training.getBestUsers(userid).keySet();
						
			System.out.println(this.name);
			int i=0;//memorizza a che punto siamo
			for(Long u:ranking){
				i++;
				boolean  isContain=relevantUsers.contains(u);
				System.out.println("     ("+i+")"+u);
				//Se è stato trovato incremento success e vado al prossimo utente
				if(isContain){
					System.out.println("     SUCCESS");
					success++;
					break;
				}
				
			}
			System.out.println("success="+success);	
		}
		
		double sak=success/numUsers;
		return sak ;
		
		
		
	}

	public String getNameFunction() {
		// TODO Auto-generated method stub
		return this.name;
	}

}
