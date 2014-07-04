

/* First created by JCasGen Fri Jul 04 21:09:04 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Jul 04 21:09:04 JST 2014
 * XML source: /mnt/data/work/readability/uima-server/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class Token extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Token.class);
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
  protected Token() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Token(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Token(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Token(JCas jcas, int begin, int end) {
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
  //* Feature: POS

  /** getter for POS - gets 
   * @generated */
  public String getPOS() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_POS == null)
      jcasType.jcas.throwFeatMissing("POS", "eu.crydee.readability.uima.ts.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_POS);}
    
  /** setter for POS - sets  
   * @generated */
  public void setPOS(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_POS == null)
      jcasType.jcas.throwFeatMissing("POS", "eu.crydee.readability.uima.ts.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_POS, v);}    
   
    
  //*--------------*
  //* Feature: syllablesNumber

  /** getter for syllablesNumber - gets 
   * @generated */
  public int getSyllablesNumber() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_syllablesNumber == null)
      jcasType.jcas.throwFeatMissing("syllablesNumber", "eu.crydee.readability.uima.ts.Token");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Token_Type)jcasType).casFeatCode_syllablesNumber);}
    
  /** setter for syllablesNumber - sets  
   * @generated */
  public void setSyllablesNumber(int v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_syllablesNumber == null)
      jcasType.jcas.throwFeatMissing("syllablesNumber", "eu.crydee.readability.uima.ts.Token");
    jcasType.ll_cas.ll_setIntValue(addr, ((Token_Type)jcasType).casFeatCode_syllablesNumber, v);}    
  }

    