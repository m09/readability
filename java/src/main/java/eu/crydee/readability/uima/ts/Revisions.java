

/* First created by JCasGen Fri Jul 04 14:40:28 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Jul 04 14:40:28 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class Revisions extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Revisions.class);
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
  protected Revisions() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Revisions(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Revisions(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Revisions(JCas jcas, int begin, int end) {
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
  //* Feature: id

  /** getter for id - gets 
   * @generated */
  public String getId() {
    if (Revisions_Type.featOkTst && ((Revisions_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.Revisions");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Revisions_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated */
  public void setId(String v) {
    if (Revisions_Type.featOkTst && ((Revisions_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.Revisions");
    jcasType.ll_cas.ll_setStringValue(addr, ((Revisions_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: revisions

  /** getter for revisions - gets 
   * @generated */
  public FSArray getRevisions() {
    if (Revisions_Type.featOkTst && ((Revisions_Type)jcasType).casFeat_revisions == null)
      jcasType.jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Revisions");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Revisions_Type)jcasType).casFeatCode_revisions)));}
    
  /** setter for revisions - sets  
   * @generated */
  public void setRevisions(FSArray v) {
    if (Revisions_Type.featOkTst && ((Revisions_Type)jcasType).casFeat_revisions == null)
      jcasType.jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Revisions");
    jcasType.ll_cas.ll_setRefValue(addr, ((Revisions_Type)jcasType).casFeatCode_revisions, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for revisions - gets an indexed value - 
   * @generated */
  public Revision getRevisions(int i) {
    if (Revisions_Type.featOkTst && ((Revisions_Type)jcasType).casFeat_revisions == null)
      jcasType.jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Revisions");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Revisions_Type)jcasType).casFeatCode_revisions), i);
    return (Revision)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Revisions_Type)jcasType).casFeatCode_revisions), i)));}

  /** indexed setter for revisions - sets an indexed value - 
   * @generated */
  public void setRevisions(int i, Revision v) { 
    if (Revisions_Type.featOkTst && ((Revisions_Type)jcasType).casFeat_revisions == null)
      jcasType.jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Revisions");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Revisions_Type)jcasType).casFeatCode_revisions), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Revisions_Type)jcasType).casFeatCode_revisions), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    