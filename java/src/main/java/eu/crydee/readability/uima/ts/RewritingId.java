

/* First created by JCasGen Fri Jun 20 17:03:34 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Jun 20 17:03:34 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class RewritingId extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(RewritingId.class);
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
  protected RewritingId() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public RewritingId(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public RewritingId(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public RewritingId(JCas jcas, int begin, int end) {
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
  //* Feature: revisionsId

  /** getter for revisionsId - gets 
   * @generated */
  public String getRevisionsId() {
    if (RewritingId_Type.featOkTst && ((RewritingId_Type)jcasType).casFeat_revisionsId == null)
      jcasType.jcas.throwFeatMissing("revisionsId", "eu.crydee.readability.uima.ts.RewritingId");
    return jcasType.ll_cas.ll_getStringValue(addr, ((RewritingId_Type)jcasType).casFeatCode_revisionsId);}
    
  /** setter for revisionsId - sets  
   * @generated */
  public void setRevisionsId(String v) {
    if (RewritingId_Type.featOkTst && ((RewritingId_Type)jcasType).casFeat_revisionsId == null)
      jcasType.jcas.throwFeatMissing("revisionsId", "eu.crydee.readability.uima.ts.RewritingId");
    jcasType.ll_cas.ll_setStringValue(addr, ((RewritingId_Type)jcasType).casFeatCode_revisionsId, v);}    
   
    
  //*--------------*
  //* Feature: revisionsIndex

  /** getter for revisionsIndex - gets 
   * @generated */
  public int getRevisionsIndex() {
    if (RewritingId_Type.featOkTst && ((RewritingId_Type)jcasType).casFeat_revisionsIndex == null)
      jcasType.jcas.throwFeatMissing("revisionsIndex", "eu.crydee.readability.uima.ts.RewritingId");
    return jcasType.ll_cas.ll_getIntValue(addr, ((RewritingId_Type)jcasType).casFeatCode_revisionsIndex);}
    
  /** setter for revisionsIndex - sets  
   * @generated */
  public void setRevisionsIndex(int v) {
    if (RewritingId_Type.featOkTst && ((RewritingId_Type)jcasType).casFeat_revisionsIndex == null)
      jcasType.jcas.throwFeatMissing("revisionsIndex", "eu.crydee.readability.uima.ts.RewritingId");
    jcasType.ll_cas.ll_setIntValue(addr, ((RewritingId_Type)jcasType).casFeatCode_revisionsIndex, v);}    
  }

    