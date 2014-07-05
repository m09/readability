
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
public class OriginalWords_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (OriginalWords_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = OriginalWords_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new OriginalWords(addr, OriginalWords_Type.this);
  			   OriginalWords_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new OriginalWords(addr, OriginalWords_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = OriginalWords.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.corpuscreator.ts.OriginalWords");
 
  /** @generated */
  final Feature casFeat_revisedWords;
  /** @generated */
  final int     casFeatCode_revisedWords;
  /** @generated */ 
  public int getRevisedWords(int addr) {
        if (featOkTst && casFeat_revisedWords == null)
      jcas.throwFeatMissing("revisedWords", "eu.crydee.readability.uima.corpuscreator.ts.OriginalWords");
    return ll_cas.ll_getRefValue(addr, casFeatCode_revisedWords);
  }
  /** @generated */    
  public void setRevisedWords(int addr, int v) {
        if (featOkTst && casFeat_revisedWords == null)
      jcas.throwFeatMissing("revisedWords", "eu.crydee.readability.uima.corpuscreator.ts.OriginalWords");
    ll_cas.ll_setRefValue(addr, casFeatCode_revisedWords, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public OriginalWords_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_revisedWords = jcas.getRequiredFeatureDE(casType, "revisedWords", "eu.crydee.readability.uima.corpuscreator.ts.RevisedWords", featOkTst);
    casFeatCode_revisedWords  = (null == casFeat_revisedWords) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_revisedWords).getCode();

  }
}



    