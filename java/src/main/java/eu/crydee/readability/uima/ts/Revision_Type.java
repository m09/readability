
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
public class Revision_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Revision_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Revision_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Revision(addr, Revision_Type.this);
  			   Revision_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Revision(addr, Revision_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Revision.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.Revision");
 
  /** @generated */
  final Feature casFeat_tokens;
  /** @generated */
  final int     casFeatCode_tokens;
  /** @generated */ 
  public int getTokens(int addr) {
        if (featOkTst && casFeat_tokens == null)
      jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revision");
    return ll_cas.ll_getRefValue(addr, casFeatCode_tokens);
  }
  /** @generated */    
  public void setTokens(int addr, int v) {
        if (featOkTst && casFeat_tokens == null)
      jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revision");
    ll_cas.ll_setRefValue(addr, casFeatCode_tokens, v);}
    
   /** @generated */
  public String getTokens(int addr, int i) {
        if (featOkTst && casFeat_tokens == null)
      jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revision");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_tokens), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_tokens), i);
	return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_tokens), i);
  }
   
  /** @generated */ 
  public void setTokens(int addr, int i, String v) {
        if (featOkTst && casFeat_tokens == null)
      jcas.throwFeatMissing("tokens", "eu.crydee.readability.uima.ts.Revision");
    if (lowLevelTypeChecks)
      ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_tokens), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_tokens), i);
    ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_tokens), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_text;
  /** @generated */
  final int     casFeatCode_text;
  /** @generated */ 
  public String getText(int addr) {
        if (featOkTst && casFeat_text == null)
      jcas.throwFeatMissing("text", "eu.crydee.readability.uima.ts.Revision");
    return ll_cas.ll_getStringValue(addr, casFeatCode_text);
  }
  /** @generated */    
  public void setText(int addr, String v) {
        if (featOkTst && casFeat_text == null)
      jcas.throwFeatMissing("text", "eu.crydee.readability.uima.ts.Revision");
    ll_cas.ll_setStringValue(addr, casFeatCode_text, v);}
    
  
 
  /** @generated */
  final Feature casFeat_count;
  /** @generated */
  final int     casFeatCode_count;
  /** @generated */ 
  public int getCount(int addr) {
        if (featOkTst && casFeat_count == null)
      jcas.throwFeatMissing("count", "eu.crydee.readability.uima.ts.Revision");
    return ll_cas.ll_getIntValue(addr, casFeatCode_count);
  }
  /** @generated */    
  public void setCount(int addr, int v) {
        if (featOkTst && casFeat_count == null)
      jcas.throwFeatMissing("count", "eu.crydee.readability.uima.ts.Revision");
    ll_cas.ll_setIntValue(addr, casFeatCode_count, v);}
    
  
 
  /** @generated */
  final Feature casFeat_score;
  /** @generated */
  final int     casFeatCode_score;
  /** @generated */ 
  public int getScore(int addr) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Revision");
    return ll_cas.ll_getRefValue(addr, casFeatCode_score);
  }
  /** @generated */    
  public void setScore(int addr, int v) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Revision");
    ll_cas.ll_setRefValue(addr, casFeatCode_score, v);}
    
   /** @generated */
  public double getScore(int addr, int i) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Revision");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getDoubleArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_score), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_score), i);
	return ll_cas.ll_getDoubleArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_score), i);
  }
   
  /** @generated */ 
  public void setScore(int addr, int i, double v) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Revision");
    if (lowLevelTypeChecks)
      ll_cas.ll_setDoubleArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_score), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_score), i);
    ll_cas.ll_setDoubleArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_score), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Revision_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_tokens = jcas.getRequiredFeatureDE(casType, "tokens", "uima.cas.StringArray", featOkTst);
    casFeatCode_tokens  = (null == casFeat_tokens) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tokens).getCode();

 
    casFeat_text = jcas.getRequiredFeatureDE(casType, "text", "uima.cas.String", featOkTst);
    casFeatCode_text  = (null == casFeat_text) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_text).getCode();

 
    casFeat_count = jcas.getRequiredFeatureDE(casType, "count", "uima.cas.Integer", featOkTst);
    casFeatCode_count  = (null == casFeat_count) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_count).getCode();

 
    casFeat_score = jcas.getRequiredFeatureDE(casType, "score", "uima.cas.DoubleArray", featOkTst);
    casFeatCode_score  = (null == casFeat_score) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_score).getCode();

  }
}



    