

/* First created by JCasGen Tue Jun 24 19:36:30 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Jun 24 19:36:30 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/EvaluationTS.xml.xml
 * @generated */
public class TokenSet extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TokenSet.class);
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
  protected TokenSet() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TokenSet(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TokenSet(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TokenSet(JCas jcas, int begin, int end) {
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
  //* Feature: tokens

  /** getter for tokens - gets 
   * @generated */
  public StringArray getTokens() {
    if (TokenSet_Type.featOkTst && ((TokenSet_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.TokenSet");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TokenSet_Type)jcasType).casFeatCode_tokens)));}
    
  /** setter for tokens - sets  
   * @generated */
  public void setTokens(StringArray v) {
    if (TokenSet_Type.featOkTst && ((TokenSet_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.TokenSet");
    jcasType.ll_cas.ll_setRefValue(addr, ((TokenSet_Type)jcasType).casFeatCode_tokens, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for tokens - gets an indexed value - 
   * @generated */
  public String getTokens(int i) {
    if (TokenSet_Type.featOkTst && ((TokenSet_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.TokenSet");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TokenSet_Type)jcasType).casFeatCode_tokens), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TokenSet_Type)jcasType).casFeatCode_tokens), i);}

  /** indexed setter for tokens - sets an indexed value - 
   * @generated */
  public void setTokens(int i, String v) { 
    if (TokenSet_Type.featOkTst && ((TokenSet_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.TokenSet");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TokenSet_Type)jcasType).casFeatCode_tokens), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TokenSet_Type)jcasType).casFeatCode_tokens), i, v);}
  }

    