

/* First created by JCasGen Sat Jul 05 15:41:20 JST 2014 */
package eu.crydee.readability.uima.server.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Jul 05 15:41:20 JST 2014
 * XML source: /mnt/data/work/readability/uima-server/src/main/resources/eu/crydee/readability/uima/server/ts/server.xml
 * @generated */
public class RewritingsByScoreThenSemiring extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(RewritingsByScoreThenSemiring.class);
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
  protected RewritingsByScoreThenSemiring() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public RewritingsByScoreThenSemiring(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public RewritingsByScoreThenSemiring(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public RewritingsByScoreThenSemiring(JCas jcas, int begin, int end) {
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
  //* Feature: rewritingsByScoreThenSemiring

  /** getter for rewritingsByScoreThenSemiring - gets 
   * @generated */
  public FSArray getRewritingsByScoreThenSemiring() {
    if (RewritingsByScoreThenSemiring_Type.featOkTst && ((RewritingsByScoreThenSemiring_Type)jcasType).casFeat_rewritingsByScoreThenSemiring == null)
      jcasType.jcas.throwFeatMissing("rewritingsByScoreThenSemiring", "eu.crydee.readability.uima.server.ts.RewritingsByScoreThenSemiring");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((RewritingsByScoreThenSemiring_Type)jcasType).casFeatCode_rewritingsByScoreThenSemiring)));}
    
  /** setter for rewritingsByScoreThenSemiring - sets  
   * @generated */
  public void setRewritingsByScoreThenSemiring(FSArray v) {
    if (RewritingsByScoreThenSemiring_Type.featOkTst && ((RewritingsByScoreThenSemiring_Type)jcasType).casFeat_rewritingsByScoreThenSemiring == null)
      jcasType.jcas.throwFeatMissing("rewritingsByScoreThenSemiring", "eu.crydee.readability.uima.server.ts.RewritingsByScoreThenSemiring");
    jcasType.ll_cas.ll_setRefValue(addr, ((RewritingsByScoreThenSemiring_Type)jcasType).casFeatCode_rewritingsByScoreThenSemiring, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for rewritingsByScoreThenSemiring - gets an indexed value - 
   * @generated */
  public RewritingsBySemiring getRewritingsByScoreThenSemiring(int i) {
    if (RewritingsByScoreThenSemiring_Type.featOkTst && ((RewritingsByScoreThenSemiring_Type)jcasType).casFeat_rewritingsByScoreThenSemiring == null)
      jcasType.jcas.throwFeatMissing("rewritingsByScoreThenSemiring", "eu.crydee.readability.uima.server.ts.RewritingsByScoreThenSemiring");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((RewritingsByScoreThenSemiring_Type)jcasType).casFeatCode_rewritingsByScoreThenSemiring), i);
    return (RewritingsBySemiring)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((RewritingsByScoreThenSemiring_Type)jcasType).casFeatCode_rewritingsByScoreThenSemiring), i)));}

  /** indexed setter for rewritingsByScoreThenSemiring - sets an indexed value - 
   * @generated */
  public void setRewritingsByScoreThenSemiring(int i, RewritingsBySemiring v) { 
    if (RewritingsByScoreThenSemiring_Type.featOkTst && ((RewritingsByScoreThenSemiring_Type)jcasType).casFeat_rewritingsByScoreThenSemiring == null)
      jcasType.jcas.throwFeatMissing("rewritingsByScoreThenSemiring", "eu.crydee.readability.uima.server.ts.RewritingsByScoreThenSemiring");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((RewritingsByScoreThenSemiring_Type)jcasType).casFeatCode_rewritingsByScoreThenSemiring), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((RewritingsByScoreThenSemiring_Type)jcasType).casFeatCode_rewritingsByScoreThenSemiring), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    