
/* First created by JCasGen Tue Jun 17 15:34:47 JST 2014 */
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
 * Updated by JCasGen Tue Jun 17 15:34:47 JST 2014
 * @generated */
public class Rewriting_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Rewriting_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Rewriting_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Rewriting(addr, Rewriting_Type.this);
  			   Rewriting_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Rewriting(addr, Rewriting_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Rewriting.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.Rewriting");
 
  /** @generated */
  final Feature casFeat_rewriting;
  /** @generated */
  final int     casFeatCode_rewriting;
  /** @generated */ 
  public String getRewriting(int addr) {
        if (featOkTst && casFeat_rewriting == null)
      jcas.throwFeatMissing("rewriting", "eu.crydee.readability.uima.ts.Rewriting");
    return ll_cas.ll_getStringValue(addr, casFeatCode_rewriting);
  }
  /** @generated */    
  public void setRewriting(int addr, String v) {
        if (featOkTst && casFeat_rewriting == null)
      jcas.throwFeatMissing("rewriting", "eu.crydee.readability.uima.ts.Rewriting");
    ll_cas.ll_setStringValue(addr, casFeatCode_rewriting, v);}
    
  
 
  /** @generated */
  final Feature casFeat_score;
  /** @generated */
  final int     casFeatCode_score;
  /** @generated */ 
  public double getScore(int addr) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Rewriting");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_score);
  }
  /** @generated */    
  public void setScore(int addr, double v) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "eu.crydee.readability.uima.ts.Rewriting");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_score, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Rewriting_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_rewriting = jcas.getRequiredFeatureDE(casType, "rewriting", "uima.cas.String", featOkTst);
    casFeatCode_rewriting  = (null == casFeat_rewriting) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rewriting).getCode();

 
    casFeat_score = jcas.getRequiredFeatureDE(casType, "score", "uima.cas.Double", featOkTst);
    casFeatCode_score  = (null == casFeat_score) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_score).getCode();

  }
}



    