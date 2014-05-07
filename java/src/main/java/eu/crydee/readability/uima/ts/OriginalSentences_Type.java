
/* First created by JCasGen Wed May 07 11:14:39 JST 2014 */
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
 * Updated by JCasGen Wed May 07 11:14:39 JST 2014
 * @generated */
public class OriginalSentences_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (OriginalSentences_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = OriginalSentences_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new OriginalSentences(addr, OriginalSentences_Type.this);
  			   OriginalSentences_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new OriginalSentences(addr, OriginalSentences_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = OriginalSentences.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.ts.OriginalSentences");
 
  /** @generated */
  final Feature casFeat_revisedSentences;
  /** @generated */
  final int     casFeatCode_revisedSentences;
  /** @generated */ 
  public int getRevisedSentences(int addr) {
        if (featOkTst && casFeat_revisedSentences == null)
      jcas.throwFeatMissing("revisedSentences", "eu.crydee.readability.uima.ts.OriginalSentences");
    return ll_cas.ll_getRefValue(addr, casFeatCode_revisedSentences);
  }
  /** @generated */    
  public void setRevisedSentences(int addr, int v) {
        if (featOkTst && casFeat_revisedSentences == null)
      jcas.throwFeatMissing("revisedSentences", "eu.crydee.readability.uima.ts.OriginalSentences");
    ll_cas.ll_setRefValue(addr, casFeatCode_revisedSentences, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public OriginalSentences_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_revisedSentences = jcas.getRequiredFeatureDE(casType, "revisedSentences", "eu.crydee.readability.uima.ts.RevisedSentences", featOkTst);
    casFeatCode_revisedSentences  = (null == casFeat_revisedSentences) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_revisedSentences).getCode();

  }
}



    