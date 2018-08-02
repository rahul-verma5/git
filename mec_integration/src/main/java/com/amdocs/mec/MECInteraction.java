package com.amdocs.mec;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.utils.KieHelper;

@SuppressWarnings("unused")
public class MECInteraction {
	
	private static RuntimeEnvironment environment = null;
	private static RuntimeManager manager = null;
	private static RuntimeEngine runtime = null;
	private static KieSession ksession = null;
	
	
	private MECInteraction(){}
	
	static{
		/*environment = RuntimeEnvironmentBuilder.Factory.get().newDefaultInMemoryBuilder().addAsset(ResourceFactory.newClassPathResource("com/amdocs/mec/mec_integration.bpmn"), ResourceType.BPMN2).get();
		manager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment);
		runtime = manager.getRuntimeEngine(EmptyContext.get());
		ksession = runtime.getKieSession();*/
		KieHelper kieHelper = new KieHelper();
		KieBase kieBase = kieHelper.addResource(ResourceFactory.newClassPathResource("com/amdocs/mec/mec_integration.bpmn")).build();
		ksession = kieBase.newKieSession();
	}
	
	public static void invokeProcess(){
		Map<String , Object> requestParams = new HashMap<String , Object>();
		requestParams.put("BooleanFlag", Boolean.TRUE.toString());
		ProcessInstance processInstance = ksession.startProcess("mec_integration",requestParams);
		/*manager.disposeRuntimeEngine(runtime);
		manager.close();*/
	}
	
	public static void main(String[] args) {
		invokeProcess();
	}

}
