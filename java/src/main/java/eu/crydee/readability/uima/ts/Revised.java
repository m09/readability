

/* First created by JCasGen Tue Jun 17 15:34:47 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Jun 17 15:34:47 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class Revised extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Revised.class);
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
  protected Revised() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Revised(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Revised(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Revised(JCas jcas, int begin, int end) {
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
  //* Feature: text

  /** getter for text - gets 
   * @generated */
  public String getText() {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "eu.crydee.readability.uima.ts.Revised");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Revised_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated */
  public void setText(String v) {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "eu.crydee.readability.uima.ts.Revised");
    jcasType.ll_cas.ll_setStringValue(addr, ((Revised_Type)jcasType).casFeatCode_text, v);}    
   
    
  //*--------------*
  //* Feature: tokens

  /** getter for tokens - gets 
   * @generated */
  public StringArray getTokens() {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revised");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Revised_Type)jcasType).casFeatCode_tokens)));}
    
  /** setter for tokens - sets  
   * @generated */
  public void setTokens(StringArray v) {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revised");
    jcasType.ll_cas.ll_setRefValue(addr, ((Revised_Type)jcasType).casFeatCode_tokens, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for tokens - gets an indexed value - 
   * @generated */
  public String getTokens(int i) {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revised");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Revised_Type)jcasType).casFeatCode_tokens), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Revised_Type)jcasType).casFeatCode_tokens), i);}

  /** indexed setter for tokens - sets an indexed value - 
   * @generated */
  public void setTokens(int i, String v) { 
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revised");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Revised_Type)jcasType).casFeatCode_tokens), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Revised_Type)jcasType).casFeatCode_tokens), i, v);}
   
    
  //*--------------*
  //* Feature: count

  /** getter for count - gets 
   * @generated */
  public int getCount() {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_count == null)
      jcasType.jcas.throwFeatMissing("count", "eu.crydee.readability.uima.ts.Revised");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Revised_Type)jcasType).casFeatCode_count);}
    
  /** setter for count - sets  
   * @generated */
  public void setCount(int v) {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_count == null)
      jcasType.jcas.throwFeatMissing("count", "eu.crydee.readability.uima.ts.Revised");
    jcasType.ll_cas.ll_setIntValue(addr, ((Revised_Type)jcasType).casFeatCode_count, v);}    
   
    
  //*--------------*
  //* Feature: score

  /** getter for score - gets 
   * @generated */
  public double getScore() {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Revised");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Revised_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets  
   * @generated */
  public void setScore(double v) {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Revised");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Revised_Type)jcasType).casFeatCode_score, v);}    
  }

    