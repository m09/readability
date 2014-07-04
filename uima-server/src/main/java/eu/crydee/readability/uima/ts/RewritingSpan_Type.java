
/* First created by JCasGen Fri Jul 04 21:09:04 JST 2014 */
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
 * Updated by JCasGen Fri Jul 04 21:09:04 JST 2014
 * @generated */
public class RewritingSpan_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (RewritingSpan_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = RewritingSpan_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new RewritingSpan(addr, RewritingSpan_Type.this);
  			   RewritingSpan_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new RewritingSpan(addr, RewritingSpan_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = RewritingSpan.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.RewritingSpan");
 
  /** @generated */
  final Feature casFeat_revisionsId;
  /** @generated */
  final int     casFeatCode_revisionsId;
  /** @generated */ 
  public String getRevisionsId(int addr) {
        if (featOkTst && casFeat_revisionsId == null)
      jcas.throwFeatMissing("revisionsId", "eu.crydee.readability.uima.ts.RewritingSpan");
    return ll_cas.ll_getStringValue(addr, casFeatCode_revisionsId);
  }
  /** @generated */    
  public void setRevisionsId(int addr, String v) {
        if (featOkTst && casFeat_revisionsId == null)
      jcas.throwFeatMissing("revisionsId", "eu.crydee.readability.uima.ts.RewritingSpan");
    ll_cas.ll_setStringValue(addr, casFeatCode_revisionsId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_revisionsIndex;
  /** @generated */
  final int     casFeatCode_revisionsIndex;
  /** @generated */ 
  public int getRevisionsIndex(int addr) {
        if (featOkTst && casFeat_revisionsIndex == null)
      jcas.throwFeatMissing("revisionsIndex", "eu.crydee.readability.uima.ts.RewritingSpan");
    return ll_cas.ll_getIntValue(addr, casFeatCode_revisionsIndex);
  }
  /** @generated */    
  public void setRevisionsIndex(int addr, int v) {
        if (featOkTst && casFeat_revisionsIndex == null)
      jcas.throwFeatMissing("revisionsIndex", "eu.crydee.readability.uima.ts.RewritingSpan");
    ll_cas.ll_setIntValue(addr, casFeatCode_revisionsIndex, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public RewritingSpan_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_revisionsId = jcas.getRequiredFeatureDE(casType, "revisionsId", "uima.cas.String", featOkTst);
    casFeatCode_revisionsId  = (null == casFeat_revisionsId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_revisionsId).getCode();

 
    casFeat_revisionsIndex = jcas.getRequiredFeatureDE(casType, "revisionsIndex", "uima.cas.Integer", featOkTst);
    casFeatCode_revisionsIndex  = (null == casFeat_revisionsIndex) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_revisionsIndex).getCode();

  }
}



    