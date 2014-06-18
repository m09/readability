

/* First created by JCasGen Wed Jun 18 17:07:12 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Jun 18 17:07:12 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class Suggestion extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Suggestion.class);
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
  protected Suggestion() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Suggestion(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Suggestion(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Suggestion(JCas jcas, int begin, int end) {
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
  //* Feature: id

  /** getter for id - gets 
   * @generated */
  public String getId() {
    if (Suggestion_Type.featOkTst && ((Suggestion_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.Suggestion");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Suggestion_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated */
  public void setId(String v) {
    if (Suggestion_Type.featOkTst && ((Suggestion_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.Suggestion");
    jcasType.ll_cas.ll_setStringValue(addr, ((Suggestion_Type)jcasType).casFeatCode_id, v);}    
  }

    