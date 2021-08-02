package delegates;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * @Author xspisa02
 *
 * Korelácia správ podľa mena správy z lokálnej premennej,
 * ktorá je nastavená ako Input hodnota v danej úlohe odosielania správy.
 *
 * Inštancie procesov s udalosťami čakajúcimi na správu s rovnakým menom správy,
 * sú filtrované podľa evidenčného čísla vozidla, ktoré je uložené v procesnej premennej 'spz'.
 */

public class ProcessMessagingDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        ProcessEngine processEngine = execution.getProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        String messageName = (String) execution.getVariableLocal("messageName");

        if(messageName.equals("message_price")) {

            String targetInstanceId = (String) execution.getVariable("processId");

            runtimeService
                    .setVariable(targetInstanceId, "price", execution.getVariable("price"));
        }

        runtimeService.createMessageCorrelation(messageName)
                .processInstanceVariableEquals("spz", execution.getVariable("spz"))
                .correlate();


        System.out.println("message with name " + messageName + " correlated");
    }
}
