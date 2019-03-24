package falcon.mvc.recipes.services;

import org.springframework.data.repository.CrudRepository;

public abstract class BaseServiceImpl <T extends Object, K extends Object, R extends CrudRepository<T,K>>
        implements BaseService<T,K,R>{


}
