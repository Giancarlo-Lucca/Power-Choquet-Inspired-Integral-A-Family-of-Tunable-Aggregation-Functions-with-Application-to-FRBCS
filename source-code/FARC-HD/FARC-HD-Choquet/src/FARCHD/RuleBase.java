package FARCHD;

/**
 * <p>Title: RuleBase</p>
 *
 * <p>Description: This class contains the representation of a Rule Set</p>
 *
 * <p>Copyright: Copyright KEEL (c) 2007</p>
 *
 * <p>Company: KEEL </p>
 *
 * @author Written by Jesus Alcala (University of Granada) 09/02/2010
 * @version 1.0
 * @since JDK1.5
 */

import java.util.*;
import org.core.*;

public class RuleBase {
  ArrayList<Rule> ruleBase;
  DataBase dataBase;
  myDataset train;
  int n_variables, K, nUncover, typeInference, defaultRule, tipoFM, opFM;
  int[] nUncoverClas;
  double fitness;

  public boolean BETTER(int a, int b) {
    return  a > b;
  }

  public RuleBase() {
  }

  public RuleBase(DataBase dataBase, myDataset train, int K, int typeInference, int tipoFM, int opFM) {
    this.ruleBase = new ArrayList<> ();
    this.dataBase = dataBase;
    this.train = train;
    this.n_variables = dataBase.numVariables();
	this.fitness = 0.0;
	this.K = K;
	this.typeInference = typeInference;
	this.defaultRule = -1;
	this.nUncover = 0;
	this.nUncoverClas = new int[this.train.getnClasses()];
        this.tipoFM = tipoFM;
        this.opFM = opFM;
  }

  public RuleBase clone() {
    RuleBase br = new RuleBase();
    br.ruleBase = new ArrayList<> ();
    for (int i = 0; i < this.ruleBase.size(); i++)  br.ruleBase.add((this.ruleBase.get(i)).clone());

    br.dataBase = this.dataBase;
    br.train = this.train;
    br.n_variables = this.n_variables;
	br.fitness = this.fitness;
	br.K = this.K;
	br.typeInference = this.typeInference;
	br.defaultRule = this.defaultRule;
	br.nUncover = this.nUncover;
	br.nUncoverClas = new int[this.train.getnClasses()];
        br.tipoFM = this.tipoFM;
        br.opFM = this.opFM;
      //for (int i = 0; i < this.train.getnClasses(); i++)  br.nUncoverClas[i] = this.nUncoverClas[i];
      System.arraycopy(this.nUncoverClas, 0, br.nUncoverClas, 0, this.train.getnClasses());

	return (br);
  }


  public void add(Rule rule) {
	  this.ruleBase.add(rule);
  }

  public void add(RuleBase ruleBase) {
	  int i;

	  for (i=0; i<ruleBase.size(); i++) {
		  this.ruleBase.add(ruleBase.get(i).clone());
	  }
  }


  public void add(Itemset itemset) {
	  int i;
	  Item item;

	  int[] antecedent = new int[n_variables];
	  for (i=0; i < n_variables; i++)  antecedent[i] = -1;  // Don't care

	  for (i=0; i < itemset.size(); i++) {
		  item = itemset.get(i);
		  antecedent[item.getVariable()] = item.getValue();
	  }
	  
	  Rule r = new Rule(this.dataBase);
      r.asignaAntecedente(antecedent);
	  r.setConsequent(itemset.getClas());
	  r.setConfidence(itemset.getSupportClass() / itemset.getSupport());
	  r.setSupport(itemset.getSupportClass());
      this.ruleBase.add(r);
  }

  public Rule get(int pos) {
	  return (this.ruleBase.get(pos));
  }

  public int size() {
	  return (this.ruleBase.size());
  }

  public void sort () {
	  Collections.sort(this.ruleBase);
  }

  public Rule remove(int pos) {
	  return (this.ruleBase.remove(pos));
  }

  public void clear() {
	  this.ruleBase.clear();
	  this.fitness = 0.0;
  }

  public int getTypeInference() {
    return  (this.typeInference);
  }

