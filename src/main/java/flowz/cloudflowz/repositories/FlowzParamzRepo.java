package flowz.cloudflowz.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import flowz.cloudflowz.domain.FlowzParamz;

public interface FlowzParamzRepo extends CrudRepository<FlowzParamz, Integer>{
		
	List<FlowzParamz> findAllByOrderByStepzTmstmpAsc();	
	List<FlowzParamz> findByUsername(String username);
	//List<FlowzParamz> findAllByFlowzId(Integer flowzId);
	
}
