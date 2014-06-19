

/* First created by JCasGen Thu Jun 19 18:22:11 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Jun 19 18:22:11 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class Rewriting extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Rewriting.class);
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
  protected Rewriting() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Rewriting(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Rewriting(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Rewriting(JCas jcas, int begin, int end) {
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
  //* Feature: revisions

  /** getter for revisions - gets 
   * @generated */
  public FSArray getRevisions() {
    if (Rewriting_Type.featOkTst && ((Rewriting_Type)jcasType).casFeat_revisions == null)
      jcasType.jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Rewriting");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Rewriting_Type)jcasType).casFeatCode_revisions)));}
    
  /** setter for revisions - sets  
   * @generated */
  public void setRevisions(FSArray v) {
    if (Rewriting_Type.featOkTst && ((Rewriting_Type)jcasType).casFeat_revisions == null)
      jcasType.jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Rewriting");
    jcasType.ll_cas.ll_setRefValue(addr, ((Rewriting_Type)jcasType).casFeatCode_revisions, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for revisions - gets an indexed value - 
   * @generated */
  public Revision getRevisions(int i) {
    if (Rewriting_Type.featOkTst && ((Rewriting_Type)jcasType).casFeat_revisions == null)
      jcasType.jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Rewriting");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Rewriting_Type)jcasType).casFeatCode_revisions), i);
    return (Revision)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Rewriting_Type)jcasType).casFeatCode_revisions), i)));}

  /** indexed setter for revisions - sets an indexed value - 
   * @generated */
  public void setRevisions(int i, Revision v) { 
    if (Rewriting_Type.featOkTst && ((Rewriting_Type)jcasType).casFeat_revisions == null)
      jcasType.jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Rewriting");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Rewriting_Type)jcasType).casFeatCode_revisions), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Rewriting_Type)jcasType).casFeatCode_revisions), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    