  public double getAccuracy() {
    return  (this.fitness);
  }

  public void setDefaultRule() {
	  int i, bestRule;

	  bestRule = 0;
/*	  if (this.nUncover > 0) {
		  for (i=1; i < this.train.getnClasses(); i++) {
			  if (this.nUncoverClas[bestRule] < this.nUncoverClas[i])  bestRule = i;
		  }
	  }
	  else {
*/		  for (i=1; i < this.train.getnClasses(); i++) {
			  if (this.train.numberInstances(bestRule) < this.train.numberInstances(i))  bestRule = i;
		  }
//	  }

	  this.defaultRule = bestRule;
  }


  public boolean hasUncover() {
    return  (this.nUncover > 0);
  }

  public int getUncover() {
    return  (this.nUncover);
  }

  public int getK() {
    return  (this.K);
  }
/*
  public int hasUncoverClass(int clas) {
    int uncover;
	int prediction;
	
	uncover = 0;
    for (int j = 0; j < train.size(); j++) {
		if (this.train.getOutputAsInteger(j) == clas) {
			prediction = this.FRM(train.getExample(j));
			if (prediction < 0)  uncover++;
		}
    }

	return uncover;
  }
*/

    public void evaluate() {
        int nHits, prediction;//,cont;

        //prediction = new int[2];
        
        //cont = 0;
        nHits = 0;
        this.nUncover = 0;
        for (int j = 0; j < this.train.getnClasses(); j++) {
            this.nUncoverClas[j] = 0;
        }

        for (int j = 0; j < train.size(); j++) {
            prediction = this.FRM(train.getExample(j));
            if (this.train.getOutputAsInteger(j) == prediction) {
                nHits++;
            }
            if (prediction < 0) {
                this.nUncover++;
                this.nUncoverClas[this.train.getOutputAsInteger(j)]++;
            }
           // cont += prediction[1];
        }
       // System.out.println("Porcentaje de ejemplos con solo una regla disparada" + (double) cont / train.size());
        this.fitness = (100.0 * nHits) / (1.0 * this.train.size());
    }
    
     public double evaluate(myDataset ds) {
        int nHits, prediction;//,cont;

        //prediction = new int[2];
        
        //cont = 0;
        nHits = 0;
        this.nUncover = 0;
        for (int j = 0; j < ds.getnClasses(); j++) {
            this.nUncoverClas[j] = 0;
        }

        for (int j = 0; j < ds.size(); j++) {
            prediction = this.FRM(ds.getExample(j));
            if (ds.getOutputAsInteger(j) == prediction) {
                nHits++;
            }
            if (prediction < 0) {
                this.nUncover++;
                this.nUncoverClas[ds.getOutputAsInteger(j)]++;
            }
           // cont += prediction[1];
        }
       // System.out.println("Porcentaje de ejemplos con solo una regla disparada" + (double) cont / train.size());
        return (100.0 * nHits) / (1.0 * ds.size());
    }



  public void evaluate(double[] gene, int[] selected) {
    int nHits, prediction;// cont;

    //prediction = new int[2];
    
	this.dataBase.decode(gene);
	
        //cont = 0;
	nHits = 0;
	this.nUncover = 0;
	for (int j = 0; j < this.train.getnClasses(); j++)  this.nUncoverClas[j] = 0;

	for (int j = 0; j < train.size(); j++) {
            prediction = this.FRM(train.getExample(j), selected);
            if (this.train.getOutputAsInteger(j) == prediction)  nHits++;
                if (prediction < 0) {
                        this.nUncover++;
                        this.nUncoverClas[this.train.getOutputAsInteger(j)]++;
                }
            //cont += prediction[1];
        }
        //System.out.println("Porcentaje de ejemplos con solo una regla disparada" + (double)cont/train.size());
	this.fitness = (100.0 * nHits) / (1.0 * this.train.size());
  }

  public int FRM(double[] example) {
    if (this.typeInference == 0){
        return FRM_WR(example);

    }

    else if (this.typeInference == 2){ 
        return FRM_Choquet(example);
    }    
    
    else if (this.typeInference == 3){
        return FRM_Prob_Sum(example);    
    }
    
    else{  
        return FRM_AC(example);
    }
  }

