
/* First created by JCasGen Sat Jul 05 15:34:58 JST 2014 */
package eu.crydee.readability.uima.core.ts;

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
 * Updated by JCasGen Sat Jul 05 15:34:58 JST 2014
 * @generated */
public class Token_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Token_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Token_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Token(addr, Token_Type.this);
  			   Token_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Token(addr, Token_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Token.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("eu.crydee.readability.uima.core.ts.Token");
 
  /** @generated */
  final Feature casFeat_POS;
  /** @generated */
  final int     casFeatCode_POS;
  /** @generated */ 
  public String getPOS(int addr) {
        if (featOkTst && casFeat_POS == null)
      jcas.throwFeatMissing("POS", "eu.crydee.readability.uima.core.ts.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_POS);
  }
  /** @generated */    
  public void setPOS(int addr, String v) {
        if (featOkTst && casFeat_POS == null)
      jcas.throwFeatMissing("POS", "eu.crydee.readability.uima.core.ts.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_POS, v);}
    
  
 
  /** @generated */
  final Feature casFeat_syllablesNumber;
  /** @generated */
  final int     casFeatCode_syllablesNumber;
  /** @generated */ 
  public int getSyllablesNumber(int addr) {
        if (featOkTst && casFeat_syllablesNumber == null)
      jcas.throwFeatMissing("syllablesNumber", "eu.crydee.readability.uima.core.ts.Token");
    return ll_cas.ll_getIntValue(addr, casFeatCode_syllablesNumber);
  }
  /** @generated */    
  public void setSyllablesNumber(int addr, int v) {
        if (featOkTst && casFeat_syllablesNumber == null)
      jcas.throwFeatMissing("syllablesNumber", "eu.crydee.readability.uima.core.ts.Token");
    ll_cas.ll_setIntValue(addr, casFeatCode_syllablesNumber, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Token_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_POS = jcas.getRequiredFeatureDE(casType, "POS", "uima.cas.String", featOkTst);
    casFeatCode_POS  = (null == casFeat_POS) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_POS).getCode();

 
    casFeat_syllablesNumber = jcas.getRequiredFeatureDE(casType, "syllablesNumber", "uima.cas.Integer", featOkTst);
    casFeatCode_syllablesNumber  = (null == casFeat_syllablesNumber) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_syllablesNumber).getCode();

  }
}



    