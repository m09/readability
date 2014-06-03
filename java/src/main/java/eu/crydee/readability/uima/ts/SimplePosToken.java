

/* First created by JCasGen Tue Jun 03 10:36:02 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Jun 03 10:36:02 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class SimplePosToken extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SimplePosToken.class);
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
  protected SimplePosToken() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public SimplePosToken(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public SimplePosToken(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public SimplePosToken(JCas jcas, int begin, int end) {
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
    if (SimplePosToken_Type.featOkTst && ((SimplePosToken_Type)jcasType).casFeat_POS == null)
      jcasType.jcas.throwFeatMissing("POS", "eu.crydee.readability.uima.ts.SimplePosToken");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SimplePosToken_Type)jcasType).casFeatCode_POS);}
    
  /** setter for POS - sets  
   * @generated */
  public void setPOS(String v) {
    if (SimplePosToken_Type.featOkTst && ((SimplePosToken_Type)jcasType).casFeat_POS == null)
      jcasType.jcas.throwFeatMissing("POS", "eu.crydee.readability.uima.ts.SimplePosToken");
    jcasType.ll_cas.ll_setStringValue(addr, ((SimplePosToken_Type)jcasType).casFeatCode_POS, v);}    
  }

    