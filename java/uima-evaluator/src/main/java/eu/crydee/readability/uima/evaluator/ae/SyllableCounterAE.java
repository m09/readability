package eu.crydee.readability.uima.evaluator.ae;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.CasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.CasUtil;

/**
 * Fallback syllable splitter.
 *
 * Based on the work of Thomas Jakobsen (thomj05@student.uia.no) and Thomas
 * Skardal (thomas04@student.uia.no) for the NLTK readability plugin.
 *
 * https://code.google.com/p/nltk/source/browse/trunk/nltk_contrib/nltk_contrib/readability/syllables_en.py
 *
 * Their work is itself based on the algorithm in Greg Fast's perl module
 * Lingua::EN::Syllable.
 */
public class SyllableCounterAE extends CasAnnotator_ImplBase {

    public final static String PARAM_TOKEN_TYPE = "TOKEN_TYPE";
    @ConfigurationParameter(name = PARAM_TOKEN_TYPE, mandatory = true)
    String tokenTName;

    public final static String PARAM_TOKEN_FEATURE = "TOKEN_FEATURE";
    @ConfigurationParameter(name = PARAM_TOKEN_FEATURE, mandatory = true)
    String tokenFName;

    public final static String PARAM_CONTAINER_TYPE = "CONTAINER_TYPE";
    @ConfigurationParameter(name = PARAM_CONTAINER_TYPE, mandatory = false)
    String containerTName;

    private Type tokenT;

    private Optional<Type> containerT;

    private Feature tokenF;

    @Override
    public void typeSystemInit(TypeSystem ts)
            throws AnalysisEngineProcessException {
        super.typeSystemInit(ts);
        tokenT = ts.getType(tokenTName);
        tokenF = tokenT.getFeatureByBaseName(tokenFName);
        if (containerTName == null) {
            containerT = Optional.empty();
        } else {
            containerT = Optional.of(ts.getType(tokenTName));
        }
    }

    @Override
    public void process(CAS cas) throws AnalysisEngineProcessException {
        if (containerT.isPresent()) {
            CasUtil.indexCovered(cas, containerT.get(), tokenT)
                    .entrySet()
                    .forEach(e -> e.getValue().forEach(t -> setCount(t)));
        } else {
            CasUtil.select(cas, tokenT).forEach(t -> setCount(t));
        }
    }

    private void setCount(AnnotationFS token) {
        token.setIntValue(tokenF, count(token.getCoveredText()));
    }

    private final static int cacheSize;

    private final static Map<String, Integer> exceptions, cache;

    private final static List<Pattern> subSyl, addSyl;

    private final static Set<Character> vowels;

    static {
        cacheSize = 20000;

        cache = new HashMap<>();

        exceptions = new HashMap<>();
        exceptions.put("tottered", 2);
        exceptions.put("chummed", 1);
        exceptions.put("peeped", 1);
        exceptions.put("moustaches", 2);
        exceptions.put("shamefully", 3);
        exceptions.put("messieurs", 2);
        exceptions.put("satiated", 4);
        exceptions.put("sailmaker", 4);
        exceptions.put("sheered", 1);
        exceptions.put("disinterred", 3);
        exceptions.put("propitiatory", 6);
        exceptions.put("bepatched", 2);
        exceptions.put("particularized", 5);
        exceptions.put("caressed", 2);
        exceptions.put("trespassed", 2);
        exceptions.put("sepulchre", 3);
        exceptions.put("flapped", 1);
        exceptions.put("hemispheres", 3);
        exceptions.put("pencilled", 2);
        exceptions.put("motioned", 2);
        exceptions.put("poleman", 2);
        exceptions.put("slandered", 2);
        exceptions.put("sombre", 2);
        exceptions.put("etc", 4);
        exceptions.put("sidespring", 2);
        exceptions.put("mimes", 1);
        exceptions.put("effaces", 2);
        exceptions.put("mr", 2);
        exceptions.put("mrs", 2);
        exceptions.put("ms", 1);
        exceptions.put("dr", 2);
        exceptions.put("st", 1);
        exceptions.put("sr", 2);
        exceptions.put("jr", 2);
        exceptions.put("truckle", 2);
        exceptions.put("foamed", 1);
        exceptions.put("fringed", 2);
        exceptions.put("clattered", 2);
        exceptions.put("capered", 2);
        exceptions.put("mangroves", 2);
        exceptions.put("suavely", 2);
        exceptions.put("reclined", 2);
        exceptions.put("brutes", 1);
        exceptions.put("effaced", 2);
        exceptions.put("quivered", 2);
        exceptions.put("h'm", 1);
        exceptions.put("veriest", 3);
        exceptions.put("sententiously", 4);
        exceptions.put("deafened", 2);
        exceptions.put("manoeuvred", 3);
        exceptions.put("unstained", 2);
        exceptions.put("gaped", 1);
        exceptions.put("stammered", 2);
        exceptions.put("shivered", 2);
        exceptions.put("discoloured", 3);
        exceptions.put("gravesend", 2);
        exceptions.put("60", 2);
        exceptions.put("lb", 1);
        exceptions.put("unexpressed", 3);
        exceptions.put("greyish", 2);
        exceptions.put("unostentatious", 5);

        subSyl = Arrays.asList("cial", "tia", "cius", "cious", "gui", "ion",
                "iou", "sia$", ".ely$").stream()
                .map(Pattern::compile)
                .collect(Collectors.toList());

        addSyl = Arrays.asList("ia", "riet", "dien", "iu", "io", "ii",
                "[aeiouy]bl$", "mbl$",
                "[aeiou]{3}",
                "^mc", "ism$",
                "(.)(?!\\1)([aeiouy])\\2l$",
                "[^l]llien",
                "^coad.", "^coag.", "^coal.", "^coax.",
                "(.)(?!\\1)[gq]ua(.)(?!\\2)[aeiou]",
                "dnt$").stream()
                .map(Pattern::compile)
                .collect(Collectors.toList());
        vowels = new HashSet<>();
        vowels.add('a');
        vowels.add('e');
        vowels.add('i');
        vowels.add('o');
        vowels.add('u');
        vowels.add('y');
    }

    public static int count(String word) {
        if (word == null) {
            return 0;
        } else if (word.length() == 1) {
            return 1;
        }

        word = word.toLowerCase(Locale.ENGLISH);

        if (exceptions.containsKey(word)) {
            return exceptions.get(word);
        }

        if (word.charAt(word.length() - 1) == 'e') {
            word = word.substring(0, word.length() - 1);
        }

        int count = 0;
        boolean prevIsVowel = false;
        for (char c : word.toCharArray()) {
            boolean isVowel = vowels.contains(c);
            if (isVowel && !prevIsVowel) {
                ++count;
            }
            prevIsVowel = isVowel;
        }
        for (Pattern pattern : addSyl) {
            if (pattern.matcher(word).find()) {
                ++count;
            }
        }
        for (Pattern pattern : subSyl) {
            if (pattern.matcher(word).find()) {
                --count;
            }
        }

        if (cache.size() < cacheSize) {
            cache.put(word, count);
        }

        return count;
    }
}
