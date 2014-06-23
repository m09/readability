

/* First created by JCasGen Mon Jun 23 12:39:02 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Jun 23 12:39:02 JST 2014
 * XML source: /mnt/data/work/readability/java/src/main/resources/eu/crydee/readability/uima/ts/DictUsageTS.xml
 * @generated */
public class RewritingSpan extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(RewritingSpan.class);
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
  protected RewritingSpan() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public RewritingSpan(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public RewritingSpan(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public RewritingSpan(JCas jcas, int begin, int end) {
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
    if (RewritingSpan_Type.featOkTst && ((RewritingSpan_Type)jcasType).casFeat_revisionsId == null)
      jcasType.jcas.throwFeatMissing("revisionsId", "eu.crydee.readability.uima.ts.RewritingSpan");
    return jcasType.ll_cas.ll_getStringValue(addr, ((RewritingSpan_Type)jcasType).casFeatCode_revisionsId);}
    
  /** setter for revisionsId - sets  
   * @generated */
  public void setRevisionsId(String v) {
    if (RewritingSpan_Type.featOkTst && ((RewritingSpan_Type)jcasType).casFeat_revisionsId == null)
      jcasType.jcas.throwFeatMissing("revisionsId", "eu.crydee.readability.uima.ts.RewritingSpan");
    jcasType.ll_cas.ll_setStringValue(addr, ((RewritingSpan_Type)jcasType).casFeatCode_revisionsId, v);}    
   
    
  //*--------------*
  //* Feature: revisionsIndex

  /** getter for revisionsIndex - gets 
   * @generated */
  public int getRevisionsIndex() {
    if (RewritingSpan_Type.featOkTst && ((RewritingSpan_Type)jcasType).casFeat_revisionsIndex == null)
      jcasType.jcas.throwFeatMissing("revisionsIndex", "eu.crydee.readability.uima.ts.RewritingSpan");
    return jcasType.ll_cas.ll_getIntValue(addr, ((RewritingSpan_Type)jcasType).casFeatCode_revisionsIndex);}
    
  /** setter for revisionsIndex - sets  
   * @generated */
  public void setRevisionsIndex(int v) {
    if (RewritingSpan_Type.featOkTst && ((RewritingSpan_Type)jcasType).casFeat_revisionsIndex == null)
      jcasType.jcas.throwFeatMissing("revisionsIndex", "eu.crydee.readability.uima.ts.RewritingSpan");
    jcasType.ll_cas.ll_setIntValue(addr, ((RewritingSpan_Type)jcasType).casFeatCode_revisionsIndex, v);}    
  }

    