

/* First created by JCasGen Wed May 07 20:09:24 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed May 07 20:09:24 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/ReadabilityTS.xml
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
  //* Feature: pos

  /** getter for pos - gets 
   * @generated */
  public StringArray getPos() {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "eu.crydee.readability.uima.ts.Revision");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Revision_Type)jcasType).casFeatCode_pos)));}
    
  /** setter for pos - sets  
   * @generated */
  public void setPos(StringArray v) {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "eu.crydee.readability.uima.ts.Revision");
    jcasType.ll_cas.ll_setRefValue(addr, ((Revision_Type)jcasType).casFeatCode_pos, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for pos - gets an indexed value - 
   * @generated */
  public String getPos(int i) {
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "eu.crydee.readability.uima.ts.Revision");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Revision_Type)jcasType).casFeatCode_pos), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Revision_Type)jcasType).casFeatCode_pos), i);}

  /** indexed setter for pos - sets an indexed value - 
   * @generated */
  public void setPos(int i, String v) { 
    if (Revision_Type.featOkTst && ((Revision_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "eu.crydee.readability.uima.ts.Revision");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Revision_Type)jcasType).casFeatCode_pos), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Revision_Type)jcasType).casFeatCode_pos), i, v);}
  }

    