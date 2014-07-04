

/* First created by JCasGen Fri Jul 04 14:40:28 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Jul 04 14:40:28 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class AllRewritings extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(AllRewritings.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected AllRewritings() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public AllRewritings(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public AllRewritings(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public AllRewritings(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: allRewritings

  /** getter for allRewritings - gets 
   * @generated */
  public FSArray getAllRewritings() {
    if (AllRewritings_Type.featOkTst && ((AllRewritings_Type)jcasType).casFeat_allRewritings == null)
      jcasType.jcas.throwFeatMissing("allRewritings", "eu.crydee.readability.uima.ts.AllRewritings");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((AllRewritings_Type)jcasType).casFeatCode_allRewritings)));}
    
  /** setter for allRewritings - sets  
   * @generated */
  public void setAllRewritings(FSArray v) {
    if (AllRewritings_Type.featOkTst && ((AllRewritings_Type)jcasType).casFeat_allRewritings == null)
      jcasType.jcas.throwFeatMissing("allRewritings", "eu.crydee.readability.uima.ts.AllRewritings");
    jcasType.ll_cas.ll_setRefValue(addr, ((AllRewritings_Type)jcasType).casFeatCode_allRewritings, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for allRewritings - gets an indexed value - 
   * @generated */
  public Rewritings getAllRewritings(int i) {
    if (AllRewritings_Type.featOkTst && ((AllRewritings_Type)jcasType).casFeat_allRewritings == null)
      jcasType.jcas.throwFeatMissing("allRewritings", "eu.crydee.readability.uima.ts.AllRewritings");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((AllRewritings_Type)jcasType).casFeatCode_allRewritings), i);
    return (Rewritings)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((AllRewritings_Type)jcasType).casFeatCode_allRewritings), i)));}

  /** indexed setter for allRewritings - sets an indexed value - 
   * @generated */
  public void setAllRewritings(int i, Rewritings v) { 
    if (AllRewritings_Type.featOkTst && ((AllRewritings_Type)jcasType).casFeat_allRewritings == null)
      jcasType.jcas.throwFeatMissing("allRewritings", "eu.crydee.readability.uima.ts.AllRewritings");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((AllRewritings_Type)jcasType).casFeatCode_allRewritings), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((AllRewritings_Type)jcasType).casFeatCode_allRewritings), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    