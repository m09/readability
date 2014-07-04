
/* First created by JCasGen Fri Jul 04 14:40:28 JST 2014 */
package eu.crydee.readability.uima.ts;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Fri Jul 04 14:40:28 JST 2014
 * @generated */
public class Revisions_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Revisions_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Revisions_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Revisions(addr, Revisions_Type.this);
  			   Revisions_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Revisions(addr, Revisions_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Revisions.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.Revisions");
 
  /** @generated */
  final Feature casFeat_id;
  /** @generated */
  final int     casFeatCode_id;
  /** @generated */ 
  public String getId(int addr) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.Revisions");
    return ll_cas.ll_getStringValue(addr, casFeatCode_id);
  }
  /** @generated */    
  public void setId(int addr, String v) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.Revisions");
    ll_cas.ll_setStringValue(addr, casFeatCode_id, v);}
    
  
 
  /** @generated */
  final Feature casFeat_revisions;
  /** @generated */
  final int     casFeatCode_revisions;
  /** @generated */ 
  public int getRevisions(int addr) {
        if (featOkTst && casFeat_revisions == null)
      jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Revisions");
    return ll_cas.ll_getRefValue(addr, casFeatCode_revisions);
  }
  /** @generated */    
  public void setRevisions(int addr, int v) {
        if (featOkTst && casFeat_revisions == null)
      jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Revisions");
    ll_cas.ll_setRefValue(addr, casFeatCode_revisions, v);}
    
   /** @generated */
  public int getRevisions(int addr, int i) {
        if (featOkTst && casFeat_revisions == null)
      jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Revisions");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_revisions), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_revisions), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_revisions), i);
  }
   
  /** @generated */ 
  public void setRevisions(int addr, int i, int v) {
        if (featOkTst && casFeat_revisions == null)
      jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Revisions");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_revisions), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_revisions), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_revisions), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Revisions_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_id = jcas.getRequiredFeatureDE(casType, "id", "uima.cas.String", featOkTst);
    casFeatCode_id  = (null == casFeat_id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_id).getCode();

 
    casFeat_revisions = jcas.getRequiredFeatureDE(casType, "revisions", "uima.cas.FSArray", featOkTst);
    casFeatCode_revisions  = (null == casFeat_revisions) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_revisions).getCode();

  }
}



    