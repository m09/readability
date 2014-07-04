
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
public class AllRewritings_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (AllRewritings_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = AllRewritings_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new AllRewritings(addr, AllRewritings_Type.this);
  			   AllRewritings_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new AllRewritings(addr, AllRewritings_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = AllRewritings.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.AllRewritings");
 
  /** @generated */
  final Feature casFeat_allRewritings;
  /** @generated */
  final int     casFeatCode_allRewritings;
  /** @generated */ 
  public int getAllRewritings(int addr) {
        if (featOkTst && casFeat_allRewritings == null)
      jcas.throwFeatMissing("allRewritings", "eu.crydee.readability.uima.ts.AllRewritings");
    return ll_cas.ll_getRefValue(addr, casFeatCode_allRewritings);
  }
  /** @generated */    
  public void setAllRewritings(int addr, int v) {
        if (featOkTst && casFeat_allRewritings == null)
      jcas.throwFeatMissing("allRewritings", "eu.crydee.readability.uima.ts.AllRewritings");
    ll_cas.ll_setRefValue(addr, casFeatCode_allRewritings, v);}
    
   /** @generated */
  public int getAllRewritings(int addr, int i) {
        if (featOkTst && casFeat_allRewritings == null)
      jcas.throwFeatMissing("allRewritings", "eu.crydee.readability.uima.ts.AllRewritings");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_allRewritings), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_allRewritings), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_allRewritings), i);
  }
   
  /** @generated */ 
  public void setAllRewritings(int addr, int i, int v) {
        if (featOkTst && casFeat_allRewritings == null)
      jcas.throwFeatMissing("allRewritings", "eu.crydee.readability.uima.ts.AllRewritings");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_allRewritings), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_allRewritings), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_allRewritings), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public AllRewritings_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_allRewritings = jcas.getRequiredFeatureDE(casType, "allRewritings", "uima.cas.FSArray", featOkTst);
    casFeatCode_allRewritings  = (null == casFeat_allRewritings) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_allRewritings).getCode();

  }
}



    