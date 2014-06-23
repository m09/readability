
/* First created by JCasGen Mon Jun 23 12:39:02 JST 2014 */
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
 * Updated by JCasGen Mon Jun 23 12:39:02 JST 2014
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
  final Feature casFeat_revisions;
  /** @generated */
  final int     casFeatCode_revisions;
  /** @generated */ 
  public int getRevisions(int addr) {
        if (featOkTst && casFeat_revisions == null)
      jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Suggestion");
    return ll_cas.ll_getRefValue(addr, casFeatCode_revisions);
  }
  /** @generated */    
  public void setRevisions(int addr, int v) {
        if (featOkTst && casFeat_revisions == null)
      jcas.throwFeatMissing("revisions", "eu.crydee.readability.uima.ts.Suggestion");
    ll_cas.ll_setRefValue(addr, casFeatCode_revisions, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Suggestion_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_revisions = jcas.getRequiredFeatureDE(casType, "revisions", "eu.crydee.readability.uima.ts.Revisions", featOkTst);
    casFeatCode_revisions  = (null == casFeat_revisions) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_revisions).getCode();

  }
}



    