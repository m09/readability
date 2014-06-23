

/* First created by JCasGen Mon Jun 23 12:39:02 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Jun 23 12:39:02 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class Rewritings extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Rewritings.class);
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
  protected Rewritings() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Rewritings(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Rewritings(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Rewritings(JCas jcas, int begin, int end) {
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
  //* Feature: rewritings

  /** getter for rewritings - gets 
   * @generated */
  public FSArray getRewritings() {
    if (Rewritings_Type.featOkTst && ((Rewritings_Type)jcasType).casFeat_rewritings == null)
      jcasType.jcas.throwFeatMissing("rewritings", "eu.crydee.readability.uima.ts.Rewritings");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Rewritings_Type)jcasType).casFeatCode_rewritings)));}
    
  /** setter for rewritings - sets  
   * @generated */
  public void setRewritings(FSArray v) {
    if (Rewritings_Type.featOkTst && ((Rewritings_Type)jcasType).casFeat_rewritings == null)
      jcasType.jcas.throwFeatMissing("rewritings", "eu.crydee.readability.uima.ts.Rewritings");
    jcasType.ll_cas.ll_setRefValue(addr, ((Rewritings_Type)jcasType).casFeatCode_rewritings, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for rewritings - gets an indexed value - 
   * @generated */
  public Rewriting getRewritings(int i) {
    if (Rewritings_Type.featOkTst && ((Rewritings_Type)jcasType).casFeat_rewritings == null)
      jcasType.jcas.throwFeatMissing("rewritings", "eu.crydee.readability.uima.ts.Rewritings");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Rewritings_Type)jcasType).casFeatCode_rewritings), i);
    return (Rewriting)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Rewritings_Type)jcasType).casFeatCode_rewritings), i)));}

  /** indexed setter for rewritings - sets an indexed value - 
   * @generated */
  public void setRewritings(int i, Rewriting v) { 
    if (Rewritings_Type.featOkTst && ((Rewritings_Type)jcasType).casFeat_rewritings == null)
      jcasType.jcas.throwFeatMissing("rewritings", "eu.crydee.readability.uima.ts.Rewritings");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Rewritings_Type)jcasType).casFeatCode_rewritings), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Rewritings_Type)jcasType).casFeatCode_rewritings), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    