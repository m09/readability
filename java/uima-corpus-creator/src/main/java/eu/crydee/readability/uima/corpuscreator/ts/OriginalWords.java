

/* First created by JCasGen Sat Jul 05 16:29:21 JST 2014 */
package eu.crydee.readability.uima.corpuscreator.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Jul 05 16:29:21 JST 2014
 * XML source: /mnt/data/work/readability/java/uima-corpus-creator/src/main/resources/eu/crydee/readability/uima/ts/corpuscreator.xml
 * @generated */
public class OriginalWords extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(OriginalWords.class);
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
  protected OriginalWords() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public OriginalWords(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public OriginalWords(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public OriginalWords(JCas jcas, int begin, int end) {
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
  //* Feature: revisedWords

  /** getter for revisedWords - gets 
   * @generated */
  public RevisedWords getRevisedWords() {
    if (OriginalWords_Type.featOkTst && ((OriginalWords_Type)jcasType).casFeat_revisedWords == null)
      jcasType.jcas.throwFeatMissing("revisedWords", "eu.crydee.readability.uima.corpuscreator.ts.OriginalWords");
    return (RevisedWords)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((OriginalWords_Type)jcasType).casFeatCode_revisedWords)));}
    
  /** setter for revisedWords - sets  
   * @generated */
  public void setRevisedWords(RevisedWords v) {
    if (OriginalWords_Type.featOkTst && ((OriginalWords_Type)jcasType).casFeat_revisedWords == null)
      jcasType.jcas.throwFeatMissing("revisedWords", "eu.crydee.readability.uima.corpuscreator.ts.OriginalWords");
    jcasType.ll_cas.ll_setRefValue(addr, ((OriginalWords_Type)jcasType).casFeatCode_revisedWords, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    