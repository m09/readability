

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
public class RevisedSentence extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(RevisedSentence.class);
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
  protected RevisedSentence() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public RevisedSentence(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public RevisedSentence(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public RevisedSentence(JCas jcas, int begin, int end) {
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
     
}

    