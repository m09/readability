

/* First created by JCasGen Mon Apr 21 14:52:22 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Apr 21 14:52:22 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/ReadabilityTS.xml
 * @generated */
public class RevisedSentences extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(RevisedSentences.class);
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
  protected RevisedSentences() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public RevisedSentences(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public RevisedSentences(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public RevisedSentences(JCas jcas, int begin, int end) {
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
  //* Feature: originalSentences

  /** getter for originalSentences - gets 
   * @generated */
  public OriginalSentences getOriginalSentences() {
    if (RevisedSentences_Type.featOkTst && ((RevisedSentences_Type)jcasType).casFeat_originalSentences == null)
      jcasType.jcas.throwFeatMissing("originalSentences", "eu.crydee.readability.uima.ts.RevisedSentences");
    return (OriginalSentences)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((RevisedSentences_Type)jcasType).casFeatCode_originalSentences)));}
    
  /** setter for originalSentences - sets  
   * @generated */
  public void setOriginalSentences(OriginalSentences v) {
    if (RevisedSentences_Type.featOkTst && ((RevisedSentences_Type)jcasType).casFeat_originalSentences == null)
      jcasType.jcas.throwFeatMissing("originalSentences", "eu.crydee.readability.uima.ts.RevisedSentences");
    jcasType.ll_cas.ll_setRefValue(addr, ((RevisedSentences_Type)jcasType).casFeatCode_originalSentences, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    