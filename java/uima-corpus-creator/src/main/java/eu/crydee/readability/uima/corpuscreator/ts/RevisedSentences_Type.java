
/* First created by JCasGen Sat Jul 05 16:29:21 JST 2014 */
package eu.crydee.readability.uima.corpuscreator.ts;

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
 * Updated by JCasGen Sat Jul 05 16:29:21 JST 2014
 * @generated */
public class RevisedSentences_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (RevisedSentences_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = RevisedSentences_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new RevisedSentences(addr, RevisedSentences_Type.this);
  			   RevisedSentences_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new RevisedSentences(addr, RevisedSentences_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = RevisedSentences.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.corpuscreator.ts.RevisedSentences");
 
  /** @generated */
  final Feature casFeat_originalSentences;
  /** @generated */
  final int     casFeatCode_originalSentences;
  /** @generated */ 
  public int getOriginalSentences(int addr) {
        if (featOkTst && casFeat_originalSentences == null)
      jcas.throwFeatMissing("originalSentences", "eu.crydee.readability.uima.corpuscreator.ts.RevisedSentences");
    return ll_cas.ll_getRefValue(addr, casFeatCode_originalSentences);
  }
  /** @generated */    
  public void setOriginalSentences(int addr, int v) {
        if (featOkTst && casFeat_originalSentences == null)
      jcas.throwFeatMissing("originalSentences", "eu.crydee.readability.uima.corpuscreator.ts.RevisedSentences");
    ll_cas.ll_setRefValue(addr, casFeatCode_originalSentences, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public RevisedSentences_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_originalSentences = jcas.getRequiredFeatureDE(casType, "originalSentences", "eu.crydee.readability.uima.corpuscreator.ts.OriginalSentences", featOkTst);
    casFeatCode_originalSentences  = (null == casFeat_originalSentences) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_originalSentences).getCode();

  }
}



    