package kz.em.logic.report.service;

import kz.em.logic.report.params.ReportType;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReportExecutorCreatorImpl implements ReportExecutorCreator {

	@Inject
	private List<ReportExecutor> executorList;

	private Map<ReportType, ReportExecutor> executorMap;

	@PostConstruct
	private void postConstruct() {
		executorMap = executorList.stream().collect(Collectors.toMap(ReportExecutor::getReportType, executor -> executor));
		executorList = null;
	}

	@Override
	public ReportExecutor createExecutor(ReportType type) {
		return executorMap.get(type);
	}
}
