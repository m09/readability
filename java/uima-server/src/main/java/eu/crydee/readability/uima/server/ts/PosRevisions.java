

/* First created by JCasGen Sat Jul 05 15:41:20 JST 2014 */
package eu.crydee.readability.uima.server.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Sat Jul 05 15:41:20 JST 2014
 * XML source: /mnt/data/work/readability/uima-server/src/main/resources/eu/crydee/readability/uima/server/ts/server.xml
 * @generated */
public class PosRevisions extends Revisions {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(PosRevisions.class);
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
  protected PosRevisions() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public PosRevisions(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public PosRevisions(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public PosRevisions(JCas jcas, int begin, int end) {
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

    