  public int FRM(double[] example, int[] selected) {
    if (this.typeInference == 0){
        return FRM_WR(example, selected);

    }
        
    else if (this.typeInference == 2){ 
        return FRM_Choquet(example, selected);
    }
    
    else if (this.typeInference == 3){
        return FRM_Prob_Sum(example, selected);
    } 
    
    else{  
        return FRM_AC(example, selected);
    }
  }

  
  private int FRM_Choquet(double[] example, int[] selected) {
      int i, clas, cont;//, cont1;
    Double maxDegree;
    Double degree, agregated;

    ArrayList<ArrayList<Double>> gAsoc = new ArrayList<>();
    ArrayList<ArrayList<Integer>> firedRules = new ArrayList<>();
    
    //clas = new int[2];
    clas = defaultRule;

    //degreeClass = new double[this.train.getnClasses()][2];
    for (i=0; i < this.train.getnClasses(); i++){
        gAsoc.add(new ArrayList<Double>());
        firedRules.add(new ArrayList<Integer>());
    }

    for (i = 0; i < this.ruleBase.size(); i++) {
        if (selected[i] > 0) {    
            Rule r = this.ruleBase.get(i);
            degree = r.matching(example);
            if (degree > 0) {
                gAsoc.get(r.getClas()).add(degree);
                firedRules.get(r.getClas()).add(i);
            }
        }
    }

    maxDegree = 0.0;
    cont = 0;
    //cont1 = 0;
    for (i = 0; i < this.train.getnClasses(); i++) {
      //agregated[0] = agChoquet(gAsocLow.get(i), firedRules.get(i));
      //agregated[1] = agChoquet(gAsocUp.get(i), firedRules.get(i));
      if (!gAsoc.get(i).isEmpty()){
        agregated = agChoquet(gAsoc.get(i), firedRules.get(i), this.dataBase.exp[i]);

      }else{
          agregated = 0.0;
      }
      if (agregated > maxDegree) {
            maxDegree = agregated;
            clas = i;
            cont = 0;
      }
      else{
          if (Objects.equals(agregated, maxDegree))  cont++;
      }
    }

    if (cont > 0)  clas = defaultRule;

    return clas;
  }
  
  private int FRM_Choquet(double[] example) {
    int i, clas, cont;//, cont1;
    Double maxDegree;
    Double degree, agregated;
    double threshold;
    
    ArrayList<ArrayList<Double>> gAsoc = new ArrayList<>();
    ArrayList<ArrayList<Integer>> firedRules = new ArrayList<>();
    

    clas = defaultRule;

    //degreeClass = new double[this.train.getnClasses()][2];
    for (i=0; i < this.train.getnClasses(); i++){
        gAsoc.add(new ArrayList<Double>());
        firedRules.add(new ArrayList<Integer>()); 
    }

    for (i = 0; i < this.ruleBase.size(); i++) {
            Rule r = this.ruleBase.get(i);

            degree = r.matching(example);
            
            if (degree > 0) {
                gAsoc.get(r.getClas()).add(degree);
                firedRules.get(r.getClas()).add(i);
            }            
    }

    maxDegree = 0.0;
    cont = 0;
    //cont1 = 0;
    for (i = 0; i < this.train.getnClasses(); i++) {
      //agregated[0] = agChoquet(gAsocLow.get(i), firedRules.get(i));
      //agregated[1] = agChoquet(gAsocUp.get(i), firedRules.get(i));
      if (!gAsoc.get(i).isEmpty()){
        agregated = agChoquet(gAsoc.get(i), firedRules.get(i), this.dataBase.exp[i]);//this.dataBase.exp[i]);

      }else{
          agregated = 0.0;
          //cont1 += 1;
      }
      if (agregated > maxDegree) {
            maxDegree = agregated;
            clas = i;
            cont = 0;
      }
      else{
          if (Objects.equals(agregated, maxDegree))  cont++;
      }
    }

	if (cont > 0)  clas = defaultRule;
        //if (cont1==(this.train.getnClasses()-1)) clas[1]=1;
    return clas;
  }
    
