
/* First created by JCasGen Mon Apr 21 15:34:02 JST 2014 */
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
 * Updated by JCasGen Mon Apr 21 15:34:02 JST 2014
 * @generated */
public class RevisedWords_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (RevisedWords_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = RevisedWords_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new RevisedWords(addr, RevisedWords_Type.this);
  			   RevisedWords_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new RevisedWords(addr, RevisedWords_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = RevisedWords.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.RevisedWords");
 
  /** @generated */
  final Feature casFeat_originalWords;
  /** @generated */
  final int     casFeatCode_originalWords;
  /** @generated */ 
  public int getOriginalWords(int addr) {
        if (featOkTst && casFeat_originalWords == null)
      jcas.throwFeatMissing("originalWords", "eu.crydee.readability.uima.ts.RevisedWords");
    return ll_cas.ll_getRefValue(addr, casFeatCode_originalWords);
  }
  /** @generated */    
  public void setOriginalWords(int addr, int v) {
        if (featOkTst && casFeat_originalWords == null)
      jcas.throwFeatMissing("originalWords", "eu.crydee.readability.uima.ts.RevisedWords");
    ll_cas.ll_setRefValue(addr, casFeatCode_originalWords, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public RevisedWords_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_originalWords = jcas.getRequiredFeatureDE(casType, "originalWords", "eu.crydee.readability.uima.ts.OriginalWords", featOkTst);
    casFeatCode_originalWords  = (null == casFeat_originalWords) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_originalWords).getCode();

  }
}



    