
/* First created by JCasGen Wed May 07 20:09:24 JST 2014 */
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
 * Updated by JCasGen Wed May 07 20:09:24 JST 2014
 * @generated */
public class RevisionInfo_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (RevisionInfo_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = RevisionInfo_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new RevisionInfo(addr, RevisionInfo_Type.this);
  			   RevisionInfo_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new RevisionInfo(addr, RevisionInfo_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = RevisionInfo.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.RevisionInfo");
 
  /** @generated */
  final Feature casFeat_id;
  /** @generated */
  final int     casFeatCode_id;
  /** @generated */ 
  public long getId(int addr) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.RevisionInfo");
    return ll_cas.ll_getLongValue(addr, casFeatCode_id);
  }
  /** @generated */    
  public void setId(int addr, long v) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "eu.crydee.readability.uima.ts.RevisionInfo");
    ll_cas.ll_setLongValue(addr, casFeatCode_id, v);}
    
  
 
  /** @generated */
  final Feature casFeat_parentId;
  /** @generated */
  final int     casFeatCode_parentId;
  /** @generated */ 
  public long getParentId(int addr) {
        if (featOkTst && casFeat_parentId == null)
      jcas.throwFeatMissing("parentId", "eu.crydee.readability.uima.ts.RevisionInfo");
    return ll_cas.ll_getLongValue(addr, casFeatCode_parentId);
  }
  /** @generated */    
  public void setParentId(int addr, long v) {
        if (featOkTst && casFeat_parentId == null)
      jcas.throwFeatMissing("parentId", "eu.crydee.readability.uima.ts.RevisionInfo");
    ll_cas.ll_setLongValue(addr, casFeatCode_parentId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_minor;
  /** @generated */
  final int     casFeatCode_minor;
  /** @generated */ 
  public boolean getMinor(int addr) {
        if (featOkTst && casFeat_minor == null)
      jcas.throwFeatMissing("minor", "eu.crydee.readability.uima.ts.RevisionInfo");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_minor);
  }
  /** @generated */    
  public void setMinor(int addr, boolean v) {
        if (featOkTst && casFeat_minor == null)
      jcas.throwFeatMissing("minor", "eu.crydee.readability.uima.ts.RevisionInfo");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_minor, v);}
    
  
 
  /** @generated */
  final Feature casFeat_timestamp;
  /** @generated */
  final int     casFeatCode_timestamp;
  /** @generated */ 
  public String getTimestamp(int addr) {
        if (featOkTst && casFeat_timestamp == null)
      jcas.throwFeatMissing("timestamp", "eu.crydee.readability.uima.ts.RevisionInfo");
    return ll_cas.ll_getStringValue(addr, casFeatCode_timestamp);
  }
  /** @generated */    
  public void setTimestamp(int addr, String v) {
        if (featOkTst && casFeat_timestamp == null)
      jcas.throwFeatMissing("timestamp", "eu.crydee.readability.uima.ts.RevisionInfo");
    ll_cas.ll_setStringValue(addr, casFeatCode_timestamp, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public RevisionInfo_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_id = jcas.getRequiredFeatureDE(casType, "id", "uima.cas.Long", featOkTst);
    casFeatCode_id  = (null == casFeat_id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_id).getCode();

 
    casFeat_parentId = jcas.getRequiredFeatureDE(casType, "parentId", "uima.cas.Long", featOkTst);
    casFeatCode_parentId  = (null == casFeat_parentId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_parentId).getCode();

 
    casFeat_minor = jcas.getRequiredFeatureDE(casType, "minor", "uima.cas.Boolean", featOkTst);
    casFeatCode_minor  = (null == casFeat_minor) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_minor).getCode();

 
    casFeat_timestamp = jcas.getRequiredFeatureDE(casType, "timestamp", "uima.cas.String", featOkTst);
    casFeatCode_timestamp  = (null == casFeat_timestamp) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_timestamp).getCode();

  }
}



    