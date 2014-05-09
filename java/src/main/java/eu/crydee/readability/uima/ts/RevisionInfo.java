

/* First created by JCasGen Fri May 09 16:51:46 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri May 09 16:51:46 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/ReadabilityTS.xml
 * @generated */
public class RevisionInfo extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(RevisionInfo.class);
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
  protected RevisionInfo() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public RevisionInfo(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public RevisionInfo(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public RevisionInfo(JCas jcas, int begin, int end) {
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
  public long getId() {
    if (RevisionInfo_Type.featOkTst && ((RevisionInfo_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.RevisionInfo");
    return jcasType.ll_cas.ll_getLongValue(addr, ((RevisionInfo_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated */
  public void setId(long v) {
    if (RevisionInfo_Type.featOkTst && ((RevisionInfo_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.RevisionInfo");
    jcasType.ll_cas.ll_setLongValue(addr, ((RevisionInfo_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: parentId

  /** getter for parentId - gets 
   * @generated */
  public long getParentId() {
    if (RevisionInfo_Type.featOkTst && ((RevisionInfo_Type)jcasType).casFeat_parentId == null)
      jcasType.jcas.throwFeatMissing("parentId", "eu.crydee.readability.uima.ts.RevisionInfo");
    return jcasType.ll_cas.ll_getLongValue(addr, ((RevisionInfo_Type)jcasType).casFeatCode_parentId);}
    
  /** setter for parentId - sets  
   * @generated */
  public void setParentId(long v) {
    if (RevisionInfo_Type.featOkTst && ((RevisionInfo_Type)jcasType).casFeat_parentId == null)
      jcasType.jcas.throwFeatMissing("parentId", "eu.crydee.readability.uima.ts.RevisionInfo");
    jcasType.ll_cas.ll_setLongValue(addr, ((RevisionInfo_Type)jcasType).casFeatCode_parentId, v);}    
   
    
  //*--------------*
  //* Feature: minor

  /** getter for minor - gets 
   * @generated */
  public boolean getMinor() {
    if (RevisionInfo_Type.featOkTst && ((RevisionInfo_Type)jcasType).casFeat_minor == null)
      jcasType.jcas.throwFeatMissing("minor", "eu.crydee.readability.uima.ts.RevisionInfo");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((RevisionInfo_Type)jcasType).casFeatCode_minor);}
    
  /** setter for minor - sets  
   * @generated */
  public void setMinor(boolean v) {
    if (RevisionInfo_Type.featOkTst && ((RevisionInfo_Type)jcasType).casFeat_minor == null)
      jcasType.jcas.throwFeatMissing("minor", "eu.crydee.readability.uima.ts.RevisionInfo");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((RevisionInfo_Type)jcasType).casFeatCode_minor, v);}    
   
    
  //*--------------*
  //* Feature: timestamp

  /** getter for timestamp - gets 
   * @generated */
  public String getTimestamp() {
    if (RevisionInfo_Type.featOkTst && ((RevisionInfo_Type)jcasType).casFeat_timestamp == null)
      jcasType.jcas.throwFeatMissing("timestamp", "eu.crydee.readability.uima.ts.RevisionInfo");
    return jcasType.ll_cas.ll_getStringValue(addr, ((RevisionInfo_Type)jcasType).casFeatCode_timestamp);}
    
  /** setter for timestamp - sets  
   * @generated */
  public void setTimestamp(String v) {
    if (RevisionInfo_Type.featOkTst && ((RevisionInfo_Type)jcasType).casFeat_timestamp == null)
      jcasType.jcas.throwFeatMissing("timestamp", "eu.crydee.readability.uima.ts.RevisionInfo");
    jcasType.ll_cas.ll_setStringValue(addr, ((RevisionInfo_Type)jcasType).casFeatCode_timestamp, v);}    
  }

    