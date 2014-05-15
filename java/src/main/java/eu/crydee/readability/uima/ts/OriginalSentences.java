

/* First created by JCasGen Thu May 15 14:44:53 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu May 15 14:44:53 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictCreationTS.xml
 * @generated */
public class OriginalSentences extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(OriginalSentences.class);
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
  protected OriginalSentences() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public OriginalSentences(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public OriginalSentences(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public OriginalSentences(JCas jcas, int begin, int end) {
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
  //* Feature: revisedSentences

  /** getter for revisedSentences - gets 
   * @generated */
  public RevisedSentences getRevisedSentences() {
    if (OriginalSentences_Type.featOkTst && ((OriginalSentences_Type)jcasType).casFeat_revisedSentences == null)
      jcasType.jcas.throwFeatMissing("revisedSentences", "eu.crydee.readability.uima.ts.OriginalSentences");
    return (RevisedSentences)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((OriginalSentences_Type)jcasType).casFeatCode_revisedSentences)));}
    
  /** setter for revisedSentences - sets  
   * @generated */
  public void setRevisedSentences(RevisedSentences v) {
    if (OriginalSentences_Type.featOkTst && ((OriginalSentences_Type)jcasType).casFeat_revisedSentences == null)
      jcasType.jcas.throwFeatMissing("revisedSentences", "eu.crydee.readability.uima.ts.OriginalSentences");
    jcasType.ll_cas.ll_setRefValue(addr, ((OriginalSentences_Type)jcasType).casFeatCode_revisedSentences, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    