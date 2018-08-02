package com.sample;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;

/**
 * This is a sample file to test a process.
 */
public class ProcessTest extends JbpmJUnitBaseTestCase {

	@Test
	public void testProcess() {
		RuntimeManager manager = createRuntimeManager("com/amdocs/mec/mec_integration.bpmn");
		RuntimeEngine engine = getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		
		ProcessInstance processInstance = ksession.startProcess("mec_integration");
		// check whether the process instance has completed successfully
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "print");
		
		manager.disposeRuntimeEngine(engine);
		manager.close();
	}

}