

/* First created by JCasGen Tue Jun 17 15:34:47 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Jun 17 15:34:47 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class Rewriting extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Rewriting.class);
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
  protected Rewriting() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Rewriting(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Rewriting(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Rewriting(JCas jcas, int begin, int end) {
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
  //* Feature: rewriting

  /** getter for rewriting - gets 
   * @generated */
  public String getRewriting() {
    if (Rewriting_Type.featOkTst && ((Rewriting_Type)jcasType).casFeat_rewriting == null)
      jcasType.jcas.throwFeatMissing("rewriting", "eu.crydee.readability.uima.ts.Rewriting");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Rewriting_Type)jcasType).casFeatCode_rewriting);}
    
  /** setter for rewriting - sets  
   * @generated */
  public void setRewriting(String v) {
    if (Rewriting_Type.featOkTst && ((Rewriting_Type)jcasType).casFeat_rewriting == null)
      jcasType.jcas.throwFeatMissing("rewriting", "eu.crydee.readability.uima.ts.Rewriting");
    jcasType.ll_cas.ll_setStringValue(addr, ((Rewriting_Type)jcasType).casFeatCode_rewriting, v);}    
   
    
  //*--------------*
  //* Feature: score

  /** getter for score - gets 
   * @generated */
  public double getScore() {
    if (Rewriting_Type.featOkTst && ((Rewriting_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Rewriting");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Rewriting_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets  
   * @generated */
  public void setScore(double v) {
    if (Rewriting_Type.featOkTst && ((Rewriting_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Rewriting");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Rewriting_Type)jcasType).casFeatCode_score, v);}    
  }

    