  private Double agChoquet(ArrayList<Double> asociations, ArrayList<Integer> firRul, double exp){
      Double agr;
      double sumando;
      Object valores[],reglas[];
      ArrayList<Double> valAux = new ArrayList<>();
      ArrayList<Integer> regAux = new ArrayList<>();
      //ArrayList<Integer> regAuxIntChoquet = new ArrayList<>();
      
      
      valores = asociations.toArray();
      reglas = firRul.toArray();

      //PARA ORDENAR LOS VALORES A AGREGAR
      List<Integer> indices = sortValues(asociations);
      Collections.sort(asociations);
    
      for(int p = 0; p < valores.length; p++) {
            valAux.add((Double)valores[indices.get(p)]);
            regAux.add((Integer)reglas[indices.get(p)]);
      }

      if(asociations.size() == 1){
          return asociations.get(0);
      }
      else if(asociations.size() == 2){
          return (asociations.get(0) + asociations.get(1))/2;
      }
      else{
            sumando = 0.0;
            regAux.remove(0);
            agr = valAux.get(0);
            double x, F; //y
            for (int i=1; i<valAux.size(); i++){   
                x = valAux.get(i) - valAux.get(i-1);

                F = PowerInspired(asociations, i, i-1, exp);
                sumando = (x * F);

                agr += sumando;

                regAux.remove(0);
            }
            
            return agr;
      }
}

private double PowerInspired(ArrayList<Double> asociations, int indiceAtual, int indiceAnterior, double exp){
    double measure = 0.0;
    double Minimitorio = 1.0;
    double MX = 0.0;
    double Produtorio = 1.0;
    double val_agg = 1.0;
    
    if (this.tipoFM == 1){
        //F = Min
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Minimitorio = Math.min(Minimitorio, asociations.get(j));
            }
        }
        measure = Math.pow(Minimitorio,exp);   
    }
    else if (this.tipoFM == 2){
        //F = Prod
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Produtorio = Produtorio * asociations.get(j);
            }
        }
        measure = Math.pow(Produtorio,exp); 
    }
    else if (this.tipoFM == 3){
        //F = Luk
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                val_agg = Math.max(0, val_agg + asociations.get(j) - 1);
            }
        }
        measure = Math.pow(val_agg, exp);   
    }    
    else if (this.tipoFM == 4){
        //F = hp
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                if ((val_agg == 0.0) && (asociations.get(j)==0.0)){
                    val_agg = 0.0;
                }
                else{
                  val_agg = (val_agg * asociations.get(j))/(val_agg + asociations.get(j) - (val_agg * asociations.get(j)));
                }
            }
        }
        measure = Math.pow(val_agg, exp);;   
    }
    
    else if (this.tipoFM == 5){
        //F = OB
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Produtorio = Produtorio * asociations.get(j);
                Minimitorio = Math.min(Minimitorio, asociations.get(j));
            }
        }
        measure = Math.pow(Math.sqrt(Produtorio * Minimitorio), exp);
    }
    
    else if (this.tipoFM == 6){
        //F = GM
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Produtorio = Produtorio * asociations.get(j);
                Minimitorio = Math.min(Minimitorio, asociations.get(j));
            }
        }
        measure = Math.pow(Math.pow(Produtorio, 1/asociations.size()-2), exp);
    }

    else if (this.tipoFM == 8){
        //F = ODIv
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Produtorio = Produtorio * asociations.get(j);
                Minimitorio = Math.min(Minimitorio, asociations.get(j));
            }
        }
        measure = Math.pow((Produtorio + Minimitorio)/2, exp);
    }

    else if (this.tipoFM == 9){
        //F = OmM
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                MX = (double) Math.max(MX, Math.pow(asociations.get(j),2));
                Minimitorio = Math.min(Minimitorio, asociations.get(j));
            }
        }
        measure = Math.pow(Minimitorio * MX, exp);
    }    
    
    else if (this.tipoFM == 10){
        //F = SIN
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Produtorio = Produtorio * asociations.get(j);
            }
        }
        measure = Math.pow(Math.sin(Math.PI/2 * Math.pow(Produtorio, 1/4)), exp);
    }    
    
    //**************GROUPING**************
    else if (this.tipoFM == 11){
        //F = GB
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Produtorio = Produtorio * (1 - asociations.get(j));
                Minimitorio = Math.min(Minimitorio, (1 -asociations.get(j)));
            }
        }
        measure = Math.pow(1 - Math.sqrt(Produtorio * Minimitorio), exp);
    }    
    
    else if (this.tipoFM == 12){
        //F = GMD
        int cont = 0;
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Produtorio = Produtorio * (1 -asociations.get(j));
                cont++;
            }
        }
        measure = Math.pow(1 - Math.pow(Produtorio, 1/cont), exp);
    } 
    
    else if (this.tipoFM == 13){
        //F = GDIV
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Produtorio = Produtorio * (1 -asociations.get(j));
                Minimitorio = Math.min(Minimitorio, (1 -asociations.get(j)));
            }
        }
        measure = Math.pow(1 - (Produtorio + Minimitorio)/2, exp);
    }     
    
    else if (this.tipoFM == 14){
        //F = GOmM
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Produtorio = Produtorio * (1 -asociations.get(j));
                MX = Math.max(MX, Math.pow(1 - asociations.get(j), 2));
            }
        }
        measure = Math.pow(1 - (Minimitorio * MX), exp);
    }         
    else if (this.tipoFM == 15){
        //F = GSIN ..... 
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Produtorio = Produtorio * (1 -asociations.get(j));
            }
        }
        measure = Math.pow(1 - Math.sin(Math.PI/2 * Math.pow(Produtorio, 1/4)), exp);
    }    
    
    // **************************** T-CONORMAS ****************************
    else if (this.tipoFM == 16){
        //F = Max
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                MX = Math.max(MX, asociations.get(j));
            }
        }
        measure = Math.pow(MX, exp);
    }     
    else if (this.tipoFM == 17){
        //F = ProbSum
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                Produtorio = Produtorio * (1 -asociations.get(j));
            }
        }
        measure = Math.pow(1 - Produtorio, exp);
    } 
    else if (this.tipoFM == 18){
        //F = BoundedLuck
         for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                val_agg = Math.min(val_agg + asociations.get(j), 1);
            }
        }
        measure = Math.pow(val_agg, exp);
    }     
    else{
        //F = Einsten sum
        val_agg = 0.0;
        for (int j = 0; j < asociations.size(); j++) {
            if (j != indiceAtual && j != indiceAnterior) {
                val_agg = (val_agg + asociations.get(j))/(1+val_agg * asociations.get(j));
            }
        }
        measure = Math.pow(val_agg, exp);
    }
    return measure;
  }

     
  private List<Integer> sortValues(ArrayList<Double> valores){
      TreeMap<Double, List<Integer>> map = new TreeMap<>();
        for(int p = 0; p < valores.size(); p++) {
            //aux[p] = (Double)ordenado[p];
            List<Integer> ind = map.get(valores.get(p));
            if(ind == null){
                ind = new ArrayList<>();
                map.put(valores.get(p), ind);
            }
            ind.add(p);
        }
        // Now flatten the list
       List<Integer> indices = new ArrayList<Integer>();
        for(List<Integer> arr : map.values()) {
            indices.addAll(arr);
        }
        
        return indices;
  }
    
  private int  FRM_WR(double[] example, int[] selected) {
    int clas;
    double max, degree;
		
	max = 0.0;
        //clas = new int[2];
	clas = defaultRule;

	for (int i = 0; i < this.ruleBase.size(); i++) {
		if (selected[i] > 0) {
			Rule r = this.ruleBase.get(i);
			degree = r.matching(example);
			
			if (degree > max) {
				max = degree;
				clas = r.getClas();
			}
		}
	}

    return clas;
  }


  private int FRM_WR(double[] example) {
    int clas;
    double max, degree;
		
	max = 0.0;
        //clas = new int[2];
	clas = defaultRule;

      /*for (int i = 0; i < this.ruleBase.size(); i++) {
      Rule r = this.ruleBase.get(i);
      degree = r.matching(example);
      if (degree > max) {
      max = degree;
      clas = r.getClas();
      }
      }*/
      for (Rule r : this.ruleBase) {
          degree = r.matching(example);
          
          if (degree > max) {
              max = degree;
              clas = r.getClas();
          }
      }

    return clas;
  }


  private int FRM_AC(double[] example, int[] selected) {
    int i, clas, cont;
	double degree, maxDegree;
	double[] degreeClass;
        
       // clas = new int[2];
	clas = defaultRule;

    degreeClass = new double[this.train.getnClasses()];
	for (i=0; i < this.train.getnClasses(); i++)  degreeClass[i] = 0.0;

	for (i = 0; i < this.ruleBase.size(); i++) {
		if (selected[i] > 0) {
			Rule r = this.ruleBase.get(i);
			
			degree = r.matching(example);
			degreeClass[r.getClas()] += degree;
		}
    }

    maxDegree = 0.0;
	cont = 0;
    for (i = 0; i < this.train.getnClasses(); i++) {
      if (degreeClass[i] > maxDegree) {
        maxDegree = degreeClass[i];
        clas = i;
		cont = 0;
      }
	  else if (degreeClass[i] == maxDegree)  cont++;
    }

	if (cont > 0)  clas = defaultRule;
    return clas;
  }


  private int FRM_AC(double[] example) {
    int i, clas, cont;
	double degree, maxDegree;
	double[] degreeClass;

       // clas = new int[2];
	clas = defaultRule;

    degreeClass = new double[this.train.getnClasses()];
	for (i=0; i < this.train.getnClasses(); i++)  degreeClass[i] = 0.0;

	for (i = 0; i < this.ruleBase.size(); i++) {
		Rule r = this.ruleBase.get(i);
			
		degree = r.matching(example);
		degreeClass[r.getClas()] += degree;
    }

    maxDegree = 0.0;
	cont = 0;
    for (i = 0; i < this.train.getnClasses(); i++) {
      if (degreeClass[i] > maxDegree) {
        maxDegree = degreeClass[i];
        clas = i;
		cont = 0;
      }
	  else if (degreeClass[i] == maxDegree)  cont++;
    }

	if (cont > 0)  clas = defaultRule;
    return clas;
  }

  
  ///////////////////////////////////////////////////////PROB_SUM/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
  private int FRM_Prob_Sum(double[] example, int[] selected) {
      //System.out.println("==================================== PROB_SUM(double[] example, int[]selected) ============================================");  
      
      int i, clas, cont;//, cont1;
        Double maxDegree, sum;
        Double degree;
        double[] degreeClass;
        
        ArrayList<ArrayList<Double>> gAsoc = new ArrayList<>();
       
       
        clas = defaultRule;
        degreeClass = new double[this.train.getnClasses()];
        
        for (i=0; i < this.train.getnClasses(); i++){
            gAsoc.add(new ArrayList<Double>());
        }
        
        //System.out.println("gAsoc.size = "+gAsoc.size());

        for (i = 0; i < this.ruleBase.size(); i++) {
                if (selected[i] > 0) {    
                    Rule r = this.ruleBase.get(i);
                    degree = r.matching(example);
            
                    if (degree > 0) {
                        gAsoc.get(r.getClas()).add(degree);
                        //firedRules.get(r.getClas()).add(i);
                    }
                }
        }
        //ASSUMINDO QUE TENHA ELEMENTOS NA LISTA
        for (int j = 0; j < gAsoc.size(); j++) {
            //sum = gAsoc.get(j).get(0) + gAsoc.get(j).get(1) - gAsoc.get(j).get(0) * gAsoc.get(j).get(1); 
            if (!gAsoc.get(j).isEmpty()){
                //System.out.println("SEGUNDO FOR gAsoc("+j+")");
                sum = gAsoc.get(j).get(0);
                for (int k = 1; k < gAsoc.get(j).size(); k++) {
                    sum = sum + gAsoc.get(j).get(k) - sum * gAsoc.get(j).get(k);
                }
            }
            else{
                sum = 0.0;
            }
            degreeClass[j] = sum;
        }
        
        maxDegree = 0.0;
        cont = 0;
        for (i = 0; i < this.train.getnClasses(); i++) {
            if (degreeClass[i] > maxDegree) {
                maxDegree = degreeClass[i];
                clas = i;
		cont = 0;
            }
            else if (degreeClass[i] == maxDegree){
                cont++;
            }
        }
	if (cont > 0)  clas = defaultRule;
        
        return clas;
  }
  
  private int FRM_Prob_Sum(double[] example) {
       //System.out.println("===============22222222222222222============= PROB_SUM(double[] example, int[]selected) ============================================");  

        int i, clas, cont;//, cont1;
        Double maxDegree, sum;
        Double degree;
        double[] degreeClass;
        
        ArrayList<ArrayList<Double>> gAsoc = new ArrayList<>();
        //ArrayList<ArrayList<Integer>> firedRules = new ArrayList<>();

        //clas = new int[2];
        clas = defaultRule;
        degreeClass = new double[this.train.getnClasses()];
        
        //degreeClass = new double[this.train.getnClasses()][2];
        for (i=0; i < this.train.getnClasses(); i++){
            gAsoc.add(new ArrayList<Double>());
            //firedRules.add(new ArrayList<Integer>()); 
        }

        for (i = 0; i < this.ruleBase.size(); i++) {
                Rule r = this.ruleBase.get(i);

                degree = r.matching(example);
                if (degree > 0) {
                    gAsoc.get(r.getClas()).add(degree);
                    //firedRules.get(r.getClas()).add(i);
                }
        }

        //ASSUMINDO QUE TENHA ELEMENTOS NA LISTA
        for (int j = 0; j < gAsoc.size(); j++) {
            //sum = gAsoc.get(j).get(0) + gAsoc.get(j).get(1) - gAsoc.get(j).get(0) * gAsoc.get(j).get(1); 
            if (!gAsoc.get(j).isEmpty()){
                sum = gAsoc.get(j).get(0);
                for (int k = 1; k < gAsoc.get(j).size(); k++) {
                    sum = sum + gAsoc.get(j).get(k) - sum * gAsoc.get(j).get(k);
                }
            }
            else{
                sum = 0.0;
            }
            degreeClass[j] = sum;
        }
        
        maxDegree = 0.0;
        cont = 0;
        for (i = 0; i < this.train.getnClasses(); i++) {
            if (degreeClass[i] > maxDegree) {
                maxDegree = degreeClass[i];
                clas = i;
		cont = 0;
            }
            else if (degreeClass[i] == maxDegree){
                cont++;
            }
        }
	if (cont > 0)  clas = defaultRule;
        
        return clas;
  }
 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 

  
  
  public int hasClassUncovered (int[] selected) {
	  int i, count;
	  int[] cover;
	  
	  cover = new int[this.train.getnClasses()];
	  for (i=0; i < cover.length; i++) {
		  if (this.train.numberInstances(i) > 0)  cover[i] = 0;
		  else  cover[i] = 1;
	  }
	  
	  for (i = 0; i < this.ruleBase.size(); i++) {
		  if (selected[i] > 0) {
			  cover[this.ruleBase.get(i).getClas()]++;
		  }
	  }

	  count = 0;
	  for (i=0; i < cover.length; i++) {
		  if (cover[i] == 0)  count++;
	  }

	  return count;
  }


  public void reduceRules(int clas) {
	  ArrayList<ExampleWeight> exampleWeight;
	  int i, posBestWracc, nExamples, nRuleSelect; 
	  double bestWracc;
	  int[] selected;
	  Rule rule;

	  exampleWeight = new ArrayList<> ();
	  for (i=0; i < this.train.size(); i++)  exampleWeight.add(new ExampleWeight(this.K));  

	  selected = new int[this.ruleBase.size()];
	  for (i=0; i < this.ruleBase.size(); i++)  selected[i] = 0;

//	  for (i=0; i < this.ruleBase.size(); i++)  this.ruleBase.get(i).iniCover(this.train);

	  nExamples = this.train.numberInstances(clas);
	  nRuleSelect = 0;

	  System.out.println("Entra en reducir reglas para la clase : " + clas + " con reglas: " + this.ruleBase.size());


//	  uncover = this.hasUncoverClass(clas);
//	  if (uncover > 0)  System.out.println("Entra en reduce Reglas para la clase " + clas + " no cubiertos por la BR: " + uncover);

	  do {
		  bestWracc = -1.0;
		  posBestWracc = -1;
		  
		  for (i=0; i < this.ruleBase.size(); i++) {
			  if (selected[i] == 0) {
				  rule = this.ruleBase.get(i);
				  rule.calculateWracc(this.train, exampleWeight);

				  if (rule.getWracc() > bestWracc) {
					  bestWracc = rule.getWracc();
					  posBestWracc = i;
				  }
			  }
		  }

		  if (posBestWracc > -1) {
			  selected[posBestWracc] = 1;
			  nRuleSelect++;

			  rule = this.ruleBase.get(posBestWracc);
			  nExamples -= rule.reduceWeight(this.train, exampleWeight);
		  }
	  } while ((nExamples > 0) && (nRuleSelect < this.ruleBase.size()) && (posBestWracc > -1));

	  System.out.println("Sale de reduce Reglas para la clase: Numero examples: " + nExamples + "/" + this.train.numberInstances(clas) + " Numero de reglas: " + nRuleSelect + "/" + this.ruleBase.size() + " Valor de K: " + this.K);

	  for (i=this.ruleBase.size() - 1; i >= 0; i--) {
		  if (selected[i] == 0)  this.ruleBase.remove(i);
	  }

//	  uncover = this.hasUncoverClass(clas);
//	  if (uncover > 0)  System.out.println("Sale de reduce Reglas para la clase " + clas + " no cubiertos por la BR: " + uncover);

	  exampleWeight.clear();
	  //System.gc();
  }


  public String printString() {
    int i, j, ant;
    String [] names = this.train.names();
    String [] clases = this.train.clases();
    String stringOut = "";

	ant = 0;
    for (i = 0; i < this.ruleBase.size(); i++) {
      Rule r = this.ruleBase.get(i);
      stringOut += (i+1)+": ";
      for (j = 0; j < n_variables && r.antecedent[j] < 0; j++);
	  if (j < n_variables && r.antecedent[j] >= 0) {
		  stringOut += names[j]+" IS " + r.dataBase.print(j,r.antecedent[j]);
		  ant++;
	  }
      for (j++; j < n_variables-1; j++) {
		if (r.antecedent[j] >=0) {
			stringOut += " AND " + names[j]+" IS " + r.dataBase.print(j,r.antecedent[j]);
		    ant++;
		}
      }
      if (j < n_variables && r.antecedent[j] >= 0)  {
		  stringOut += " AND " + names[j]+" IS " + r.dataBase.print(j,r.antecedent[j]) + ": " + clases[r.clas];
  		  ant++;
	  }
	  else  stringOut += ": " + clases[r.clas];

	  stringOut += " CF: " + r.getConfidence() + "\n";
    }

	stringOut += "\n\n";
    stringOut += "@supp and CF:\n\n";
    for (i = 0; i < this.ruleBase.size(); i++) {
    	Rule rule = this.ruleBase.get(i);
    	stringOut += (i+1)+": ";
    	stringOut += "supp: " + rule.getSupport() + " AND CF: " + rule.getConfidence() + "\n";
	}

    stringOut = "@Number of rules: " + this.ruleBase.size() + " Number of Antecedents by rule: " + ant * 1.0 / this.ruleBase.size() + "\n\n" + stringOut;
	return (stringOut);
  }

  public void saveFile(String filename) {
    String stringOut = "";
    stringOut = printString();
    Files.writeFile(filename, stringOut);
  }

}
