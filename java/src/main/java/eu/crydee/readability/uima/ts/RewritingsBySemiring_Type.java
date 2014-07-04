
/* First created by JCasGen Fri Jul 04 15:35:10 JST 2014 */
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
 * Updated by JCasGen Fri Jul 04 15:35:10 JST 2014
 * @generated */
public class RewritingsBySemiring_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (RewritingsBySemiring_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = RewritingsBySemiring_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new RewritingsBySemiring(addr, RewritingsBySemiring_Type.this);
  			   RewritingsBySemiring_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new RewritingsBySemiring(addr, RewritingsBySemiring_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = RewritingsBySemiring.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.RewritingsBySemiring");
 
  /** @generated */
  final Feature casFeat_rewritingsBySemiring;
  /** @generated */
  final int     casFeatCode_rewritingsBySemiring;
  /** @generated */ 
  public int getRewritingsBySemiring(int addr) {
        if (featOkTst && casFeat_rewritingsBySemiring == null)
      jcas.throwFeatMissing("rewritingsBySemiring", "eu.crydee.readability.uima.ts.RewritingsBySemiring");
    return ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsBySemiring);
  }
  /** @generated */    
  public void setRewritingsBySemiring(int addr, int v) {
        if (featOkTst && casFeat_rewritingsBySemiring == null)
      jcas.throwFeatMissing("rewritingsBySemiring", "eu.crydee.readability.uima.ts.RewritingsBySemiring");
    ll_cas.ll_setRefValue(addr, casFeatCode_rewritingsBySemiring, v);}
    
   /** @generated */
  public int getRewritingsBySemiring(int addr, int i) {
        if (featOkTst && casFeat_rewritingsBySemiring == null)
      jcas.throwFeatMissing("rewritingsBySemiring", "eu.crydee.readability.uima.ts.RewritingsBySemiring");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsBySemiring), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsBySemiring), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsBySemiring), i);
  }
   
  /** @generated */ 
  public void setRewritingsBySemiring(int addr, int i, int v) {
        if (featOkTst && casFeat_rewritingsBySemiring == null)
      jcas.throwFeatMissing("rewritingsBySemiring", "eu.crydee.readability.uima.ts.RewritingsBySemiring");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsBySemiring), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsBySemiring), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsBySemiring), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public RewritingsBySemiring_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_rewritingsBySemiring = jcas.getRequiredFeatureDE(casType, "rewritingsBySemiring", "uima.cas.FSArray", featOkTst);
    casFeatCode_rewritingsBySemiring  = (null == casFeat_rewritingsBySemiring) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rewritingsBySemiring).getCode();

  }
}



    