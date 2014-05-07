package controllers

import eu.crydee.readability.uima.DictUsagePipeline
import eu.crydee.readability.uima.ts.Revision
import eu.crydee.readability.uima.ts.Suggestion
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.cas.FeatureStructure
import org.apache.uima.fit.util.CasUtil
import org.apache.uima.fit.util.JCasUtil
import org.apache.uima.jcas.cas.FSArray
import org.apache.uima.jcas.cas.StringArray
import org.apache.uima.jcas.JCas
import play.api._
import play.api.libs.json._
import play.api.mvc._
import scala.collection.JavaConversions._

object Application extends Controller {

  private val ae = DictUsagePipeline buildAe "file:dict.xml"
  private val typeRev = CasUtil.getType(ae.newCAS, classOf[Revision])
  private val featTxt = typeRev.getFeatureByBaseName("text")
  private val featTok = typeRev.getFeatureByBaseName("tokens")
  private val featPos = typeRev.getFeatureByBaseName("pos")

  implicit val stringArrayWrites = new Writes[StringArray] {
    def writes(sa: StringArray): JsValue = Json.toJson(sa.toArray)
  }

  implicit val revisionWrites = new Writes[Revision] {
    def writes(revision: Revision): JsValue = Json.obj(
      "text"   -> revision.getText,
      "tokens" -> revision.getTokens,
      "pos"    -> revision.getPos
    )
  }

  implicit val suggestionWrites = new Writes[Suggestion] {
    def writes(suggestion: Suggestion): JsValue = {
      val arr = suggestion.getRevised.toArray
      Json.obj(
        "original" -> suggestion.getOriginal,
        "revised"  -> Json.arr(
          arr.map( fs =>
            Json.obj(
              "text"   -> fs.getStringValue(featTxt),
              "tokens" -> fs.getFeatureValue(featTok).asInstanceOf[StringArray],
              "pos"    -> fs.getFeatureValue(featPos).asInstanceOf[StringArray]
            )
          )
        )
      )
    }
  }

  def index = Action {
    Ok(views.html.index())
  }

  def annotate = Action(parse.text) { request =>
    val jcas = ae.newJCas()
    jcas.setDocumentText("Hello there, and how do you do?")
    ae.process(jcas)
    val suggestions : scala.collection.Iterable[Suggestion] = JCasUtil.select(
      jcas,
      classOf[Suggestion])
    Console println suggestions
    Ok(Json.toJson(suggestions))
  }
}
