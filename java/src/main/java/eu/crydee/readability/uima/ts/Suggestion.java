

/* First created by JCasGen Tue Jun 17 15:34:47 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Jun 17 15:34:47 JST 2014
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
  //* Feature: original

  /** getter for original - gets 
   * @generated */
  public Original getOriginal() {
    if (Suggestion_Type.featOkTst && ((Suggestion_Type)jcasType).casFeat_original == null)
      jcasType.jcas.throwFeatMissing("original", "eu.crydee.readability.uima.ts.Suggestion");
    return (Original)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Suggestion_Type)jcasType).casFeatCode_original)));}
    
  /** setter for original - sets  
   * @generated */
  public void setOriginal(Original v) {
    if (Suggestion_Type.featOkTst && ((Suggestion_Type)jcasType).casFeat_original == null)
      jcasType.jcas.throwFeatMissing("original", "eu.crydee.readability.uima.ts.Suggestion");
    jcasType.ll_cas.ll_setRefValue(addr, ((Suggestion_Type)jcasType).casFeatCode_original, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: revised

  /** getter for revised - gets 
   * @generated */
  public FSArray getRevised() {
    if (Suggestion_Type.featOkTst && ((Suggestion_Type)jcasType).casFeat_revised == null)
      jcasType.jcas.throwFeatMissing("revised", "eu.crydee.readability.uima.ts.Suggestion");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Suggestion_Type)jcasType).casFeatCode_revised)));}
    
  /** setter for revised - sets  
   * @generated */
  public void setRevised(FSArray v) {
    if (Suggestion_Type.featOkTst && ((Suggestion_Type)jcasType).casFeat_revised == null)
      jcasType.jcas.throwFeatMissing("revised", "eu.crydee.readability.uima.ts.Suggestion");
    jcasType.ll_cas.ll_setRefValue(addr, ((Suggestion_Type)jcasType).casFeatCode_revised, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for revised - gets an indexed value - 
   * @generated */
  public Revised getRevised(int i) {
    if (Suggestion_Type.featOkTst && ((Suggestion_Type)jcasType).casFeat_revised == null)
      jcasType.jcas.throwFeatMissing("revised", "eu.crydee.readability.uima.ts.Suggestion");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Suggestion_Type)jcasType).casFeatCode_revised), i);
    return (Revised)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Suggestion_Type)jcasType).casFeatCode_revised), i)));}

  /** indexed setter for revised - sets an indexed value - 
   * @generated */
  public void setRevised(int i, Revised v) { 
    if (Suggestion_Type.featOkTst && ((Suggestion_Type)jcasType).casFeat_revised == null)
      jcasType.jcas.throwFeatMissing("revised", "eu.crydee.readability.uima.ts.Suggestion");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Suggestion_Type)jcasType).casFeatCode_revised), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Suggestion_Type)jcasType).casFeatCode_revised), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    