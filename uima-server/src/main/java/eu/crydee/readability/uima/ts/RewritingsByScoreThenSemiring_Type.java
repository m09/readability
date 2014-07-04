
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
public class RewritingsByScoreThenSemiring_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (RewritingsByScoreThenSemiring_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = RewritingsByScoreThenSemiring_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new RewritingsByScoreThenSemiring(addr, RewritingsByScoreThenSemiring_Type.this);
  			   RewritingsByScoreThenSemiring_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new RewritingsByScoreThenSemiring(addr, RewritingsByScoreThenSemiring_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = RewritingsByScoreThenSemiring.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.RewritingsByScoreThenSemiring");
 
  /** @generated */
  final Feature casFeat_rewritingsByScoreThenSemiring;
  /** @generated */
  final int     casFeatCode_rewritingsByScoreThenSemiring;
  /** @generated */ 
  public int getRewritingsByScoreThenSemiring(int addr) {
        if (featOkTst && casFeat_rewritingsByScoreThenSemiring == null)
      jcas.throwFeatMissing("rewritingsByScoreThenSemiring", "eu.crydee.readability.uima.ts.RewritingsByScoreThenSemiring");
    return ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsByScoreThenSemiring);
  }
  /** @generated */    
  public void setRewritingsByScoreThenSemiring(int addr, int v) {
        if (featOkTst && casFeat_rewritingsByScoreThenSemiring == null)
      jcas.throwFeatMissing("rewritingsByScoreThenSemiring", "eu.crydee.readability.uima.ts.RewritingsByScoreThenSemiring");
    ll_cas.ll_setRefValue(addr, casFeatCode_rewritingsByScoreThenSemiring, v);}
    
   /** @generated */
  public int getRewritingsByScoreThenSemiring(int addr, int i) {
        if (featOkTst && casFeat_rewritingsByScoreThenSemiring == null)
      jcas.throwFeatMissing("rewritingsByScoreThenSemiring", "eu.crydee.readability.uima.ts.RewritingsByScoreThenSemiring");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsByScoreThenSemiring), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsByScoreThenSemiring), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsByScoreThenSemiring), i);
  }
   
  /** @generated */ 
  public void setRewritingsByScoreThenSemiring(int addr, int i, int v) {
        if (featOkTst && casFeat_rewritingsByScoreThenSemiring == null)
      jcas.throwFeatMissing("rewritingsByScoreThenSemiring", "eu.crydee.readability.uima.ts.RewritingsByScoreThenSemiring");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsByScoreThenSemiring), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsByScoreThenSemiring), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritingsByScoreThenSemiring), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public RewritingsByScoreThenSemiring_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_rewritingsByScoreThenSemiring = jcas.getRequiredFeatureDE(casType, "rewritingsByScoreThenSemiring", "uima.cas.FSArray", featOkTst);
    casFeatCode_rewritingsByScoreThenSemiring  = (null == casFeat_rewritingsByScoreThenSemiring) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rewritingsByScoreThenSemiring).getCode();

  }
}



    