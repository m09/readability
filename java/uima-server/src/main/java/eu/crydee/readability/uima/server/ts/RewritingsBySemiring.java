

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
public class RewritingsBySemiring extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(RewritingsBySemiring.class);
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
  protected RewritingsBySemiring() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public RewritingsBySemiring(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public RewritingsBySemiring(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public RewritingsBySemiring(JCas jcas, int begin, int end) {
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
  //* Feature: rewritingsBySemiring

  /** getter for rewritingsBySemiring - gets 
   * @generated */
  public FSArray getRewritingsBySemiring() {
    if (RewritingsBySemiring_Type.featOkTst && ((RewritingsBySemiring_Type)jcasType).casFeat_rewritingsBySemiring == null)
      jcasType.jcas.throwFeatMissing("rewritingsBySemiring", "eu.crydee.readability.uima.server.ts.RewritingsBySemiring");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((RewritingsBySemiring_Type)jcasType).casFeatCode_rewritingsBySemiring)));}
    
  /** setter for rewritingsBySemiring - sets  
   * @generated */
  public void setRewritingsBySemiring(FSArray v) {
    if (RewritingsBySemiring_Type.featOkTst && ((RewritingsBySemiring_Type)jcasType).casFeat_rewritingsBySemiring == null)
      jcasType.jcas.throwFeatMissing("rewritingsBySemiring", "eu.crydee.readability.uima.server.ts.RewritingsBySemiring");
    jcasType.ll_cas.ll_setRefValue(addr, ((RewritingsBySemiring_Type)jcasType).casFeatCode_rewritingsBySemiring, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for rewritingsBySemiring - gets an indexed value - 
   * @generated */
  public Rewritings getRewritingsBySemiring(int i) {
    if (RewritingsBySemiring_Type.featOkTst && ((RewritingsBySemiring_Type)jcasType).casFeat_rewritingsBySemiring == null)
      jcasType.jcas.throwFeatMissing("rewritingsBySemiring", "eu.crydee.readability.uima.server.ts.RewritingsBySemiring");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((RewritingsBySemiring_Type)jcasType).casFeatCode_rewritingsBySemiring), i);
    return (Rewritings)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((RewritingsBySemiring_Type)jcasType).casFeatCode_rewritingsBySemiring), i)));}

  /** indexed setter for rewritingsBySemiring - sets an indexed value - 
   * @generated */
  public void setRewritingsBySemiring(int i, Rewritings v) { 
    if (RewritingsBySemiring_Type.featOkTst && ((RewritingsBySemiring_Type)jcasType).casFeat_rewritingsBySemiring == null)
      jcasType.jcas.throwFeatMissing("rewritingsBySemiring", "eu.crydee.readability.uima.server.ts.RewritingsBySemiring");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((RewritingsBySemiring_Type)jcasType).casFeatCode_rewritingsBySemiring), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((RewritingsBySemiring_Type)jcasType).casFeatCode_rewritingsBySemiring), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    