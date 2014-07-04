

/* First created by JCasGen Fri Jul 04 14:40:28 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Fri Jul 04 14:40:28 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class TxtRevisions extends Revisions {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TxtRevisions.class);
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
  protected TxtRevisions() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TxtRevisions(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TxtRevisions(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TxtRevisions(JCas jcas, int begin, int end) {
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

    