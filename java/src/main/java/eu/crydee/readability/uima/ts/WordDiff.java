

/* First created by JCasGen Thu Apr 17 21:11:44 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Apr 17 21:11:44 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/ReadabilityTS.xml
 * @generated */
public class WordDiff extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(WordDiff.class);
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
  protected WordDiff() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public WordDiff(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public WordDiff(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public WordDiff(JCas jcas, int begin, int end) {
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
  //* Feature: original

  /** getter for original - gets 
   * @generated */
  public Area getOriginal() {
    if (WordDiff_Type.featOkTst && ((WordDiff_Type)jcasType).casFeat_original == null)
      jcasType.jcas.throwFeatMissing("original", "eu.crydee.readability.uima.ts.WordDiff");
    return (Area)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((WordDiff_Type)jcasType).casFeatCode_original)));}
    
  /** setter for original - sets  
   * @generated */
  public void setOriginal(Area v) {
    if (WordDiff_Type.featOkTst && ((WordDiff_Type)jcasType).casFeat_original == null)
      jcasType.jcas.throwFeatMissing("original", "eu.crydee.readability.uima.ts.WordDiff");
    jcasType.ll_cas.ll_setRefValue(addr, ((WordDiff_Type)jcasType).casFeatCode_original, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    