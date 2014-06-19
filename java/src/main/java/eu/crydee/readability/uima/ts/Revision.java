

/* First created by JCasGen Thu Jun 19 18:22:11 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Jun 19 18:22:11 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class Revision extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Revision.class);
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
  protected Revision() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Revision(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Revision(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Revision(JCas jcas, int begin, int end) {
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
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.Revision");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Revision_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated */
  public void setId(String v) {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.Revision");
    jcasType.ll_cas.ll_setStringValue(addr, ((Revision_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: tokens

  /** getter for tokens - gets 
   * @generated */
  public StringArray getTokens() {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revision");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Revision_Type)jcasType).casFeatCode_tokens)));}
    
  /** setter for tokens - sets  
   * @generated */
  public void setTokens(StringArray v) {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revision");
    jcasType.ll_cas.ll_setRefValue(addr, ((Revision_Type)jcasType).casFeatCode_tokens, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for tokens - gets an indexed value - 
   * @generated */
  public String getTokens(int i) {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revision");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Revision_Type)jcasType).casFeatCode_tokens), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Revision_Type)jcasType).casFeatCode_tokens), i);}

  /** indexed setter for tokens - sets an indexed value - 
   * @generated */
  public void setTokens(int i, String v) { 
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revision");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Revision_Type)jcasType).casFeatCode_tokens), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Revision_Type)jcasType).casFeatCode_tokens), i, v);}
   
    
  //*--------------*
  //* Feature: text

  /** getter for text - gets 
   * @generated */
  public String getText() {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "eu.crydee.readability.uima.ts.Revision");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Revision_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated */
  public void setText(String v) {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "eu.crydee.readability.uima.ts.Revision");
    jcasType.ll_cas.ll_setStringValue(addr, ((Revision_Type)jcasType).casFeatCode_text, v);}    
   
    
  //*--------------*
  //* Feature: count

  /** getter for count - gets 
   * @generated */
  public int getCount() {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_count == null)
      jcasType.jcas.throwFeatMissing("count", "eu.crydee.readability.uima.ts.Revision");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Revision_Type)jcasType).casFeatCode_count);}
    
  /** setter for count - sets  
   * @generated */
  public void setCount(int v) {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_count == null)
      jcasType.jcas.throwFeatMissing("count", "eu.crydee.readability.uima.ts.Revision");
    jcasType.ll_cas.ll_setIntValue(addr, ((Revision_Type)jcasType).casFeatCode_count, v);}    
   
    
  //*--------------*
  //* Feature: score

  /** getter for score - gets 
   * @generated */
  public double getScore() {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Revision");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Revision_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets  
   * @generated */
  public void setScore(double v) {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Revision");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Revision_Type)jcasType).casFeatCode_score, v);}    
  }

    