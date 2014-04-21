

/* First created by JCasGen Mon Apr 21 15:34:02 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Apr 21 15:34:02 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/ReadabilityTS.xml
 * @generated */
public class RevisedWords extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(RevisedWords.class);
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
  protected RevisedWords() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public RevisedWords(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public RevisedWords(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public RevisedWords(JCas jcas, int begin, int end) {
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
  //* Feature: originalWords

  /** getter for originalWords - gets 
   * @generated */
  public OriginalWords getOriginalWords() {
    if (RevisedWords_Type.featOkTst && ((RevisedWords_Type)jcasType).casFeat_originalWords == null)
      jcasType.jcas.throwFeatMissing("originalWords", "eu.crydee.readability.uima.ts.RevisedWords");
    return (OriginalWords)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((RevisedWords_Type)jcasType).casFeatCode_originalWords)));}
    
  /** setter for originalWords - sets  
   * @generated */
  public void setOriginalWords(OriginalWords v) {
    if (RevisedWords_Type.featOkTst && ((RevisedWords_Type)jcasType).casFeat_originalWords == null)
      jcasType.jcas.throwFeatMissing("originalWords", "eu.crydee.readability.uima.ts.RevisedWords");
    jcasType.ll_cas.ll_setRefValue(addr, ((RevisedWords_Type)jcasType).casFeatCode_originalWords, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    