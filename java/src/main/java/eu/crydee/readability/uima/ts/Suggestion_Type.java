
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
public class Suggestion_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Suggestion_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Suggestion_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Suggestion(addr, Suggestion_Type.this);
  			   Suggestion_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Suggestion(addr, Suggestion_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Suggestion.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.Suggestion");
 
  /** @generated */
  final Feature casFeat_original;
  /** @generated */
  final int     casFeatCode_original;
  /** @generated */ 
  public int getOriginal(int addr) {
        if (featOkTst && casFeat_original == null)
      jcas.throwFeatMissing("original", "eu.crydee.readability.uima.ts.Suggestion");
    return ll_cas.ll_getRefValue(addr, casFeatCode_original);
  }
  /** @generated */    
  public void setOriginal(int addr, int v) {
        if (featOkTst && casFeat_original == null)
      jcas.throwFeatMissing("original", "eu.crydee.readability.uima.ts.Suggestion");
    ll_cas.ll_setRefValue(addr, casFeatCode_original, v);}
    
  
 
  /** @generated */
  final Feature casFeat_revised;
  /** @generated */
  final int     casFeatCode_revised;
  /** @generated */ 
  public int getRevised(int addr) {
        if (featOkTst && casFeat_revised == null)
      jcas.throwFeatMissing("revised", "eu.crydee.readability.uima.ts.Suggestion");
    return ll_cas.ll_getRefValue(addr, casFeatCode_revised);
  }
  /** @generated */    
  public void setRevised(int addr, int v) {
        if (featOkTst && casFeat_revised == null)
      jcas.throwFeatMissing("revised", "eu.crydee.readability.uima.ts.Suggestion");
    ll_cas.ll_setRefValue(addr, casFeatCode_revised, v);}
    
   /** @generated */
  public int getRevised(int addr, int i) {
        if (featOkTst && casFeat_revised == null)
      jcas.throwFeatMissing("revised", "eu.crydee.readability.uima.ts.Suggestion");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_revised), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_revised), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_revised), i);
  }
   
  /** @generated */ 
  public void setRevised(int addr, int i, int v) {
        if (featOkTst && casFeat_revised == null)
      jcas.throwFeatMissing("revised", "eu.crydee.readability.uima.ts.Suggestion");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_revised), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_revised), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_revised), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Suggestion_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_original = jcas.getRequiredFeatureDE(casType, "original", "eu.crydee.readability.uima.ts.Revision", featOkTst);
    casFeatCode_original  = (null == casFeat_original) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_original).getCode();

 
    casFeat_revised = jcas.getRequiredFeatureDE(casType, "revised", "uima.cas.FSArray", featOkTst);
    casFeatCode_revised  = (null == casFeat_revised) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_revised).getCode();

  }
}



    