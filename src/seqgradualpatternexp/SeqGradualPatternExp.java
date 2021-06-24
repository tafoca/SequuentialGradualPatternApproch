package seqgradualpatternexp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import static seqgradualpatternexp.test.TestTipper.determineFuzzyItemByValueOfItem;
import static seqgradualpatternexp.test.TestTipper.fileName;
import static seqgradualpatternexp.test.TestTipper.getFISByFileDesc;

/**
 *
 * @author Laurent
 */
public class SeqGradualPatternExp extends TemplateMethod {

    public static String fileName = "fcl/maket.fcl";
    static Map<String, Double> valuePrice = new HashMap<>();
    static Map<String, Double> valueConsumption = new HashMap<>();
    static Map<String, Double> valueSales = new HashMap<>();

    public SeqGradualPatternExp() throws IOException {

        initMethode();

    }

    /**
     * determine la valeur de (o,o') a utiliser pour determiner fuzzification
     *
     * @param val1
     * @param val2
     * @return
     */
    double computeValeAttr(double val1, double val2) {
        return (val1 - val2) / 30;
    }

    public static void main(String[] args) throws IOException {
        SeqGradualPatternExp sgpe = new SeqGradualPatternExp();
    }

    public void affcheRecord(Object[] record) {
        for (Object record1 : record) {
            System.out.println(" " + record.toString());
            System.out.println();
        }

    }
    public static final String[] items = {"time", "price", "consumption", "sales"};

    @Override
    public Object[][] algoTEDOneDataSequence(Object[][] Qdb, int objet) {
        System.out.println("seqgradualpatternexp.SeqGradualPatternExp.algoTEDOneDataSequence()" + nbtransaction + "--" + (nbitems - 1));

        List<List<Object>> deltaBD = new ArrayList();
        FIS fis = getFISByFileDesc(fileName);
        int fuzzySetPriceSize = fis.getVariable("price").getLinguisticTerms().values().size();
        int fuzzySetConsumptionSize = fis.getVariable("consumption").getLinguisticTerms().values().size();
        int fuzzySetSaleSize = fis.getVariable("sales").getLinguisticTerms().values().size();
        for (int i = 0; i < nbtransaction; i++) {
            for (int j = i + 1; j < nbtransaction; j++) {
                System.out.println("(" + i + "," + j + ")");
                Object[] record0 = Qdb[i];
                Object[] record1 = Qdb[j];
                List<Object> deltaRecord = new ArrayList<>();
                String s0 = (String) record0[0];
                String s1 = (String) record1[0];
                deltaRecord.add((s0 + '-' + s1));
                for (int k = 1; k < nbitems; k++) {
                    double computeValeAttr = computeValeAttr((double) record0[k], (double) record1[k]);
                    Map<String, Double> map = determineFuzzyItemByValueOfItem(fis, items[k], computeValeAttr);
                    System.out.println("> item :" +  items[k] + " : " + computeValeAttr);
                    for (Map.Entry<String, Double> entrySet : map.entrySet()) {
                        String key = entrySet.getKey();
                        Double value = entrySet.getValue();
                        System.out.println("> \t fuzy item :" + key + " : " + value);
                        deltaRecord.add(value);
                    }
                }
                //TODO ADD deta time aussi
                deltaBD.add(deltaRecord);
            }
        }
        try {
            FileWriter fw = new FileWriter(new File("detatest.test"));
            wrtiteBD(deltaBD, fw);
        } catch (IOException ex) {
            Logger.getLogger(SeqGradualPatternExp.class.getName()).log(Level.SEVERE, null, ex);
        }

        Map<String, Double> a = determineFuzzyItemByValueOfItem(fis, "price", 0.5);
        Map<String, Double> b = determineFuzzyItemByValueOfItem(fis, "consumption", 0.5);
        Map<String, Double> c = determineFuzzyItemByValueOfItem(fis, "sales", 0.5);
        return null;
    }

    // a is item number a
    @Override
    public Object[] getDataColByCol(Object[][] dataset, Object[] item, int a, int taille) {
        taille = taille;
        item = new Object[taille];
        int l = 0;
        for (int i = 0; i < dataset.length; i++) {

            for (int k = 0; k < nbitems; k++) {
                if (l == i && k == a) {
                    item[i] = dataset[i][k];
                    // System.out.println(item[i]+ " ");
                }
            }
            // res.add(item);
            l++;
        }

        return item;
    }

    @Override
    public void getAllColum(Object[][] dataset, Object[] item, int a, int taille) {
        for (a = 0; a < nbitems; a++) {
            Object[] rescol = getDataColByCol(dataset, item, a, taille);
            afficheColonne(rescol);
        }
    }

    public void afficheColonne(Object[] rescol) {
        for (int i = 0; i < rescol.length; i++) {
            System.out.println(rescol[i] + "  ");
        }
        System.out.println();
    }

    @Override
    public Map<Integer, Object[][]> algoTED(List<Integer> listObj) {
        return new HashMap<>(); //To change body of generated methods, choose Tools | Templates.
    }

}
