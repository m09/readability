

/* First created by JCasGen Wed Jun 18 17:07:12 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Wed Jun 18 17:07:12 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class TxtSuggestion extends Suggestion {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TxtSuggestion.class);
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
  protected TxtSuggestion() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TxtSuggestion(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TxtSuggestion(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TxtSuggestion(JCas jcas, int begin, int end) {
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

    