/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.its;

import java.util.Comparator;

/**
 *
 * @author Krystian
 */
public class IndividualComparator implements Comparator<Individual> {

    public int compare(Individual individual_1, Individual individual_2) {
        if (individual_1.getRate() == individual_2.getRate()) {
            return 0;
        } else if (individual_1.getRate() < individual_2.getRate()) {
            return 1;
        } else {
            return -1;
        }
    }
}
