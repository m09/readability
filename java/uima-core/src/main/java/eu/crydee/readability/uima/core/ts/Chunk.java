

/* First created by JCasGen Sat Jul 05 16:30:40 JST 2014 */
package eu.crydee.readability.uima.core.ts;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Jul 05 16:30:40 JST 2014
 * XML source: /mnt/data/work/readability/java/uima-core/src/main/resources/eu/crydee/readability/uima/core/ts/core.xml
 * @generated */
public class Chunk extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Chunk.class);
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
  protected Chunk() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Chunk(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Chunk(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Chunk(JCas jcas, int begin, int end) {
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
  //* Feature: label

  /** getter for label - gets 
   * @generated */
  public String getLabel() {
    if (Chunk_Type.featOkTst && ((Chunk_Type)jcasType).casFeat_label == null)
      jcasType.jcas.throwFeatMissing("label", "eu.crydee.readability.uima.core.ts.Chunk");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Chunk_Type)jcasType).casFeatCode_label);}
    
  /** setter for label - sets  
   * @generated */
  public void setLabel(String v) {
    if (Chunk_Type.featOkTst && ((Chunk_Type)jcasType).casFeat_label == null)
      jcasType.jcas.throwFeatMissing("label", "eu.crydee.readability.uima.core.ts.Chunk");
    jcasType.ll_cas.ll_setStringValue(addr, ((Chunk_Type)jcasType).casFeatCode_label, v);}    
   
    
  //*--------------*
  //* Feature: children

  /** getter for children - gets 
   * @generated */
  public FSArray getChildren() {
    if (Chunk_Type.featOkTst && ((Chunk_Type)jcasType).casFeat_children == null)
      jcasType.jcas.throwFeatMissing("children", "eu.crydee.readability.uima.core.ts.Chunk");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Chunk_Type)jcasType).casFeatCode_children)));}
    
  /** setter for children - sets  
   * @generated */
  public void setChildren(FSArray v) {
    if (Chunk_Type.featOkTst && ((Chunk_Type)jcasType).casFeat_children == null)
      jcasType.jcas.throwFeatMissing("children", "eu.crydee.readability.uima.core.ts.Chunk");
    jcasType.ll_cas.ll_setRefValue(addr, ((Chunk_Type)jcasType).casFeatCode_children, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for children - gets an indexed value - 
   * @generated */
  public TOP getChildren(int i) {
    if (Chunk_Type.featOkTst && ((Chunk_Type)jcasType).casFeat_children == null)
      jcasType.jcas.throwFeatMissing("children", "eu.crydee.readability.uima.core.ts.Chunk");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Chunk_Type)jcasType).casFeatCode_children), i);
    return (TOP)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Chunk_Type)jcasType).casFeatCode_children), i)));}

  /** indexed setter for children - sets an indexed value - 
   * @generated */
  public void setChildren(int i, TOP v) { 
    if (Chunk_Type.featOkTst && ((Chunk_Type)jcasType).casFeat_children == null)
      jcasType.jcas.throwFeatMissing("children", "eu.crydee.readability.uima.core.ts.Chunk");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Chunk_Type)jcasType).casFeatCode_children), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Chunk_Type)jcasType).casFeatCode_children), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    