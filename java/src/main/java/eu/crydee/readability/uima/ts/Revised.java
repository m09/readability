

/* First created by JCasGen Sun May 11 01:49:52 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun May 11 01:49:52 JST 2014
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
  //* Feature: pos

  /** getter for pos - gets 
   * @generated */
  public StringArray getPos() {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "eu.crydee.readability.uima.ts.Revised");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Revised_Type)jcasType).casFeatCode_pos)));}
    
  /** setter for pos - sets  
   * @generated */
  public void setPos(StringArray v) {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "eu.crydee.readability.uima.ts.Revised");
    jcasType.ll_cas.ll_setRefValue(addr, ((Revised_Type)jcasType).casFeatCode_pos, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for pos - gets an indexed value - 
   * @generated */
  public String getPos(int i) {
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "eu.crydee.readability.uima.ts.Revised");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Revised_Type)jcasType).casFeatCode_pos), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Revised_Type)jcasType).casFeatCode_pos), i);}

  /** indexed setter for pos - sets an indexed value - 
   * @generated */
  public void setPos(int i, String v) { 
    if (Revised_Type.featOkTst && ((Revised_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "eu.crydee.readability.uima.ts.Revised");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Revised_Type)jcasType).casFeatCode_pos), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Revised_Type)jcasType).casFeatCode_pos), i, v);}
   
    
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
  }

    