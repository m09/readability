package eu.crydee.readability.uima.ae;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.CasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.CasUtil;

public class CoveredCopierAE extends CasAnnotator_ImplBase {

    public static final String PARAM_CONTAINER_TYPE = "CONTAINER_TYPE";
    @ConfigurationParameter(
            name = PARAM_CONTAINER_TYPE,
            mandatory = true)
    private String containerType;

    public static final String PARAM_CHILD_TYPE = "CHILD_TYPE";
    @ConfigurationParameter(
            name = PARAM_CHILD_TYPE,
            mandatory = true)
    private String childType;

    public static final String PARAM_NEW_CHILD_TYPE = "NEW_CHILD_TYPE";
    @ConfigurationParameter(
            name = PARAM_NEW_CHILD_TYPE,
            mandatory = true)
    private String newChildType;

    protected Type containerTypeT = null,
            childTypeT = null,
            newChildTypeT = null;

    @Override
    public void typeSystemInit(TypeSystem aTypeSystem)
            throws AnalysisEngineProcessException {
        super.typeSystemInit(aTypeSystem);
        containerTypeT = aTypeSystem.getType(containerType);
        childTypeT = aTypeSystem.getType(childType);
        newChildTypeT = aTypeSystem.getType(newChildType);
    }

    @Override
    public void process(CAS cas) throws AnalysisEngineProcessException {
        Map<AnnotationFS, Collection<AnnotationFS>> index
                = CasUtil.indexCovered(cas, containerTypeT, childTypeT);
        Set<AnnotationFS> seen = new HashSet<>();
        for (AnnotationFS container : index.keySet()) {
            for (AnnotationFS child : index.get(container)) {
                if (!seen.contains(child)) {
                    cas.addFsToIndexes(cas.createAnnotation(
                            newChildTypeT,
                            child.getBegin(),
                            child.getEnd()));
                    seen.add(child);
                }
            }
        }
    }
}
