
/* First created by JCasGen Thu Apr 17 21:11:44 JST 2014 */
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
 * Updated by JCasGen Thu Apr 17 21:11:44 JST 2014
 * @generated */
public class WordDiff_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (WordDiff_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = WordDiff_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new WordDiff(addr, WordDiff_Type.this);
  			   WordDiff_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new WordDiff(addr, WordDiff_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = WordDiff.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.WordDiff");
 
  /** @generated */
  final Feature casFeat_original;
  /** @generated */
  final int     casFeatCode_original;
  /** @generated */ 
  public int getOriginal(int addr) {
        if (featOkTst && casFeat_original == null)
      jcas.throwFeatMissing("original", "eu.crydee.readability.uima.ts.WordDiff");
    return ll_cas.ll_getRefValue(addr, casFeatCode_original);
  }
  /** @generated */    
  public void setOriginal(int addr, int v) {
        if (featOkTst && casFeat_original == null)
      jcas.throwFeatMissing("original", "eu.crydee.readability.uima.ts.WordDiff");
    ll_cas.ll_setRefValue(addr, casFeatCode_original, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public WordDiff_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_original = jcas.getRequiredFeatureDE(casType, "original", "eu.crydee.readability.uima.ts.Area", featOkTst);
    casFeatCode_original  = (null == casFeat_original) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_original).getCode();

  }
}



    