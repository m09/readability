
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
public class Rewritings_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Rewritings_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Rewritings_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Rewritings(addr, Rewritings_Type.this);
  			   Rewritings_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Rewritings(addr, Rewritings_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Rewritings.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.Rewritings");
 
  /** @generated */
  final Feature casFeat_rewritings;
  /** @generated */
  final int     casFeatCode_rewritings;
  /** @generated */ 
  public int getRewritings(int addr) {
        if (featOkTst && casFeat_rewritings == null)
      jcas.throwFeatMissing("rewritings", "eu.crydee.readability.uima.ts.Rewritings");
    return ll_cas.ll_getRefValue(addr, casFeatCode_rewritings);
  }
  /** @generated */    
  public void setRewritings(int addr, int v) {
        if (featOkTst && casFeat_rewritings == null)
      jcas.throwFeatMissing("rewritings", "eu.crydee.readability.uima.ts.Rewritings");
    ll_cas.ll_setRefValue(addr, casFeatCode_rewritings, v);}
    
   /** @generated */
  public int getRewritings(int addr, int i) {
        if (featOkTst && casFeat_rewritings == null)
      jcas.throwFeatMissing("rewritings", "eu.crydee.readability.uima.ts.Rewritings");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritings), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_rewritings), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritings), i);
  }
   
  /** @generated */ 
  public void setRewritings(int addr, int i, int v) {
        if (featOkTst && casFeat_rewritings == null)
      jcas.throwFeatMissing("rewritings", "eu.crydee.readability.uima.ts.Rewritings");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritings), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_rewritings), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_rewritings), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Rewritings_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_rewritings = jcas.getRequiredFeatureDE(casType, "rewritings", "uima.cas.FSArray", featOkTst);
    casFeatCode_rewritings  = (null == casFeat_rewritings) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rewritings).getCode();

  }
}



    