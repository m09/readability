

/* First created by JCasGen Sat Jul 05 15:41:20 JST 2014 */
package eu.crydee.readability.uima.server.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Jul 05 15:41:20 JST 2014
 * XML source: /mnt/data/work/readability/uima-server/src/main/resources/eu/crydee/readability/uima/server/ts/server.xml
 * @generated */
public class Suggestion extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Suggestion.class);
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
  protected Suggestion() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Suggestion(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Suggestion(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Suggestion(JCas jcas, int begin, int end) {
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
  public Revisions getRevisions() {
    if (Suggestion_Type.featOkTst && ((Suggestion_Type)jcasType).casFeat_revisions == null)
      jcasType.jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.server.ts.Suggestion");
    return (Revisions)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Suggestion_Type)jcasType).casFeatCode_revisions)));}
    
  /** setter for revisions - sets  
   * @generated */
  public void setRevisions(Revisions v) {
    if (Suggestion_Type.featOkTst && ((Suggestion_Type)jcasType).casFeat_revisions == null)
      jcasType.jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.server.ts.Suggestion");
    jcasType.ll_cas.ll_setRefValue(addr, ((Suggestion_Type)jcasType).casFeatCode_revisions, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    