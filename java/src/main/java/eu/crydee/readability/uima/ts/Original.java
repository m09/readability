

/* First created by JCasGen Fri May 09 16:51:46 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri May 09 16:51:46 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/ReadabilityTS.xml
 * @generated */
public class Original extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Original.class);
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
  protected Original() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Original(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Original(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Original(JCas jcas, int begin, int end) {
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
    if (Original_Type.featOkTst && ((Original_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "eu.crydee.readability.uima.ts.Original");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Original_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated */
  public void setText(String v) {
    if (Original_Type.featOkTst && ((Original_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "eu.crydee.readability.uima.ts.Original");
    jcasType.ll_cas.ll_setStringValue(addr, ((Original_Type)jcasType).casFeatCode_text, v);}    
   
    
  //*--------------*
  //* Feature: tokens

  /** getter for tokens - gets 
   * @generated */
  public FSArray getTokens() {
    if (Original_Type.featOkTst && ((Original_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Original");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Original_Type)jcasType).casFeatCode_tokens)));}
    
  /** setter for tokens - sets  
   * @generated */
  public void setTokens(FSArray v) {
    if (Original_Type.featOkTst && ((Original_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Original");
    jcasType.ll_cas.ll_setRefValue(addr, ((Original_Type)jcasType).casFeatCode_tokens, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for tokens - gets an indexed value - 
   * @generated */
  public Token getTokens(int i) {
    if (Original_Type.featOkTst && ((Original_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Original");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Original_Type)jcasType).casFeatCode_tokens), i);
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Original_Type)jcasType).casFeatCode_tokens), i)));}

  /** indexed setter for tokens - sets an indexed value - 
   * @generated */
  public void setTokens(int i, Token v) { 
    if (Original_Type.featOkTst && ((Original_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Original");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Original_Type)jcasType).casFeatCode_tokens), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Original_Type)jcasType).casFeatCode_tokens), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    